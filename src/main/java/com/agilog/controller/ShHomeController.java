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
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.MyPageBean;
import com.agilog.services.Authentication;
import com.agilog.services.BebeCalendar;
import com.agilog.services.BebeMap;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.DailyDiary;
import com.agilog.services.DashBoard;
import com.agilog.services.MyPage;

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
}
