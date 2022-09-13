package com.agilog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.services2.Board2;

@RestController
public class ThAPIController {
	@Autowired
	Board2 board;

	@PostMapping("/upload_ok2")
	public Model upload_ok2(Model model, MultipartHttpServletRequest files) {
		
		System.out.println("API 컨트롤러 진입 체크1");
		this.board.backController(model, files, 1);
		return model;
	}

}
