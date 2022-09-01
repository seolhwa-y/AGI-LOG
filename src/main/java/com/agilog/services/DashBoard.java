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
public class DashBoard implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;

	public DashBoard() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 4:
			this.moveMainCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {

	}

	private void moveMainCtl(ModelAndView mav) {
		AuthBean ab;
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			if (ab != null) {
				mav.addObject("accessInfo", ab);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("dashBoard");
	}

	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
