package com.agilog.services2;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.ProjectUtils;

@Service
public class BebeMap2 implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;
	
	public BebeMap2() {}
	
	public void backController(ModelAndView mav, int serviceCode) {
		
	}

	public void backController(Model model, int serviceCode) {
		
	}
	
	private void moveMapCtl(ModelAndView mav) {
		
	}
	
	private void viewCompanyInfoCtl(Model model) {
		
	}
	
	private void insertCompanyCommentCtl(Model model) {
		
	}
	
	private void deleteMapComment(Model model) {
		
	}
	
	private void reservationCtl(ModelAndView mav) {
		
	}
	
	private boolean convertToBoolean(int booleanCheck) {
		boolean result=false;
		
		return result;
	}
}
