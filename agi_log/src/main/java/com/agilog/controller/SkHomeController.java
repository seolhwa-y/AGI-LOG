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
import com.agilog.services.Authentication;
import com.agilog.services.BebeCalendar;
import com.agilog.services.BebeMap;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.DailyDiary;
import com.agilog.services.DashBoard;
import com.agilog.services.MyPage;

@Controller
public class SkHomeController {
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

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {

		return "dashBoard";
	}

	@RequestMapping(value = "/MoveLoginPage", method = RequestMethod.GET)
	public ModelAndView moveLoginPage(ModelAndView mav, @ModelAttribute AuthBean auth) {
		mav.addObject(auth);
		this.auth.backController(mav, 0);

		return mav;
	}

	@RequestMapping(value = "/MoveJoinPage", method = RequestMethod.GET)
	public ModelAndView moveJoinPage(ModelAndView mav, @ModelAttribute AuthBean auth) {
		mav.addObject(auth);
		this.auth.backController(mav, 1);

		return mav;
	}

	@RequestMapping(value = "/MoveCompanyLoginPage", method = RequestMethod.GET)
	public ModelAndView moveCompanyLoginPage(ModelAndView mav, @ModelAttribute AuthBean auth) {
		mav.addObject(auth);
		this.auth.backController(mav, 2);

		return mav;
	}

	@RequestMapping(value = "/MoveMainPage", method = RequestMethod.GET)
	public ModelAndView moveMainPage(ModelAndView mav, @ModelAttribute AuthBean auth) {
		mav.addObject(auth);
		this.dashBoard.backController(mav, 0);

		return mav;
	}

	@RequestMapping(value = "/MoveDailyDiaryPage", method = RequestMethod.GET)
	public ModelAndView moveDailyDiaryPage(ModelAndView mav, @ModelAttribute DailyDiaryBean ddb) {
		mav.addObject(ddb);
		this.dailyDiary.backController(mav, 0);

		return mav;
	}

	@RequestMapping(value = "/MoveMapPage", method = RequestMethod.GET)
	public ModelAndView moveMapPage(ModelAndView mav) {
		this.bebeMap.backController(mav, 0);

		return mav;
	}

	@RequestMapping(value = "/MoveCalendarPage", method = RequestMethod.GET)
	public ModelAndView moveCalendarPage(ModelAndView mav, @ModelAttribute BebeCalendarBean bcb) {
		mav.addObject(bcb);
		this.bebeCalendar.backController(mav, 0);

		return mav;
	}

	@RequestMapping(value = "/MoveBoardPage", method = RequestMethod.GET)
	public ModelAndView moveBoardPage(ModelAndView mav, @ModelAttribute BoardBean bb) {
		mav.addObject(bb);
		this.board.backController(mav, 0);

		return mav;
	}

	@RequestMapping(value = "/MoveMyPage", method = RequestMethod.GET)
	public ModelAndView moveMyPage(ModelAndView mav, @ModelAttribute MyPageBean mb) {
		mav.addObject(mb);
		this.myPage.backController(mav, 0);

		return mav;
	}
}
