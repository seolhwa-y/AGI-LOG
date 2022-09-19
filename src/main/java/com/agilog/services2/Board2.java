package com.agilog.services2;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

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
import com.agilog.beans.PostPhotoBean;
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
		case 96:
			this.deleteFBPostCtl(mav);
			break;
		case 101:
			this.moveUpdateFBPostCtl(mav);
			break;
		case 102:
			this.updatePostCtl(mav);
			break;
		default:
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
	
	private void deleteFBPostCtl(ModelAndView mav) {
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				PostBean pb = (PostBean) mav.getModel().get("postBean");
				PostPhotoBean ppb = new PostPhotoBean();
				ppb.setFpFbCode(pb.getFbCode());
				
				System.out.println(ppb.getFpFbCode());
				
				List<PostPhotoBean> ppbl = this.session.selectList("getPhotoList", ppb);
				
				if (ppbl != null) {
					for (int idx = 0; idx < ppbl.size(); idx++) {
						File file = new File(ppbl.get(idx).getFpLink());
				        
				    	if( file.exists() ){
				    		if(file.delete()){
				    			System.out.println("파일삭제 성공");
				    		}else{
				    			System.out.println("파일삭제 실패");
				    		}
				    	}else{
				    		System.out.println("파일이 존재하지 않습니다.");
				    	}
					}
				}
				
				//포스트 DB 삭제
				this.session.delete("delFbPost", pb);
				//포스트 이미지 DB 삭제
				this.session.delete("delFbPostPhoto", ppb);
				
				mav.addObject("freeBoardList", this.makeBoardList(this.session.selectList("getFbPostList")));
				mav.setViewName("freeBoard");
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void moveUpdateFBPostCtl(ModelAndView mav) {
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				PostBean pb = (PostBean) mav.getModel().get("postBean");
				
				pb = this.session.selectOne("getTnC", pb);

				System.out.println("title : " + pb.getFbTitle());
				System.out.println("title : " + pb.getFbContent());
				
				mav.addObject("postBean", pb);
				mav.setViewName("postWrite");
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	
	private void insertPostCtl(ModelAndView mav) {
		System.out.println("인서트 진입 체크1");
		
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				//게시글 이미지 삽입 성공 여부
				boolean flag = true;
				//포스트빈 세팅
				PostBean pb = (PostBean) mav.getModel().get("postBean");
				
				//작성자 코드 설정
				pb.setFbSuCode(ab.getSuCode());

				//게시글 코드 설정. 게시글이 없으면 1로 설정하고 있으면 마지막 게시글 코드+1값으로 설정함
				if (this.session.selectOne("getFbCode") == null) {
					pb.setFbCode("1");
				} else {
					pb.setFbCode(Integer.toString((int)this.session.selectOne("getFbCode")+1));
				}
				
				//이미지 파일 설정
				MultipartFile[] files = ((MultipartFile[])mav.getModel().get("files"));

				/* 저장 폴더 경로 설정 */
				String path = "C:\\Users\\user\\git\\agi-log\\src\\main\\webapp\\resources\\img\\"+ab.getSuCode()+"\\board\\";
				
				//게시글 DB 삽입.
				if(this.convertToBoolean(this.session.insert("insFbPost", pb))) {
					//이미지 파일이 있는지 체크
					if (files[0].getSize() != 0) {
						PostPhotoBean ppb = new PostPhotoBean();
						//이미지 파일 갯수만큼 반복
						for (int idx = 0; idx < files.length; idx++) {
							//랜덤 이름 생성
							UUID uuid = UUID.randomUUID();
							String[] uuids = uuid.toString().split("-");
							
							String uniqueName = uuids[0];

							//확장자 획득
							int pos = files[idx].getOriginalFilename().lastIndexOf(".");
							String ext = files[idx].getOriginalFilename().substring(pos);
							//생성된 랜덤이름 + 확장자 저장 
							String fileName = uniqueName+ext;
			
							File realPath = new File(path,fileName);//최종 경로로 파일 저장
							files[idx].transferTo(realPath);//파일 실제 전송
	
							//이미지 코드 설정. 이미지가 없으면 1로 설정하고 있으면 마지막 게시글 코드+1값으로 설정함
							if (this.session.selectOne("getFpCode", pb) == null) {
								ppb.setFpCode(1);
							} else {
								ppb.setFpCode(((int)this.session.selectOne("getFpCode", pb)+1));
							}
							
							//DB에 삽입할 정보 세팅
							ppb.setFpFbCode(pb.getFbCode());
							ppb.setFpFbSuCode(pb.getFbSuCode());
							ppb.setFpFbDate(((PostBean)this.session.selectOne("getFbDate", pb)).getFbDate());
							ppb.setFpLink("/res/img/"+ppb.getFpFbSuCode()+"/board/"+fileName);
							
							//이미지 삽입 성공시 flag를 true로 설정. 실패시 flag를 false로 설정하고 반복문 탈출
							if(this.convertToBoolean(this.session.insert("insFp", ppb))) {
								System.out.println("이미지 삽입 성공");
								flag = true;
							} else {
								flag = false;
								break;
							}
						}
						
						//flag가 트루면 자유게시판으로. flase면 DB에 저장됐던 정보들을 지우고 자유게시판으로.
						if (flag) {
							mav.addObject("content",this.makePostView(this.session.selectOne("getFbPostContent", pb)));
							mav.setViewName("post");
						} else {
							this.session.delete("delFbPost", pb);
							this.session.delete("delFbPostPhoto", ppb);
							
							mav.addObject("message", "네트워크가 불안정합니다. 잠시 후 다시 시도해 주세요.");
							mav.addObject("freeBoardList", this.makeBoardList(this.session.selectList("getFbPostList")));
							mav.setViewName("freeBoard");
						}
					} else {
						mav.addObject("content",this.makePostView(this.session.selectOne("getFbPostContent", pb)));
						mav.setViewName("post");
					}
				} else {
					mav.addObject("message", "네트워크가 불안정합니다. 잠시 후 다시 시도해 주세요.");
					mav.addObject("freeBoardList", this.makeBoardList(this.session.selectList("getFbPostList")));
					mav.setViewName("freeBoard");
				}
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void moveUpdatePostPageCtl(ModelAndView mav) {
		
	}
	
	private void updatePostCtl(ModelAndView mav) {
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if (ab != null) {
				PostBean pb = (PostBean) mav.getModel().get("postBean");
				
				if(this.convertToBoolean(this.session.update("updFbPost", pb))) {
					mav.addObject("content",this.makePostView(this.session.selectOne("getFbPostContent", pb)));
					mav.setViewName("post");
				} else {
					mav.addObject("message", "네트워크가 불안정합니다. 잠시 후 다시 시도해 주세요.");
					mav.addObject("freeBoardList", this.makeBoardList(this.session.selectList("getFbPostList")));
					mav.setViewName("freeBoard");
				}
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//자유게시판 게시글 EL 작업
	private String makePostView(PostBean pb) {
		StringBuffer sb = new StringBuffer();
		AuthBean ab;
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			List<PostPhotoBean> ppb = this.session.selectList("getFbPp", pb);
			if(ab != null) {
				sb.append("<div class=\"pTitle\">" + pb.getFbTitle() + "</div>");
				sb.append("<div class=\"pHead\">");
				try {
					sb.append("<div class=\"pWriter\">작성자&ensp;<small class=\"swriter\">" + this.enc.aesDecode(pb.getFbSuName(), pb.getFbSuCode()) + "</small></div>");
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
						| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
					e.printStackTrace();
				}
				sb.append("<div class=\"pDate\">작성일&ensp;<small class=\"sDate\">" + pb.getFbDate() +"</small></div>");
				sb.append("<div class=\"pView\">조회수&ensp;<small class=\"sView\">" + pb.getFbView() + "</small></div>");
				sb.append("<div class=\"pLike\">좋아요&ensp;<small class=\"sLike\">" + pb.getLikes() + "</small></div>");
				if (pb.getFbSuCode().equals(ab.getSuCode())) {
					sb.append("<div class=\"pUDIcon\">" + "<input type='button' class='updatePost' value='수정' onClick='updatePost(" + pb.getFbCode() + ")'> | <input type='button' class='deletePost' value='삭제' onClick='deletePost(" + pb.getFbCode() + ")'>" + "</div>");
				}
				sb.append("</div>");
				sb.append("<div class=\"pBody\">");
				sb.append("<div class=\"pContent\"> " + pb.getFbContent() + " </div>");	
				sb.append("<div class=\"pPhoto\"> ");
				if (ppb != null) {
					for (int idx = 0; idx < ppb.size(); idx++) {
						if (idx%2 == 0) {
							sb.append("<div class='imgContainer' style='width:280px;float: left;'><img src='" + ppb.get(idx).getFpLink() + "'>");
						} else {
							sb.append("<div class='imgContainer' style='width:280px;float: right;'><img src='" + ppb.get(idx).getFpLink() + "'>");
						}
						sb.append("</div>");
					}
				}
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<button class=\"likeBtn\" onClick=\"likeBtn()\">좋아요</button>");
				sb.append("<button class=\"backList\" onClick=\"movePage('MoveBoardPage')\">목록</button>");
				sb.append("</div");
			} else {
				sb.append("<div class=\"pTitle\">" + pb.getFbTitle() + "</div>");
				sb.append("<div class=\"pHead\">");
				try {
					sb.append("<div class=\"pWriter\">작성자&ensp;<small class=\"sWriter\">" + this.enc.aesDecode(pb.getFbSuName(), pb.getFbSuCode()) + "</small></div>");
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
						| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
					e.printStackTrace();
				}
				sb.append("<div class=\"pDate\">작성일&ensp;<small class=\"sDate\">" + pb.getFbDate() +"</small></div>");
				sb.append("<div class=\"pView\">조회수&ensp;<small class=\"sView\">" + pb.getFbView() + "</small></div>");
				sb.append("<div class=\"pLike\">좋아요&ensp;<small class=\"sLike\">" + pb.getLikes() + "</small></div>");
				sb.append("</div>");
				sb.append("<div class=\"pBody\">");
				sb.append("<div class=\"pContent\"> " + pb.getFbContent() + " </div>");
				sb.append("<div class=\"pPhoto\"> ");
				if (ppb != null) {
					for (int idx = 0; idx < ppb.size(); idx++) {
						if (idx%2 == 0) {
							sb.append("<div class='imgContainer' style='width:280px;float: left;'><img src='" + ppb.get(idx).getFpLink() + "'>");
						} else {
							sb.append("<div class='imgContainer' style='width:280px;float: right;'><img src='" + ppb.get(idx).getFpLink() + "'>");
						}
						sb.append("</div>");
					}
				}
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<button class=\"backList\" onClick=\"movePage('MoveBoardPage')\">목록</button>");
				sb.append("</div");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return sb.toString();
	}
	//자유게시판 목록 EL 작업
	private String makeBoardList(List<PostBean> fbBoardList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<select id=\"freeBoardSelect\" onChange=\"changeSort()\">");
			sb.append("<option value = \"newList\">최신순</option>");
			sb.append("<option value = \"oldList\">오래된순</option>");
			sb.append("<option value = \"likeList\">좋아요순</option>");
			sb.append("<option value = \"viewList\">조회수순</option>");
		sb.append("</select>");
		
		sb.append("<table class=\"freeTable\">");
			sb.append("<tr>");
			sb.append("<th class=\"freeBoardM title\">제목</th>");
			sb.append("<th class=\"freeBoardM writer\">작성자</th>");
			sb.append("<th class=\"freeBoardM date\">작성일</th>");
			sb.append("<th class=\"freeBoardM like\">좋아요</th>");
			sb.append("<th class=\"freeBoardM view\">조회수</th>");
			sb.append("</tr>");
			for(int idx=0; idx<fbBoardList.size(); idx++) {
				PostBean pb = (PostBean)fbBoardList.get(idx);
				sb.append("<tr class=\"selectBoard\" onClick=\"boardContent('"+ pb.getFbCode() + "','" + pb.getFbSuCode() + "','" + pb.getFbDate() +"')\">\n");
				sb.append("<td class=\"freeBoardTitle\">"+ pb.getFbTitle() +"</td>");
				try {
					sb.append("<td class=\"freeBoardWriter\">"+ this.enc.aesDecode(pb.getFbSuName(), pb.getFbSuCode()) +"</td>");
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
						| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
						| BadPaddingException e) {
					e.printStackTrace();
				}
				sb.append("<td class=\"fbBoardB\">"+ pb.getFbDate() +"</td>");
				sb.append("<td class=\"fbBoardB\">"+ pb.getLikes() +"</td>");
				sb.append("<td class=\"fbBoardB\">"+ pb.getFbView() +"</td>");
				sb.append("</tr>");
			//this.page.makePageGroup();	
			}
		sb.append("</table>");
		
		return sb.toString();
	}
	
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;
	}
}