package com.agilog.interfaces;

import java.util.List;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BabyBean;
import com.agilog.beans.BebeCalendarBean;
import com.agilog.beans.BebeMapCommentBean;
import com.agilog.beans.BoardBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.DailyDiaryCommentBean;
import com.agilog.beans.DailyDiaryPhotoBean;
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.MyPageBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.PostCommentBean;
import com.agilog.beans.ReservationBean;
import com.agilog.beans.ScheduleBean;

public interface ShMapperInterface {
	// 메인 : 감성일기 피드 사진 불러오기
	public List<DailyDiaryPhotoBean> getDairyDiaryPhoto();
	// 건강 추세 : 아이 리스트 불러오기
	public List<BabyBean> getTotalBabyCode(AuthBean ab);
	// 건강 추세 : 내 아이 건강 데이터 나이별로 가져오기
	public List<HealthDiaryBean> getHealthStatusList(AuthBean ab);
}
