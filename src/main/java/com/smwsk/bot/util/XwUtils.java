package com.smwsk.bot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smwsk.bot.constant.SysConstant;
import com.smwsk.bot.entity.XwRespMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 16:58
 * Description:
 */
@Component
public class XwUtils {
	@Value("${xiaowei.token}")
	private String xwToken;

	@Value("${xiaowei.evn}")
	private String evn;

	final static Logger logger = LoggerFactory.getLogger(XwUtils.class);

	/**
	 * 根据文本获取响应消息
	 * @param sendMsg 用户发送消息
	 * @return
	 */
	public XwRespMsg getRespMsg(String sendMsg){
		String requestUrl = "https://openai.weixin.qq.com/openapi/aibot/"+ xwToken;
		logger.info("getMsgBy-start:{}", LocalDateTime.now());
		Map<String,Object> requestParams = new HashMap<>();
		requestParams.put("signature", SysConstant.xwSignature);
		requestParams.put("query", sendMsg);
		requestParams.put("env", evn);
		String resultInfo = HttpUtil.requestPost(requestUrl, requestParams);
		logger.info("getMsgBy request result:{}", resultInfo);
		XwRespMsg xwRespMsg = JSON.parseObject(resultInfo, XwRespMsg.class);
		return xwRespMsg;
	}


	public void getSignInfo(){
		String requestUrl = "https://openai.weixin.qq.com/openapi/sign/"+ xwToken;
		logger.info("updateSignature-start:{}", LocalDateTime.now());
		Map<String,Object> requestParams = new HashMap<>();
		requestParams.put("userid", "smwsk0");
		String resultInfo = HttpUtil.requestPost(requestUrl, requestParams);
		logger.info("request result:{}", resultInfo);
		JSONObject resultJson = (JSONObject) JSONObject.parse(resultInfo);
		if(resultJson != null){
			SysConstant.xwSignature = resultJson.getString("signature");
			logger.info("update xwSignature data:{}", SysConstant.xwSignature);
		}
	}

}
