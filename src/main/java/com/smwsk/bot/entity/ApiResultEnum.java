package com.smwsk.bot.entity;

import lombok.Getter;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/21 - 22:21
 * Description:
 */
@Getter
public enum ApiResultEnum {
	FAIL("-1","数据获取失败"),
	SUCCESS("200","数据获取成功"),
	IP_ADDRESS("401","IP没有添加白名单"),
	VISITS_EXCEEDED("429","访问次数超限"),
	LossOfKeyParameter("402","关键参数丢失"),
	REQUEST_METHOD_ERROR("405","请求方式不对");


	ApiResultEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	private String code;

	private String message;

}
