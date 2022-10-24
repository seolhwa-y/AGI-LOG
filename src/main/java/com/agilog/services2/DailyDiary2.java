package com.agilog.services2;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.DailyDiaryCommentBean;
import com.agilog.beans.DailyDiaryPhotoBean;
import com.agilog.beans.PostBean;
import com.agilog.beans.PostPhotoBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class DailyDiary2 implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;

	public DailyDiary2() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {

		switch(serviceCode) {

		case 35:
			this.insertDailyDiaryCtl(mav);
			break;
		case 87:
			this.deleteDailyDiaryFeedCtl(mav);
			break;
		default:
			break;
		}	
	}

	public void backController(Model model, int serviceCode) {

		switch(serviceCode) {

		case 31:
			this.getDailyDiaryFeedCtl(model);
			break;
		case 88:
			this.updateDailyDiaryFeedCtl(model);
			break;
		default:
			break;
		}	
	}

	// 감성일기 피드 상세내용 보기
	private void getDailyDiaryFeedCtl(Model model) {
		/* 
		 * 개발자 : 김태훈(게시글), 염설화(댓글), 한슬기(좋아요)
		 * 세부기능 : 사용자가 감성일기 피드를 눌렀을 때, 게시글 내용, 댓글, 좋아요 불러온다.
		 */
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String,String> likeMap = new HashMap<String,String>();
        DailyDiaryCommentBean ddcb = (DailyDiaryCommentBean)model.getAttribute("dailyDiaryCommentBean");
        DailyDiaryBean ddb = (DailyDiaryBean) model.getAttribute("dailyDiaryBean");
        boolean like = false;
        AuthBean ab;
        
        // 김태훈 : 감성일기 내용 불러오기
        ddb = this.session.selectOne("getDDFeed", ddb);
        
        // 감성일기 내용에 사진이 없을 경우 기본 이미지로 대체.
        if(ddb.getDpLink() == null) {
            ddb.setDpLink("/res/img/non_photo.png");
        }
        
        // 한슬기 : 감성일기 좋아요
        try {
            ab = (AuthBean)this.pu.getAttribute("accessInfo");
            if(ab != null) {
				// 염설화 : 현재 로그인한 유저와 게시글 및 댓글 작성자 확인을 위하여 세션에서 유저코드 가져와서 저장
                map.put("suCode", ab.getSuCode());
                
                likeMap.put("ddCode", ddb.getDdCode());
                likeMap.put("suCode", ab.getSuCode());
                // 좋아요 누른적 있음
                if (this.convertToBoolean(this.session.selectOne("isDdLikeM", likeMap))) {
                	like = true;
                } else { // 좋아요 누른적 없음
                	like = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 염설화 : 감성일기 댓글 내용 불러오기
        ddcb.setDcDdCode(ddb.getDdCode());
        ddcb.setDcDdSuCode(ddb.getSuCode());//suCode == ddSuCode
        ddcb.setDcDdDate(ddb.getDdDate());
        
        map.put("ddComment", this.session.selectList("getDailyDiaryComment", ddcb));

        // 감성일기 상세 내용 map 담아서 전송
        ddb.setLike(like);
        map.put("ddFeed", ddb);

        model.addAttribute("dailyDiaryFeed", map);
    }

	private void insertDailyDiaryCtl(ModelAndView mav) {
		HttpServletRequest req = (HttpServletRequest)mav.getModel().get("req");

		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				//게시글 이미지 삽입 성공 여부
				boolean flag = true;
				//디디빈 세팅
				DailyDiaryBean db = (DailyDiaryBean) mav.getModel().get("dailyDiaryBean");

				//작성자 코드 설정
				db.setSuCode(ab.getSuCode());

				//피드 코드 설정. 피드가 없으면 1로 설정하고 있으면 마지막 피드 코드+1값으로 설정함
				if (this.session.selectOne("getDdCode") == null) {
					db.setDdCode("1");
				} else {
					
					db.setDdCode(Integer.toString((int)this.session.selectOne("getDdCode")+1));
				}

				//이미지 파일 설정
				MultipartFile files = ((MultipartFile)mav.getModel().get("files"));
				
				/* 저장 폴더 경로 설정 */
				String path = req.getSession().getServletContext().getRealPath("/resources/img/")+ab.getSuCode()+"/dailydiary/";
				
				if(this.convertToBoolean(this.session.insert("insDd", db))) {
					//이미지 파일이 있는지 체크
					if (files.getSize() != 0) {
						DailyDiaryPhotoBean ddpb = new DailyDiaryPhotoBean();
						
						//랜덤 이름 생성
						UUID uuid = UUID.randomUUID();
						String[] uuids = uuid.toString().split("-");
						
						String uniqueName = uuids[0];

						//확장자 획득
						int pos = files.getOriginalFilename().lastIndexOf(".");
						String ext = files.getOriginalFilename().substring(pos);
						//생성된 랜덤이름 + 확장자 저장 
						String fileName = uniqueName+ext;

						File realPath = new File(path,fileName);//최종 경로로 파일 저장
					System.out.println("path" + path);
					System.out.println("fileName" + fileName);
					System.out.println("realPath" + realPath);
						files.transferTo(realPath);//파일 실제 전송

						//이미지 코드 설정. 이미지가 없으면 1로 설정하고 있으면 마지막 게시글 코드+1값으로 설정함
						if (this.session.selectOne("getDdpCode", db) == null) {
							ddpb.setDpCode("1");
						} else {
							ddpb.setDpCode(Integer.toString((int)this.session.selectOne("getDdpCode", db)+1));
						}
						
						//DB에 삽입할 정보 세팅
						ddpb.setDpDdCode(db.getDdCode());
						ddpb.setDpDdSuCode(db.getSuCode());
						ddpb.setDpDdDate(((DailyDiaryBean)this.session.selectOne("getDdDate", db)).getDdDate());
						ddpb.setDpLink("/res/img/"+ddpb.getDpDdSuCode()+"/dailydiary/"+fileName);
						
						
						//이미지 삽입 성공시 flag를 true로 설정. 실패시 flag를 false로 설정하고 반복문 탈출
						if(this.convertToBoolean(this.session.insert("insDDP", ddpb))) {
							flag = true;
						} else {
							flag = false;
						}
					
						
						//flag가 트루면 자유게시판으로. flase면 DB에 저장됐던 정보들을 지우고 자유게시판으로.
						if (flag) {
							mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
							mav.addObject("message","감성일기 등록을 성공하셨습니다!");
							mav.addObject("title","감성일기 등록");
							mav.setViewName("dailyDiary");
						} else {
							this.session.delete("delDd", db);
							this.session.delete("delDdPhoto", ddpb);

							mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
							mav.setViewName("dailyDiary");
						}
					} else {
						mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
						mav.addObject("message","감성일기 등록을 성공하셨습니다!");
						mav.addObject("title","감성일기 등록");
						mav.setViewName("dailyDiary");
					}
				} else {
					mav.addObject("message", "네트워크가 불안정합니다. 잠시 후 다시 시도해 주세요.");
					mav.addObject("title","경고");
					mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
					mav.setViewName("dailyDiary");
				}
			} else {
				mav.addObject("title","경고");
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("title","경고");
			mav.addObject("message", "네트워크가 불안정합니다.");
			mav.setViewName("dashBoard");
		}
	}

	private void insertDailyDiaryCommentCtl(Model model) {

	}

	private void updateDailyDiaryCommentCtl(Model model) {

	}

	private void deleteDailyDiaryComentCtl(Model model) {

	}

	private void selectFeedSortCtl(ModelAndView mav) {

	}

	private void updateDailyDiaryFeedCtl(Model model) {
		DailyDiaryBean ddb = (DailyDiaryBean) model.getAttribute("dailyDiaryBean");
		if(this.convertToBoolean(this.session.update("updDDFeed", ddb))) {
			this.getDailyDiaryFeedCtl(model);
		}
	}

	private void deleteDailyDiaryFeedCtl(ModelAndView mav) {
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				//디디빈 세팅
				DailyDiaryBean db = (DailyDiaryBean) mav.getModel().get("dailyDiaryBean");
				
				HttpServletRequest req = (HttpServletRequest)mav.getModel().get("req");
				DailyDiaryPhotoBean ddpb = this.session.selectOne("getDDPhoto", db);
				
				if (ddpb != null) {
					File file = new File(req.getSession().getServletContext().getRealPath("/resources")+ddpb.getDpLink().substring(4));
			        
			    	if( file.exists() ){
			    		if(file.delete()){
			    			//데일리 다이어리 이미지 삭제
			    			this.session.delete("delDdPhoto", db);
			    		}else{
			    		}
			    	}else{
			    	}
				}
				
				//데일리 다이어리 댓글 삭제
				this.session.delete("delDdComment", db);
				//데일리 다이어리 좋아요 삭제
				this.session.delete("delDdLike", db);
				//데일리 다이어리 피드 삭제
				if(this.convertToBoolean(this.session.delete("delDd", db))) {
					mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
					mav.setViewName("dailyDiary");
					mav.addObject("title","감성일기 삭제");
					mav.addObject("message", "감성일기 삭제를 성공하였습니다!");
				} else {
					mav.addObject("title","경고");
					mav.addObject("message", "네트워크가 불안정합니다.");
					mav.setViewName("dailyDiary");
				}
			} else {
				mav.addObject("title","경고");
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("title","경고");
			mav.addObject("message", "네트워크가 불안정합니다.");
			mav.setViewName("dashBoard");
		}
	}

	private void trendHashTagCtl(Model model) {

	}

	private void searchHashTagCtl(Model model) {

	}

	private String makeDialyFeed(List<DailyDiaryBean> feedList) {
		StringBuffer sb = new StringBuffer();
		
		for(DailyDiaryBean fl : feedList) {
			sb.append("<li class=\'feed\' onClick=\'getFeed(" + fl.getDdCode() + ")\'>");
			sb.append("<div class=\'feed_top\'>");
			//sb.append("<i class='fa-solid fa-xmark closeBtn editBtn' onclick=\'modalClose()\'>");
			
			if(fl.getDpLink()!=null) {
				sb.append("<img src=\'"+fl.getDpLink()+"\'>");
			}else {
				sb.append("<img src=\'/res/img/non_photo.png\'>");
			}
	
			sb.append("<div class=\'like\'>❤ "+fl.getLikes()+"</div>");
			sb.append("</div>");
			sb.append("<div class=\'feed_bottom\'>");
			sb.append("<div id=\'feedDate\'>"+ fl.getDdDate()+"</div>");
			if(fl.getDdContent().length()>32) {
				sb.append(fl.getDdContent().substring(0,25)+"...</div>");
			}else {
				sb.append(fl.getDdContent()+"</div>");
			}
			
			sb.append("</li>");
			 
		}
		
		return sb.toString();
	}

	// BooleanCheck ��
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}