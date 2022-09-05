package com.agilog.beans;

import java.util.List;

import lombok.Data;

@Data
public class MyPageBean {
	private String suCode;
	private String suName;
	private String suPw;
	private String suPhone;
	private String suEmail;
	private String suNickName;
	private String suAddress;
	private String suPhoto;
	private String suTheme;
	
	private String fColor;
	private String hColor;
	private String bColor;
	
	private List<BabyBean> babyList;
	
}
