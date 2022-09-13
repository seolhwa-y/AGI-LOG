package com.agilog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilog.beans.AuthBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.PostBean;
import com.agilog.services.Authentication;
import com.agilog.services.BebeCalendar;
import com.agilog.services.BebeMap;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.DailyDiary;
import com.agilog.services.DashBoard;
import com.agilog.services.HealthDiary;
import com.agilog.services.MyPage;
import com.agilog.services.SkBoard;
import com.agilog.services.SkDailyDiary;

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
	HealthDiary healthDiary;
	@Autowired
	BebeMap bebeMap;
	@Autowired
	BebeCalendar bebeCalendar;
	@Autowired
	SkBoard board;
	@Autowired
	SkDailyDiary dd;
	@Autowired
	MyPage myPage;
	
	@PostMapping("/CheckPersonalOverlap")
	public String checkPersonalOverlap(Model model, HttpServletRequest req, @ModelAttribute AuthBean ab) {
		model.addAttribute("code",req.getParameter("code"));
		model.addAttribute(ab);
		this.auth.backController(model, 18);
		return (String)model.getAttribute("check");
	}
	
	@PostMapping("/ShowHealthDiary")
	public HealthDiaryBean showHealthDiary(Model model, @ModelAttribute HealthDiaryBean hb) {
		model.addAttribute(hb);
		this.healthDiary.backController(model, 103);
		return (HealthDiaryBean)model.getAttribute("healthDetail");
	}
	
	@PostMapping("/UpdateMyHealthDiary")
	public HealthDiaryBean updateMyHealthDiary(Model model, @ModelAttribute HealthDiaryBean hb) {
		model.addAttribute(hb);
		this.healthDiary.backController(model, 94);
		return (HealthDiaryBean)model.getAttribute("memo");
	}
	
	@PostMapping("/DailyDiaryLike")
	public DailyDiaryBean dailyDiaryLike(Model model, @ModelAttribute DailyDiaryBean ddb) {
		model.addAttribute(ddb);
		this.dd.backController(model, 110);
		return (DailyDiaryBean)model.getAttribute("ddLike");
	}
	
	@PostMapping("/FreeBoardLike")
	public PostBean freeBoardLike(Model model, @ModelAttribute PostBean pb) {
		model.addAttribute(pb);
		this.board.backController(model, 111);
		return (PostBean)model.getAttribute("fbLike");
	}
}
