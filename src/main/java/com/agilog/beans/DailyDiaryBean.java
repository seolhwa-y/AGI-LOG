package com.agilog.beans;

import lombok.Data;

@Data
public class DailyDiaryBean {
	private String suCode;
	private String ddCode;
	private String ddDate;
	private String ddContent;
	private String dpLink;
	
	private String ddStatus;
	
	private boolean isLike;
	private int likes;
	
}