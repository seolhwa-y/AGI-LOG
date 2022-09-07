package com.agilog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.CompanyBean;
import com.agilog.beans.DoctorBean;
import com.agilog.services.Authentication;
import com.agilog.services.Company;
import com.agilog.services.DashBoard;

@RestController
public class JpAPIController {
	@Autowired
	Authentication auth;
	@Autowired
	DashBoard dashBoard;
	@Autowired
	Company company;
	
	
	//의사등록 중복확인 메소드
	
	@PostMapping("/CheckDoctorCode")
	public String checkDoctorCodeCtl(Model model, HttpServletRequest req, @ModelAttribute DoctorBean db) {
		model.addAttribute("code",req.getParameter("code"));
		model.addAttribute(db);
		System.out.println("중복체크 컨트롤러 진입");
		this.company.backController(model, 79);
		System.out.println((String)model.getAttribute("check"));
		return (String)model.getAttribute("check");
	}
	//의사 삭제 메소드
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/DeleteDoctor", method = RequestMethod.POST)
	public List<DoctorBean> deleteDoctorCtl(Model model, @ModelAttribute DoctorBean db) {
		model.addAttribute(db);	
		System.out.println("의사정보" + db);
		this.company.backController(model, 78);
		return (List<DoctorBean>) model.getAttribute("doctor");
	}
	//의사등록 메소드
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/InsertDoctor", method = RequestMethod.POST)
	public List<DoctorBean> insertDoctorCtl(Model model, @ModelAttribute DoctorBean db) {
		model.addAttribute(db);
		this.company.backController(model, 80);
		return (List<DoctorBean>) model.getAttribute("doctor");
	}
}
