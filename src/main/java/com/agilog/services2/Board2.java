package com.agilog.services2;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BoardBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.MultiUploadVO;
import com.agilog.beans.PostBean;
import com.agilog.beans.ReservationBean;
import com.agilog.beans.UploadVO;
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

		case 8:
			this.moveBoardPageCtl(mav);
			break;
		case 57:
			this.moveWritePageCtl(mav);
			break;
		case 581:
			this.moveShowFbPostCtl(mav);
			break;
		case 65:
			this.insertPostCtl(mav);
			break;
		default:
			break;
		}	
	}
	
	public void backController(Model model, MultipartHttpServletRequest files, int serviceCode) {
		switch(serviceCode) {

		case 1:
			this.upload2(model, files);
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
		
		mav.addObject("freeBoardList", this.makeBoardList(this.session.selectList("getFbPostList")));
		mav.setViewName("freeBoard");
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
	
	private void moveShowFbPostCtl(ModelAndView mav) {
		PostBean pb = (PostBean) mav.getModel().get("postBean");
		
		mav.addObject("content",this.makePostView(this.session.selectOne("getFbPostContent", pb)));
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
	

	public void upload2(Model model, MultipartHttpServletRequest files) {
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			//서버에서 저장 할 경로
			String uploadFolder = "C:\\Users\\user\\Documents\\agi-log\\src\\main\\webapp\\resources\\img\\" + ab.getSuCode() +"\\board";

			List<MultipartFile> list = files.getFiles("files");
			for(int i = 0; i<list.size(); i++) {
				String fileRealName = list.get(i).getOriginalFilename();
				long size = list.get(i).getSize();
				
				System.out.println("파일명 :" + fileRealName);
				System.out.println("사이즈" + size);
				
				File saveFile = new File(uploadFolder + "\\" + fileRealName);
				try {
					list.get(i).transferTo(saveFile);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			//mav.addObject("message", "fileupload/upload_ok");
		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void insertPostCtl(ModelAndView mav) {
		System.out.println("인서트 진입 체크1");
		
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				//포스트빈 세팅
				PostBean pb = (PostBean) mav.getModel().get("postBean");

				MultipartFile[] files = ((MultipartFile[])mav.getModel().get("files"));

				System.out.println("파일 왔는지 체크 : " + files);
				
				System.out.println("코드 체크 : " + ab.getSuCode());
				pb.setFbSuCode(ab.getSuCode());

				if (this.session.selectOne("getFbCode") == null) {
					pb.setFbCode("1");
				} else {
					System.out.println("코드 체크1 : " + (this.session.selectOne("getFbCode")));
					pb.setFbCode(Integer.toString((int)this.session.selectOne("getFbCode")+1));
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

		mav.addObject("freeBoardList", this.makeBoardList(this.session.selectList("getFbPostList")));
		mav.setViewName("freeBoard");
	}
	
	private void moveUpdatePostPageCtl(ModelAndView mav) {
		
	}
	
	private void updatePostCtl(ModelAndView mav) {
		
	}
	
	//정보게시판 게시글 EL 작업
	private String makePostView(PostBean pb) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"pTitle\">" + pb.getFbTitle() + "</div>");
		sb.append("<div class=\"pHead\">");
		sb.append("<div class=\"pDate\">작성일&ensp;<small class=\"sDate\">" + pb.getFbDate() +"</small></div>");
		sb.append("<div class=\"pView\">조회수&ensp;<small class=\"sView\">" + pb.getFbView() + "</small></div>");
		sb.append("<div class=\"pLike\">좋아요&ensp;<small class=\"sLike\">" + pb.getFbLike() + "</small></div>");
		sb.append("</div>");
		sb.append("<div class=\"pBody\">");
		sb.append("<div class=\"pContent\"> " + pb.getFbContent() + " </div>");	
		sb.append("</div>");
		sb.append("<button class=\"likeBtn\" onClick=\"likeBtn()\">좋아요</button>");
		sb.append("<button class=\"backList\" onClick=\"movePage('MoveFreeBoardPage')\">목록</button>");
		sb.append("</div");
		
		return sb.toString();
	}
	//정보게시판 목록 EL 작업
	private String makeBoardList(List<PostBean> fbBoardList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<select id=\"fbBoardSelect\" onChange=\"changeSort()\">");
			sb.append("<option value = \"newList\">최신순</option>");
			sb.append("<option value = \"oldList\">오래된순</option>");
			sb.append("<option value = \"likeList\">좋아요순</option>");
			sb.append("<option value = \"viewList\">조회수순</option>");
		sb.append("</select>");
		
		sb.append("<table class=\"fbTable\">");
			sb.append("<tr>");
			sb.append("<th class=\"fbBoardM no\">No.</th>");
			sb.append("<th class=\"fbBoardM title\">제목</th>");
			sb.append("<th class=\"fbBoardM writer\">작성자</th>");
			sb.append("<th class=\"fbBoardM date\">작성일</th>");
			sb.append("<th class=\"fbBoardM like\">좋아요</th>");
			sb.append("<th class=\"fbBoardM view\">조회수</th>");
			sb.append("</tr>");
			for(int idx=0; idx<fbBoardList.size(); idx++) {
				PostBean pb = (PostBean)fbBoardList.get(idx);
				int max = fbBoardList.size();
				sb.append("<tr class=\"selectBoard\" onClick=\"boardContent("+ pb.getFbCode() +")\">");
				sb.append("<td class=\"fbBoardB\">"+ (max-idx) +"</td>");
				sb.append("<td class=\"fbBoardTitle\">"+ pb.getFbTitle() +"</td>");
				try {
					sb.append("<td class=\"fbBoardWriter\">"+ this.enc.aesDecode(pb.getFbSuName(), pb.getFbSuCode()) +"</td>");
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
						| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
						| BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sb.append("<td class=\"fbBoardB\">"+ pb.getFbDate() +"</td>");
				sb.append("<td class=\"fbBoardB\">"+ pb.getFbLike() +"</td>");
				sb.append("<td class=\"fbBoardB\">"+ pb.getFbView() +"</td>");
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