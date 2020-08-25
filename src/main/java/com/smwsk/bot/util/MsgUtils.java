package com.smwsk.bot.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smwsk.bot.entity.XwRespMsg;
import com.smwsk.bot.enums.AnswerTypeEnum;
import io.github.wechaty.schemas.UrlLinkPayload;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import io.github.wechaty.user.UrlLink;

import java.util.List;
import java.util.Map;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 18:07
 * Description:
 */
public class MsgUtils {

	public static void sendContactMsg(Contact from, XwRespMsg respMsg){
		if(AnswerTypeEnum.music_type.getCode().equals(respMsg.getAnswer_type())){
			String msg = respMsg.getMsg();
			List<Map<String,Object>> songList = (List<Map<String, Object>>) JSONArray.parse(msg);
			UrlLinkPayload urlLinkPayload = new UrlLinkPayload(songList.get(0).get("song_name").toString(), songList.get(0).get("music_url").toString());
			urlLinkPayload.setThumbnailUrl(songList.get(0).get("pic_url").toString());
			urlLinkPayload.setDescription(songList.get(0).get("song_name").toString() + "-" + songList.get(0).get("singer_name").toString());
			UrlLink urlLink = new UrlLink(urlLinkPayload);
			from.say(urlLink);
		}else if(AnswerTypeEnum.news_type.getCode().equals(respMsg.getAnswer_type())){
			String msg = respMsg.getMsg();
			List<Map<String,Object>> msgList = (List<Map<String, Object>>) JSONArray.parse(msg);
			JSONArray articleArray = (JSONArray) msgList.get(0).get("articles");
			JSONObject articles = (JSONObject) articleArray.get(0);
			UrlLinkPayload urlLinkPayload = new UrlLinkPayload(articles.getString("title"),articles.getString("url"));
			urlLinkPayload.setThumbnailUrl(articles.getString("pic_url"));
			urlLinkPayload.setDescription(articles.getString("description"));
			UrlLink urlLink = new UrlLink(urlLinkPayload);
			from.say(urlLink);
		}else{
			String msg = respMsg.getMsg();
			List<Map<String,Object>> msgList = (List<Map<String, Object>>) JSONArray.parse(msg);
			from.say(msgList.get(0).get("content").toString());
		}
	}
	public static void sendRoomMsg(Room room ,XwRespMsg respMsg){
		if(AnswerTypeEnum.music_type.getCode().equals(respMsg.getAnswer_type())){
			String msg = respMsg.getMsg();
			List<Map<String,Object>> songList = (List<Map<String, Object>>) JSONArray.parse(msg);
			UrlLinkPayload urlLinkPayload = new UrlLinkPayload(songList.get(0).get("song_name").toString(), songList.get(0).get("music_url").toString());
			urlLinkPayload.setThumbnailUrl(songList.get(0).get("pic_url").toString());
			urlLinkPayload.setDescription(songList.get(0).get("song_name").toString() + "-" + songList.get(0).get("singer_name").toString());
			UrlLink urlLink = new UrlLink(urlLinkPayload);
			room.say(urlLink);
		}else if(AnswerTypeEnum.news_type.getCode().equals(respMsg.getAnswer_type())){
			String msg = respMsg.getMsg();
			List<Map<String,Object>> msgList = (List<Map<String, Object>>) JSONArray.parse(msg);
			JSONArray articleArray = (JSONArray) msgList.get(0).get("articles");
			JSONObject articles = (JSONObject) articleArray.get(0);
			UrlLinkPayload urlLinkPayload = new UrlLinkPayload(articles.getString("title"),articles.getString("url"));
			urlLinkPayload.setThumbnailUrl(articles.getString("pic_url"));
			urlLinkPayload.setDescription(articles.getString("description"));
			UrlLink urlLink = new UrlLink(urlLinkPayload);
			room.say(urlLink);
		}else{
			String msg = respMsg.getMsg();
			List<Map<String,Object>> msgList = (List<Map<String, Object>>) JSONArray.parse(msg);
			room.say(msgList.get(0).get("content").toString());
		}
	}
}
