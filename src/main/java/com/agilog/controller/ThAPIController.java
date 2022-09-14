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
import com.agilog.beans.ReservationBean;
import com.agilog.services2.Board2;
import com.agilog.services2.DailyDiary2;

@RestController
public class ThAPIController {
	@Autowired
	Board2 board;
	@Autowired
	DailyDiary2 dailyDiary;

	@PostMapping("/upload_ok2")
	public Model upload_ok2(Model model, MultipartHttpServletRequest files) {
		
		System.out.println("API 컨트롤러 진입 체크1");
		this.board.backController(model, files, 1);
		return model;
	}

	//피드 내용 불러오기
	@SuppressWarnings("unchecked")
	@PostMapping("/GetDailyDiaryFeed")
	public HashMap<Object, String> getDailyDiaryFeed(Model model, @ModelAttribute DailyDiaryBean db){
		model.addAttribute(db);
		this.dailyDiary.backController(model, 31);
		
		return (HashMap<Object, String>)model.getAttribute("getDDFeed");
	}
}
