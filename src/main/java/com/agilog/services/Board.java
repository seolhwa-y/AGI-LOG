package com.agilog.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BoardBean;
import com.agilog.beans.PostBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.Paging;
import com.agilog.utils.ProjectUtils;

@Service
public class Board implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	private Paging page;

	public Board() {
	}

	public void backController(ModelAndView mav, int serviceCode) {
		
		switch (serviceCode) {
		case 8:
			this.moveBoardPageCtl(mav);
			break;
		case 58 :
			this.moveShowPostCtl(mav);
			break;
		case 56:
			System.out.println("인포보드");
			this.moveInfoBoardCtl(mav);
			break;
		case 500:
			this.changeSortCtl(mav);
			break;
		case 200:
			System.out.println("넘버링");
			this.moveInfoBoardCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {

	}

	private void moveBoardPageCtl(ModelAndView mav) {
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
		mav.setViewName("totalBoard");
	}

	/*private void changeSortCtl(Model model) {
		
	}*/
	private void changeSortCtl(ModelAndView mav) {
		PostBean pb = (PostBean) mav.getModel().get("postBean");
		System.out.println(pb);
		
		if(pb.getIbSort() == "newList") {
			//최신순 가져오기
			mav.addObject("bebeBoardList", this.makeBoardList(this.session.selectList("getBebeInfo", pb)));
		}else if(pb.getIbSort().equals("oldList")){
			//오래된순 가져오기
			mav.addObject("bebeBoardList", this.makeBoardList(this.session.selectList("getBebeInfoOld", pb)));
		}else if(pb.getIbSort().equals("likeList")){
			//좋아요순 가져오기
			mav.addObject("bebeBoardList", this.makeBoardList(this.session.selectList("getBebeInfoLike", pb)));
		}else if(pb.getIbSort().equals("viewList")){
			//조회수순 가져오기
			mav.addObject("bebeBoardList", this.makeBoardList(this.session.selectList("getBebeInfoView", pb)));
		}
		
		mav.setViewName("infoBoard");
	}

	private void changeListCtl(ModelAndView mav) {

	}

	private void moveFreeBoardCtl(ModelAndView mav) {

	}

	private void moveMeetingBoardCtl(ModelAndView mav) {

	}

	private void moveInfoBoardCtl(ModelAndView mav) {
		
		mav.addObject("bebeBoardList", this.makeBoardList(this.session.selectList("getBebeInfo")));
		BoardBean bb = (BoardBean) mav.getModel().get("boardBean");
		System.out.println("찍는 메소드 : " + bb);
		mav.addObject("pageNum", this.makePageNum(this.session.selectList("getBebeInfo"), bb));

		mav.setViewName("infoBoard");
	}

	private void moveWritePageCtl(ModelAndView mav) {

	}

	private void moveShowPostCtl(ModelAndView mav) {
		
		PostBean pb = (PostBean) mav.getModel().get("postBean");
		mav.addObject("content",this.makePostView(this.session.selectList("getBebePost",pb)));
		mav.setViewName("post");
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

	private void searchPostCtl(ModelAndView mav) {

	}

	private void insertPostCommentCtl(Model model) {

	}

	private void updateBoardCommentCtl(Model model) {

	}

	private void deleteBoardCommentCtl(Model model) {

	}

	private void insertPostCtl(ModelAndView mav) {

	}

	private void moveUpdatePostPageCtl(ModelAndView mav) {

	}

	private void updatePostCtl(ModelAndView mav) {

	}

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
	private String makePageNum(List<PostBean> bebeBoardList,BoardBean bb) {
		StringBuffer sb = new StringBuffer();
		/*-----------------페이징 실험---------------------*/
		//전체글의 수
		int totalPage = bebeBoardList.size();
		System.out.println("전체글 수 : " + totalPage);
		//마지막 페이지 구하기     12 -> 2page     29 ->  3page ...
		int lastpage = (int)(Math.ceil((double)totalPage/10));
		System.out.println("마지막 페이지 : " + lastpage);
		
		
		// 1 -> 0 , 2 -> 10 , 3 ->20 , 4 ->30 
		//int index_no = Integer.parseInt(bb.getPageNum());
		//System.out.println(index_no);
		//페이징 html출력
		for(int i=1; i<=lastpage; i++) {
		sb.append("<a class=\"pagenum\" onClick=\"pageNum('" + i + "')\" href='MoveInfoBoard?pageNum="+i+"'>" + i + "</a>");
		
		}
		return sb.toString();
	}
	private boolean convertToBoolean(int booleanCheck) {
		boolean result = false;

		return result;
	}
}
