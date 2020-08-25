package com.smwsk.bot.task;

import com.smwsk.bot.constant.SysConstant;
import io.github.wechaty.Wechaty;
import io.github.wechaty.WechatyOptions;
import io.github.wechaty.schemas.ContactQueryFilter;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.manager.ContactManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/25 - 8:56
 * Description:
 */
public class ContactMsgThread implements Runnable{

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private ContactQueryFilter contactQueryFilter;
	private String msg;

	public ContactMsgThread(String msg, ContactQueryFilter contactQueryFilter) {
		this.msg = msg;
		this.contactQueryFilter = contactQueryFilter;
	}

	@Override
	public void run() {
		// 群发联系人
		ContactManager contactManager = SysConstant.wechatyBot.getContactManager();
		List<Contact> contactList = contactManager.findAll(this.contactQueryFilter);
		contactList.forEach(contact->{
			String name = contact.name();
			if(contact.stranger() != null){ // 联系人
				if(!contact.friend()){
					SysConstant.wechatyBot.getFriendshipManager().add(contact, "hello ");
				}
				logger.info("contactName: {}, contactId:{}, contactType:{}, friend:{}", name, contact.getId(), contact.type(), contact.friend());
			}else{
				contact.say(this.msg);
				logger.info("contactName: {}, sendMsg:{}, contactType:{}, friend:{}", name, this.msg, contact.type(), contact.friend());
			}
		});
	}
}
