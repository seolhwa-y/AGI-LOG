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
import com.agilog.beans.PostPhotoBean;
import com.agilog.beans.ReservationBean;
import com.agilog.beans.ScheduleBean;

public interface ThMapperInterface {

	public CompanyBean getCompanyAccessAllInfo(CompanyBean cb);
	public int isManagerCode(CompanyBean cb);
	public ReservationBean getResInfo(CompanyBean cb);
	public int updRDRes(ReservationBean rb);
	public int updCPRes(ReservationBean rb);
	public int getFbCode();
	public int insFbPost(PostBean pb);
	public List<PostBean> getFbPostList();
	public PostBean getFbPostContent(PostBean pb);
	public int getDdCode(DailyDiaryBean db);
	public int insDd(DailyDiaryBean db);
	public DailyDiaryBean getDDFeed(DailyDiaryBean ddb);
	public DailyDiaryBean getDDPhoto(DailyDiaryBean ddb);
	public int updDDFeed(DailyDiaryBean ddb);
	public int delDd(DailyDiaryBean ddb);
	public ReservationBean getResInfoForSms(ReservationBean rb);
	public int getFpCode(PostBean pb);
	public int insFp(PostPhotoBean ppb);
	public int delFbPost(PostBean pb);
	public int delFbPostPhoto(PostPhotoBean ppb);
	public PostBean getFbDate(PostBean pb);
	public PostBean getTnC(PostBean pb);
	public int updFbPost(PostBean pb);
	public DailyDiaryBean getDdDate(DailyDiaryBean db);
	public int insDDP(DailyDiaryPhotoBean ddpb);
	public int delDdPhoto(DailyDiaryPhotoBean ddpb);
	public int getDdpCode(DailyDiaryBean db);
	public List<PostPhotoBean> getFbPp(PostPhotoBean ppb);
}