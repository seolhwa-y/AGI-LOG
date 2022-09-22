package com.agilog.services2;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.CompanyBean;
import com.agilog.beans.DoctorBean;
import com.agilog.beans.ReservationBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;
import java.util.Base64.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

@Service
public class Company2 implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	@Autowired
	private smsSend sms;
	public Company2() {
	}
	// Controller
	public void backController(ModelAndView mav, int serviceCode) {
		/*
		 * try {
			if(this.pu.getAttribute("accessInfo")!=null) {
		 */
		switch(serviceCode) {
		
//		case 72:
//			this.updateReservationCtl(mav);
//			break;
		default:
			break;
		}	
	}

	public void backController(Model model, int serviceCode) {

	}


	private void sendSmsCtl() {
	                
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

	private String makeHTMLCReservation(List<DoctorBean> doctorList, List<ReservationBean> reservationList) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		int idx2 = 0;
			sb.append("<table id=\"reservationTable\">\n");
			sb.append("<tr>\n");
			sb.append("<th>예약시간</th>\n");
			sb.append("<th>환자명</th>\n");
			sb.append("<th>담당의</th>\n");
			sb.append("<th>예약상태</th>\n");
			sb.append("<th>상태변경</th>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			for(ReservationBean rb : reservationList) {
				sb.append("<tr>\n");
				sb.append("<td>\n" + rb.getResTime() + "</td>\n");
				sb.append("<td>\n" + rb.getResBbName() + "</td>\n");
				switch(rb.getRcCode()) {
				case "RD" :
					sb.append("<td>\n");
					sb.append("<select name = 'selectDoctor'>\n");
					for (DoctorBean db : doctorList) {
						sb.append("<option value='"+ db.getDoCode() +"'>\n" + db.getDoName() + "</option>\n");
					}
					sb.append("</td>\n");
					sb.append("<td>\n");
					sb.append("<select name = 'selectResState'>\n");
					sb.append("<option disabled selected>예약중</option>\n");
					sb.append("<option value='CP'>예약완료</option>\n");
					sb.append("<option value='CC'>예약취소</option>\n");
					sb.append("</td>\n");
					sb.append("<td><input type='button' value='저장' class='saveBtn' onClick=\"updateReservation('" + rb.getResCode() + "','" + idx + "','" + idx2 + "')\"></td>\n");
					idx++;
					idx2++;
					break;
				case "CP" :
					sb.append("<td>\n" + rb.getResDoName() + "</td>\n");
					sb.append("<td>\n");
					sb.append("<select name = 'selectResState'>\n");
					sb.append("<option disabled selected>예약완료</option>\n");
					sb.append("<option value='CC'>예약취소</option>\n");
					sb.append("</td>\n");
					sb.append("<td><input type='button' value='저장' class='saveBtn' onClick=\"updateReservation('" + rb.getResCode() + "','" + idx + "', '')\"></td>\n");
					idx++;
					break;
				case "CC" :
					sb.append("<td></td>\n");
					sb.append("<td>\n");
					sb.append("<option disabled selected>예약취소</option>\n");
					sb.append("</td>\n");
					sb.append("<td></td>\n");
					break;
				case "MC" :
					sb.append("<td>\n" + rb.getResDoName() + "</td>\n");
					sb.append("<td>\n");
					sb.append("<option disabled selected>진료 완료</option>\n");
					sb.append("</td>\n");
					sb.append("<td></td>\n");
					break;
				default:
				}
				sb.append("</tr>\n");
			}
			sb.append("</div>\n");
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
