package com.smwsk.bot.service;

import com.smwsk.bot.task.ContactMsgThread;
import com.smwsk.bot.task.RoomMsgThread;
import io.github.wechaty.schemas.ContactQueryFilter;
import io.github.wechaty.schemas.RoomQueryFilter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/25 - 10:17
 * Description:
 */
@Service
public class WaterService {

	/**
	 * 提醒喝水
	 */
	@Scheduled(cron = "0 0 9-16 * * ? ")
	public void tipDrinkWater(){
		new ContactMsgThread("停下手里的工作、起来喝杯水吧┏(゜ω゜)=☞", new ContactQueryFilter()).run();
		new RoomMsgThread("停下手里的工作、起来喝杯水吧┏(゜ω゜)=☞", new RoomQueryFilter()).run();
	}

}
