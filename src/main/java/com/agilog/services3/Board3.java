package com.agilog.services3;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BoardBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.PostCommentBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.Paging;
import com.agilog.utils.ProjectUtils;

@Service
public class Board3 implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	private Paging page;

	public Board3() {}

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
		switch (serviceCode) {
		case 60 : this.insertPostCommentCtl(model); break;
		case 61 : this.updatePostCommentCtl(model); break;
		case 62 : this.deletePostCommentCtl(model); break;
		}
	}

	// 특정 게시글에 댓글 등록
	@Transactional(rollbackFor = SQLException.class)
	private void insertPostCommentCtl(Model model) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 사용자가 감성일기 피드를 눌렀을 때, 게시글 내용, 댓글, 좋아요 불러온다.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		PostCommentBean pcb = (PostCommentBean)model.getAttribute("postCommentBean");
		System.out.println("날짜 확인 : " + pcb.getFcDate());
		System.out.println("날짜 확인 : " + pcb.getFcFbDate());
		
		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			pcb.setFcSuCode(ab.getSuCode());
			pcb.setFcCode((String)this.session.selectOne("getFcCode", pcb.getFcFbCode()));
			if(this.convertToBoolean(this.session.insert("insPostComment", pcb))) {
				System.out.println("자유게시판 댓글 등록 성공");
				
				map.put("suCode", ab.getSuCode());
				map.put("fbComment", this.session.selectList("getPostCommentList", pcb));
				model.addAttribute("insfbComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 특정 게시글에 댓글 수정
	@Transactional(rollbackFor = SQLException.class)
	private void updatePostCommentCtl(Model model) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 사용자가 감성일기 피드를 눌렀을 때, 게시글 내용, 댓글, 좋아요 불러온다.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		PostCommentBean pcb = (PostCommentBean)model.getAttribute("postCommentBean");

		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			pcb.setFcSuCode(ab.getSuCode());

			if(this.convertToBoolean(this.session.update("updPostComment", pcb))) {
				System.out.println("자유게시판 댓글 수정 성공");
				
				map.put("suCode", ab.getSuCode());
				map.put("fbComment", this.session.selectList("getPostCommentList", pcb));
				model.addAttribute("updfbComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 특정 게시글에 댓글 삭제
	@Transactional(rollbackFor = SQLException.class)
	private void deletePostCommentCtl(Model model) {
		/* 
		 * 개발자 : 염설화
		 * 세부기능 : 사용자가 감성일기 피드를 눌렀을 때, 게시글 내용, 댓글, 좋아요 불러온다.
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		PostCommentBean pcb = (PostCommentBean)model.getAttribute("postCommentBean");

		try {
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			
			pcb.setFcSuCode(ab.getSuCode());
			
			if(this.convertToBoolean(this.session.delete("delPostComment", pcb))) {
				System.out.println("자유게시판 댓글 삭제 성공");

				map.put("suCode", ab.getSuCode());
				map.put("fbComment", this.session.selectList("getPostCommentList", pcb));
				model.addAttribute("delfbComment", map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;
	}

}
