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

	//컴퍼니 관련
	public ReservationBean getResInfo(CompanyBean cb);
	public CompanyBean getCompanyAccessAllInfo(CompanyBean cb);
	
	public int isManagerCode(CompanyBean cb);
	
	
	//예약 관련
	public int isResInfo(ReservationBean rb);
	public ReservationBean getResInfoForSms(ReservationBean rb);
	
	public int updRDRes(ReservationBean rb);
	public int updCPRes(ReservationBean rb);
	
	
	//데일리 다이어리 관련
	public int getDdCode(DailyDiaryBean db);
	public int getDdpCode(DailyDiaryBean db);
	public DailyDiaryBean getDdDate(DailyDiaryBean db);
	public DailyDiaryBean getDDFeed(DailyDiaryBean ddb);
	public DailyDiaryPhotoBean getDDPhoto(DailyDiaryBean ddb);
	
	public int insDd(DailyDiaryBean db);
	public int insDDP(DailyDiaryPhotoBean ddpb);
	
	public int updDDFeed(DailyDiaryBean ddb);
	
	public int delDd(DailyDiaryBean ddb);
	public int delDdLike(DailyDiaryBean ddb);
	public int delDdPhoto(DailyDiaryBean ddb);
	public int delDdComment(DailyDiaryBean ddb);
	
	
	//게시글 관련
	public int getFbCode();
	public int getFpCode(PostBean pb);
	public PostBean getTnC(PostBean pb);
	public PostBean getFbDate(PostBean pb);
	public PostBean getFbPostContent(PostBean pb);
	public List<PostBean> getFbPostList();
	public List<PostPhotoBean> getFbPp(PostBean pb);
	
	public int insFbPost(PostBean pb);
	public int insFp(PostPhotoBean ppb);

	public int updFbPost(PostBean pb);
	
	public int delFbPost(PostBean pb);
	public int delFbPostLike(PostBean pb);
	public int delFbPostPhoto(PostBean pb);
	public int delFbPostComment(PostBean pb);
}