package com.agilog.services3;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.CompanyBean;
import com.agilog.beans.DoctorBean;
import com.agilog.beans.ReservationBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.services2.smsSend;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;
import java.util.Base64.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

@Service
public class Company3 implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	@Autowired
	private smsSend sms;
	public Company3() {
	}
	// Controller
	public void backController(ModelAndView mav, int serviceCode) {
		switch(serviceCode) {
		
		default:
			break;
		}	
	}

	public void backController(Model model, int serviceCode) {
		switch(serviceCode) {
		case 72: this.updateReservationCtl(model); break;
		case 118: this.resManagementCtl(model); break;
		case 119: this.setResTimeCtl(model); break;
		case 120: this.getDoctorResTimeCtl(model); break;
		default:
			break;
		}	
	}
	//의사변경
	private void getDoctorResTimeCtl(Model model) {
		HashMap<String,String> map = new HashMap<String,String>();
		try {
			CompanyBean cb = (CompanyBean) this.pu.getAttribute("companyAccessInfo");
			if(cb != null) {
				ReservationBean rb = (ReservationBean) model.getAttribute("reservationBean");
				if(cb.getCoManagerCode()!=null) {
					if(this.convertToBoolean(this.session.selectOne("isManagerCode", cb))) {
						rb.setResCoCode(cb.getCoCode());
						//의사정보 가져오기
						//해당 의사의 시간정보 가져오기
						List<ReservationBean> r = this.session.selectList("getDoctorResTime",rb);
						map.put("time", this.makeTimeInfo(r, rb));
						
//						SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//						Date d = form.parse(rb.getResDate());
//						rb.setResDate(sdf.format(d));
//						
//						map.put("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getDateResInfo", rb)));
//						model.addAttribute("dateInfo", map);
						model.addAttribute("time", map);
					}
					else {
					}
				}
			}
			else {
				System.out.println("세션만료");
			}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	@Transactional(rollbackFor = SQLException.class)
	private void updateReservationCtl(Model model) {
		try {
			//세션 획득 후 체크. 없으면 로그인 페이지로
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			if (cb.getCoManagerCode() != null) {
				//예약빈 세팅
				ReservationBean rb = (ReservationBean) model.getAttribute("reservationBean");
				rb.setResCoCode(cb.getCoCode());
				
				//닥터 코드가 있으면 닥터 코드를 포함하여 업데이트, 없으면 닥터 코드를 제외한 업데이트
				if (rb.getResDoCode() != null) {
					if(this.convertToBoolean(this.session.update("updRDRes", rb))) {
						if(rb.getRcCode().equals("CC")) {
							rb.setResCount(this.session.selectOne("getTimeResCount",rb));
							//예약취소 시 예약시간 테이블 count컬럼 -1 업데이트
							if(this.convertToBoolean(this.session.update("updDoctorResTime",rb))) {
								//예약상태 변경 문자보내기
								rb = this.session.selectOne("getResInfoForSms", rb);
								HashMap<String,String> map = new HashMap<String,String>();
								map.put("resDate", rb.getResDate());
								map.put("resSuName", this.enc.aesDecode(rb.getResSuName(), rb.getResSuCode()));
								map.put("resSuPhone", this.enc.aesDecode(rb.getResSuPhone(), rb.getResSuCode()));
								map.put("resCoName", rb.getResCoName());
								map.put("resBbName", rb.getResBbName());
								map.put("resActionName", rb.getResActionName());
								map.put("resDoName", rb.getResDoName());
								//sms.sendSMS(map);
							}
						}
					}
				} else {
					if(this.convertToBoolean(this.session.update("updCPRes", rb))) {
						if(rb.getRcCode().equals("CC")) {
							rb.setResCount(this.session.selectOne("getTimeResCount",rb));
							//예약취소 시 예약시간 테이블 count컬럼 -1 업데이트
							if(this.convertToBoolean(this.session.update("updDoctorResTime",rb))) {
								//예약상태 변경 문자보내기
								rb = this.session.selectOne("getResInfoForSms", rb);
								HashMap<String,String> map = new HashMap<String,String>();
								map.put("resDate", rb.getResDate());
								map.put("resSuName", this.enc.aesDecode(rb.getResSuName(), rb.getResSuCode()));
								map.put("resSuPhone", this.enc.aesDecode(rb.getResSuPhone(), rb.getResSuCode()));
								map.put("resCoName", rb.getResCoName());
								map.put("resBbName", rb.getResBbName());
								map.put("resActionName", rb.getResActionName());
								map.put("resDoName", rb.getResDoName());
								//sms.sendSMS(map);
							}
						}
					}
				}
				//새 표 작성 후 페이지 리다이렉트
				this.resManagementCtl(model);
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//클릭한 날짜의 에약정보 불러오긴
	private void resManagementCtl(Model model) {
		HashMap<String,String> map = new HashMap<String,String>();
		try {
			CompanyBean cb = (CompanyBean) this.pu.getAttribute("companyAccessInfo");
			if(cb != null) {
				ReservationBean rb = (ReservationBean) model.getAttribute("reservationBean");
				if(cb.getCoManagerCode()!=null) {
					if(this.convertToBoolean(this.session.selectOne("isManagerCode", cb))) {
						rb.setResCoCode(cb.getCoCode());
						//의사정보 가져오기
						List<DoctorBean> docList = this.session.selectList("getDoctorInfo", cb);
						map.put("doctorList", this.makeDoctorSelect(docList));
						
						//첫번째 의사의 시간정보 가져오기
						if(docList.size()!=0) {
							rb.setResDoCode(docList.get(0).getDoCode());
							List<ReservationBean> r = this.session.selectList("getDoctorResTime",rb);
							map.put("time", this.makeTimeInfo(r, rb));
						}
						
						SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						Date d = form.parse(rb.getResDate());
						rb.setResDate(sdf.format(d));
						
						map.put("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getDateResInfo", rb)));
						model.addAttribute("dateInfo", map);
					}
					else {
					}
				}
			}
			else {
				System.out.println("세션만료");
			}
		} catch (Exception e) {e.printStackTrace();}
	}
	
	//시간 업데이트
	@Transactional(rollbackFor = SQLException.class)
	private void setResTimeCtl(Model model) {
		HashMap<String,String> map = new HashMap<String,String>();
		try {
			ReservationBean rb = (ReservationBean) model.getAttribute("reservationBean");
			rb.setResCoCode(((CompanyBean) this.pu.getAttribute("companyAccessInfo")).getCoCode());
			// 예약가능시간 삽입 rt->insert
			if (this.convertToBoolean(this.session.insert("insDoctorResTime", rb))) {
				// 성공
				List<ReservationBean> r = this.session.selectList("getDoctorResTime",rb);
				map.put("time", this.makeTimeInfo(r, rb));
				model.addAttribute("time", map);
			} else {
				// 실패
				System.out.println("실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String makeDoctorSelect(List<DoctorBean> doctorList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<select name = 'docTime' onchange='chageDoctor()'>\n");
		int idx = 0;
		for (DoctorBean db : doctorList) {
			if(idx++==0) {
				sb.append("<option value='"+ db.getDoCode() +"' selected>\n" + db.getDoName() + "</option>\n");
			} else {
				sb.append("<option value='"+ db.getDoCode() +"'>\n" + db.getDoName() + "</option>\n");
			}
		}
		return sb.toString();
	}
	
	private String makeTimeInfo(List<ReservationBean> r, ReservationBean rb) {
		String sample[] = {"09","10","11","12","14","15","16","17"};
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\'morning\'>");
		
		
		for(int idx=0;idx<sample.length;idx++) {
			sb.append("<button value=\'"+rb.getResDate()+":"+sample[idx]+"\' ");
			for(int a=0;a<r.size();a++) {
				if(sample[idx].equals(r.get(a).getResTime())) {
					//그냥 연 예약시간
					sb.append("class=\'mBtnO disable\' disabled ");
					break;
				}
			}
			sb.append("class=\'mBtnO btn\' onclick=\'setResTime(this)\'");
			sb.append(">"+sample[idx]+":00</button>");
			
			if(idx==3) {
				sb.append("</div>");
				sb.append("<div id=\'line\'></div>");
				sb.append("<div id=\'afternoon\'>");
			}
		}
		
		sb.append("</div>");
		return sb.toString();
	}
	
	private String makeHTMLCReservation(List<DoctorBean> doctorList, List<ReservationBean> reservationList) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		int idx2 = 0;
			sb.append("<table id=\"reservationTable\">\n");
			sb.append("<tr>\n");
			sb.append("<th class='resH'>예약시간</th>\n");
			sb.append("<th class='resH'>환자명</th>\n");
			sb.append("<th class='resH'>담당의</th>\n");
			sb.append("<th class='resH'>예약상태</th>\n");
			sb.append("<th class='resH'>상태변경</th>\n");
			sb.append("</tr>\n");
			sb.append("<tr>\n");
			for(ReservationBean rb : reservationList) {
				sb.append("<tr >\n");
				sb.append("<td class='resD resTime'>" + rb.getResTime().substring(0, 2)+":00" + "</td>\n");
				sb.append("<td class='resD'>\n" + rb.getResBbName() + "</td>\n");
				switch(rb.getRcCode()) {
				case "RD" :
					sb.append("<td class='resD'>\n" + rb.getResDoName() + "<input type='hidden' name='resDoCode' value='" + rb.getResDoCode() + "'/></td>\n");
					sb.append("<td class='resD'>\n");
					sb.append("<select name = 'selectResState'>\n");
					sb.append("<option disabled selected>예약중</option>\n");
					sb.append("<option value='CP'>예약완료</option>\n");
					sb.append("<option value='CC'>예약취소</option>\n");
					sb.append("</td>\n");
					sb.append("<td class='resD'><input type='button' value='저장' class='saveBtn' onClick=\"updateReservation('" + rb.getResCode() + "','" + idx + "','" + idx2 + "')\"></td>\n");
					idx++;
					idx2++;
					break;
				case "CP" :
					sb.append("<td class='resD'>\n" + rb.getResDoName() + "<input type='hidden' name='resDoCode' value='" + rb.getResDoCode() + "'/></td>\n");
					sb.append("<td class='resD'>\n");
					sb.append("<select name = 'selectResState'>\n");
					sb.append("<option disabled selected>예약완료</option>\n");
					sb.append("<option value='CC'>예약취소</option>\n");
					sb.append("</td>\n");
					sb.append("<td class='resD'><input type='button' value='저장' class='saveBtn' onClick=\"updateReservation('" + rb.getResCode() + "','" + idx + "', '')\"></td>\n");
					idx++;
					break;
				case "CC" :
					sb.append("<td class='resD'></td>\n");
					sb.append("<td class='resD'>\n");
					sb.append("<option disabled selected>예약취소</option>\n");
					sb.append("</td>\n");
					sb.append("<td class='resD'></td>\n");
					break;
				case "MC" :
					sb.append("<td class='resD'>\n" + rb.getResDoName() + "</td>\n");
					sb.append("<td class='resD'>\n");
					sb.append("<option disabled selected>진료 완료</option>\n");
					sb.append("</td>\n");
					sb.append("<td class='resD'></td>\n");
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
