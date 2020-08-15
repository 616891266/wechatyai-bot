package com.smwsk.bot.task;

import com.smwsk.bot.constant.SysConstant;
import com.smwsk.bot.entity.XwRespMsg;
import com.smwsk.bot.util.MsgUtils;
import com.smwsk.bot.util.XwUtils;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Room;
import io.github.wechaty.utils.QrcodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 22:02
 * Description:
 */
public class BotThread implements Runnable {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private XwUtils xwUtils;

	public BotThread(XwUtils xwUtils) {
		this.xwUtils = xwUtils;
	}

	@Override
	public void run() {

		SysConstant.wechatyBot.onScan((qrcode, statusScanStatus, data) -> {
			System.out.println(QrcodeUtils.getQr(qrcode));
		});

		SysConstant.wechatyBot.onMessage(message -> {
			logger.info("lister message: fromName:{}, toName:{}, time:{}", message.from().name(), message.to().name(), LocalDateTime.now());
			String text = message.text();
			Contact from = message.from();
			Contact contact = message.to();
			Room room = message.room();
			if (contact.name().contains("微信") || from.name().contains("微信")) {
				return;
			}
			if (room != null && message.mentionSelf()) {
				String mentionText = message.mentionText();
				XwRespMsg respMsg = xwUtils.getRespMsg(mentionText);
				if (respMsg != null) {
					MsgUtils.sendMsg(room, respMsg);
				}
			} else if(room == null){
				XwRespMsg respMsg = xwUtils.getRespMsg(text);
				if (respMsg != null) {
					MsgUtils.sendMsg(from, respMsg);
				}
			}
		});
		SysConstant.wechatyBot.start(true);
	}
}
