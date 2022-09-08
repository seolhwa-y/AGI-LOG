package com.agilog.beans;

import lombok.Data;

@Data
public class HealthDiaryBean {
	private String suCode;
	
	private String bbCode;
	private String bbName;
	
	private String hdCode;
	private String hdDate;
	private String hdValue;
	
	private String bbWeight;
	private String bbHeight;
	private String head;
	private String foot;
	private String temperature;
	private String sleep;
	private String defecation;
	private String defstatus;
	private String meal;
	private String memo;
	
	private String caCode;
	private String caName;
	private String caType;
	
	private int age;
	private int rnk;
}
