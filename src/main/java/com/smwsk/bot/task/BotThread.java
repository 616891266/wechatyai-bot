package com.smwsk.bot.task;

import com.smwsk.bot.constant.SysConstant;
import com.smwsk.bot.entity.XwRespMsg;
import com.smwsk.bot.util.MsgUtils;
import com.smwsk.bot.util.XwUtils;
import io.github.wechaty.LoginListener;
import io.github.wechaty.RoomJoinListener;
import io.github.wechaty.RoomLeaveListener;
import io.github.wechaty.Wechaty;
import io.github.wechaty.io.github.wechaty.schemas.EventEnum;
import io.github.wechaty.schemas.FriendshipType;
import io.github.wechaty.schemas.RoomQueryFilter;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.ContactSelf;
import io.github.wechaty.user.Friendship;
import io.github.wechaty.user.Room;
import io.github.wechaty.user.manager.RoomManager;
import io.github.wechaty.utils.QrcodeUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 22:02
 * Description:
 */
public class BotThread implements Runnable {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String wechatToken;

	private XwUtils xwUtils;

	public BotThread(XwUtils xwUtils, String wechatToken) {
		this.xwUtils = xwUtils;
		this.wechatToken = wechatToken;
	}

	@Override
	public void run() {

		Wechaty wechaty = Wechaty.instance(this.wechatToken);
		SysConstant.wechatyBot = wechaty;
		// 监听扫描登录
		wechaty.onScan((qrcode, statusScanStatus, data) -> {
			logger.info("scan: {}", QrcodeUtils.getQr(qrcode));
		});

		// 监听退出的情况
		wechaty.on(EventEnum.LOGOUT, logOut->{
			logger.info("the bot logout: {}, logOutTime:{}", logOut, LocalDateTime.now());
			wechaty.stop();
			wechaty.start(true);
		});

		// 有人加入群的时候
		wechaty.onRoomJoin(new RoomJoinListener() {
			@Override
			public void handler(@NotNull Room room, @NotNull List<? extends Contact> list, @NotNull Contact contact, @NotNull Date date) {
				logger.info("joinRoomName: {}, contactListSize:{}, contactName: {}, dateTime:{}", room.getTopic(), list.size(), contact.name(), date);
			}
		});

		// 当有人离群的时候触发
		wechaty.onRoomLeave(new RoomLeaveListener(){
			@Override
			public void handler(@NotNull Room room, @NotNull List<? extends Contact> list, @NotNull Contact contact, @NotNull Date date) {
				logger.info("leaveRoomName: {}, contactListSize: {}, contactName: {}, dateTime:{}", room.getTopic(), list.size(), contact.name(), date);
			}
		});

		// 监听登录
		wechaty.onLogin(new LoginListener(){
			@Override
			public void handler(@NotNull ContactSelf contactSelf) {
				logger.info("login userName:{}", contactSelf.name());
				SysConstant.loginUserName = contactSelf.name();
			}
		});

		// 监听好友请求
		wechaty.on(EventEnum.FRIENDSHIP, friendshipList -> {
			logger.info("lister friendship: friendshipId:{}, time:{}", friendshipList, LocalDateTime.now());
			RoomManager roomManager = SysConstant.wechatyBot.getRoomManager();
			List<Room> roomList = roomManager.findAll(new RoomQueryFilter());
			for(Object friendshipObj : friendshipList){
				Friendship friendship = (Friendship) friendshipObj;
				if(friendship.type().equals(FriendshipType.Receive)){
					friendship.accept();
					Contact contact = friendship.contact();
					contact.say("hello");
					for(Room room : roomList){
						try {
							String topicName = room.getTopic().get();
							if("王者-交流群".equals(topicName)){
								room.add(contact);
								logger.info("room id:{}", topicName);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}

					}
				}
			}
		});
		// 监听发送消息
		wechaty.onMessage(message -> {
			logger.info("lister message, messageId:{}, isFromRoom:{}, fromName:{}, toName:{},  content:{}, time:{}", message.getId(), message.room() != null, message.from().name(), message.to().name(), message.text(), LocalDateTime.now());
			if(SysConstant.localMessageId.equals(message.getId())){ // 处理重复消息
				return;
			}
			SysConstant.localMessageId = message.getId();
			String text = message.text(); // 消息内容
			Contact from = message.from(); // 来自哪个用户的信息
			Contact contact = message.to(); // 发送给哪个用户
			Room room = message.room();

			if(SysConstant.loginUserName.equals(message.from().name())){ // 自己发送出去的消息不做处理
				return;
			}

			if (contact.name().contains("微信")
					|| from.name().contains("微信")
					|| from.name().contains("文件传输助手")) {
				return;
			}
			if (room != null && message.mentionSelf()) {
				String mentionText = message.mentionText();
				if(StringUtils.isEmpty(mentionText)){
					room.say("你想对我说什么呢？");
					return;
				}
				XwRespMsg respMsg = xwUtils.getRespMsg(from.name(),mentionText);
				if (respMsg != null) {
					MsgUtils.sendRoomMsg(room, respMsg);
				}
			} else if (room == null && contact != null) {
				XwRespMsg respMsg = xwUtils.getRespMsg(from.name(), text);
				if (respMsg != null) {
					MsgUtils.sendContactMsg(from, respMsg);
				}
			}
		});
		wechaty.start(true);
	}
}
