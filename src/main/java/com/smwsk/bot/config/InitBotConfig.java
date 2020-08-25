package com.smwsk.bot.config;

import com.smwsk.bot.constant.SysConstant;
import com.smwsk.bot.task.BotThread;
import com.smwsk.bot.util.XwUtils;
import io.github.wechaty.Wechaty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
		logger.info("initbot 初始化机器人");
		new BotThread(xwUtils, wechatyToken).run();
	}

}
