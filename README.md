* JAVA SpringBoot 搭建wechaty智能聊天机器
> 本项目基于[wechaty-bot](https://github.com/smwsk/wechaty-bot) 的基础上修改，实现了小爱同学智能应答，bilibili画友会和摄影展。
## 智能机器人功能
* 基于小爱同学智能回复 命令：#你想说的话
* 基于bilibili摄影展，获取cosplay图片返回到聊天 命令：#cos（包含cos就行）
* 基于bilibili画友，获取手绘画返回到聊天 命令：#漫画（包含漫画就行）
## 微信机器人头像


## 准备工作
* 跟wechaty人员申请token
* [小爱同学开放平台](https://developers.xiaoai.mi.com/) 注册账号，下面有获取token教程

## 项目信息
* [项目地址](https://github.com/616891266/wechatyai-bot) 

## 小爱同学token获取
* 获取过程比较复杂，不细讲了，直接贴代码，因为我服务器用的是centos，所以跟windwos不太一样。
* 现在你的运行机器中安装 chrome 然后安装同版本 chronedriver [centos看这里](https://blog.csdn.net/herobacking/article/details/80276060) windows自行百度。

###添加依赖
```
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>3.141.59</version>
        </dependency>
```
获取小爱同学token Main类
```
    private static String webDriver = "webdriver.chrome.driver";
    /**
     * 你的chromedriver路径
     */
    private static String webDriverPath = "D:\\chromedriver\\chromedriver.exe";
    private static WebDriver driver = null;

    public static void main(String[] args) throws MalformedURLException {
        //这里如果是windows系统不用注释
        //System.setProperty(webDriver, webDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless","--no-sandbox","--disable-gpu","--window-size=1920,1080");
        options.addArguments("blink-settings=imagesEnabled=false");
        //chromeOptions.addArguments("headless");//无界面参数
        //chromeOptions.addArguments("no-sandbox");//禁用沙盒
        options.addArguments("Accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        options.addArguments("Accept-Encoding=gzip, deflate, sdch");
        options.addArguments("Accept-Language=zh-CN,zh;q=0.8");
        options.addArguments("Connection=keep-alive");
        options.addArguments("Host=activityunion-marketing.meituan.com");
        options.addArguments("Upgrade-Insecure-Requests=1");
        options.addArguments("User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://developers.xiaoai.mi.com/");
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector(".login-btn")).click();
        driver.findElement(By.cssSelector("input[name=user]")).sendKeys("小爱开放平台账号");
        driver.findElement(By.cssSelector("input[name=password]")).sendKeys("小爱开放平台密码");
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.findElement(By.id("login-button")).click();
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("username")));
        Set<Cookie> cookies = driver.manage().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie!=null&&"serviceToken".equals(cookie.getName())){
                //小爱同学token在这，想办法更新到项目中
                System.out.println("serviceToken:-->"+cookie.getValue());
                break;
            }
            if (cookie!=null&&"cUserId".equals(cookie.getName())){
                //小爱同学cUserId
                System.out.println("cUserId:-->"+cookie.getValue());
                break;
            }
        }
        driver.quit();
    }
```
* 该demo建议集成到项目中，添加定时任务，每两小时执行一次！

## 项目说明
* wechaty token在application.properties配置文件中修改
* 小爱同学token com.smwsk.bot.util.MessageProcessingUtils 类中，想办法集成进去。

### 客服发的材料
* 请务必反复阅读我们的Wiki，Wechaty 的 API 中英文文档、各个项目链接、多个协议的使用说明、Web协议不能使用情况下如何申请其他协议Token等内容均在其中：https://github.com/juzibot/Welcome/wiki/Everything-about-Wechaty
* API文档：https://wechaty.js.org/api
* Padplus Token 进行多语言开发指南 https://github.com/wechaty/wechaty/issues/1985

### 联系我 有什么问题欢迎打扰
微信号：wwwlolcn
![微信](http://47.112.21.193:9997/wx.jpg)

## Copyright & License
[![Powered by Wechaty](https://img.shields.io/badge/Powered%20By-Wechaty-green.svg)](https://github.com/chatie/wechaty)
[![Wechaty开源激励计划](https://img.shields.io/badge/Wechaty-开源激励计划-green.svg)](https://github.com/juzibot/Welcome/wiki/Everything-about-Wechaty)