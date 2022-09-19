package com.agilog.beans;

import java.util.List;

import lombok.Data;

@Data
public class BebeMapBean {
	private String name;
	private String address;
	private String tell;
	private String info;
	private Double x;
	private Double y;
	
	private List <BebeMapCommentBean> bcList;
}
