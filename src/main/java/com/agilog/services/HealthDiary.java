package com.agilog.services;

import java.sql.SQLException;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BabyBean;
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.ReservationBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;
@Service
public class HealthDiary implements ServiceRule {
	
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	
	public HealthDiary() {}
	
	
	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		switch(serviceCode) {
		case 30: this.deleteHealthDiaryCtl(mav); break;
		case 33: this.moveHealthDiaryPageCtl(mav); break;
		case 37: this.insertHealthDiaryCtl(mav); break;
		case 76: this.moveDoctorCommentCtl(mav); break;
		}
	}
	public void backController(Model model, int serviceCode) {
		switch(serviceCode) {
		case 94: this.updateMyHealthDiaryCtl(model);break;
		case 103: this.showHealthDiaryCtl(model); break;
		}
	}
	
	@Transactional(rollbackFor = SQLException.class)
	private void deleteHealthDiaryCtl(ModelAndView mav) {
		try {
			HealthDiaryBean hb = (HealthDiaryBean) mav.getModel().get("healthDiaryBean");
			//세션검사
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			//세션존재 -> 건강일기 삭제
			if(ab!=null) {
				hb.setSuCode(ab.getSuCode());
				if(this.converToBoolean(this.session.delete("delHealthDiary",hb))) {
					mav.setViewName("redirect:/MoveHealthDiaryPage");
				}
			} else {
				mav.setViewName("login");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//건강일기 페이지 이동
	private void moveHealthDiaryPageCtl(ModelAndView mav) {
		try {
			String page = "login";
			//세션검사
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			//세션존재 -> 해당유저의 전체 아기&전체 건강일기 가져오기
			if(ab!=null) {
				//아기정보
				List<BabyBean> babyList = this.session.selectList("getTotalBabyCode",ab);
				mav.addObject("babyInfo", this.makeBabySelect(babyList));
				//건강일기
				List<HealthDiaryBean> healthList = this.session.selectList("getHealthDiary",ab);
				mav.addObject("diaryList", this.makeHealthDiaryHtml(healthList));
				page="healthDiary";
			}
			
			mav.setViewName(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void moveHealthStatusPageCtl(ModelAndView mav) {
		
	}
	@Transactional(rollbackFor = SQLException.class)
	private void insertHealthDiaryCtl(ModelAndView mav) {
		try {
			boolean result = false;
			HealthDiaryBean hb = (HealthDiaryBean) mav.getModel().get("healthDiaryBean");
			//세션검사
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			//세션존재 -> insert
			if(ab!=null) {
				hb.setSuCode(ab.getSuCode());
				//다이어리 코드 가져오기
				hb.setHdCode(this.session.selectOne("getHealthDiaryCode",hb));
				//항목 검사
				//몸무게
				if(hb.getBbWeight()!=""&&hb.getBbWeight()!=null) {
					hb.setCaCode("01");
					if(this.converToBoolean(this.session.insert("insHDWeight",hb)))
						result = true;
					else result = false;
				}
				//키
				if(hb.getBbHeight()!=""&&hb.getBbHeight()!=null) {
					hb.setCaCode("02");
					if(this.converToBoolean(this.session.insert("insHDHeight",hb)))
						result = true;
					else result = false;
				}
				//머리둘레
				if(hb.getHead()!=""&&hb.getHead()!=null) {
					hb.setCaCode("03");
					if(this.converToBoolean(this.session.insert("insHDHead",hb)))
						result = true;
					else result = false;
				}
				//발사이즈
				if(hb.getFoot()!=""&&hb.getFoot()!=null) {
					hb.setCaCode("04");
					if(this.converToBoolean(this.session.insert("insHDFoot",hb)))
						result = true;
					else result = false;
				}
				//체온
				if(hb.getTemperature()!=""&&hb.getTemperature()!=null) {
					hb.setCaCode("05");
					if(this.converToBoolean(this.session.insert("insHDTemp",hb)))
						result = true;
					else result = false;
				}
				//수면량
				if(hb.getSleep()!=""&&hb.getSleep()!=null) {
					hb.setCaCode("06");
					if(this.converToBoolean(this.session.insert("insHDSleep",hb)))
						result = true;
					else result = false;
				}
				//배변량
				if(hb.getDefecation()!=""&&hb.getDefecation()!=null) {
					hb.setCaCode("07");
					if(this.converToBoolean(this.session.insert("insHDdefecation",hb)))
						result = true;
					else result = false;
				}
				//배변상태
				if(hb.getDefstatus()!=""&&hb.getDefstatus()!=null) {
					hb.setCaCode("08");
					if(this.converToBoolean(this.session.insert("insHDDefstatus",hb)))
						result = true;
					else result = false;
				}
				//식사량
				if(hb.getMeal()!=""&&hb.getMeal()!=null) {
					hb.setCaCode("09");
					if(this.converToBoolean(this.session.insert("insHDMeal",hb)))
						result = true;
					else result = false;
				}
				//메모
				if(hb.getMemo()!=""&&hb.getMemo()!=null) {
					hb.setCaCode("10");
					if(this.converToBoolean(this.session.insert("insHDMemo",hb)))
						result = true;
					else result = false;
				}
				if(result) {
					mav.setViewName("redirect:/MoveHealthDiaryPage");
				} else {
					mav.addObject("fail", "일기 작성 오류");
					mav.setViewName("redirect:/MoveHealthDiaryPage");
				}
			} else mav.setViewName("login");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//건강일기 메모 수정
	@Transactional(rollbackFor = SQLException.class)
	private void updateMyHealthDiaryCtl(Model model) {
		try {
			HealthDiaryBean hb = (HealthDiaryBean) model.getAttribute("healthDiaryBean");
			//세션검사
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			//세션존재 -> 해당 유저의 해당 건강일기 메모 수정
			if(ab!=null) {
				hb.setSuCode(ab.getSuCode());
				//해당 건강일기에 입력한 메모가 있음 -> 업데이트, 없음 -> 인서트
				if(this.session.selectOne("getHealthMemo",hb)!=null) {
					if(this.converToBoolean(this.session.update("updMyHealthDiary",hb))) {
						model.addAttribute("memo", this.session.selectOne("getHealthMemo",hb));
					} else {
						HealthDiaryBean h = new HealthDiaryBean();
						h.setMemo("실패");
						model.addAttribute("memo", h);
					}
				} else {
					if(this.converToBoolean(this.session.insert("insNewHDMemo",hb))) {
						model.addAttribute("memo", this.session.selectOne("getHealthMemo",hb));
					} else {
						HealthDiaryBean h = new HealthDiaryBean();
						h.setMemo("실패");
						model.addAttribute("memo", h);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//진료기록 페이지 이동(진료기록 조회)
	private void moveDoctorCommentCtl(ModelAndView mav) {
		try {
			String page = "login";
			//세션검사
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			//세션존재 -> 해당 유저의 전체 의사소견서 가져오기
			if(ab!=null) {
				ReservationBean rb = new ReservationBean();
				rb.setResSuCode(ab.getSuCode());
				List<ReservationBean> commentList = this.session.selectList("getDoctorCommentList",rb);
				
				mav.addObject("commentList", this.makeDoctorCommentHtml(commentList));
				page = "healthComment";
			}
			
			mav.setViewName(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void selectBabyCtl(Model model) {
		
	}
	
	@Transactional(rollbackFor = SQLException.class)
	private void showHealthDiaryCtl(Model model) {
		HealthDiaryBean hb = (HealthDiaryBean) model.getAttribute("healthDiaryBean");
		try {
			//세션검사
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			//세션존재 -> 조회
			if(ab!=null) {
				hb.setSuCode(ab.getSuCode());
				//특정 일기코드에 대한 세부정보 가져오기
				HealthDiaryBean detail = this.session.selectOne("getHealthDiaryDetail",hb);
				model.addAttribute("healthDetail", detail);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//아기 목록html
	private String makeBabySelect(List<BabyBean> babyList) {
		StringBuffer sb = new StringBuffer();
		
		if(babyList.size()!=0&&babyList!=null) {
			sb.append("<select class=\'mMiniSelect babyInfo\'>");
			for(BabyBean bb:babyList) {
				sb.append("<option value=\'"+bb.getBbCode()+"\'>"+bb.getBbName()+"</option>");
			}
			sb.append("</select>");
		} else {
			sb.append("<button class=\"saveBtn\" onclick=\"movePage('MoveMyPage')\">아이등록</button>");
		}

		return sb.toString();
	}
	//건강일기 리스트 html
	private String makeHealthDiaryHtml(List<HealthDiaryBean> healthList) {
		StringBuffer sb = new StringBuffer();
		
		for(HealthDiaryBean hb:healthList) {
			sb.append("<div class=\"diary "+hb.getBbCode()+"\" >");
			sb.append("<i class=\"fa-solid fa-trash-can delBtn editBtn\" onclick=\"deleteHealthDiary("+hb.getHdCode()+")\"></i>");
			sb.append("<div onclick=\"showHealthDiary("+hb.getHdCode()+")\">");
			sb.append("<div class=\"miniProfile\">");
			sb.append("<img src=\"/res/img/profile_default.png\" alt=\"images\">");
			sb.append("<div class=\"text\">");
			sb.append("<span class=\"userId\">"+hb.getBbName()+"</span>");
			sb.append("<span class=\"userId\">"+hb.getHdDate()+"</span>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("<div class=\"title\">"+hb.getHdDate()+"</div>");
			sb.append("</div>");
			sb.append("</div>");
		}
		
		return sb.toString();
	}
	//의사소견 리스트 html
	private String makeDoctorCommentHtml(List<ReservationBean> commentList) {
		StringBuffer sb = new StringBuffer();
		try {
			for(ReservationBean rb:commentList) {
				sb.append("<div class=\"diary "+rb.getResBbCode()+"\" >");
				sb.append("<div>");
				sb.append("<div class=\"miniProfile\">");
				sb.append("<img src=\"/res/img/profile_default.png\" alt=\"images\">");
				sb.append("<div class=\"text\">");
				sb.append("<span class=\"userId\">"+rb.getResBbName()+"</span>");
				sb.append("</div>");
				sb.append("<div class=\"text\" style=\"margin-left:20px;\">");
				sb.append("<p class=\"userId\">"+this.enc.aesDecode(rb.getResCoName(), rb.getResCoCode())+"("+rb.getResDoName()+")</p>");
				sb.append("</div>");
				sb.append("<div class=\"text\" style=\"margin-left:10px;\">");
				sb.append("<p class=\"userId\">진료일자:"+rb.getResDate()+"</p>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<div class=\"title\">"+rb.getDoComment()+"</div>");
				sb.append("</div>");
				sb.append("</div>");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
