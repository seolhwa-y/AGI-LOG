package com.agilog.services2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BabyBean;
import com.agilog.beans.HealthDiaryBean;
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
		try {
			if (this.pu.getAttribute("accessInfo") != null) {
				switch(serviceCode) {
				case 95 : this. selectBabyCtl(model); break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 아이 건강 추세 페이지 이동
	private void moveHealthStatusPageCtl(ModelAndView mav) {
		HealthDiaryBean hdb = new HealthDiaryBean();
		HashMap<String,Object> map = new HashMap<String,Object>();

		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
				// 아이 정보 리스트 가져오기
				List<BabyBean> babyList = this.session.selectList("getTotalBabyCode", ab);
				map.put("babyList",this.makeSelectBaby(babyList)); 
				
				hdb.setSuCode(ab.getSuCode());
				hdb.setBbCode(babyList.get(0).getBbCode());
				
				// 아이 건강정보 나이별로 가져오기
				List<HealthDiaryBean> healthList = this.session.selectList("getHealthStatusList", hdb);
				map = this.healthStatusList(healthList, map); 
				
				mav.addObject("babyStatusList", map);
				mav.setViewName("healthStatus");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	// 추세 페이지 다른 아이 선택시 데이터 가져오기
	private void selectBabyCtl(Model model) {
		AuthBean ab;
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		HealthDiaryBean hdb = (HealthDiaryBean)model.getAttribute("healthDiaryBean");
		
		try {
			ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			hdb.setSuCode(ab.getSuCode());
			
			// 아이 건강정보 나이별로 가져오기
			List<HealthDiaryBean> healthList = this.session.selectList("getHealthStatusList", hdb);
			map = this.healthStatusList(healthList, map); 
			
			model.addAttribute("babyStatusList", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 아이 셀렉트 리스트 HTML
	private String makeSelectBaby(List<BabyBean> babyList) {
		StringBuffer sb = new StringBuffer();
		int i = -1;

		sb.append("<select id='babyName' class='select' onChange = 'cngHealthStatus()'>");
		
		if(babyList.size()!=0 && babyList != null) {
			// 기본 고정은 아이를 첫번째 등록한 아이
			sb.append("<option value = '" + babyList.get(0).getBbCode() + "' disabled selected> " + babyList.get(0).getBbName() + "</option>");
			
			// 선택 가능한 아이 리스트
			for(BabyBean bb : babyList) {
				i++;
				sb.append("<option value='"+ babyList.get(i).getBbCode() +"'>"+ babyList.get(i).getBbName() +"</option>");
			}
		} else {
			sb.append("<option disabled selected>선택할 아이가 없습니다.</option>");
		}
		
		sb.append("</select>");

		
		return sb.toString();
	}
	
	// 건강일기 추세데이터 분리 저장
	private HashMap<String, Object> healthStatusList(List<HealthDiaryBean> healthList, HashMap<String, Object> map) {
		int i = -1, ii = 0, he = 0, we = 0, hd = 0;
		int[] height = new int[8], weight = new int[8], head = new int[8];
		String sStatus = "";
		
		for(HealthDiaryBean hdb : healthList) {
			i++;
			
			for(ii = 1; ii < 8; ii++) {
				// 해당 나이가 있을 때 :: 같은 나이가 있을 때를 대비하여 변수에 담아서 비교
				if(healthList.get(i).getAge() == ii) {
					// 키 1차원 배열 :: 1단계 3항식
					he = (healthList.get(i).getBbHeight() != null)? Integer.parseInt(healthList.get(i).getBbHeight()) : 0;
					height[ii] = height[ii] < he ? he : height[ii];
			
					// 몸무게 1차원 배열 :: 1단계 3항식
					we = (healthList.get(i).getBbWeight() != null)? Integer.parseInt(healthList.get(i).getBbWeight()) : 0;
					weight[ii] = weight[ii] < we ? we : weight[ii];

					// 머리둘레 1차원 배열 :: 1단계 3항식
					hd = (healthList.get(i).getHead() != null)? Integer.parseInt(healthList.get(i).getHead()) : 0;
					head[ii] = head[ii] < hd ? hd : head[ii];
				} 
				// 배열 -> 문자 -> MAP
				sStatus = Arrays.toString(height);
				map.put("heightList", sStatus);

				sStatus = Arrays.toString(weight);
				map.put("weightList", sStatus);

				sStatus = Arrays.toString(head);
				map.put("headList", sStatus);
			}
		}
		return map;
	}
	
	
	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
