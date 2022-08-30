package com.agilog.services;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.CompanyBean;
import com.agilog.beans.ReservationBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class Company implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;

	public Company() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {

	}

	public void backController(Model model, int serviceCode) {

	}

	private void moveCheckManagerCtl(ModelAndView mav) {

	}

	private void checkManagetCtl(ModelAndView mav) {

	}

	private void deleteDoctorCtl(ModelAndView mav) {

	}

	private void checkDoctorCodeCtl(Model model) {

	}

	private void moveCheckDoctorCtl(ModelAndView mav) {

	}

	private void insertDoctorCommentCtl(ModelAndView mav) {

	}

	private String makeHTMLCReservation(List<CompanyBean> companyList, List<ReservationBean> reservationList) {
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}

	private String makeHTMLDoctor(List<CompanyBean> companyList) {
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}

	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}

}
