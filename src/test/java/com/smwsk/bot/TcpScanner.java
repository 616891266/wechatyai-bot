import java.io.*;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * @author: Molier
 * @dateTime: 2020/11/11
 **/
public class TcpScanner implements Runnable {
    private static final String scannerResultFilePath = "c:\\scan-result\\";

    //max 65535
    private static final String host = "43.154.94.251"; //扫描的ip
    public static final int MIN_PORT_NUMBER = 1;
    public static final int MAX_PORT_NUMBER = 9999;
    public static final int threadCnt = 200;


    /**
     * 数据执行
     *
     * @throws InterruptedException
     * @throws IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        int totalPortCnt = MAX_PORT_NUMBER - MIN_PORT_NUMBER;// 待处理端口数
        int threadDealCnt = totalPortCnt / threadCnt;// 每一个线程处理的数目
        int lastThreadDealCnt = totalPortCnt % threadCnt;// 最后一个线程处理数目
        System.out.println("待处理数" + totalPortCnt);
        System.out.println("每一个线程处理的数目" + threadDealCnt);
        System.out.println("余数为 " + lastThreadDealCnt);
        CountDownLatch countDownLatch = new CountDownLatch(threadCnt);


        int moreLastCnt = MIN_PORT_NUMBER - 1 ;
        // 线程分发
        for (int threadIndex = 1; threadIndex <= threadCnt; threadIndex++) {
            int startPort = 0;
            int endPort = 0;
            // 余数分布到每一个线程里面去处理
            startPort = moreLastCnt + 1;
            if (threadIndex <= lastThreadDealCnt && 0 != lastThreadDealCnt) {
                endPort = startPort + threadDealCnt;
            } else {
                endPort = startPort + (threadDealCnt - 1);
            }
            moreLastCnt = endPort;
            new TcpScanner2(startPort, endPort, countDownLatch).start();
        }
        countDownLatch.await();
        dealFile();
    }


    /**
     * 删除所有的空文件
     *
     * @throws IOException
     */
    private static void dealFile() throws IOException {
        System.out.println("delete files start");
        File fileDir = new File(scannerResultFilePath);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            long fileLength = file.length();
            if (fileLength == 0) {
                file.delete();
            }
        }


        File resultTxtFile = new File(scannerResultFilePath.concat("/result.txt"));
        if (resultTxtFile.exists()) {
            resultTxtFile.delete();
        }
        resultTxtFile.createNewFile();
        RandomAccessFile randomAccessFile = new RandomAccessFile(resultTxtFile, "rw");
        files = fileDir.listFiles();
        for (File file : files) {
            if (file.getName().contains("result.txt"))
                continue;
            randomAccessFile.seek(resultTxtFile.length());
            BufferedReader bufferReader = new BufferedReader(new FileReader(file));
            while (true) {
                String line = bufferReader.readLine();
                if (null == line || "".equals(line.trim())) {
                    break;
                }
                randomAccessFile.write(line.concat("\n").getBytes());
            }
            bufferReader.close();
            file.delete();
        }
        randomAccessFile.close();
    }


    // 主要业务处理逻辑
    private int begIndex;
    private int endIndex;
    private Thread thread;
    private CountDownLatch countDownLatch;


    public TcpScanner2(int begIndex, int endIndex, CountDownLatch countDownLatch) {
        super();
        this.begIndex = begIndex;
        this.endIndex = endIndex;
        this.countDownLatch = countDownLatch;
        try {
            thread = new Thread(this);
            File file = new File(scannerResultFilePath + "result-" + thread.getName() + ".txt");
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            resultFile = new RandomAccessFile(file, "rw");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 线程启动
     */
    public TcpScanner2 start() {
        thread.start();
        return this;
    }


    // 线程执行
    @Override
    public void run() {
        for (int i = begIndex; i <= endIndex; i++) {
            if (scan(i, 3000)) {
                System.out.println(host+ ":" +i);
                //System.out.println("js" +i);
                writeResultToFile("port Listener " + i);
            }
        }
        countDownLatch.countDown();
        close();
    }




    // 端口扫描
    public boolean scan(int port, int timeOut) {
        boolean flag = false;
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(timeOut);
            flag = true;
        } catch (IOException e) {
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }


    // 文件写入
    private RandomAccessFile resultFile = null;


    private void writeResultToFile(String result) {
        try {
            resultFile.writeBytes(result.concat("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void close() {
        if (null != resultFile)
            try {
                resultFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
