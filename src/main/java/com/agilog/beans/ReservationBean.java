package com.agilog.beans;

import lombok.Data;

@Data
public class ReservationBean {
	public String resSuCode;
	public String resBbCode;
	public String resSuName;
	public String resSuPhone;
	
	
	public String resCode;
	public String resAccess;
	
	public String resDoCode;
	public String resDoName;
	
	public String rcCode;
	public String doComment;

	private String resDate;
	private String resCoName;
	private String resCoCode;
	private String resBbName;
	//예약상태
	private String resAction;
	private String resActionName;
	private String resMemo;
	
	private int resCount;
	private String resTime;
	private String resReal;
}
