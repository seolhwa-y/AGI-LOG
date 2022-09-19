package com.agilog.beans;

import lombok.Data;

@Data
public class PostBean {
	private String fbCode;
	private String fbDate;
	private String fbSuCode;
	private String fbSuName;
	private String fbTitle;
	private String fbContent;
	private int fbView;
//	private int fbLike;
	
	private String ibCode;
	private String ibDate;
	private String ibTitle;
	private String ibContent;
	private int ibView;
//	private int ibLike;
	
	private String ibSort;
	private int pageNum;
	
	private int likes; //좋아요 개수
	private boolean isLike; //좋아요 여부
	
	private String suCode;
}
