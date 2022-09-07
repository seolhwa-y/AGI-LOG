package com.agilog.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.CompanyBean;
import com.agilog.beans.DoctorBean;
import com.agilog.services.Company;

@Controller
public class JpHomeController {
	@Autowired
	private Company company;

	//예약관리 관리자페이지 이동 : 재필
	@RequestMapping(value = "/CheckManager", method = RequestMethod.POST)
	public ModelAndView checkManager(ModelAndView mav,@ModelAttribute CompanyBean cb) {
		mav.addObject(cb);
		this.company.backController(mav, 71);
		
		return mav;
	}
	
	//전문의 관리 페이지 이동
	@RequestMapping(value = "/MoveDoctorManagement", method = RequestMethod.GET)
	public ModelAndView moveDoctorManagementCtl(HttpServletRequest req,ModelAndView mav, @ModelAttribute CompanyBean cb) {
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
	@RequestMapping(value = "/MovePatientManagement", method = RequestMethod.GET)
	public ModelAndView movePatientManagementCtl(HttpServletRequest req,ModelAndView mav, @ModelAttribute DoctorBean db) {

		mav.addObject(db);
		this.company.backController(mav, 81);
		return mav;
	}


	
	
	
}
