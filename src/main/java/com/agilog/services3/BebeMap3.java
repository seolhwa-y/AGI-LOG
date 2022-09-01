package com.agilog.services3;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.ProjectUtils;

@Service
public class BebeMap3 implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;
	
	public BebeMap3() {}
	
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
