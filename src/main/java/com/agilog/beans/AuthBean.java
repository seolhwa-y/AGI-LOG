package com.agilog.beans;

import lombok.Data;

@Data
public class AuthBean {
	private String suCode;
	private String suName;
	private String suPhone;
	private String suEmail;
	private String suNickName;
	
	private String alDate;
	private int alAction;
	
	private String type;
}
