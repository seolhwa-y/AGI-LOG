package com.agilog.beans;

import lombok.Data;

@Data
public class ReservationBean {
	public String suCode;
	public String bbCode;
	public String bbName;
	
	public String resCode;
	public String resAccess;
	
	public String coCode;
	public String coName;
	
	public String doCode;
	public String doName;
	
	public String rcCode;
	public String doComment;

	private String resDate;
	private String resCoName;
	private String resCoCode;
	private String resBbName;
}
