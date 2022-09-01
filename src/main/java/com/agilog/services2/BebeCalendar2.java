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
public class BebeCalendar2 implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	
	public BebeCalendar2() {	}
	
	public void backController(ModelAndView mav, int serviceCode) {
		
	}

	public void backController(Model model, int serviceCode) {
		
	}
	
	private void moveCalendarCtl(ModelAndView mav) {
		
	}
	
	private void moveMonthCtl(Model model) {
		
	}
	
	private void dateDetailCtl(ModelAndView mav) {
		
	}
	
	private void deleteScheduleCtl(Model model) {
		
	}
	
	private void updateScheduleCtl(Model model) {
		
	}
	
	private void insertScheduleCtl(Model model) {
		
	}
	
	private void deleteReservationCtl(ModelAndView mav) {
		
	}

	private boolean convertToBoolean(int booleanCheck) {
		boolean result=false;
		
		return result;
	}
}
