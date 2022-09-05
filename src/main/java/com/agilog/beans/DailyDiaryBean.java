package com.agilog.beans;

import java.util.List;

import lombok.Data;

@Data
public class DailyDiaryBean {
	private String ddCode;
	private String ddDate;
	private String suCode;
	private String ddStasus;
	private String ddContent;
	private String ddview;
	private String ddLike;
	
	private List<DailyDiaryCommentBean> ddc;
	private List<DailyDiaryPhotoBean> ddp;
}
