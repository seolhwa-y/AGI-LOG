package com.agilog.services2;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BoardBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.ReservationBean;
import com.agilog.utils.Encryption;
import com.agilog.utils.Paging;
import com.agilog.utils.ProjectUtils;
@Service
public class Board2 {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	private Paging page;
	
	public Board2() {}
	
	public void backController(ModelAndView mav, int serviceCode) {
		/*
		 * try {
			if(this.pu.getAttribute("accessInfo")!=null) {
		 */
		switch(serviceCode) {

		case 57:
			this.moveWritePageCtl(mav);
			break;
		case 65:
			this.insertPostCtl(mav);
			break;
		default:
			break;
		}	
	}

	public void backController(Model model, int serviceCode) {
		
	}
	
	private void moveBoardPageCtl(ModelAndView mav) {
		
	}
	
	private void changeSortCtl(Model model) {
		
	}
	
	private void changeListCtl(ModelAndView mav) {
		
	}
	
	private void moveFreeBoardCtl(ModelAndView mav) {
		
	}
	
	private void moveMeetingBoardCtl(ModelAndView mav) {
		
	}
	
	private void moveInfoBoardCtl(ModelAndView mav) {
		
	}
	
	private void moveWritePageCtl(ModelAndView mav) {
		System.out.println("라이트 진입 체크1");

		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				mav.setViewName("postWrite");
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void moveShowPostCtl(ModelAndView mav) {
		
	}
	
	private void searchPostCtl(ModelAndView mav) {
		
	}
	
	private void insertPostCommentCtl(Model model) {
		
	}
	
	private void updateBoardCommentCtl(Model model) {
		
	}
	
	private void deleteBoardCommentCtl(Model model) {
		
	}
	
	private void insertPostCtl(ModelAndView mav) {
		System.out.println("인서트 진입 체크1");
		
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				//포스트빈 세팅
				PostBean pb = (PostBean) mav.getModel().get("postBean");
				
				pb.setFbSuCode(ab.getSuCode());

				if (this.session.selectOne("getFbCode") == null) {
					pb.setFbCode("1");
				} else {
					pb.setFbCode(Integer.toString(Integer.parseInt(this.session.selectOne("getFbCode"))+1));
				}
				System.out.println("코드 체크2 : " + pb.getFbCode());
				System.out.println("유저 코드 체크 : " + pb.getFbSuCode());
				System.out.println("타이틀 체크 : " + pb.getFbTitle());
				System.out.println("컨텐츠 체크 : " + pb.getFbContent());
				
				if(this.convertToBoolean(this.session.insert("insFbPost", pb))) {
					mav.setViewName("freeBoard");
				}
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//포스트빈 세팅
		PostBean pb = (PostBean) mav.getModel().get("postBean");
		
		System.out.println("타이틀 체크 : " + pb.getFbTitle());
		System.out.println("컨텐츠 체크 : " + pb.getFbContent());

		mav.setViewName("freeBoard");
	}
	
	private void moveUpdatePostPageCtl(ModelAndView mav) {
		
	}
	
	private void updatePostCtl(ModelAndView mav) {
		
	}
	
	private String makeBoardList(List<BoardBean> boardList) {
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
	
	private boolean convertToBoolean(int booleanCheck) {
		boolean result=false;
		
		return result;
	}
}
