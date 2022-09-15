package com.agilog.services;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
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
	
	private int maxNum; 			// 전체 글의 숫자
	private int pageNum; 		// 현재 페이지 번호
	private int listCount; 			// 페이지당 나타낼 글의 갯수
	private int pageCount; 		// 페이지그룹당 페이지 갯수
	private String pageName; 	// 게시판의 종류
	private String ibSort;
	
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
			this.moveInfoBoardCtl(mav);
			break;
		case 200:
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
	
	private void changeListCtl(ModelAndView mav) {

	}

	private void moveFreeBoardCtl(ModelAndView mav) {

	}

	private void moveMeetingBoardCtl(ModelAndView mav) {

	}

	private void moveInfoBoardCtl(ModelAndView mav) {
		PostBean pb = (PostBean) mav.getModel().get("postBean");
		int pageNum = 0;	// 현재 페이지 번호
		int listCount = 5;	// 페이지당 나타낼 글의 갯수
		int pageCount = 3;	// 페이지그룹당 페이지 갯수
		int maxNum =0; // 전체 글의 숫자	
		System.out.println("처음 : " + pageNum);
		/*페이징 제작 */
		maxNum = this.session.selectOne("InfoCount");
		if(pb.getPageNum() != 0) {
			pageNum =  pb.getPageNum();
		}else {
			pb = new PostBean();
			pageNum=1;
			pb.setPageNum(pageNum);
		}

		// 필드로 저장
		this.maxNum = maxNum;
		this.pageNum = pageNum;
		this.listCount = listCount;
		this.pageCount = pageCount;
		System.out.println("현재 : " + pageNum);
		
		if((pb.getIbSort() == "newList") || (pb.getIbSort() == null)) {
			//최신순 가져오기
			pb.setIbSort("newList");
			mav.addObject("bebeBoardList", this.makeBoardList(this.session.selectList("getBebeInfo", pb)));
			System.out.println("지나감");
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
		
		
		
		//페이징 el제작
		mav.addObject("pagingList",	this.makePageGroup());
		
		
		mav.setViewName("infoBoard");
	}
	@SuppressWarnings("unused")
	public String makePageGroup() {
		// 전체 페이지 갯수
		int totalPage = (maxNum % listCount > 0) ? maxNum / listCount + 1 : maxNum / listCount;
		// 전체 페이지 그룹 갯수
		int totalGroup = (totalPage % pageCount > 0) ? totalPage / pageCount + 1 : totalPage / pageCount;
		// 현재 페이지가 속해 있는 그룹 번호
		int currentGroup = (pageNum % pageCount > 0) ? pageNum / pageCount + 1 : pageNum / pageCount;
		return makeHtml(currentGroup, totalPage, ibSort);
	}
		
	private String makeHtml(int currentGroup, int totalPage, String ibSort) {
		StringBuffer sb = new StringBuffer();
		int start = (currentGroup * pageCount) - (pageCount - 1);
		int end = (currentGroup * pageCount >= totalPage) ? totalPage : currentGroup * pageCount;
		System.out.println("전체페이지 : " + totalPage);
		System.out.println("전체 그룹 : " + currentGroup);
		System.out.println("현재 페이지 : " + pageNum);
		System.out.println("시작 페이지 : " + start);
		System.out.println("끝 페이지 : " + end);
		if (start != 1) {
			sb.append("<a href='MoveInfoBoard?ibSort=" + ibSort + "&pageNum=" + (start - 1) + "'>");
			sb.append("[이전]");
			sb.append("</a>");
		}

		for (int i = start; i <= end; i++) {
			if (pageNum != i) {
				sb.append("<a class=\"pagenum\" href='MoveInfoBoard?ibSort=" + ibSort + "&pageNum="+i+"'>" + i + "</a>");
			}else {
				sb.append("<font class=\"pagenum\"font style='color:red;'>"+ i + "</font>");
			}
		}
		if (end != totalPage) {
			sb.append("<a class=\"pagenum\" href='MoveInfoBoard?ibSort=\" + ibSort + \"&pageNum=\" + (end + 1) + \"'>");
			sb.append("[다음]");
			sb.append("</a>");
		}
		return sb.toString();
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
		System.out.println("사이즈 : " + bebeBoardList.size());
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
		boolean result = false;

		return result;
	}
}
