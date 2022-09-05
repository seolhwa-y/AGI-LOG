package com.agilog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilog.beans.BabyBean;
import com.agilog.beans.MyPageBean;
import com.agilog.services.MyPage;

@RestController
public class JsAPIController {
	@Autowired
	MyPage myPage;
	
	@SuppressWarnings("unchecked")
	@PostMapping("/GetBabyInfo")
	public MyPageBean getBabyInfo(Model model, @ModelAttribute BabyBean bb){

		myPage.backController(model, 107);
		return (MyPageBean)model.getAttribute("myPageBean");
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/ChangeTheme")
	public String changeTheme(Model model, @ModelAttribute MyPageBean mpb){
		model.addAttribute(mpb);
		myPage.backController(model, 66);
		return ((MyPageBean)model.getAttribute("myPageBean")).getSuTheme();
	}
	
}