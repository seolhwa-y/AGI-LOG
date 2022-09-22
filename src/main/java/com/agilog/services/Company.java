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
					this.insertDoctorComment(mav);
					break;
				case 83:
					this.moveHealthDataList(mav);
					break;
				case 1000:
					this.moveCheckDoctor(mav);
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



	private void moveCheckDoctor(ModelAndView mav) {
		
		try {
			CompanyBean cb;
			cb =(CompanyBean)this.pu.getAttribute("companyAccessInfo");
			mav.addObject("companyAccessInfo",cb);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				mav.addObject("companyAccessInfo",compb);
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
				mav.addObject("companyAccessInfo",cb);
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
			}

		} catch (Exception e) {e.printStackTrace();}
	}

	//환자정보 불러오기 인증 및 EL 출력
	private void movePatientManagementCtl(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));

			if(cb != null) {
				DoctorBean db = (DoctorBean) mav.getModel().get("doctorBean");
				//세션에서 coCode를 가져와 삽입
				db.setCoCode(cb.getCoCode());

				if(this.convertToBoolean(this.session.selectOne("isDoctorCode", db))){
					DoctorBean acDoc = this.session.selectOne("isDoctorMember", db);

					//password 비교
					if(this.enc.matches(db.getDoPassword(), acDoc.getDoPassword())){
						this.movePatList(mav);
					}
				}else {
					mav.setViewName("checkDoctor");
				}
			}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	private void movePatList(ModelAndView mav) {
		CompanyBean cb;
		try {
			cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			mav.addObject("companyAccessInfo",cb);
			DoctorBean db = (DoctorBean) mav.getModel().get("doctorBean");
			
			db.setCoCode(cb.getCoCode());
			
			mav.addObject("patient", this.makePatientList(this.session.selectList("getPatientInfoList", db),db));
			mav.setViewName("patientManagement");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void moveHealthDataList(ModelAndView mav) {
		CompanyBean cb;
		try {
			cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			ReservationBean rb = (ReservationBean) mav.getModel().get("reservationBean");
			rb.setResCoCode(cb.getCoCode());


			//예약완료, 진료완료일 경우를 걸러냄
			if(this.session.selectOne("isResOpenData",rb) != null) {
				String doCode = rb.getResDoCode();
				//Access가 1인 경우만 조회
				if(this.convertToBoolean(this.session.selectOne("isPrivateData", rb))){
					mav.addObject("healthDataList", this.makeHealthData(this.session.selectList("getHealthDataList", rb), rb));
					mav.addObject("doctorComment",this.makePatientCo(this.session.selectOne("getPatientComment",rb),doCode));
				}else {
					mav.addObject("doctorComment",this.makePatientCo((ReservationBean)this.session.selectOne("getPatientComment",rb),doCode));
				}mav.setViewName("doctorHealthData");

			}else if(this.session.selectOne("isResOpenData",rb) == null){
				mav.addObject("message",this.alert());
			}mav.setViewName("doctorHealthData");
			
		}catch (Exception e) {e.printStackTrace();}
	}
	
	private String alert() {
		StringBuffer sb = new StringBuffer();
			sb.append("<div class =\"resAlert\">예약상태가<br/>'예약중'이거나 '예약취소'인 예약건은<br/>열람하실 수 없습니다.</div>");
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private void checkManager(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			if(cb != null) {
				//매니저 코드 치고 로그인 성공 했을때
				if(cb.getCoManagerCode()!=null) {
					//예약 갯수 조회
					List<ReservationBean> re = this.session.selectList("getResCount",cb);
					mav.addObject("resCount",this.makeEventResCount(re));
					mav.addObject("coCode",cb.getCoManagerCode());
					mav.addObject("companyAccessInfo",cb);
					//mav.addObject("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getResInfo", cb)));
					mav.setViewName("reservationManagement");
				}
				//처음 매니저 코드 치고 로그인 해야할 때
				else if(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode()!=null) {
					cb.setCoManagerCode(this.enc.aesEncode(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode(), cb.getCoCode()));
					if(this.convertToBoolean(this.session.selectOne("isManagerCode", cb))) {
						// 세션에 저장할 로그인 유저 정보 가져오기
						CompanyBean company = (CompanyBean) this.session.selectList("getCompanyAccessAllInfo", cb).get(0);
						// 세션에 userCode저장
						this.pu.setAttribute("companyAccessInfo", company);
						//예약 갯수 조회
						List<ReservationBean> re = this.session.selectList("getResCount",cb);
						mav.addObject("resCount",this.makeEventResCount(re));
						mav.addObject("coCode",cb.getCoManagerCode());
						mav.addObject("companyAccessInfo",cb);
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
			}

		} catch (Exception e) {e.printStackTrace();}
	}
	//의사 삭제
	private void deleteDoctorCtl(Model model) {
		DoctorBean db = (DoctorBean)model.getAttribute("doctorBean");

		try {
			db.setCoCode(((CompanyBean)this.pu.getAttribute("companyAccessInfo")).getCoCode());

			this.session.delete("delDoctorInfo", db);
			model.addAttribute("doctor",this.session.selectList("getDoctorInfo", db));
		} catch (Exception e) {e.printStackTrace();}
	}
	//의사 코드 중복검사
	private void checkDoctorCodeCtl(Model model) {
		String code = (String) model.getAttribute("code");
		DoctorBean db = (DoctorBean) model.getAttribute("doctorBean");
		int result = 0;
		switch (code) {
		case "0":

			break;
		case "1":
			result = Integer.parseInt(this.session.selectOne("checkDoctorCode", db).toString());
			break;
		}
		if (this.convertToBoolean(result)) {
			model.addAttribute("check", "no");
		} else {
			model.addAttribute("check", "ok");
		}
	}

	private void insertDoctorComment(ModelAndView mav) {
		ReservationBean rb = (ReservationBean) mav.getModel().get("reservationBean");

		if(rb.getDoComment()!="" || rb.getDoComment()!=null) {

			if(this.convertToBoolean(this.session.update("updDoctorComment",rb))){
				//mav.addObject("message", "소견서를 성공적으로 작성했습니다.");
				//mav.setViewName("checkDoctor");
				rb.setResBbCode(((ReservationBean)this.session.selectOne("getPatInfo",rb)).getResBbCode());
				DoctorBean db = new DoctorBean();
				db.setDoCode(rb.getResDoCode());
				mav.addObject(rb);
				mav.addObject(db);
				this.moveHealthDataList(mav);
				//this.movePatList(mav);

			}else {
				mav.addObject("message", "소견서 작성에 실패했습니다.");
				mav.setViewName("checkDoctor");

			}
		}
	}


	//환자 리스트 EL작업
	private String makePatientList(List<ReservationBean> patientList,DoctorBean db) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table class=\"doctorMgrH\">" );
		sb.append("<tr>");
		sb.append("<th class=\"doctorMgrM\">진료 날짜</th>");
		sb.append("<th class=\"doctorMgrM\">환자 이름</th>");
		sb.append("<th class=\"doctorMgrM\">진료 상태</th>");
		sb.append("</tr>");
		for(int idx=0; idx<patientList.size(); idx++) {
			ReservationBean rb = (ReservationBean)patientList.get(idx);
			sb.append("<tr onClick=\"moveHealthData('"+ rb.getResCode() +"','"+ rb.getResBbCode() +"','"+ rb.getResDate()+"','"+ db.getDoCode()+"')\">");
			sb.append("<td class=\"doctorMgrB\">" + rb.getResDate() + "</td>");
			sb.append("<td class=\"doctorMgrB\">" + rb.getResBbName() + "</td>");
			sb.append("<td class=\"doctorMgrB\">" + rb.getResActionName() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		return sb.toString();
	}
	private String makePatientCo(ReservationBean rb,String doCode) {
		StringBuffer sb = new StringBuffer();
		if(rb.getDoComment() != null) {
			sb.append("<table class=\"commentTb\"><tr>");
			sb.append("<th class=\"doctorMgrCM1\">작성한 진료 소견</th>");
			sb.append("<th class=\"doctorMgrCM2\">"+ rb.getDoComment() + "</th>");
			sb.append("</tr></table>");
		}else if(rb.getDoComment() == null){
			sb.append("<div class=\"docCo\">의사소견 입력<br/><br/>");
			sb.append("<input type=\"text\" name=\"doctorComment\" class=\"commentInput\" placeholder=\"내용을 입력하세요.\"/>");
			sb.append("<button class=\"submitBtn btn\" "+"onClick=\"insDoctorComment('"+ rb.getResCode() +"','"+doCode+ "')\">입력</button></div>");
		}
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
			sb.append("<td class=\"doctorMgrB\">"
					+ "<button class=\"delBtn\" onClick=\"deleteDoctor("+ db.getDoCode() +")\">");
			sb.append("<i class=\"fa-solid fa-trash-can delBtn\"></i>삭제</button>");
			sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		return sb.toString();
	}
	//건강기록 EL작업
	private String makeHealthData(List<HealthDiaryBean> HealthDataList,ReservationBean resBean) {
		StringBuffer sb = new StringBuffer();
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
		for(int idx=0; idx<HealthDataList.size(); idx++) {
			HealthDiaryBean hdb = ((HealthDiaryBean)HealthDataList.get(idx));
			
				sb.append("<tr>");
				if(hdb.getHdDate()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getHdDate() + "</td>");
				} else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getBbHeight()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getBbHeight() + "&nbsp;cm</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getBbWeight()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getBbWeight() + "&nbsp;kg</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}	
				if(hdb.getFoot()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getFoot() + "&nbsp;mm</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getHead()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getHead() + "&nbsp;inch</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getTemperature()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getTemperature() + "&nbsp;&deg;c</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getSleep()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getSleep() + "</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getDefecation()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getDefecation() + "</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getDefstatus()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getDefstatus() + "</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getMeal()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getMeal() + "</td>");
				}else {sb.append("<td class=\"patientMgrB\">정보없음</td>");}
				if(hdb.getMemo()!=null) {
					sb.append("<td class=\"patientMgrB m\">" + hdb.getMemo() + "</td>");
				}else {sb.append("<td class=\"patientMgrB m\">정보없음</td>");}
				sb.append("</tr>");
		}
		sb.append("</table>");
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
