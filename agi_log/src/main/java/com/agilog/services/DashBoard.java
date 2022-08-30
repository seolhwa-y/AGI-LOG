package com.agilog.services;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.ProjectUtils;
@Service
public class DashBoard implements ServiceRule {
	
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;
	
	public DashBoard() {}
	
	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		
	}
	public void backController(Model model, int serviceCode) {
		
	}
	
	
	private void moveMainCtl(ModelAndView mav) {
		
	}
	
	
	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
