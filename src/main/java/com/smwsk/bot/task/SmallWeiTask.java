package com.smwsk.bot.task;

import com.alibaba.fastjson.JSONObject;
import com.smwsk.bot.constant.SysConstant;
import com.smwsk.bot.entity.XwRespMsg;
import com.smwsk.bot.util.HttpUtil;
import com.smwsk.bot.util.XwUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 小微机器人
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 16:06
 * Description:
 */
@Service
public class SmallWeiTask {

	@Autowired
	private XwUtils xwUtils;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Scheduled(cron = "0 0/10 * * * ?")
	public void updateSignature(){
		xwUtils.getSignInfo();
	}

}
