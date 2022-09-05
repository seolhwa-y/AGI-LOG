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
	public List<DailyDiaryPhotoBean> getDairyDiaryPhoto();
	public List<BabyBean> getTotalBabyCode(AuthBean ab);

}
