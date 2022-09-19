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

public interface JsMapperInterface {
	public String isMember(AuthBean ab);
	public int isAccess(AuthBean ab);
	public int insAuthLog(AuthBean ab);
	public AuthBean getUserEmail(String userEmail);
	public String getUserId(AuthBean ab);
	public int updUserPassword(AuthBean ab);
	public int checkCompanyCodeEmail(CompanyBean cb);
	public int updNewpw(AuthBean ab);
	public int insNaverMember(AuthBean ab);
	public int insKakaoMember(AuthBean ab);
	public int insSocialAccessLog(AuthBean ab);
	public int insPersonalJoin(AuthBean ab);
	public String getSocialUserCode();
	public int insSocialUser(AuthBean ab);
	public int insCompanyInfo(CompanyBean cb);  
	public CompanyBean isCompanyMember(CompanyBean cb);
	public String isCompanyAccess(CompanyBean cb);
	public int insCompanyAuthLog(CompanyBean cb);
	public CompanyBean getCompanyAccessInfo(CompanyBean cb);
	public int insSocialAuthLog(AuthBean ab);
	public int insDoctorInfo(CompanyBean cb);
	public int isDoctorCode(CompanyBean cb);
	public int getPatientInfo(ReservationBean rb);
	//
	public AuthBean getAccessInfo(AuthBean ab);
	public String checkNickName(AuthBean ab);
	//
	public List<CompanyBean> getDoctorInfo(CompanyBean cb);
	public List<ReservationBean> getReservationInfo(ReservationBean rb);
	public List<ReservationBean> getReservationCategory(ReservationBean rb);
	public int updReservation(CompanyBean cb);
	public int delDoctor(CompanyBean cb);
	public int insDoctorComment(ReservationBean rb);
	public int updReservation(ReservationBean rb);
	//
	public List<BebeMapCommentBean> getCompanyInfoComment(BebeMapCommentBean bmcb);
	public int insCompanyComment(BebeMapCommentBean bmcb);
	public int delMapComment(BebeMapCommentBean bmcb);
	public int insReservationList(ReservationBean rb);
	//
	public List<DailyDiaryBean> getDailyDiaryList();
	public DailyDiaryBean getDailyDiaryFeed();
	public List<DailyDiaryCommentBean> getFeedComment(DailyDiaryCommentBean ddcb);
	public List<DailyDiaryBean> getMyDailyDiary(AuthBean ab);
	public int insDailyDiary(DailyDiaryBean ddb);
	public int insDailyDiaryComment(DailyDiaryCommentBean ddcb);
	public int updComment(DailyDiaryCommentBean ddcb);
	public int delDailyDiaryComment(DailyDiaryCommentBean ddcb);
	public int updMyDailyDiary(DailyDiaryBean ddb);
	public DailyDiaryBean getMyDailyDiary(DailyDiaryBean ddb);
	public List<DailyDiaryCommentBean> getMyDailyDiaryComment(DailyDiaryCommentBean ddcb);
	public int delDailyDiary(DailyDiaryBean ddb);
	//
	public MyPageBean getMyInfo(String userCode);
	public List<BabyBean> getBabyInfoList(String userCode);
	public String isPersonalPassword(AuthBean ab);
	public int updTheme(AuthBean ab);
	public int updUserPassword(MyPageBean mb);
	public int updUserPhone(MyPageBean mb);
	public int updBabyBirthDay(BabyBean bb);
	public int updBabyHeight(BabyBean bb);
	public int updBabyWeight(BabyBean bb);
	public String getNewBabyCode(AuthBean ab);
	public int insBabyInfo(BabyBean bb);
	public int updProfile(MyPageBean mb);
	//
	public int delHealthDiary(HealthDiaryBean hb);
	public List<HealthDiaryBean> getHealthDiary(AuthBean ab);
	public List<BabyBean> getTotalBabyCode(AuthBean ab);
	public List<HealthDiaryBean> getBabyHealthInfo(BabyBean bb);
	public int insHealthDiary(HealthDiaryBean hb);
	public int updMyHealthDiary(HealthDiaryBean hb);
	public List<ReservationBean> getDoctorCommentList(ReservationBean rb);
	//
	public List<BoardBean> getNewestTotalPost();
	public List<BoardBean> getChangeSort(BoardBean bb);
	public List<BoardBean> getPostList(BoardBean bb);
	public List<BoardBean> getFreeBoard(BoardBean bb);
	public List<BoardBean> getMeetingBoard(BoardBean bb);
	public List<BoardBean> getBebeInfo(BoardBean bb);
	public PostBean getPostInfo(PostBean pb);
	public List<BoardBean> getSearchWriter(BoardBean bb);
	public List<BoardBean> getSearchTitle(BoardBean bb);
	public String getPostCommentCode(PostCommentBean pcb);
	public int insPostComment(PostCommentBean pcb);
	public int updBoardComment(PostCommentBean pcb);
	public int delBoardComment(PostCommentBean pcb);
	public String getPostCode(PostBean pb);
	public int insPost(PostBean pb);
	public int delPost(PostBean pb);
	//
	public List<BebeCalendarBean> getMonthDiary(BebeCalendarBean bcb);
	public BebeCalendarBean getReservation(BebeCalendarBean bcb);
	public List<BebeCalendarBean> getSchedule(BebeCalendarBean bcb);
	public int delSchecule(ScheduleBean sb);
	public int updSchedule(ScheduleBean sb);
	public int insSchedule(ScheduleBean sb);
	public int delReservation(ReservationBean rb);
	public HealthDiaryBean getHealthDiaryFeed(HealthDiaryBean hb);
	public List<DailyDiaryBean> getTrendHashTags();
	public List<DailyDiaryBean> getHashTagFeed(DailyDiaryBean ddb);
	public int updPost(PostBean pb);
	public int updDiaryVIew();
	public int insHashTag(DailyDiaryBean ddb);
	
	// 지수 추가
	public int updSocialUserPhoto(AuthBean ab);
	public int updBabyPhoto(BabyBean bb);
	public int insHDWeight(HealthDiaryBean hdb);
	public int insHDHeight(HealthDiaryBean hdb);
	public String isWriteHd(BebeCalendarBean bcb);
	public String isWriteWd(BebeCalendarBean bcb);
	public ReservationBean getReservationInfo(BebeCalendarBean bcb);
	public List<ScheduleBean> getScheduleList(BebeCalendarBean bcb);
	public String getNewScheduleCode(ScheduleBean sb);
	public int updateReservationStatus(ReservationBean rb);
//	public List<ReservationBean> getCoResList(ReservationBean rb);
//  public int insResTIme(ReservationBean rb);
//  public int delResTime(ReservationBean rb);
}
