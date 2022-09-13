package com.agilog.services3;

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
public class Board3 {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	private Paging page;
	
	public Board3() {}
	
	public void backController(ModelAndView mav, int serviceCode) {
		/*
		 * try {
			if(this.pu.getAttribute("accessInfo")!=null) {
		 */
		switch(serviceCode) {
		case 56:
			this.moveInfoBoardCtl(mav);
			break;
		case 58:
			this.moveShowPostCtl(mav);
			break;
		case 500:
			this.moveSortPostCtl(mav);
			break;
		default:
			break;
		}	
	}

	private void moveSortPostCtl(ModelAndView mav) {
		
		
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
		PostBean pb = (PostBean) mav.getModel().get("postBean");
		mav.addObject("bebeBoardList", this.makeBoardList(this.session.selectList("getBebeInfo", pb)));
		mav.setViewName("infoBoard");
	}
	
	private void moveWritePageCtl(ModelAndView mav) {

	}
	
	private void moveShowPostCtl(ModelAndView mav) {
		PostBean pb = (PostBean) mav.getModel().get("postBean");

		mav.addObject("content",this.makePostView(this.session.selectList("getBebePost",pb)));
		mav.setViewName("post");
	}
	
	private void searchPostCtl(ModelAndView mav) {
		
	}
	
	private void insertPostCommentCtl(Model model) {
		
	}
	
	private void updateBoardCommentCtl(Model model) {
		
	}
	
	private void deleteBoardCommentCtl(Model model) {
		
	}
	
	/*private void insertPostCtl(ModelAndView mav) {
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
	}*/
	
	private void moveUpdatePostPageCtl(ModelAndView mav) {
		
	}
	
	private void updatePostCtl(ModelAndView mav) {
		
	}
	//정보게시판 게시글 EL 작업
	private String makePostView(List<PostBean> bebePost) {
		StringBuffer sb = new StringBuffer();
		//if(bebePost.size() !=0) {
		for(int idx=0; idx<1; idx++) {
				PostBean pb = (PostBean)bebePost.get(idx);
			sb.append("<div class=\"pTitle\">" + pb.getIbTitle() + "</div>");
			sb.append("<div class=\"pHead\">");
			sb.append("<div class=\"pDate\">작성일&ensp;<small class=\"sDate\">" + pb.getIbDate() +"</small></div>");
			sb.append("<div class=\"pView\">조회수&ensp;<small class=\"sView\">" + pb.getIbView() + "</small></div>");
			sb.append("<div class=\"pLike\">좋아요&ensp;<small class=\"sLike\">" + pb.getIbLike() + "</small></div>");
			sb.append("</div>");
			sb.append("<div class=\"pBody\">");
			sb.append("<div class=\"pContent\"> " + pb.getIbContent() + " </div>");	
			sb.append("</div>");
			sb.append("<button class=\"likeBtn\" onClick=\"likeBtn()\">좋아요</button>");
			sb.append("<button class=\"backList\" onClick=\"movePage('MoveInfoBoard')\">목록</button>");
			}
		sb.append("</div");
		//}
		return sb.toString();
	}
	//정보게시판 목록 EL 작업
	private String makeBoardList(List<PostBean> bebeBoardList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<select id=\"infoBoardSelect\" onChange=\"changeSort()\">");
			sb.append("<option value = \"newList\">최신순</option>");
			sb.append("<option value = \"oldList\">오래된순</option>");
			sb.append("<option value = \"likeList\">좋아요순</option>");
			sb.append("<option value = \"viewList\">조회수순</option>");
		sb.append("</select>");
		
		sb.append("<table class=\"infoTable\">");
			sb.append("<tr>");
			sb.append("<th class=\"infoBoardM no\">No.</th>");
			sb.append("<th class=\"infoBoardM title\">제목</th>");
			sb.append("<th class=\"infoBoardM date\">작성일</th>");
			sb.append("<th class=\"infoBoardM like\">좋아요</th>");
			sb.append("<th class=\"infoBoardM view\">조회수</th>");
			sb.append("</tr>");
			for(int idx=0; idx<bebeBoardList.size(); idx++) {
				PostBean pb = (PostBean)bebeBoardList.get(idx);
				int max = bebeBoardList.size();
				sb.append("<tr class=\"selectBoard\" onClick=\"boardContent("+ pb.getIbCode() +")\">");
				sb.append("<td class=\"infoBoardB\">"+ (max-idx) +"</td>");
				sb.append("<td class=\"infoBoardTitle\">"+ pb.getIbTitle() +"</td>");
				sb.append("<td class=\"infoBoardB\">"+ pb.getIbDate() +"</td>");
				sb.append("<td class=\"infoBoardB\">"+ pb.getIbLike() +"</td>");
				sb.append("<td class=\"infoBoardB\">"+ pb.getIbView() +"</td>");
				sb.append("</tr>");
			//this.page.makePageGroup();	
			}
		sb.append("</table>");
		
		return sb.toString();
	}
	
	private boolean convertToBoolean(int booleanCheck) {
		boolean result=false;
		
		return result;
	}
}
