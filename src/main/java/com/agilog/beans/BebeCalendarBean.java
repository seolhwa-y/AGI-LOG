package com.agilog.beans;

import java.util.List;

import lombok.Data;

@Data
public class BebeCalendarBean {
	private String month;
	private String date;
	private boolean dailyDiary;
	private boolean healthDiary;
	private boolean schedule;
	private List<ReservationBean> reservationInfo;
	private String babyBirthDay;
	private List<ScheduleBean> scheduleList;
	private String suCode;
}
