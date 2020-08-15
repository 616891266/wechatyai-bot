package com.smwsk.bot.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: Wang Shao Kui
 * Create date: 2020/8/14 - 16:57
 * Description:
 */
@Data
public class XwRespMsg implements Serializable {
	private String ans_node_name;
	private String title;
	private String answer;
	private String answer_type;
	private String msg;
	private String from_user_name;
	private String to_user_name;
	private String status;
}
