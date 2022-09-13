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
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.MyPageBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.PostCommentBean;
import com.agilog.beans.ReservationBean;
import com.agilog.beans.ScheduleBean;

public interface ThMapperInterface {

	public CompanyBean getCompanyAccessAllInfo(CompanyBean cb);
	public int isManagerCode(CompanyBean cb);
	public ReservationBean getResInfo(CompanyBean cb);
	public int updRDRes(ReservationBean rb);
	public int updCPRes(ReservationBean rb);
	public int getFbCode(PostBean pb);
	public int insFbPost(PostBean pb);
	public List<PostBean> getPostList(PostBean pb);
}
