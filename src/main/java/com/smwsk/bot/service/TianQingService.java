package com.smwsk.bot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smwsk.bot.constant.SysConstant;
import com.smwsk.bot.entity.ApiResultEnum;
import com.smwsk.bot.task.ContactMsgThread;
import com.smwsk.bot.task.RoomMsgThread;
import com.smwsk.bot.util.HttpUtil;
import io.github.wechaty.Wechaty;
import io.github.wechaty.eventEmitter.Event;
import io.github.wechaty.schemas.ContactQueryFilter;
import io.github.wechaty.schemas.RoomQueryFilter;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import io.github.wechaty.user.manager.ContactManager;
import io.github.wechaty.user.manager.FriendshipManager;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/24 - 15:10
 * Description:
 */
@Service
public class TianQingService {

	@Value("${tianxing.apiKey}")
	private String apiKey;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 每日一句英语
	 */
	@Scheduled(cron = "0 0 9 * * ?")
	public void onEnglishWordVeryDay(){
		logger.info("onEnglishWordVeryDay start time:", LocalDateTime.now());
		String request = "http://api.tianapi.com/txapi/ensentence/index";
		Map<String,Object> requestParams = new HashMap<>();
		requestParams.put("key", apiKey);
		String resultInfo = HttpUtil.requestGet(request, requestParams);
		logger.info("request time:{} result: {}", LocalDateTime.now(), resultInfo);
		JSONObject resultObj = JSON.parseObject(resultInfo, JSONObject.class);
		if(ApiResultEnum.SUCCESS.getCode().equals(resultObj.getString("code"))){
			JSONArray newslist = resultObj.getJSONArray("newslist");
			newslist.forEach(item->{
				JSONObject jsonObject = (JSONObject) item;
				String en = jsonObject.getString("en");
				String zh = jsonObject.getString("zh");
				logger.info("request result time:{}, en:{}, zh:{}", LocalDateTime.now(), en, zh);
				String englishMsg = "每日一句英语提升自己: \n" + en + " \n" + zh;
				new ContactMsgThread(englishMsg, new ContactQueryFilter()).run();
				new RoomMsgThread(englishMsg, new RoomQueryFilter()).run();
			});
		}
	}

	public void drinkWater(){
		logger.info("drinkWater time:{}", LocalDateTime.now());
	}


}
