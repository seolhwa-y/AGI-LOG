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
			this.moveCheckManagerForRMPage(mav);
			break;
		case 701:
			this.moveCheckManagerForRM(mav);
			break;
		case 702:
			this.moveReservationManagementCtl(mav);	// Board로 페이지 이동
			break;		
			
		case 77:
			this.moveCheckManagerForDMPage(mav);
			break;
		case 771:
			this.moveCheckManagerForDM(mav);
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




	private void moveCheckManagerForDMPage(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			if(cb != null) {
				mav.setViewName("checkManagerForDM");
			}else {
				mav.setViewName("companyLogin");
				System.out.println("세션만료");
			}
		
		} catch (Exception e) {e.printStackTrace();}
	}
	private void moveCheckManagerForDM(ModelAndView mav) {
		//전문의 관리 페이지에서 관리자 코드 인증
		String page = "companyLogin";
		CompanyBean cb;
			try {
				cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
				CompanyBean cmb = (CompanyBean) mav.getModel().get("companyBean");
				/* 세션 데이터가 있는 경우 */
				if (cb != null) {
					//CompanyBean cmb = (CompanyBean) this.session.selectList("getCompanyAccessInfo", this.pu.getAttribute("companyAccessInfo")).get(0);
					System.out.println(cmb.getCoManagerCode());
					System.out.println(cb.getCoManagerCode());
			
					if(cmb.getCoManagerCode().equals(cb.getCoManagerCode())) {
						this.moveDoctorManagementCtl(mav);
						mav.setViewName("doctorManagement");
						System.out.println("관리자코드 일치");
					}else {
						mav.setViewName(page);
						System.out.println("관리자코드 불일치");
					}
		//cmb.setcoManagerCode(this.enc.aesDecode(cmb.getCoManagerCode(), cmb.getCoCode()));		
		// coManagerCode를 복호화 후 다시 cmb에 저장
				} else {
					mav.setViewName(page);
					System.out.println("세션미확인");
				}
			} catch (Exception e) {	e.printStackTrace(); }	
	}
	
	private void moveCheckManagerForRMPage(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			if(cb != null) {
				mav.setViewName("checkManagerForRM");
			}else {
				mav.setViewName("companyLogin");
				System.out.println("세션만료");
			}
		
		} catch (Exception e) {e.printStackTrace();}
	}
	private void moveCheckManagerForRM(ModelAndView mav) {
	String page = "companyLogin";
	CompanyBean cb;
		try {
			cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			CompanyBean cmb = (CompanyBean) mav.getModel().get("companyBean");
			/* 세션 데이터가 있는 경우 */
			if (cb != null) {
				//CompanyBean cmb = (CompanyBean) this.session.selectList("getCompanyAccessInfo", this.pu.getAttribute("companyAccessInfo")).get(0);
				System.out.println(cmb.getCoManagerCode());
				System.out.println(cb.getCoManagerCode());
		
				if(cmb.getCoManagerCode().equals(cb.getCoManagerCode())) {
					mav.setViewName("reservationManagement");
					System.out.println("관리차코드 일치");
				}else {
					mav.setViewName(page);
					System.out.println("관리자코드 불일치");
				}
	//cmb.setcoManagerCode(this.enc.aesDecode(cmb.getCoManagerCode(), cmb.getCoCode()));		
	// coManagerCode를 복호화 후 다시 cmb에 저장
			} else {
				mav.setViewName(page);
				System.out.println("세션미확인");
			}
		} catch (Exception e) {	e.printStackTrace(); }	
	}
  /*
	 private void moveCheckManagerCtl(ModelAndView mav) {
		String page = "checkManager";

		mav.setViewName(page);
	}
   */
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

	private String makeHTMLCReservation(List<CompanyBean> companyList, List<ReservationBean> reservationList) {
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}


	// BooleanCheck ��
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}

}
