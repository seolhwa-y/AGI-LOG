package com.agilog.services3;

import java.sql.SQLException;
import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;

import com.agilog.beans.DailyDiaryCommentBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.ProjectUtils;

@Service
public class DailyDiary3 implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;

	public DailyDiary3() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		try {
			if (this.pu.getAttribute("accessInfo") != null) {
				switch (serviceCode) {
				}
			} else {
				mav.setViewName("login");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void backController(Model model, int serviceCode) {
		try {
			if(this.pu.getAttribute("accessInfo") != null) {
				switch (serviceCode) {
				case 31 : this.showDailyDiaryCtl(model); break;
				case 36 : this.insertDailyDiaryCommentCtl(model); break;
				case 63 : this.updateDailyDiaryCommentCtl(model); break;
				case 64 : this.deleteDailyDiaryCommentCtl(model); break;
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void moveDailyDiaryPageCtl(ModelAndView mav) {
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
		mav.setViewName("dailyDiary");
	}
	
	// 감성일기 디테일 보기
	private void showDailyDiaryCtl(Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		DailyDiaryCommentBean ddcb = (DailyDiaryCommentBean)model.getAttribute("dailyDiaryCommentBean");

		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			if(ab != null) {
				map.put("suCode", ab.getSuCode());
			} 
			ddcb.setDcDdCode("9");
			ddcb.setDcDdSuCode("2409449450");
			ddcb.setDcDdDate("20220905000000");
		
			map.put("ddComment", this.session.selectList("getDailyDiaryComment", ddcb));
			model.addAttribute("ddCommentList", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 감성일기 댓글 등록
	@Transactional(rollbackFor = SQLException.class)
	private void insertDailyDiaryCommentCtl(Model model) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 사용자가 특정 감성일기에서 댓글 내용을 입력 후 등록 버튼을 눌렀을 때 게시글에 댓글을 남긴다.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		DailyDiaryCommentBean ddcb = (DailyDiaryCommentBean)model.getAttribute("dailyDiaryCommentBean");

		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");

			// 현재 로그인한 유저와 댓글 작성자 확인을 위하여 세션에서 유저코드 가져와서 저장
			ddcb.setDcSuCode(ab.getSuCode());
			// 댓글 코드 Max + 1
			ddcb.setDcCode(this.session.selectOne("getDcCode", ddcb.getDcDdCode()));

			// DB에 댓글 INSERT
			if(this.convertToBoolean(this.session.insert("insDailyDiaryComment", ddcb))) {
				map.put("suCode", ab.getSuCode());
				// 해당 감성일기에 댓글 내용을 다시 읽어온다.
				map.put("ddComment", this.session.selectList("getDailyDiaryComment", ddcb));
				model.addAttribute("insDdComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 감성일기 댓글 수정
	@Transactional(rollbackFor = SQLException.class)
	private void updateDailyDiaryCommentCtl(Model model) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 사용자가 특정 감성일기에서 댓글 내용을 수정 후 수정 버튼을 눌렀을 때 게시글에 댓글을 수정한다.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		DailyDiaryCommentBean ddcb = (DailyDiaryCommentBean)model.getAttribute("dailyDiaryCommentBean");
		
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			ddcb.setDcSuCode(ab.getSuCode());
			
			// DB에 댓글 UPDATE
			if(this.convertToBoolean(this.session.update("updDailyDiaryComment", ddcb))) {
				map.put("suCode", ab.getSuCode());
				// 해당 감성일기에 댓글 내용을 다시 읽어온다.
				map.put("ddComment", this.session.selectList("getDailyDiaryComment", ddcb));
				model.addAttribute("updDdComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	// 감성일기 댓글 삭제
	@Transactional(rollbackFor = SQLException.class)
	private void deleteDailyDiaryCommentCtl(Model model) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 사용자가 특정 감성일기에서 댓글을 삭제 버튼을 눌렀을 때 게시글에 댓글을 삭제한다.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		DailyDiaryCommentBean ddcb = (DailyDiaryCommentBean)model.getAttribute("dailyDiaryCommentBean");
		
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			ddcb.setDcSuCode(ab.getSuCode());

			// DB에 댓글 UPDATE
			if(this.convertToBoolean(this.session.delete("delDailyDiaryComment", ddcb))) {
				map.put("suCode", ab.getSuCode());
				// 해당 감성일기에 댓글 내용을 다시 읽어온다.
				map.put("ddComment", this.session.selectList("getDailyDiaryComment", ddcb));
				model.addAttribute("delDdComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;
	}
}
