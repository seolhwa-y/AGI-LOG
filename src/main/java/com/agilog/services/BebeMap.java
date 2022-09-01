package com.agilog.services;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.ProjectUtils;

@Service
public class BebeMap implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;

	public BebeMap() {
	}

	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 6:
			this.moveMapCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {

	}

	private void moveMapCtl(ModelAndView mav) {
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
		mav.setViewName("bebeMap");
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
		boolean result = false;

		return result;
	}
}
