package com.smwsk.bot.enums;

import lombok.Data;
import lombok.Getter;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 16:12
 * Description:
 */
@Getter
public enum AnswerTypeEnum {
	text_type("text","文本"),
	music_type("music","音乐"),
	news_type("news","新闻");
	private String code;
	private String remark;

	AnswerTypeEnum(String code, String remark) {
		this.code = code;
		this.remark = remark;
	}

}
