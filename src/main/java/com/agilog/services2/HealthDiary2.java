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
import com.google.gson.Gson;

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
	
	// 건강일기 통계기록 페이지 이동
	private void moveHealthStatusPageCtl(ModelAndView mav) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 건강일기 통계기록 페이지에 표현할 정보를 가져와서 이동한다.
		 */
		HealthDiaryBean hdb = new HealthDiaryBean();
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
				// 사용자의 아이 정보 DB에서 가져와서 셀렉트 박스로 만들기
				List<BabyBean> babyList = this.session.selectList("getTotalBabyCode", ab);
				map.put("babyList", this.makeSelectBaby(babyList)); 
				
				// 아이가 1명이라도 있을 때 그래프에 표현한 데이터 만들기
				if(babyList.size() != 0) {
					hdb.setSuCode(ab.getSuCode());
					hdb.setBbCode(babyList.get(0).getBbCode());
					
					// 아이 건강정보 나이별로 DB에서 가져오기
					List<HealthDiaryBean> healthList = this.session.selectList("getHealthStatusList", hdb);
					
					// DB에서 가져온 정보 분리하여 저장해서 Map 담기
					map = this.healthStatusList(healthList, map); 
					mav.addObject("babyStatusList", map);
				}
				mav.setViewName("healthStatus");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	// 아이 셀렉트 리스트 HTML
	private String makeSelectBaby(List<BabyBean> babyList) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 건강일기 통계기록 페이지에서 아이를 선택할 수 있는 셀렉트 박스를 만들어서 보낸다.
		 */
		StringBuffer sb = new StringBuffer();
		int i = -1;

		sb.append("<select id='babyName' class='select' onChange = 'cngHealthStatus()'>");
		
		// 아이가 있을 경우
		if(babyList.size()!=0 && babyList != null) {
			// 기본적으로 보여줄 아이는 첫번째로 등록한 아이
			sb.append("<option disabled selected>아이 선택</option>");
			
			// 선택 가능한 아이 리스트
			for(BabyBean bb : babyList) {
				i++;
				sb.append("<option value='"+ babyList.get(i).getBbCode() +"'>"+ babyList.get(i).getBbName() +"</option>");
			}
		} else {
			// 아이가 없을 경우
			sb.append("<option disabled selected>선택할 아이가 없습니다.</option>");
		}
		
		sb.append("</select>");

		return sb.toString();
	}
	
	
	// 다른 아이 선택시 데이터 가져오기
	private void selectBabyCtl(Model model) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 건강일기 통계기록 페이지에서 아이 셀렉트 박스에서 아이 선택시 아이의 건강일기 데이터를 가져온다.
		 */
		HashMap<String,Object> map = new HashMap<String,Object>();
		HealthDiaryBean hdb = (HealthDiaryBean)model.getAttribute("healthDiaryBean");
		
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			// 아이 건강정보 나이별로 DB에서 가져오기
			hdb.setSuCode(ab.getSuCode());
			List<HealthDiaryBean> healthList = this.session.selectList("getHealthStatusList", hdb);
			
			// DB에서 가져온 정보 분리하여 저장해서 Map 담기
			map = this.healthStatusList(healthList, map); 
			model.addAttribute("babyStatusList", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 건강일기 추세데이터 분리 저장
	private HashMap<String, Object> healthStatusList(List<HealthDiaryBean> healthList, HashMap<String, Object> map) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 건강일기 통계기록 페이지에 건강일기 데이터를 키, 몸무게, 머리둘레로 분리하여 저장한다.
		 */
		int i = -1, ii = -1;
		// 키, 몸무게, 머리둘레 소수점 때문에 double 사용
		//double he = 0, we = 0, hd = 0; 
		double[] height = new double[9], weight = new double[9], head = new double[9];
		Gson gson = new Gson();
		
		for(HealthDiaryBean hdb : healthList) {
			i++;
			
			for(ii = 0; ii < 9; ii++) {
				// 해당 나이가 있을 때 :: 같은 나이가 있을 때를 대비하여 변수에 담아서 비교
				if(healthList.get(i).getAge() == ii) {
					// 키 1차원 배열 :: 1단계 3항식
					double he = (healthList.get(i).getBbHeight() != null)? Double.parseDouble(healthList.get(i).getBbHeight()) : 0;
					height[ii] = height[ii] < he ? he : height[ii];
		
					// 몸무게 1차원 배열 :: 1단계 3항식
					double we = (healthList.get(i).getBbWeight() != null)? Double.parseDouble(healthList.get(i).getBbWeight()) : 0;
					weight[ii] = weight[ii] < we ? we : weight[ii];

					// 머리둘레 1차원 배열 :: 1단계 3항식
					double hd = (healthList.get(i).getHead() != null)? Double.parseDouble(healthList.get(i).getHead()) : 0;
					head[ii] = head[ii] < hd ? hd : head[ii];
				} 	
			}
			// 배열 -> 리스트 -> JSON -> MAP
			List<double[]> heList = Arrays.asList(height), weList = Arrays.asList(weight), hdList = Arrays.asList(head);

			map.put("heightList", gson.toJson(heList));
			map.put("weightList", gson.toJson(weList));
			map.put("headList", gson.toJson(hdList));
		}
		return map;
	}
}
