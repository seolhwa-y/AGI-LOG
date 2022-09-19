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
import com.agilog.beans.DoctorBean;
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.MyPageBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.PostCommentBean;
import com.agilog.beans.ReservationBean;
import com.agilog.beans.ScheduleBean;

public interface JpMapperInterface {
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
	public String isCompanyMember(CompanyBean cb);
	public String isCompanyAccess(CompanyBean cb);
	public int insCompanyAuthLog(CompanyBean cb);
	public CompanyBean getCompanyAccessInfo(CompanyBean cb);
	public int insSocialAuthLog(AuthBean ab);
	
	public int isDoctorCode(CompanyBean cb);
	public int getPatientInfo(ReservationBean rb);
	//
	public AuthBean getAccessInfo(AuthBean ab);
	//
	public List<DoctorBean> getDoctorInfo(DoctorBean db);
	public List<ReservationBean> getPatientInfoList(ReservationBean rb);
	public List<ReservationBean> getReservationInfo(ReservationBean rb);
	public List<ReservationBean> getReservationCategory(ReservationBean rb);
	public List<HealthDiaryBean> getHealthDataList(HealthDiaryBean hb);
	public ReservationBean getPatientComment(ReservationBean rb);
	public int isPrivateData (ReservationBean rb);
	public String isResOpenData (ReservationBean rb);
	public String checkDoctorCode(CompanyBean cb);
	public String isDoctorMember(DoctorBean db);
	public int isDoctorCode(DoctorBean db);
	public int updReservation(CompanyBean cb);
	public int insDoctorInfo(CompanyBean cb);
	public int delDoctorInfo(CompanyBean cb);
	public int insDoctorComment(ReservationBean rb);
	public int updReservation(ReservationBean rb);
	//
	public List<BebeMapCommentBean> getCompanyInfoComment(BebeMapCommentBean bmcb);
	public int insCompanyComment(BebeMapCommentBean bmcb);
	public int delMapComment(BebeMapCommentBean bmcb);
	public int insReservationList(ReservationBean rb);
	//
	public List<DailyDiaryBean> getDailyDiaryList();
	public DailyDiaryBean getDailyDiaryFeed(DailyDiaryBean ddb);
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
	
	public List<DailyDiaryBean> getMyDailyDiaryFeedNew(DailyDiaryBean dd);
	public List<DailyDiaryBean> getMyDailyDiaryFeedOld(DailyDiaryBean dd);
	public List<DailyDiaryBean> getMyDailyDiaryFeedLike(DailyDiaryBean dd);
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
	//
	//게시판목록
	public List<PostBean> getBebeInfo(PostBean pb);
	public PostBean getBebePost(PostBean pb);
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
	public int delSchecule(BebeCalendarBean bcb);
	public int updSchedule(BebeCalendarBean bcb);
	public int insSchedule(ScheduleBean sb);
	public int delReservation(ReservationBean rb);
	public HealthDiaryBean getHealthDiaryFeed(HealthDiaryBean hb);
	public List<DailyDiaryBean> getTrendHashTags();
	public List<DailyDiaryBean> getHashTagFeed(DailyDiaryBean ddb);
	public int updPost(PostBean pb);
	public int updDiaryVIew();
	public int insHashTag(DailyDiaryBean ddb);

}
