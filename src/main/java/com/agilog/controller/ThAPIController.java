package com.agilog.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.DailyDiaryCommentBean;
import com.agilog.beans.ReservationBean;
import com.agilog.services2.Board2;
import com.agilog.services2.DailyDiary2;

@RestController
public class ThAPIController {
	@Autowired
	Board2 board;
	@Autowired
	DailyDiary2 dailyDiary;

	//피드 내용 불러오기
	@SuppressWarnings("unchecked")
	@PostMapping("/GetDailyDiaryFeed")
	public HashMap<String, Object> getDailyDiaryFeed(Model model, @ModelAttribute DailyDiaryBean db, @ModelAttribute DailyDiaryCommentBean ddcb){
		model.addAttribute(db);
		model.addAttribute(ddcb);
		this.dailyDiary.backController(model, 31);
		
		return (HashMap<String, Object>)model.getAttribute("dailyDiaryFeed");
	}

	//피드 업데이트
	@SuppressWarnings("unchecked")
	@PostMapping("/UpdateDailyDiaryFeed")
	public HashMap<String, Object> updateDailyDiaryFeed(Model model, @ModelAttribute DailyDiaryBean db, @ModelAttribute DailyDiaryCommentBean ddcb){
		model.addAttribute(db);
		model.addAttribute(ddcb);
		this.dailyDiary.backController(model, 88);
		
		return (HashMap<String, Object>)model.getAttribute("dailyDiaryFeed");
	}
}
