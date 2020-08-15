package com.smwsk.bot.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.smwsk.bot.constant.SysConstant;
import com.smwsk.bot.entity.XwRespMsg;
import com.smwsk.bot.enums.AnswerTypeEnum;
import com.smwsk.bot.task.BotThread;
import com.smwsk.bot.util.MsgUtils;
import com.smwsk.bot.util.XwUtils;
import io.github.wechaty.Wechaty;
import io.github.wechaty.filebox.FileBox;
import io.github.wechaty.schemas.UrlLinkPayload;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import io.github.wechaty.user.UrlLink;
import io.github.wechaty.utils.QrcodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 15:23
 * Description:
 */
@Component
public class InitBotConfig implements CommandLineRunner {

	@Value("${wechat.token}")
	private String wechatyToken;

	@Autowired
	private XwUtils xwUtils;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void run(String... args) throws Exception {
		xwUtils.getSignInfo();
		logger.info("initbot 初始化机器人");
		SysConstant.wechatyBot = Wechaty.instance(wechatyToken);

		new BotThread(xwUtils).run();

	}

}
