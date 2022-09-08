package com.agilog.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DoctorBean;
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

	// Controller
	public void backController(ModelAndView mav, int serviceCode) {
		/*
		 * try {
			if(this.pu.getAttribute("accessInfo")!=null) {
		 */
		switch(serviceCode) {
		
		case 70:
			this.checkManager(mav);
			break;
			
		case 772:
			this.moveDoctorManagementCtl(mav);
			break;
			
		case 81:
			this.movePatientManagement(mav);
			break;
			
		case 80:
			this.insertDoctor(mav);
			break;
		default:
			break;
		}	
	}

	public void backController(Model model, int serviceCode) {
		switch (serviceCode) {
		case 79:
			this.checkDoctorCodeOverlap(model);
			break;
			case 202:
			this.deleteDoctor(model);	
			break;
		}
	}
	
	
	private void deleteDoctor(Model model) {
		CompanyBean cb = (CompanyBean) model.getAttribute("companyBean");
		System.out.println("의사정보" +cb);
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void insertDoctor(ModelAndView mav) {
		String page = "doctorManagement", message = "등록에 실패했습니다.";
		DoctorBean db = (DoctorBean) mav.getModel().get("doctorBean");
		try {
			CompanyBean compb = (CompanyBean)this.pu.getAttribute("companyAccessInfo");
			System.out.println("세션 :" + compb);
			db.setCoCode(compb.getCoCode());
			System.out.println("의사 병원코드 : " + db);
			} catch (Exception e1) {e1.printStackTrace();}
	
		System.out.println("코드 들어있나?"+db);

		try {
			db.setDoPassword(this.enc.encode(db.getDoPassword()));
		} catch (Exception e) {e.printStackTrace();}
		mav.addObject(db);
		if (this.convertToBoolean(this.session.insert("insDoctorInfo", db))) {
			message = "등록에 성공했습니다.";
			mav.setViewName(page);
		}

		mav.setViewName("redirect:/");
		mav.addObject("message", message);
	}
	
	private void checkDoctorCodeOverlap(Model model) {
		System.out.println("중복체크 메소드 진입");
		String code = (String) model.getAttribute("code");
		DoctorBean db = (DoctorBean) model.getAttribute("doctorBean");
		int result = 0;
		switch (code) {
		case "0":
			
			break;
		case "1":
			result = Integer.parseInt(this.session.selectOne("checkDoctorCode", db).toString());
			System.out.println(result);
			break;
		}
		if (this.convertToBoolean(result)) {
			model.addAttribute("check", "no");
		} else {
			model.addAttribute("check", "ok");
		}
	}

	//페이지 이동 메소드 라인

	//전문의 관리 페이지 이동
	private void moveDoctorManagementCtl(ModelAndView mav) {
		try {
			CompanyBean compb = (CompanyBean)this.pu.getAttribute("companyAccessInfo");
			CompanyBean cb = new CompanyBean();

			cb.setCoCode(compb.getCoCode());
			/* 내가 받은 프로젝트의 데이터를 html에 만들고 EL로 저장 */
			mav.addObject("doctor",this.makeDoctorList(this.session.selectList("getDoctorInfo", cb)));
		} catch (Exception e) {e.printStackTrace();}
		// Board로 페이지 이동
		mav.setViewName("doctorManagement");

	}


	//예약관리 페이지 이동
	private void moveReservationManagementCtl(ModelAndView mav) {
		/*try {
			CompanyBean compb = (CompanyBean)this.pu.getAttribute("companyAccessInfo");
			CompanyBean cb = new CompanyBean();

			cb.setCoCode(compb.getCoCode());
		 */
		/* 내가 받은 프로젝트의 데이터를 html에 만들고 EL로 저장 */
		//	mav.addObject("doctor",this.makeHTMLDoctor(this.session.selectList("getCompanyAccessInfo", cb)));
		//	} catch (Exception e) {e.printStackTrace();}
		// Board로 페이지 이동

		mav.setViewName("reservationManagement");

	}

	//환자관리 페이지 이동
	private void movePatientManagement(ModelAndView mav) {

		mav.setViewName("patientManagement");

	}
	
	private void checkManager(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			cb.setCoManagerCode(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode());
			if(cb != null) {
				if(this.convertToBoolean(this.session.selectOne("isManagerCode", cb))) {
					// 세션에 저장할 로그인 유저 정보 가져오기
					CompanyBean company = (CompanyBean) this.session.selectList("getCompanyAccessAllInfo", cb).get(0);
					System.out.println("체크 매니저 진입 체크 : "+company);
					// 세션에 userCode저장
					this.pu.setAttribute("companyAccessInfo", company);
					
					mav.addObject("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getResInfo", cb)));
					mav.setViewName("reservationManagement");
				}
				else {
					mav.addObject("message", "매니저 코드가 일치하지 않습니다. 다시 입력해주세요.");
					mav.setViewName("redirect:/");
				}
			}
			else {
				mav.setViewName("companyLogin");
				System.out.println("세션만료");
			}
		
		} catch (Exception e) {e.printStackTrace();}
	}
	
	private void deleteDoctorCtl(ModelAndView mav) {

	}

	private void checkDoctorCodeCtl(Model model) {

	}
	private void insertDoctorCommentCtl(ModelAndView mav) {

	}



	private String makeDoctorList(List<DoctorBean> doctorList) {
		StringBuffer sb = new StringBuffer();
	
		sb.append("<table class=\"doctorMgr\">" );
		sb.append("<tr>");
		sb.append("<th>직원코드</th>");
		sb.append("<th>직원이름</th>");
		sb.append("<th>관리</th>");
		sb.append("</tr>");
		for(int idx=0; idx<doctorList.size(); idx++) {
			DoctorBean db = ((DoctorBean)doctorList.get(idx));
			sb.append("<tr>");
			sb.append("<td>" + db.getDoCode() + "</td>");
			sb.append("<td>" + db.getDoName() + "</td>");
			sb.append("<td><button class=\"delBtn\">");
			sb.append("<i class=\"fa-solid fa-trash-can delBtn editBtn\" "
					+ "onClick=\"deleteDoctor("+ db.getCoCode() + db.getDoCode() +")\"></i>삭제</button>");
			sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		return sb.toString();
	}

	private String makeHTMLCReservation(List<DoctorBean> doctorList, List<ReservationBean> reservationList) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		int idx2 = 0;
			sb.append("<table id=\"reservationTable\">\n");
			sb.append("<tr>\n");
			sb.append("<th>예약일</th>\n");
			sb.append("<th>환자명</th>\n");
			sb.append("<th>담당의</th>\n");
			sb.append("<th>예약상태</th>\n");
			sb.append("<th>상태변경</th>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			for(ReservationBean rb : reservationList) {
				sb.append("<tr>\n");
				sb.append("<td>\n" + rb.getResDate() + "</td>\n");
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
					sb.append("<td><input type='button' value='저장' onClick=\"updateReservation('" + rb.getResCode() + "','" + idx + "','" + idx2 + "')\"></td>\n");
					idx++;
					idx2++;
					break;
				case "CP" :
					sb.append("<td>\n" + rb.getDoName() + "</td>\n");
					sb.append("<td>\n");
					sb.append("<select name = 'selectResState'>\n");
					sb.append("<option disabled selected>예약완료</option>\n");
					sb.append("<option value='CC'>예약취소</option>\n");
					sb.append("</td>\n");
					sb.append("<td><input type='button' value='저장' onClick=\"updateReservation('" + rb.getResCode() + "','" + idx + "', '')\"></td>\n");
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
					sb.append("<td>\n" + rb.getDoName() + "</td>\n");
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


	// BooleanCheck ��
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}

}
