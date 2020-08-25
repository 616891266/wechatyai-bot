package com.smwsk.bot.controller;

import com.smwsk.bot.entity.RespMsg;
import com.smwsk.bot.service.TianQingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/24 - 22:13
 * Description:
 */
@RestController
@RequestMapping(value = "botServiceTest")
public class BotServiceTestController {

	@Autowired
	private TianQingService tianQingService;

	@RequestMapping(value = "testEnglishWordVeryDay")
	public RespMsg testEnglishWordVeryDay(){
		tianQingService.onEnglishWordVeryDay();
		return RespMsg.success();
	}



}
