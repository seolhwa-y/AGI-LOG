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
import com.agilog.beans.HealthDiaryBean;
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
				case 800:
					this.moveHealthDataList(mav);
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
	
	//환자정보 불러오기 인증 및 EL 출력
	private void movePatientManagementCtl(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			System.out.println("세션 : " + cb);
			
			if(cb != null) {
				DoctorBean db = (DoctorBean) mav.getModel().get("doctorBean");
				//세션에서 coCode를 가져와 삽입
				db.setCoCode(cb.getCoCode());
				System.out.println("코드합친거 : " + db);
			
				if(this.convertToBoolean(this.session.selectOne("isDoctorCode", db))){
					DoctorBean acDoc = this.session.selectOne("isDoctorMember", db);

					//password 비교
					if(this.enc.matches(db.getDoPassword(), acDoc.getDoPassword())){
						
						mav.addObject("patient", this.makePatientList(this.session.selectList("getPatientInfoList", db)));
						mav.setViewName("patientManagement");
					}
				}else {
					System.out.println("인증실패");
					mav.setViewName("checkDoctor");
				}
			}
		} catch (Exception e) {e.printStackTrace();}
	}

	private void moveHealthDataList(ModelAndView mav) {
		ReservationBean rb = (ReservationBean) mav.getModel().get("reservationBean");
		System.out.println("예약정보" +rb);
		//cb.setCoManagerCode(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode());
		
		mav.addObject("healthDataList", this.makeHealthData(this.session.selectList("getHealthDataList", rb)));
		mav.setViewName("doctorHealthData");
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
					System.out.println(company);
					// 세션에 userCode저장
					this.pu.setAttribute("companyAccessInfo", company);
					mav.setViewName("reservationManagement");
				}
			}else{
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

	//환자 리스트 EL작업
	private String makePatientList(List<ReservationBean> patientList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table class=\"doctorMgr\">" );
		sb.append("<tr>");
		sb.append("<th>진료 날짜</th>");
		sb.append("<th>환자 이름</th>");
		sb.append("<th>진료 상태</th>");
		sb.append("</tr>");
		for(int idx=0; idx<patientList.size(); idx++) {
			ReservationBean rb = (ReservationBean)patientList.get(idx);
			sb.append("<tr onClick=\"moveHealthData("+ rb.getResCode() + rb.getResBbCode() +")\">");
			sb.append("<td>" + rb.getResDate() + "</td>");
			sb.append("<td>" + rb.getResBbName() + "</td>");
			sb.append("<td>" + rb.getResActionName() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		return sb.toString();
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
	//건강기록 EL작업
	private String makeHealthData(List<HealthDiaryBean> HealthDataList) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div id=\"doctorMgrTitleDiv\">" );
		sb.append("<p id=\"doctorMgrTitle\">건강 기록지</p></div>");
		sb.append("<table class=\"doctorMgr\">" );
		sb.append("<tr>");
		sb.append("<th colspan=\"11\">날짜</th>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<th>날짜</th>");
		sb.append("<th>키</th>");
		sb.append("<th>몸무게</th>");
		sb.append("<th>발사이즈</th>");
		sb.append("<th>머리둘레</th>");
		sb.append("<th>체온</th>");
		sb.append("<th>수면시간</th>");
		sb.append("<th>배변량</th>");
		sb.append("<th>배변상태</th>");
		sb.append("<th>식사량</th>");
		sb.append("<th>메모</th>");
		sb.append("</tr>");
		System.out.println("리스트 : "+ HealthDataList.size());
		for(int idx=0; idx<HealthDataList.size(); idx++) {
			HealthDiaryBean hdb = ((HealthDiaryBean)HealthDataList.get(idx));
			sb.append("<tr>");
			sb.append("<td>" + hdb.getHdDate() + "</td>");
			sb.append("<td>" + hdb.getBbHeight() + "</td>");
			sb.append("<td>" + hdb.getBbWeight() + "</td>");
			sb.append("<td>" + hdb.getFoot() + "</td>");
			sb.append("<td>" + hdb.getHead() + "</td>");
			sb.append("<td>" + hdb.getTemperature() + "</td>");
			sb.append("<td>" + hdb.getSleep() + "</td>");
			sb.append("<td>" + hdb.getDefecation() + "</td>");
			sb.append("<td>" + hdb.getDefstatus() + "</td>");
			sb.append("<td>" + hdb.getMeal() + "</td>");
			sb.append("<td>" + hdb.getMemo() + "</td>");
			sb.append("</tr>");
			System.out.println("리스트 : " +hdb);
			System.out.println("리스트1 : " +hdb.getAge());
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
