package com.agilog.beans;

import lombok.Data;

@Data
public class DailyDiaryBean {
	private String dpLink;
	private String ddCode;
	private String suCode;
	private String ddDate;
	private String ddStatus;
	private String ddContent;
	private String ddView;
	private boolean isLike;
	private int likes;
}
