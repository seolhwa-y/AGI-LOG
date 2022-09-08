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

@Service
public class Company2 implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;

	public Company2() {
	}
	// Controller
	public void backController(ModelAndView mav, int serviceCode) {
		/*
		 * try {
			if(this.pu.getAttribute("accessInfo")!=null) {
		 */
		switch(serviceCode) {
		
		case 72:
			this.updateReservationCtl(mav);
			break;
		default:
			break;
		}	
	}

	public void backController(Model model, int serviceCode) {

	}

	private void updateReservationCtl(ModelAndView mav) {
		System.out.println("업데이트 진입 체크");
		try {
			//세션 획득 후 체크. 없으면 로그인 페이지로
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			if (cb.getCoManagerCode() != null) {
				//예약빈 세팅
				ReservationBean rb = (ReservationBean) mav.getModel().get("reservationBean");
				rb.setResCoCode(cb.getCoCode());
				System.out.println("닥터 코드 체크 : " + rb.getDoCode());
				System.out.println("카테고리 코드 체크 : " + rb.getRcCode());
				System.out.println("예약 코드 체크 : " + rb.getResCode());
				System.out.println("컴패니 코드 체크 : " + rb.getResCoCode());
				
				//닥터 코드가 있으면 닥터 코드를 포함하여 업데이트, 없으면 닥터 코드를 제외한 업데이트
				if (rb.getDoCode() != null) {
					System.out.println("rd 진입 체크");
					this.session.update("updRDRes", rb);
				} else {
					System.out.println("cp 진입 체크");
					this.session.update("updCPRes", rb);
				}

				//새 표 작성 후 페이지 리다이렉트
				mav.addObject("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getResInfo", cb)));
				mav.setViewName("reservationManagement");
			} else {
				mav.addObject("message", "세션 만료");
				mav.setViewName("companyLogin");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private String makeHTMLDoctor(List<CompanyBean> companyList) {
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}

	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}

}
