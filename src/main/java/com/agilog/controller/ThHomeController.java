package com.agilog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

@Controller
public class ThHomeController {
	@Autowired
	Authentication auth;
	@Autowired
	DashBoard dashBoard;
	@Autowired
	Company2 company;
	@Autowired
	DailyDiary dailyDiary;
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
}
