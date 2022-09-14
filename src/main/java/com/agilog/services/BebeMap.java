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
	
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0? false : true;
	}
}
