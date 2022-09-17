package com.agilog.interfaces;

import java.util.List;

import com.agilog.beans.BebeCalendarBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.MyPageBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.ReservationBean;

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
	
	public int isDdLike(DailyDiaryBean ddb);
	public int delDdLike(DailyDiaryBean ddb);
	public int insDdLike(DailyDiaryBean ddb);
	public int getDdLike(DailyDiaryBean ddb);
	public int updDdLike(DailyDiaryBean ddb);
	
	public int isFbLike(PostBean pb);
	public int delFbLike(PostBean pb);
	public int insFbLike(PostBean pb);
	public int getFbLike(PostBean pb);
	public int updFbLike(PostBean pb);
	
	public int isIbLike(PostBean pb);
	public int delIbLike(PostBean pb);
	public int insIbLike(PostBean pb);
	public int getIbLike(PostBean pb);
	public int updIbLike(PostBean pb);
	
	public List<ReservationBean> getResCount(CompanyBean cb);
	public List<ReservationBean> getDateResInfo(ReservationBean rb);
	
	public int updAddress(MyPageBean mpb);
	
	public List<ReservationBean> getDoctorResTime(ReservationBean rb);
}