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
public class MyPage implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;

	public MyPage() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {

	}

	public void backController(Model model, int serviceCode) {

	}

	private String makeMyPageHtlm(AuthBean ab) {
		return null;

	}

	private void moveMyPageCtl(ModelAndView mav) {

	}

	private void isPersonalPasswordCtl(Model model) {

	}

	private void changeThemeCtl(ModelAndView mav) {

	}

	private void changeBabyInfoCtl(ModelAndView mav) {

	}

	private void insertBabyInfoCtl(ModelAndView mav) {

	}

	private void updateProfileCtl(ModelAndView mav) {

	}

	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}

}
