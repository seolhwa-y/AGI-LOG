package com.agilog.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.json.XML;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BabyBean;
import com.agilog.beans.BebeMapBean;
import com.agilog.beans.BebeMapCommentBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DailyDiaryCommentBean;
import com.agilog.beans.DoctorBean;
import com.agilog.beans.ReservationBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
		try {
			if(this.pu.getAttribute("accessInfo") != null) {
				switch (serviceCode) {
				case 6: this.moveMapCtl(mav); break;
//				case 44 : this.reservationCtl(mav); break;
				}
			} else {
				mav.setViewName("login");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void backController(Model model, int serviceCode) {
		switch (serviceCode) {
		case 42 : this.viewCompanyInfoCtl(model); break;
		case 45 : this.insertMapCommentCtl(model); break;
		case 97 : this.updateMapCommentCtl(model); break;
		case 85 : this.deleteMapCommentCtl(model); break;
		case 43 : this.showReservationCtl(model); break;
		case 44 : this.reservationCtl(model); break;
		case 200 : this.getResTime(model); break;
		}
	}

	private void moveMapCtl(ModelAndView mav) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		
		try {
			AuthBean ab = (AuthBean) this.pu.getAttribute("accessInfo");
			if (ab != null) {
				if (ab.getSuCode().length() == 10) {
					ab.setType("kakao");
				} else {
					ab.setType("naver");
				}
				mav.addObject("accessInfo", ab);
				
				List<BebeMapBean> bmbList = this.ApiExplorer(mav);
				String bmList = gson.toJson(bmbList);
				
				ab = this.session.selectOne("getSuAddress", ab);
				String suInfo = gson.toJson(ab);
				
				map.put("bmList", bmList);
				map.put("suInfo", suInfo);
				
				mav.setViewName("bebeMap");
				
				mav.addObject("suInfo", suInfo);
				mav.addObject("bmList", bmList);
				//mav.addObject("bmList", map);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// API 국립중앙의료원 :: 전국 병·의원 찾기 서비스 :: 달빛어린이병원 및 소아전문센터
	public List<BebeMapBean> ApiExplorer(ModelAndView mav) throws IOException {
		/* 소아전문 */
		String url = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getBabyListInfoInqire";	// EndPoint
		String key = "?serviceKey=O1knyhqepuJAw4ayaaud9WPTKZtq0t8v8MKi6RidO7aqPOqF1o%2B8NNqgqpPV4%2BG4fqiFdK4RzH0vE%2BV5Viv1%2BQ%3D%3D";		// Encoding
		String callURL = url + key + "&ORD=NAME";
		/* 병원 전체 */
//		String url = "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire";	// EndPoint
//		String key = "?serviceKey=O1knyhqepuJAw4ayaaud9WPTKZtq0t8v8MKi6RidO7aqPOqF1o%2B8NNqgqpPV4%2BG4fqiFdK4RzH0vE%2BV5Viv1%2BQ%3D%3D";		// Encoding
//		String callURL = url + key + "&QZ=B&ORD=NAME&pageNo=1&numOfRows=15";
		
		// openAPI에서 받아온 데이터를 JSON으로 파싱
		Map<String, Object> map = this.getApi(callURL);
		
		// 받아온 데이터 분리해서 저장
		List<BebeMapBean> bmList = this.jsonParser(map);

		return bmList;
	}
	
	// API DATA -> JSON
	private Map<String, Object> getApi(String callURL) throws MalformedURLException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		URL url;
		url = new URL(callURL);

		// API 호출
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		StringBuffer sb = new StringBuffer();
		String tempStr = null;

		while((tempStr = br.readLine()) != null) {
			sb.append(tempStr.trim());
		}

		br.close();

		// 받아온 xml데이터 -> json 형태로 변환
		org.json.JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
		String xmlJSONObjString = xmlJSONObj.toString();
		ObjectMapper objectMapper = new ObjectMapper();

		map = objectMapper.readValue(xmlJSONObjString, new TypeReference<Map<String, Object>>() {});

		return map;
	}
	
	// JSON DATA -> List<BebeMapBean>
	@SuppressWarnings("unchecked")
	private List<BebeMapBean> jsonParser(Map<String, Object> map) {
		Map<String, Object> response = (Map<String, Object>) map.get("response");
		Map<String, Object> body = (Map<String, Object>) response.get("body");
		Map<String, Object> items = (Map<String, Object>) body.get("items");
		//Map<String, Object> page = (Map<String, Object>) items.get("pageNo");
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");
		List<BebeMapBean> bmList = new ArrayList<BebeMapBean>();
		
		int i = -1;
		if(itemList.size() != 0) {
			for(Map<String, Object> item : itemList) {
				i++;
				BebeMapBean bmb = new BebeMapBean();
				// 상호
				bmb.setName((String)item.get("dutyName"));

				// 주소 + 상세주소
				bmb.setAddress((String) item.get("dutyAddr"));
				// 연락처
				bmb.setTell((String) item.get("dutyTel1")); 
				// 정보
				bmb.setInfo((String) item.get("dutyInf"));
				// 위도 X
				bmb.setX((Double) item.get("wgs84Lat"));
				// 경도 Y
				bmb.setY((Double) item.get("wgs84Lon"));

				bmList.add(bmb);
			}
		}else {System.out.println("요청한 지역의 정보가 없습니다.");}

		return bmList;
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
				map.put("coInfo", cb);
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
			bmcb.setMcCode(this.session.selectOne("getMcCode", bmcb.getCoCode()));
			
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
	
	// 내 아기 정보, 해당 병원에 의사정보 및 예약 가능여부 불러오기
	private void showReservationCtl(Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ReservationBean rb = (ReservationBean)model.getAttribute("reservationBean");
		DoctorBean db = new DoctorBean();
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			// 아이 정보 불러오기
			List<BabyBean> babyList = this.session.selectList("getTotalBabyCode", ab);
			map.put("babyList", babyList); 
			
			// 의사 정보 불러오기
			db.setCoCode(rb.getResCoCode());
			List<DoctorBean> doctorList = this.session.selectList("getDoctorInfo", db);
			map.put("doctorList", doctorList); 
			
//			// 해당 병원 
//			List<ReservationBean> resList = this.session.selectList("getResList", rb);
//			map.put("resList", resList); 
			
			model.addAttribute("resInfo", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		model.addAttribute("resList", rb);
	}

	private void getResTime(Model model) {
		ReservationBean rb = (ReservationBean)model.getAttribute("reservationBean");
		
		// 해당 병원 
		List<ReservationBean> resList = this.session.selectList("getReservationList", rb);
		
		model.addAttribute("resTime", resList);
	}
	
	// 지도 예약완료
	@Transactional(rollbackFor = SQLException.class)
	//private void reservationCtl(ModelAndView mav) {
	private void reservationCtl(Model model) {
		System.out.println("어서와");
		ReservationBean rb = (ReservationBean)model.getAttribute("reservationBean");
		
		rb.setResCode(this.session.selectOne("getResCode", rb));
		rb.setResCount(this.session.selectOne("getResCountPlus", rb));
//		rb.setResCount(rb.getResCount()+1);
		
		if(this.convertToBoolean(this.session.insert("insReservationList", rb))) {
			System.out.println("지도 예약 성공");
			if(this.convertToBoolean(this.session.update("updResTime", rb))) {
				System.out.println("지도 예약 인원 추가 성공");
			}
		}
	}
	

	
	
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0? false : true;
	}
}
