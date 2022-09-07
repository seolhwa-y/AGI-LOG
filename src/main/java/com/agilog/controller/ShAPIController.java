package com.agilog.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilog.beans.HealthDiaryBean;
import com.agilog.services.Authentication;
import com.agilog.services.BebeCalendar;
import com.agilog.services.BebeMap;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.DailyDiary;
import com.agilog.services.DashBoard;
import com.agilog.services.HealthDiary;
import com.agilog.services.MyPage;
import com.agilog.services2.HealthDiary2;

@RestController
public class ShAPIController {
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
	Board board;
	@Autowired
	MyPage myPage;
	@Autowired
	HealthDiary2 healthDiary2;

	@SuppressWarnings("unchecked")
	@PostMapping("/SelectBaby")
	public HashMap<String, Object> showHealthDiary(Model model, @ModelAttribute HealthDiaryBean hb) {
		model.addAttribute(hb);
		this.healthDiary2.backController(model, 95);

		return (HashMap<String, Object>)model.getAttribute("babyStatusList");
	}
}
