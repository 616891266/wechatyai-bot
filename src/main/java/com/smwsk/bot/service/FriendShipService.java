package com.smwsk.bot.service;

import com.smwsk.bot.constant.SysConstant;
import io.github.wechaty.schemas.ContactQueryFilter;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.manager.ContactManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 好友关系服务
 * Author: Wang Shao Kui
 * Create date: 2020/8/25 - 9:53
 * Description:
 */
@Service
public class FriendShipService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 自动添加联系人
	 */
	@Scheduled(cron = "0 0 8 * * ?")
	public void autoAddFriendShip(){
		ContactManager contactManager = SysConstant.wechatyBot.getContactManager();
		List<Contact> contactList = contactManager.findAll(new ContactQueryFilter());
		contactList.forEach(contact->{
			String name = contact.name();
			if(contact.stranger() != null){ // 联系人
				if(!contact.friend()){
					SysConstant.wechatyBot.getFriendshipManager().add(contact, "贴心小棉袄 为你提供服务");
					logger.info("contactName: {}, contactId:{}, contactType:{}, friend:{}", name, contact.getId(), contact.type(), contact.friend());
					try {
						TimeUnit.SECONDS.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

}
