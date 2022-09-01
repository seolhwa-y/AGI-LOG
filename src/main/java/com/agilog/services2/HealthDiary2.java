package com.agilog.services2;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;
@Service
public class HealthDiary2 implements ServiceRule {
	
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	
	public HealthDiary2() {}
	
	
	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		
	}
	public void backController(Model model, int serviceCode) {
		
	}
	
	
	private void deleteHealthDiaryCtl(ModelAndView mav) {
		
	}
	private void moveHealthDiaryPageCtl(ModelAndView mav) {
		
	}
	private void moveHealthStatusPageCtl(ModelAndView mav) {
		
	}
	private void insertHealthDiaryCtl(ModelAndView mav) {
		
	}
	private void updateMyHealthDiaryCtl(ModelAndView mav) {
		
	}
	private void moveDoctorCommentCtl(ModelAndView mav) {
		
	}
	private void selectBabyCtl(Model model) {
		
	}
	private void showHealthDiaryCtl(Model model) {
		
	}
	
	
	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
