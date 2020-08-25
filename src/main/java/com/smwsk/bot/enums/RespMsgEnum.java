package com.smwsk.bot.enums;

import lombok.Getter;

@Getter
public enum RespMsgEnum {
	SUCCESS(1000,"操作成功"),
	FAIL(1001,"操作失败")
	;

	RespMsgEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	private Integer code;
	private String message;

}
