package com.agilog.beans;

import lombok.Data;

@Data
public class DailyDiaryBean {
	private String suCode;
	private String ddSuName;
	private String ddCode;
	private String ddDate;
	private String ddContent;
	private String dpLink;
	
	private String ddStatus;
	private String ddSuCode;
	
	private boolean isLike;
	private int likes;
	
	private String moveWrite;
	private String ddSort;
}