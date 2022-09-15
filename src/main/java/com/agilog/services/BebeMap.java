package com.agilog.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BebeMapCommentBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DailyDiaryCommentBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

@Service
public class BebeMap implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;
	@Autowired
	private Encryption enc;

	public BebeMap() {
	}

	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 6: this.moveMapCtl(mav); break;
		case 44 : this.reservationCtl(mav); break;
		}
	}

	public void backController(Model model, int serviceCode) {
		try {
			if(this.pu.getAttribute("accessInfo") != null) {
				switch (serviceCode) {
				case 42 : this.viewCompanyInfoCtl(model); break;
				case 45 : this.insertMapCommentCtl(model); break;
				case 97 : this.updateMapCommentCtl(model); break;
				case 85 : this.deleteMapCommentCtl(model); break;
				case 43 : this.showReservationCtl(model); break;
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void moveMapCtl(ModelAndView mav) {
		AuthBean ab;
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			if (ab != null) {
				if (ab.getSuCode().length() == 10) {
					ab.setType("kakao");
				} else {
					ab.setType("naver");
				}
				mav.addObject("accessInfo", ab);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("bebeMap");
	}

	// 지도 정보 확인 후 댓글 불러오기
	private void viewCompanyInfoCtl(Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		CompanyBean cb = (CompanyBean)model.getAttribute("companyBean");
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			String coCode = this.session.selectOne("getCoCode", cb);
			
			cb.setCoCode(coCode);
			if(cb.getCoCode() != null) {
				map.put("suCode", ab.getSuCode());
				map.put("mcComment", this.session.selectList("getMapCommentList", cb));
				model.addAttribute("mcCommentList", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 지도 댓글 등록
	@Transactional(rollbackFor = SQLException.class)
	private void insertMapCommentCtl(Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		BebeMapCommentBean bmcb = (BebeMapCommentBean)model.getAttribute("bebeMapCommentBean");
		
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			bmcb.setMcSuCode(ab.getSuCode());
			bmcb.setMcCode(this.session.selectOne("getMcCode", bmcb.getMcSuCode()));
			
			if(bmcb.getMcCode() == null) {
				bmcb.setMcCode("1");
			}
			
			if(this.convertToBoolean(this.session.insert("insMapComment", bmcb))) {
				System.out.println("지도 댓글 등록 성공");
				
				map.put("suCode", ab.getSuCode());
				map.put("mcComment", this.session.selectList("getMapCommentList", bmcb));
				model.addAttribute("insMapComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 지도 댓글 수정
	@Transactional(rollbackFor = SQLException.class)
	private void updateMapCommentCtl(Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		BebeMapCommentBean bmcb = (BebeMapCommentBean)model.getAttribute("bebeMapCommentBean");
		
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			bmcb.setMcSuCode(ab.getSuCode());
			if(this.convertToBoolean(this.session.update("updMapComment", bmcb))) {
				System.out.println("지도 댓글 수정 성공");
				
				map.put("suCode", ab.getSuCode());
				map.put("mcComment", this.session.selectList("getMapCommentList", bmcb));
				model.addAttribute("updMapComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	// 지도 댓글 삭제
	@Transactional(rollbackFor = SQLException.class)
	private void deleteMapCommentCtl(Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		BebeMapCommentBean bmcb = (BebeMapCommentBean)model.getAttribute("bebeMapCommentBean");
		
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			bmcb.setMcSuCode(ab.getSuCode());
			if(this.convertToBoolean(this.session.delete("delMapComment", bmcb))) {
				System.out.println("지도 댓글 삭제 성공");
				
				map.put("suCode", ab.getSuCode());
				map.put("mcComment", this.session.selectList("getMapCommentList", bmcb));
				model.addAttribute("delMapComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 해당 병원에 얘약 가능여부 불러오기
	private void showReservationCtl(Model model) {
		
	}

	// 지도 예약완료
	@Transactional(rollbackFor = SQLException.class)
	private void reservationCtl(ModelAndView mav) {
		mav.setViewName("bebeMap");
	}
	
	/* 공공데이터 API :: 건강보험심사평가원_병원정보서비스 */
	// 클라이언트쪽에서 동을 String으로 받아서 DB에서 like를 이용하여 시군구코드를 뽑아와서 넣어준다.
	// 결과값으로 x 축 y 축을 받아서 마커에 표시
	// 요청 변수 :: 서비스코드,  1페이지, 한 페이지 표기 45개, 시군구코드
	// 출력 변수 ::  x좌표, Y좌표
	public void ApiExplorer(String[] args) throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551182/hospInfoService1/getHospBasisList1"); /*URL*/
		urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=O1knyhqepuJAw4ayaaud9WPTKZtq0t8v8MKi6RidO7aqPOqF1o%2B8NNqgqpPV4%2BG4fqiFdK4RzH0vE%2BV5Viv1%2BQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
		urlBuilder.append("&" + URLEncoder.encode("sgguCd","UTF-8") + "=" + URLEncoder.encode("110019", "UTF-8")); /*시군구코드*/

		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		System.out.println(sb.toString());
	}
	
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0? false : true;
	}
}
