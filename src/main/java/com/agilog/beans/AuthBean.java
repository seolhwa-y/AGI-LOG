package com.agilog.beans;

import java.util.List;

import lombok.Data;

@Data
public class AuthBean {
	private String suCode;
	private String suName;
	private String suPhone;
	private String suEmail;
	private String suNickName;
	private String suAddress;
	private String suPhoto;
	private String suTheme;
	private String alDate;
	private int alAction;
	
	private String type;
	
	private List<BabyBean> babyList;
	
}
