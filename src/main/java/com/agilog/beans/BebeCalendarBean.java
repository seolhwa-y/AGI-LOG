package com.agilog.beans;

import java.util.List;

import lombok.Data;

@Data
public class BebeCalendarBean {
	private String month;
	private String date;
	private boolean dailyDiary;
	private String ddDate;
	private boolean healthDiary;
	private String hdDate;
	private boolean schedule;
	private String scDate;
	private boolean reservation;
	private String resDate;
	private List<ReservationBean> reservationInfo;
	private String babyBirthDay;
	private String babyName;
	private List<ScheduleBean> scheduleList;
	private String suCode;
}
