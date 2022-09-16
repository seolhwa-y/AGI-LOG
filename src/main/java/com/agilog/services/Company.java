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
import com.agilog.beans.BebeCalendarBean;
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
				case 84:
					this.updDoctorComment(mav);
					break;
				case 800:
					this.moveHealthDataList(mav);
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
        try {
              CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
              if(cb != null) {
            	  if(cb.getCoManagerCode()!=null) {
            		  //mav.addObject("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getResInfo", cb)));
            		  //mav.setViewName("reservationManagement");
            		  this.checkManager(mav);
                  } else {
                	  mav.setViewName("checkManager");
                  }
              }
              else {
                  mav.setViewName("companyLogin");
                  System.out.println("세션만료");
              }

          } catch (Exception e) {e.printStackTrace();}
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
		
		mav.addObject("healthDataList", this.makeHealthData(this.session.selectList("getHealthDataList", rb), rb));
		mav.setViewName("doctorHealthData");
	}

	@SuppressWarnings("unused")
	private void checkManager(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			if(cb != null) {
				if(cb.getCoManagerCode()!=null) {
					//예약 갯수 조회
					List<ReservationBean> re = this.session.selectList("getResCount",cb);
					mav.addObject("resCount",this.makeEventResCount(re));
					mav.addObject("coCode",cb.getCoManagerCode());
					//mav.addObject("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getResInfo", cb)));
					mav.setViewName("reservationManagement");
				}
				else if(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode()!=null) {
					cb.setCoManagerCode(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode());
					if(this.convertToBoolean(this.session.selectOne("isManagerCode", cb))) {
						// 세션에 저장할 로그인 유저 정보 가져오기
						CompanyBean company = (CompanyBean) this.session.selectList("getCompanyAccessAllInfo", cb).get(0);
						company.setCoName(this.enc.aesDecode(company.getCoName(), company.getCoCode()));
						System.out.println("체크 매니저 진입 체크 : "+company);
						// 세션에 userCode저장
						this.pu.setAttribute("companyAccessInfo", company);
						//예약 갯수 조회
						List<ReservationBean> re = this.session.selectList("getResCount",cb);
						mav.addObject("resCount",this.makeEventResCount(re));
						mav.addObject("coCode",cb.getCoManagerCode());
						//mav.addObject("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getResInfo", cb)));
						mav.setViewName("reservationManagement");
					}
					else {
						mav.addObject("message", "매니저 코드가 일치하지 않습니다. 다시 입력해주세요.");
						mav.setViewName("redirect:/");
					}
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

	private void updDoctorComment(ModelAndView mav) {
		ReservationBean rb = (ReservationBean) mav.getModel().get("reservationBean");
		
			if(rb.getDoComment()!="" || rb.getDoComment()!=null) {
				
				if(this.convertToBoolean(this.session.update("updDoctorComment",rb))){
					System.out.println("업데이트성공");
					mav.addObject("message", "소견서를 성공적으로 작성했습니다.");
					mav.setViewName("checkDoctor");
					
				}else {
					System.out.println("업데이트실패");
					mav.addObject("message", "소견서 작성에 실패했습니다.");
					mav.setViewName("checkDoctor");

				}
			}
	}

	
	//환자 리스트 EL작업
	private String makePatientList(List<ReservationBean> patientList) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<table class=\"doctorMgrH\">" );
		sb.append("<tr>");
		sb.append("<th class=\"doctorMgrM\">진료 날짜</th>");
		sb.append("<th class=\"doctorMgrM\">환자 이름</th>");
		sb.append("<th class=\"doctorMgrM\">진료 상태</th>");
		sb.append("</tr>");
		for(int idx=0; idx<patientList.size(); idx++) {
			ReservationBean rb = (ReservationBean)patientList.get(idx);
			sb.append("<tr onClick=\"moveHealthData('"+ rb.getResCode() +"','"+ rb.getResBbCode() +"','"+ rb.getResDate()+"')\">");
			sb.append("<td class=\"doctorMgrB\">" + rb.getResDate() + "</td>");
			sb.append("<td class=\"doctorMgrB\">" + rb.getResBbName() + "</td>");
			sb.append("<td class=\"doctorMgrB\">" + rb.getResActionName() + "</td>");
			sb.append("</tr>");
			if(rb.getDoComment() != null) {
				sb.append("<tr>");
				sb.append("<th class=\"doctorMgrCM\" colspan=\"3\">의사 소견서</th>");
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append("<td class=\"doctorMgrC\" colspan=\"3\">" + rb.getDoComment() + "</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</table>");
		
		return sb.toString();
	}

	//의사 리스트 EL작업
	private String makeDoctorList(List<DoctorBean> doctorList) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table class=\"doctorMgrH\">" );
		sb.append("<tr>");
		sb.append("<th class=\"doctorMgrM\">직원코드</th>");
		sb.append("<th class=\"doctorMgrM\">직원이름</th>");
		sb.append("<th class=\"doctorMgrM\">관리</th>");
		sb.append("</tr>");
		for(int idx=0; idx<doctorList.size(); idx++) {
			DoctorBean db = ((DoctorBean)doctorList.get(idx));
			sb.append("<tr>");
			sb.append("<td class=\"doctorMgrB\">" + db.getDoCode() + "</td>");
			sb.append("<td class=\"doctorMgrB\">" + db.getDoName() + "</td>");
			sb.append("<td class=\"doctorMgrB\"><button class=\"delBtn\">");
			sb.append("<i class=\"fa-solid fa-trash-can delBtn editBtn\" "
					+ "onClick=\"deleteDoctor("+ db.getDoCode() +")\"></i>Delete</button>");
			sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		return sb.toString();
	}
	//건강기록 EL작업
	private String makeHealthData(List<HealthDiaryBean> HealthDataList,ReservationBean resBean) {
		StringBuffer sb = new StringBuffer();
		
		for(int idx=0; idx<7; idx++) {
			HealthDiaryBean hdb = ((HealthDiaryBean)HealthDataList.get(idx));
			if(idx==0) {
				sb.append("<div id=\"doctorMgrTitleDiv\">" );
				sb.append("<p id=\"doctorMgrTitle\">건강 기록지</p></div>");
				sb.append("<table class=\"patientMgrH\">" );
				sb.append("<tr>");
				sb.append("<th colspan=\"11\" class=\"patientMrgS\">예약날짜 : "+ resBean.getResDate() +"</th>");
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append("<th class=\"patientMgrM\">날짜</th>");
				sb.append("<th class=\"patientMgrM\">키</th>");
				sb.append("<th class=\"patientMgrM\">몸무게</th>");
				sb.append("<th class=\"patientMgrM\">발사이즈</th>");
				sb.append("<th class=\"patientMgrM\">머리둘레</th>");
				sb.append("<th class=\"patientMgrM\">체온</th>");
				sb.append("<th class=\"patientMgrM\">수면시간</th>");
				sb.append("<th class=\"patientMgrM\">배변량</th>");
				sb.append("<th class=\"patientMgrM\">배변상태</th>");
				sb.append("<th class=\"patientMgrM\">식사량</th>");
				sb.append("<th class=\"patientMgrM\">메모</th>");
				sb.append("</tr>");
			}else {
				sb.append("<tr>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getHdDate() + "</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getBbHeight() + "&nbsp;cm</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getBbWeight() + "&nbsp;kg</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getFoot() + "&nbsp;mm</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getHead() + "&nbsp;inch</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getTemperature() + "&nbsp;&deg;c</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getSleep() + "</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getDefecation() + "</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getDefstatus() + "</td>");
				sb.append("<td class=\"patientMgrB\">" + hdb.getMeal() + "</td>");
				sb.append("<td class=\"patientMgrB m\">" + hdb.getMemo() + "</td>");
				sb.append("</tr>");
			}
		}
			sb.append("</table><br/>");
			System.out.println(resBean.getDoComment());
			sb.append("<div class=\"docCo\">의사소견 입력<br/><br/>");
			sb.append("<input type=\"text\" name=\"doctorComment\" class=\"commentInput\" placeholder=\"내용을 입력하세요.\"/>");
			sb.append("<button class=\"submitBtn btn\" "
					+ "onClick=\"updDoctorComment('"+ resBean.getResCode()+"','"+ resBean.getDoComment() + "')\">입력</button></div>");
		return sb.toString();
	}

	//캘린더 내부 예약개수 형식 만들기
	private String makeEventResCount(List<ReservationBean> re) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		if(re.size()!=0) {
			for (ReservationBean rb:re) {
				sb.append("{start:'" + rb.getResDate() + "'");
				sb.append(",title:'" + rb.getResCount() + "'");
				sb.append(",display:'list-item'");
				sb.append(",borderColor:'#ff9e80'");
				sb.append(",classNames:'resCount'}");
				if (idx++ != re.size() - 1) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	// BooleanCheck ��
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}

}
