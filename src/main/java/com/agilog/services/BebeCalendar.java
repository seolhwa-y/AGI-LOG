package com.agilog.services;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class BebeCalendar implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;

	public BebeCalendar() {
	}

	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 7:
			this.moveCalendarCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {

	}

	private void moveCalendarCtl(ModelAndView mav) {
		AuthBean ab;
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			if (ab != null) {
				if (ab.getSuCode().length() == 10) {
					ab.setType("kakao");
				} else {
					ab.setType("naver");
				}
				mav.addObject("accessInfo", ab);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("bebeCalendar");
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
		boolean result = false;

		return result;
	}
}
