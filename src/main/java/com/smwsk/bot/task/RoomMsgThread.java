package com.smwsk.bot.task;

import com.smwsk.bot.constant.SysConstant;
import io.github.wechaty.schemas.RoomQueryFilter;
import io.github.wechaty.user.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/25 - 8:58
 * Description:
 */
public class RoomMsgThread implements Runnable{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private String msg;
	private RoomQueryFilter roomQueryFilter;

	public RoomMsgThread(String msg, RoomQueryFilter roomQueryFilter) {
		this.msg = msg;
		this.roomQueryFilter = roomQueryFilter;
	}

	@Override
	public void run() {
		// 群发
		List<Room> roomList = SysConstant.wechatyBot.getRoomManager().findAll(this.roomQueryFilter);
		roomList.forEach(room ->{
			try {
				logger.info("roomName:{}", room.getTopic().get());
				room.say(this.msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		});
	}
}
