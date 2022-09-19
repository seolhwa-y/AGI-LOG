package com.agilog.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.BoardBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.DoctorBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.ReservationBean;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.JpDailyDiary;

@Controller
public class JpHomeController {
	@Autowired
	private Company company;
	@Autowired
	private Board board;
	@Autowired
	private JpDailyDiary dailyDiary;
	
	//예약관리 관리자페이지 이동 : 재필
	@RequestMapping(value = "/CheckManager", method = RequestMethod.POST)
	public ModelAndView checkManager(ModelAndView mav,@ModelAttribute CompanyBean cb) {
		mav.addObject(cb);
		this.company.backController(mav, 71);
		
		return mav;
	}
	//환자관리 의사인증 페이지 이동 : 재필
	@RequestMapping(value = "/IntoCheckDoctor", method = RequestMethod.GET)
	public ModelAndView intoCheckDoctor(ModelAndView mav) {
		mav.setViewName("checkDoctor");
		return mav;
	}
	
	
	//전문의 관리 페이지 이동
	@RequestMapping(value = "/MoveDoctorManagement", method = RequestMethod.GET)
	public ModelAndView moveDoctorManagementCtl(ModelAndView mav, @ModelAttribute CompanyBean cb) {
		mav.addObject(cb);
		this.company.backController(mav, 77);
		return mav;
	}
	
	//예약관리 페이지 이동
	@RequestMapping(value = "/MoveReservationManagement", method = RequestMethod.GET)
	public ModelAndView moveReservationManagementCtl(HttpServletRequest req,ModelAndView mav, @ModelAttribute CompanyBean cb) {
		mav.addObject(cb);
		this.company.backController(mav, 70);
		return mav;
	}
	//환자관리 페이지 이동
	@RequestMapping(value = "/MovePatientManagement", method = RequestMethod.POST)
	public ModelAndView movePatientManagementCtl(ModelAndView mav, @ModelAttribute DoctorBean db) {
		mav.addObject(db);
		this.company.backController(mav, 81);
		return mav;
	}
	//환자관리 -> 건강기록지 페이지 이동
	@RequestMapping(value = "/MoveHealthDataList", method = RequestMethod.POST)
	public ModelAndView moveHealthDataList(ModelAndView mav, @ModelAttribute ReservationBean rb) {
		mav.addObject(rb);
		this.company.backController(mav, 83);
		return mav;
	}
	//의사 소견 입력 작업
	@RequestMapping(value = "/InsertDoctorComment", method = RequestMethod.POST)
	public ModelAndView updDoctorComment(ModelAndView mav, @ModelAttribute ReservationBean rb) {
		mav.addObject(rb);
		this.company.backController(mav, 84);
		return mav;
	}
	
	
	
	
	//육아정보 게시판
	@RequestMapping(value = "/MoveInfoBoard", method = RequestMethod.GET)
	public ModelAndView moveInfoBoardPage(ModelAndView mav, @ModelAttribute PostBean pb) {
		mav.addObject(pb);
		this.board.backController(mav, 56);
		return mav;
	}
	//게시판 글 내용 보기
	@RequestMapping(value = "/MoveShowPost", method = RequestMethod.POST)
	public ModelAndView MoveShowPostCtl(ModelAndView mav, @ModelAttribute PostBean pbs) {
		mav.addObject(pbs);
		this.board.backController(mav, 58);
		return mav;
	}
 
	//감성일기 정렬
	@RequestMapping(value = "/ChangeDailyDiary", method = RequestMethod.GET)
	public ModelAndView changeDailyDiary(ModelAndView mav, @ModelAttribute DailyDiaryBean db) {
		mav.addObject(db);
		this.dailyDiary.backController(mav, 86);
		return mav;
	}
	//감성일기 정렬
		@RequestMapping(value = "/ChangeMyDailyDiary", method = RequestMethod.GET)
		public ModelAndView changeMyDailyDiary(ModelAndView mav, @ModelAttribute DailyDiaryBean db) {
			mav.addObject(db);
			this.dailyDiary.backController(mav, 121);
			return mav;
		}
	
}
