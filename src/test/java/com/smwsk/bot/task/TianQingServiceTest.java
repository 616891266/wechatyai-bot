package com.smwsk.bot.task;

import com.smwsk.bot.BaseTestConfig;
import com.smwsk.bot.service.TianQingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TianQingServiceTest extends BaseTestConfig {

	@Autowired
	private TianQingService tianQingService;

	@Test
	void onEnglishWordVeryDay() {
		tianQingService.onEnglishWordVeryDay();
	}

	@Test
	void drinkWater() {
		tianQingService.drinkWater();
	}
}