package com.agilog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BebeCalendarBean;
import com.agilog.beans.BoardBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.MyPageBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.ReservationBean;
import com.agilog.services.Authentication;
import com.agilog.services.BebeCalendar;
import com.agilog.services.BebeMap;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.DailyDiary;
import com.agilog.services.DashBoard;
import com.agilog.services.MyPage;
import com.agilog.services2.Board2;
import com.agilog.services2.Company2;
import com.agilog.services2.DailyDiary2;

@Controller
public class ThHomeController {
	@Autowired
	Authentication auth;
	@Autowired
	DashBoard dashBoard;
	@Autowired
	Company2 company;
	@Autowired
	DailyDiary2 dailyDiary;
	@Autowired
	BebeMap bebeMap;
	@Autowired
	BebeCalendar bebeCalendar;
	@Autowired
	Board2 board;
	@Autowired
	MyPage myPage;

	@RequestMapping(value = "/UpdateReservation", method = RequestMethod.POST)
	public ModelAndView updateReservation(ModelAndView mav, @ModelAttribute ReservationBean rb) {
		System.out.println("컨트롤러 진입 체크1");
		mav.addObject(rb);
		System.out.println("컨트롤러 진입 체크2");
		this.company.backController(mav, 72);
		return mav;
	}
	
	@RequestMapping(value = "/MoveWritePage", method = RequestMethod.POST)
	public ModelAndView moveWritePage(ModelAndView mav) {
		System.out.println("컨트롤러 진입 체크1");
		this.board.backController(mav, 57);
		return mav;
	}
	
	@RequestMapping(value = "/InsertPost", method = RequestMethod.POST)
	public ModelAndView insertPost(ModelAndView mav, @ModelAttribute PostBean pb) {
		System.out.println("컨트롤러 진입 체크1");
		mav.addObject(pb);
		this.board.backController(mav, 65);
		return mav;
	}

	@RequestMapping(value = "/MoveFreeBoardPage", method = RequestMethod.GET)
	public ModelAndView moveBoardPage(ModelAndView mav, @ModelAttribute BoardBean bb) {
		mav.addObject(bb);
		this.board.backController(mav, 8);

		return mav;
	}
	
	//게시판 글 내용 보기
	@RequestMapping(value = "/MoveShowFbPost", method = RequestMethod.POST)
	public ModelAndView moveShowFbPostCtl(ModelAndView mav, @ModelAttribute PostBean pb) {
		mav.addObject(pb);
		this.board.backController(mav, 581);
		return mav;
	}

	//감성일기 등록
	@RequestMapping(value = "/InsertDailyDiary", method = RequestMethod.POST)
	public ModelAndView insertDailyDiary(ModelAndView mav, @ModelAttribute DailyDiaryBean db) {
		mav.addObject(db);
		this.dailyDiary.backController(mav, 35);
		return mav;
	}

	//감성일기 등록
	@RequestMapping(value = "/DeleteDailyDiaryFeed", method = RequestMethod.POST)
	public ModelAndView deleteDailyDiaryFeed(ModelAndView mav, @ModelAttribute DailyDiaryBean db) {
		mav.addObject(db);
		this.dailyDiary.backController(mav, 87);
		return mav;
	}
}