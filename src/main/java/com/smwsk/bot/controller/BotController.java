package com.smwsk.bot.controller;

import com.smwsk.bot.constant.SysConstant;
import com.smwsk.bot.entity.RespMsg;
import io.github.wechaty.Wechaty;
import io.github.wechaty.schemas.RoomQueryFilter;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import io.github.wechaty.user.manager.FriendshipManager;
import io.github.wechaty.user.manager.RoomManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 19:50
 * Description:
 */
@RestController
@RequestMapping(value = "bot")
public class BotController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param topic 群名称
	 * @return
	 */
	@RequestMapping(value = "addContactByRoom")
	public RespMsg addContactByRoom(@RequestParam String topic) {
		RoomManager roomManager = SysConstant.wechatyBot.getRoomManager();
		RoomQueryFilter roomQueryFilter = new RoomQueryFilter();
		roomQueryFilter.setTopic(topic);
		List<Room> roomList = roomManager.findAll(roomQueryFilter);
		for (Room room : roomList) {
			List<Contact> contactList = room.memberList();
			try {
				logger.info("room name: {}", room.getTopic().get());
//					logger.info("addContactByRoom roomList - romId: {} , roomName: {} ,isFriend:{} ,contactId{}, contact: {}",room.getTopic().get(), room.getId(), room.getId() , item.friend(), item.getId(), item.name());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			contactList.forEach(item -> {
			});
		}
		Room room = roomManager.find(roomQueryFilter);
		if (room == null) {
			return RespMsg.success();
		}
		List<Contact> contactList = room.memberList();
		FriendshipManager friendshipManager = SysConstant.wechatyBot.getFriendshipManager();
		contactList.forEach(item -> {
			friendshipManager.add(item, "hello");
			logger.info("addContactByRoom - roomName: {},isFriend:{}  , alias:{}", topic, item.friend(), item.tags());
		});
		return RespMsg.success();
	}

}
