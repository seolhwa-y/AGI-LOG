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
	
	
	// 게시판 댓글 코드 MAX + 1  ::  나중에 시퀀스 사용하기
	public String getFcCode(PostCommentBean pcb);
	// 게시판 댓글 조회
	public List<PostCommentBean> getPostCommentList(PostCommentBean pcb);
	// 게시판 댓글 등록
	public int insPostCommnet(PostCommentBean pcb);
	// 게시판 댓글 수정
	public int updPostComment(PostCommentBean pcb);
	// 게시판 댓글 삭제
	public int delPostComment(PostCommentBean pcb);
	
	
	// 감성일기 댓글 코드 MAX + 1  ::  나중에 시퀀스 사용하기
	public String getDcCode(DailyDiaryCommentBean ddcb);
	// 감성일기 댓글 조회
	public List<DailyDiaryCommentBean> getDailyDiaryComment(DailyDiaryCommentBean ddcb);
	// 감성일기 댓글 등록
	public int insDailyDiaryComment(DailyDiaryCommentBean ddcb);
	// 감성일기 댓글 수정
	public int updDailyDiaryComment(DailyDiaryCommentBean ddcb);
	// 감성일기 댓글 삭제
	public int delDailyDiaryComment(DailyDiaryCommentBean ddcb);
	
	// 지도 본인 지역 확인
	public AuthBean getSuAddress(AuthBean ab);
	// 지도 기업 연계 정보 조회
	public String getCoCode(CompanyBean cb);
	// 지도 댓글 조회
	public List<BebeMapCommentBean> getMapCommentList(CompanyBean cb);
	
	// 지도 댓글 코드 MAX + 1  ::  나중에 시퀀스 사용하기
	public String getMcCode(BebeMapCommentBean bmcb);

	// 지도 댓글 등록
	public int insMapComment(BebeMapCommentBean bmcb);
	// 지도 댓글 수정
	public int updMapComment(BebeMapCommentBean bmcb);
	// 지도 댓글 삭제
	public int delMapComment(BebeMapCommentBean bmcb);

	// 지도 예약 가능 일정 조회
	public List<ReservationBean> getReservationList(ReservationBean rb);
	// 지도 예약 코드 MAX + 1
	public String getResCode(ReservationBean rb);
	// 지도 예약일정 등록
	public int insReservationList(ReservationBean rb);
	// 지도 예약 인원수 MAX + 1
	public int getResCountPlus(ReservationBean rb);
	// 지도 예약인원 수정
	public int updResTime(ReservationBean rb);
}