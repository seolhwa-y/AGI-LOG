package com.agilog.controller;

import java.util.HashMap;

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
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.MyPageBean;
import com.agilog.services.Authentication;
import com.agilog.services.BebeCalendar;
import com.agilog.services.BebeMap;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.DailyDiary;
import com.agilog.services.DashBoard;
import com.agilog.services.MyPage;
import com.agilog.services2.HealthDiary2;

@Controller
public class ShHomeController {
	@Autowired
	Authentication auth;
	@Autowired
	DashBoard dashBoard;
	@Autowired
	Company company;
	@Autowired
	DailyDiary dailyDiary;
	@Autowired
	BebeMap bebeMap;
	@Autowired
	BebeCalendar bebeCalendar;
	@Autowired
	Board board;
	@Autowired
	MyPage myPage;
	@Autowired
	HealthDiary2 healthDiary;

	@RequestMapping(value = "/MoveCompanyJoinPage", method = RequestMethod.GET)
	public ModelAndView moveCompanyJoinPage(ModelAndView mav, @ModelAttribute AuthBean auth) {
		this.auth.backController(mav, 13);

		return mav;
	}
	
	@RequestMapping(value = "/CompanyJoin", method = RequestMethod.POST)
	public ModelAndView companyJoin(ModelAndView mav, @ModelAttribute CompanyBean cb) {
		mav.addObject("companyBean", cb);
		this.auth.backController(mav, 21);

		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/MoveHealthStatusPage", method = RequestMethod.POST)
	public ModelAndView moveHealthStatusPage(ModelAndView mav, @ModelAttribute HealthDiaryBean hdb) {
		mav.addObject("healthDiaryBean", hdb);
		this.healthDiary.backController(mav, 21);
		
		// 리턴값 :: 아이 건강 추세 내용(키, 몸무게 등 나이별로 또는 일자 별로), 아이 셀렉트리스트
		return mav;
	}
	
}
