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
		try {
			if(this.pu.getAttribute("companyAccessInfo")!=null) {
		switch(serviceCode) {
		
		case 71:
			this.checkManager(mav);
			break;
		case 77:
			this.moveDoctorManagementCtl(mav);
			break;
		case 70:
			this.moveReservationManagementCtl(mav);
			break;
		case 81:
			this.movePatientManagementCtl(mav);
			break;
		default:
			break;
		}
		}else {
			// landing page로 이동
			mav.setViewName("companyLogin");
		}
	} catch (Exception e) {e.printStackTrace();}
}

	public void backController(Model model, int serviceCode) {
		switch (serviceCode) {
		case 79:
			this.checkDoctorCodeCtl(model);
			break;
		case 78:
			this.deleteDoctorCtl(model);	
			break;
		case 80:
			this.insertDoctorCtl(model);
			break;}
	}
	
	//의사 추가
	@Transactional(propagation = Propagation.REQUIRED)
	private void insertDoctorCtl(Model model) {

		DoctorBean db = (DoctorBean)model.getAttribute("doctorBean");
		try {
			CompanyBean compb = (CompanyBean)this.pu.getAttribute("companyAccessInfo");
			System.out.println("세션 :" + compb);
			
			db.setCoCode(compb.getCoCode());
			db.setDoPassword(this.enc.encode(db.getDoPassword()));
			
			this.session.insert("insDoctorInfo",db);
			model.addAttribute("doctor",this.session.selectList("getDoctorInfo", db));
			
			
		} catch (Exception e1) {e1.printStackTrace();}

	}

	//전문의 관리 페이지 이동
	private void moveDoctorManagementCtl(ModelAndView mav) {
		try {
			if(((CompanyBean)(this.pu.getAttribute("companyAccessInfo"))).getCoManagerCode() != null) {
							
			CompanyBean compb = (CompanyBean)this.pu.getAttribute("companyAccessInfo");
			CompanyBean cb = new CompanyBean();
			
			cb.setCoCode(compb.getCoCode());
			/* 내가 받은 프로젝트의 데이터를 html에 만들고 EL로 저장 */
			mav.addObject("doctor",this.makeDoctorList(this.session.selectList("getDoctorInfo", cb)));
			mav.setViewName("doctorManagement");	
			}else {
				mav.setViewName("checkManager");
				
			}
		} catch (Exception e1) {e1.printStackTrace();}
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
	@SuppressWarnings("unused")
	private void movePatientManagementCtl(ModelAndView mav) {
		String page = "checkDoctor";
		
		DoctorBean auth = (DoctorBean) mav.getModel().get("doctornBean");
		CompanyBean cb;
		try {
			cb = (CompanyBean)this.pu.getAttribute("companyAccessInfo");
			System.out.println("세션 :" + cb);
		} catch (Exception e) {e.printStackTrace();}
	
		
		/*db.setCoCode(cb.getCoCode());
		
		if (this.enc.matches(db.getDoPassword(), this.session.selectOne("isMember", auth))) {
	
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			cb.setCoManagerCode(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode());
			
			if(cb != null) {
				if(this.convertToBoolean(this.session.selectOne("isDoctorCode", cb))) {
					
					mav.setViewName("patientManagement");
				}
			}else{
				mav.setViewName(page);
				System.out.println("세션만료");
			}
		
		} catch (Exception e) {e.printStackTrace();}
		*/
	}




	@SuppressWarnings("unused")
	private void checkManager(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			cb.setCoManagerCode(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode());
			if(cb != null) {
				if(this.convertToBoolean(this.session.selectOne("isManagerCode", cb))) {
					// 세션에 저장할 로그인 유저 정보 가져오기
					CompanyBean company = (CompanyBean) this.session.selectList("getCompanyAccessAllInfo", cb).get(0);
					company.setCoName(this.enc.aesDecode(company.getCoName(), company.getCoCode()));
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
	//의사 삭제
	private void deleteDoctorCtl(Model model) {
		DoctorBean db = (DoctorBean)model.getAttribute("doctorBean");
		System.out.println("의사정보" +db);
		
		try {
			db.setCoCode(((CompanyBean)this.pu.getAttribute("companyAccessInfo")).getCoCode());
		
			this.session.delete("delDoctorInfo", db);
			model.addAttribute("doctor",this.session.selectList("getDoctorInfo", db));
		} catch (Exception e) {e.printStackTrace();}
	}
	//의사 코드 중복검사
	private void checkDoctorCodeCtl(Model model) {
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
	
	private void insertDoctorCommentCtl(ModelAndView mav) {

	}


	//의사 리스트 EL작업
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
					+ "onClick=\"deleteDoctor("+ db.getDoCode() +")\"></i>삭제</button>");
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
					sb.append("<td>\n" + rb.getResDoName() + "</td>\n");
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


	// BooleanCheck ��
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}

}
