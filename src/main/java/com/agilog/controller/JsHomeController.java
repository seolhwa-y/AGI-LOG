package com.agilog.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BabyBean;
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
public class JsHomeController {
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

	@RequestMapping(value = "/CompanyLogin", method = RequestMethod.POST)
	public ModelAndView companyLogin(ModelAndView mav, @ModelAttribute CompanyBean cb) {
		mav.addObject(cb);
		this.auth.backController(mav, 29);
		
		return mav;
	}

	@RequestMapping(value = "/CompanyLogout", method = RequestMethod.GET)
	public ModelAndView companyLogout(ModelAndView mav, @ModelAttribute CompanyBean cb) {
		this.auth.backController(mav, 94);
		
		return mav;
	}
	
	@RequestMapping(value = "/ChangeParentInfo", method = RequestMethod.POST)
	public ModelAndView updateProfile(ModelAndView mav, @ModelAttribute MyPageBean mb) {
		mav.addObject(mb);
	
		this.myPage.backController(mav, 91);
		
		return mav;
	}
	
	@RequestMapping(value = "/ChangeBabyInfo", method = RequestMethod.POST)
	public ModelAndView changeBabyInfo(ModelAndView mav, @ModelAttribute BabyBean bb) {
		mav.addObject(bb);
		this.myPage.backController(mav, 68);
		
		return mav;
	}
	@RequestMapping(value = "/InsertBabyInfo", method = RequestMethod.POST)
	public ModelAndView insertBabyInfo(ModelAndView mav, @ModelAttribute BabyBean bb) {
		mav.addObject(bb);
		this.myPage.backController(mav, 69);
		
		return mav;
	}
	@RequestMapping(value = "/UploadParentImage", method = RequestMethod.POST)
	public ModelAndView uploadFile(MultipartFile file, ModelAndView mav, @ModelAttribute MyPageBean mb) {
		mav.addObject("file",file);
		mav.addObject(mb);
		this.myPage.backController(mav, 108);
		return mav;
	}
	@RequestMapping(value = "/UploadBabyImage", method = RequestMethod.POST)
	public ModelAndView uploadBabyImage(MultipartFile file, ModelAndView mav, @ModelAttribute BabyBean bb) {
		mav.addObject("file",file);
		mav.addObject(bb);
		this.myPage.backController(mav, 109);
		
		return mav;
	}
	@RequestMapping(value = "/MoveMyDailyDiaryPage", method = RequestMethod.GET)
	public ModelAndView moveDoctorComment(ModelAndView mav, @ModelAttribute AuthBean ab) {
		mav.addObject(ab);
		this.dailyDiary.backController(mav, 32);
		
		return mav;
	}
}
