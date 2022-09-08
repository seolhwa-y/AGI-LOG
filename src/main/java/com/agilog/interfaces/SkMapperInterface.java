package com.agilog.interfaces;

import java.util.List;

import com.agilog.beans.BebeCalendarBean;
import com.agilog.beans.HealthDiaryBean;

public interface SkMapperInterface {
	public String getHealthDiaryCode(HealthDiaryBean hb);
	
	public int insHDWeight(HealthDiaryBean hb);
	public int insHDHeight(HealthDiaryBean hb);
	public int insHDHead(HealthDiaryBean hb);
	public int insHDFoot(HealthDiaryBean hb);
	public int insHDTemp(HealthDiaryBean hb);
	public int insHDSleep(HealthDiaryBean hb);
	public int insHDdefecation(HealthDiaryBean hb);
	public int insHDDefstatus(HealthDiaryBean hb);
	public int insHDMeal(HealthDiaryBean hb);
	public int insHDMemo(HealthDiaryBean hb);

	public HealthDiaryBean getHealthDiaryDetail(HealthDiaryBean hb);
	
	public HealthDiaryBean getHealthMemo(HealthDiaryBean hb);
	public int insNewHDMemo(HealthDiaryBean hb);
	
	public List<BebeCalendarBean> checkWriteDD(BebeCalendarBean bcb);
	public List<BebeCalendarBean> checkWriteHD(BebeCalendarBean bcb);
	public List<BebeCalendarBean> checkReservation(BebeCalendarBean bcb);
	public List<BebeCalendarBean> checkSchedule(BebeCalendarBean bcb);
	public List<BebeCalendarBean> getBabyBirth(BebeCalendarBean bcb);
}
