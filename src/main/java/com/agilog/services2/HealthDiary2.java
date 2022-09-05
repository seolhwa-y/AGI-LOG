package com.agilog.services2;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BabyBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class HealthDiary2 implements ServiceRule {
	
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	
	public HealthDiary2() {}
	
	
	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		try {
			if (this.pu.getAttribute("accessInfo") != null) {
				switch(serviceCode) {
				case 21 : this.moveHealthStatusPageCtl(mav); break;
				}
			}else {
				mav.setViewName("login");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void backController(Model model, int serviceCode) {
		
	}
	
	
	private void deleteHealthDiaryCtl(ModelAndView mav) {
		
	}
	private void moveHealthDiaryPageCtl(ModelAndView mav) {
		
	}
	
	// 아이 건강 추세 페이지 이동
	private void moveHealthStatusPageCtl(ModelAndView mav) {
		AuthBean ab;
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		try {
			ab = (AuthBean)this.pu.getAttribute("accessInfo");
				// 아이 정보 리스트 가져오기
				List<BabyBean> babyList = this.session.selectList("getTotalBabyCode", ab);
				map.put("babyList",this.makeSelectBaby(babyList)); 
				mav.addObject("babyStatusList", map);
				mav.setViewName("healthStatus");
		
			// getTotalBabyCode    //getBabyHealthInfo
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// 아이 셀렉트 리스트 HTML
	private String makeSelectBaby(List<BabyBean> babyList) {
		StringBuffer sb = new StringBuffer();
		int i = -1;

		sb.append("<select id='babyName' class='select' onChange = 'cngHealthStatus()'>");
		
		if(babyList.size()!=0&&babyList != null) {
			sb.append("<option value = '" + babyList.get(0).getBbCode() + "' disabled selected> " + babyList.get(0).getBbName() + "</option>");
		
			for(BabyBean bb : babyList) {
				++i;
				sb.append("<option value='"+ babyList.get(i).getBbCode() +"'>"+ babyList.get(i).getBbName() +"</option>");
			}
		} else {
			sb.append("<option disabled selected>선택할 아이가 없습니다.</option>");
		}
		
		sb.append("</select>");

		
		return sb.toString();
	}


	private void insertHealthDiaryCtl(ModelAndView mav) {
		
	}
	private void updateMyHealthDiaryCtl(ModelAndView mav) {
		
	}
	private void moveDoctorCommentCtl(ModelAndView mav) {
		
	}
	private void selectBabyCtl(Model model) {
		
	}
	private void showHealthDiaryCtl(Model model) {
		
	}
	
	
	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
