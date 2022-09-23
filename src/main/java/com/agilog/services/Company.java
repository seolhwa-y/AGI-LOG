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
				// landing page濡� �씠�룞
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

	//�쓽�궗 異붽�
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

	//�쟾臾몄쓽 愿�由� �럹�씠吏� �씠�룞
	private void moveDoctorManagementCtl(ModelAndView mav) {
		try {
			if(((CompanyBean)(this.pu.getAttribute("companyAccessInfo"))).getCoManagerCode() != null) {

				CompanyBean compb = (CompanyBean)this.pu.getAttribute("companyAccessInfo");
				CompanyBean cb = new CompanyBean();

				cb.setCoCode(compb.getCoCode());
				/* �궡媛� 諛쏆� �봽濡쒖젥�듃�쓽 �뜲�씠�꽣瑜� html�뿉 留뚮뱾怨� EL濡� ���옣 */
				mav.addObject("doctor",this.makeDoctorList(this.session.selectList("getDoctorInfo", cb)));
				mav.addObject("companyAccessInfo",compb);
				mav.setViewName("doctorManagement");	
			}else {
				mav.setViewName("checkManager");

			}
		} catch (Exception e1) {e1.printStackTrace();}
	}


	//�삁�빟愿�由� �럹�씠吏� �씠�룞
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

	//�솚�옄�젙蹂� 遺덈윭�삤湲� �씤利� 諛� EL 異쒕젰
	private void movePatientManagementCtl(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			if(cb != null) {
				DoctorBean db = (DoctorBean) mav.getModel().get("doctorBean");
				//�꽭�뀡�뿉�꽌 coCode瑜� 媛��졇�� �궫�엯
				db.setCoCode(cb.getCoCode());

				if(this.convertToBoolean(this.session.selectOne("isDoctorCode", db))){
					DoctorBean acDoc = this.session.selectOne("isDoctorMember", db);

					//password 鍮꾧탳
					if(this.enc.matches(db.getDoPassword(), acDoc.getDoPassword())){
						this.movePatList(mav);
					} else {
						mav.addObject("message", "아이디 또는 비밀번호를 확인해주세요.");
						mav.setViewName("checkDoctor");
					}
				}else {
					mav.addObject("message", "아이디 또는 비밀번호를 확인해주세요.");
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


			//�삁�빟�셿猷�, 吏꾨즺�셿猷뚯씪 寃쎌슦瑜� 嫄몃윭�깂
			if(this.session.selectOne("isResOpenData",rb) != null) {
				String doCode = rb.getResDoCode();
				//Access媛� 1�씤 寃쎌슦留� 議고쉶
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
			sb.append("<div class =\"resAlert\">�삁�빟�긽�깭媛�<br/>'�삁�빟以�'�씠嫄곕굹 '�삁�빟痍⑥냼'�씤 �삁�빟嫄댁�<br/>�뿴�엺�븯�떎 �닔 �뾾�뒿�땲�떎.</div>");
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private void checkManager(ModelAndView mav) {
		try {
			CompanyBean cb = ((CompanyBean) this.pu.getAttribute("companyAccessInfo"));
			if(cb != null) {
				//留ㅻ땲�� 肄붾뱶 移섍퀬 濡쒓렇�씤 �꽦怨� �뻽�쓣�븣
				if(cb.getCoManagerCode()!=null) {
					//�삁�빟 媛��닔 議고쉶
					List<ReservationBean> re = this.session.selectList("getResCount",cb);
					mav.addObject("resCount",this.makeEventResCount(re));
					mav.addObject("coCode",cb.getCoManagerCode());
					mav.addObject("companyAccessInfo",cb);
					//mav.addObject("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getResInfo", cb)));
					mav.setViewName("reservationManagement");
				}
				//泥섏쓬 留ㅻ땲�� 肄붾뱶 移섍퀬 濡쒓렇�씤 �빐�빞�븷 �븣
				else if(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode()!=null) {
					cb.setCoManagerCode(this.enc.aesEncode(((CompanyBean)mav.getModel().get("companyBean")).getCoManagerCode(), cb.getCoCode()));
					if(this.convertToBoolean(this.session.selectOne("isManagerCode", cb))) {
						// �꽭�뀡�뿉 ���옣�븷 濡쒓렇�씤 �쑀�� �젙蹂� 媛��졇�삤湲�
						CompanyBean company = (CompanyBean) this.session.selectList("getCompanyAccessAllInfo", cb).get(0);
						// �꽭�뀡�뿉 userCode���옣
						this.pu.setAttribute("companyAccessInfo", company);
						//�삁�빟 媛��닔 議고쉶
						List<ReservationBean> re = this.session.selectList("getResCount",cb);
						mav.addObject("resCount",this.makeEventResCount(re));
						mav.addObject("coCode",cb.getCoManagerCode());
						mav.addObject("companyAccessInfo",cb);
						//mav.addObject("resInfo", this.makeHTMLCReservation(this.session.selectList("getDoctorInfo", cb), this.session.selectList("getResInfo", cb)));
						mav.setViewName("reservationManagement");
					}
					else {
						mav.addObject("message", "留ㅻ땲�� 肄붾뱶媛� �씪移섑븯吏� �븡�뒿�땲�떎. �떎�떆 �엯�젰�빐二쇱꽭�슂.");
						mav.setViewName("redirect:/");
					}
				}
			}
			else {
				mav.setViewName("companyLogin");
			}

		} catch (Exception e) {e.printStackTrace();}
	}
	//�쓽�궗 �궘�젣
	private void deleteDoctorCtl(Model model) {
		DoctorBean db = (DoctorBean)model.getAttribute("doctorBean");

		try {
			db.setCoCode(((CompanyBean)this.pu.getAttribute("companyAccessInfo")).getCoCode());

			this.session.delete("delDoctorInfo", db);
			model.addAttribute("doctor",this.session.selectList("getDoctorInfo", db));
		} catch (Exception e) {e.printStackTrace();}
	}
	//�쓽�궗 肄붾뱶 以묐났寃��궗
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
				//mav.addObject("message", "�냼寃ъ꽌瑜� �꽦怨듭쟻�쑝濡� �옉�꽦�뻽�뒿�땲�떎.");
				//mav.setViewName("checkDoctor");
				rb.setResBbCode(((ReservationBean)this.session.selectOne("getPatInfo",rb)).getResBbCode());
				DoctorBean db = new DoctorBean();
				db.setDoCode(rb.getResDoCode());
				mav.addObject(rb);
				mav.addObject(db);
				this.moveHealthDataList(mav);
				//this.movePatList(mav);

			}else {
				mav.addObject("message", "�냼寃ъ꽌 �옉�꽦�뿉 �떎�뙣�뻽�뒿�땲�떎.");
				mav.setViewName("checkDoctor");

			}
		}
	}


	//�솚�옄 由ъ뒪�듃 EL�옉�뾽
	private String makePatientList(List<ReservationBean> patientList,DoctorBean db) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table class=\"doctorMgrH\">" );
		sb.append("<tr>");
		sb.append("<th class=\"doctorMgrM\">吏꾨즺 �궇吏�</th>");
		sb.append("<th class=\"doctorMgrM\">�솚�옄 �씠由�</th>");
		sb.append("<th class=\"doctorMgrM\">吏꾨즺 �긽�깭</th>");
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
			sb.append("<th class=\"doctorMgrCM1\">�옉�꽦�븳 吏꾨즺 �냼寃�</th>");
			sb.append("<th class=\"doctorMgrCM2\">"+ rb.getDoComment() + "</th>");
			sb.append("</tr></table>");
		}else if(rb.getDoComment() == null){
			sb.append("<div class=\"docCo\">�쓽�궗�냼寃� �엯�젰<br/><br/>");
			sb.append("<input type=\"text\" name=\"doctorComment\" class=\"commentInput\" placeholder=\"�궡�슜�쓣 �엯�젰�븯�꽭�슂.\"/>");
			sb.append("<button class=\"submitBtn btn\" "+"onClick=\"insDoctorComment('"+ rb.getResCode() +"','"+doCode+ "')\">�엯�젰</button></div>");
		}
		return sb.toString();
	}

	//�쓽�궗 由ъ뒪�듃 EL�옉�뾽
	private String makeDoctorList(List<DoctorBean> doctorList) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table class=\"doctorMgrH\">" );
		sb.append("<tr>");
		sb.append("<th class=\"doctorMgrM\">吏곸썝肄붾뱶</th>");
		sb.append("<th class=\"doctorMgrM\">吏곸썝�씠由�</th>");
		sb.append("<th class=\"doctorMgrM\">愿�由�</th>");
		sb.append("</tr>");
		for(int idx=0; idx<doctorList.size(); idx++) {
			DoctorBean db = ((DoctorBean)doctorList.get(idx));
			sb.append("<tr>");
			sb.append("<td class=\"doctorMgrB\">" + db.getDoCode() + "</td>");
			sb.append("<td class=\"doctorMgrB\">" + db.getDoName() + "</td>");
			sb.append("<td class=\"doctorMgrB\">"
					+ "<button class=\"delBtn\" onClick=\"deleteDoctor("+ db.getDoCode() +")\">");
			sb.append("<i class=\"fa-solid fa-trash-can delBtn\"></i>�궘�젣</button>");
			sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		return sb.toString();
	}
	//嫄닿컯湲곕줉 EL�옉�뾽
	private String makeHealthData(List<HealthDiaryBean> HealthDataList,ReservationBean resBean) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id=\"doctorMgrTitleDiv\">" );
		sb.append("<p id=\"doctorMgrTitle\">嫄닿컯 湲곕줉吏�</p></div>");
		sb.append("<table class=\"patientMgrH\">" );
		sb.append("<tr>");
		sb.append("<th colspan=\"11\" class=\"patientMrgS\">�삁�빟�궇吏� : "+ resBean.getResDate() +"</th>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<th class=\"patientMgrM\">�궇吏�</th>");
		sb.append("<th class=\"patientMgrM\">�궎</th>");
		sb.append("<th class=\"patientMgrM\">紐몃Т寃�</th>");
		sb.append("<th class=\"patientMgrM\">諛쒖궗�씠利�</th>");
		sb.append("<th class=\"patientMgrM\">癒몃━�몮�젅</th>");
		sb.append("<th class=\"patientMgrM\">泥댁삩</th>");
		sb.append("<th class=\"patientMgrM\">�닔硫댁떆媛�</th>");
		sb.append("<th class=\"patientMgrM\">諛곕��웾</th>");
		sb.append("<th class=\"patientMgrM\">諛곕��긽�깭</th>");
		sb.append("<th class=\"patientMgrM\">�떇�궗�웾</th>");
		sb.append("<th class=\"patientMgrM\">硫붾え</th>");
		sb.append("</tr>");
		for(int idx=0; idx<HealthDataList.size(); idx++) {
			HealthDiaryBean hdb = ((HealthDiaryBean)HealthDataList.get(idx));
			
				sb.append("<tr>");
				if(hdb.getHdDate()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getHdDate() + "</td>");
				} else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getBbHeight()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getBbHeight() + "&nbsp;cm</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getBbWeight()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getBbWeight() + "&nbsp;kg</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}	
				if(hdb.getFoot()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getFoot() + "&nbsp;mm</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getHead()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getHead() + "&nbsp;inch</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getTemperature()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getTemperature() + "&nbsp;&deg;c</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getSleep()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getSleep() + "</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getDefecation()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getDefecation() + "</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getDefstatus()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getDefstatus() + "</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getMeal()!=null) {
					sb.append("<td class=\"patientMgrB\">" + hdb.getMeal() + "</td>");
				}else {sb.append("<td class=\"patientMgrB\">�젙蹂댁뾾�쓬</td>");}
				if(hdb.getMemo()!=null) {
					sb.append("<td class=\"patientMgrB m\">" + hdb.getMemo() + "</td>");
				}else {sb.append("<td class=\"patientMgrB m\">�젙蹂댁뾾�쓬</td>");}
				sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}


	//罹섎┛�뜑 �궡遺� �삁�빟媛쒖닔 �삎�떇 留뚮뱾湲�
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

	// BooleanCheck 占쏙옙
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}

}
