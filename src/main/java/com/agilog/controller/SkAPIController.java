package com.agilog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilog.beans.AuthBean;
import com.agilog.services.Authentication;
import com.agilog.services.BebeCalendar;
import com.agilog.services.BebeMap;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.DailyDiary;
import com.agilog.services.DashBoard;
import com.agilog.services.MyPage;

@RestController
public class SkAPIController {
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
	
	@SuppressWarnings("unchecked")
	@PostMapping("/CheckPersonalOverlap")
	public String checkPersonalOverlap(Model model, HttpServletRequest req, @ModelAttribute AuthBean ab) {
		model.addAttribute("code",req.getParameter("code"));
		model.addAttribute(ab);
		System.out.println("중복체크 컨트롤러 진입");
		this.auth.backController(model, 18);
		return (String)model.getAttribute("check");
	}
}