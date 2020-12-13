
package com.smwsk.bot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MessageProcessingUtils {

    /**
     * 小米小爱开放平台 token
     */
    @Value("${xiaomi.token}")
    public static String token="your developer xiaomi token";

    /**
     * 正常文本信息时
     * @param msg
     * @return
     * @throws IOException
     */
    public static Map onMessage(String msg) throws IOException {
        Map<String,Object> map=new HashMap<>();
        String resultJson = Jsoup.connect("https://developers.xiaoai.mi.com/api/testplatform/test")
                .ignoreContentType(true)
                .data("query",msg)
                .method(Connection.Method.GET)
                .header("Cookie","serviceToken="+token+"; cUserId= ;")
                .execute().body();
        JSONObject answer=JSON.parseObject(resultJson).getJSONArray("answer").getJSONObject(0);
        map.put("text",answer.getJSONObject("content").getString("to_speak"));
        try{
           map.put("image",answer.getJSONObject("intention").getJSONObject("individual").getJSONArray("IMAGE").getJSONObject(0).getString("image"));
        }catch (Exception e){}
        return map;
    }

    /**
     * 获取图片时
     * @param type
     * @return
     * @throws IOException
     */
    public static String[] findBizs(String type) throws IOException {
        String url=type.equals("biz")?"https://api.vc.bilibili.com/link_draw/v2/Doc/index?type=recommend&page_num=0&page_size=1"
                :"https://api.vc.bilibili.com/link_draw/v2/Photo/index?type=recommend&page_num=0&page_size=1";
        String result = Jsoup.connect(url)
                .ignoreContentType(true)
                .method(Connection.Method.GET)
                .execute()
                .body();
        JSONArray array = JSON.parseObject(result).getJSONObject("data").getJSONArray("items").getJSONObject(0).getJSONObject("item").getJSONArray("pictures");
        String[] imgarr;
        if (array.size()>3){
            imgarr =new String[3];
            for (int i = 0; i < 3; i++) {
                imgarr[i] = array.getJSONObject(i).getString("img_src");
                if (array.size()>3&&i>3){
                    break;
                }
            }
        }else{
            imgarr =new String[array.size()];
            for (int i = 0; i < array.size(); i++) {
                imgarr[i] = array.getJSONObject(i).getString("img_src");
            }
        }
        return imgarr;
    }


    public static void main(String[] args) throws IOException {
        System.out.println(onMessage("吃饭了吗！"));
    }
}
