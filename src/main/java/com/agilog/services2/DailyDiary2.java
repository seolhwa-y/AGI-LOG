package com.agilog.services2;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

	private void moveDailyDiaryPageCtl(ModelAndView mav) {
		
	}

	private void showDailyDiaryCtl(Model model) {

	}

	private void moveMyDailyDiaryPageCtl(ModelAndView mav) {

	}

	private void getDailyDiaryFeedCtl(Model model) {
		System.out.println("겟 디디 피드 진입 체크");
		HashMap<String, Object> map = new HashMap<String, Object>();
		DailyDiaryCommentBean ddcb = (DailyDiaryCommentBean)model.getAttribute("dailyDiaryCommentBean");
		DailyDiaryBean ddb = (DailyDiaryBean) model.getAttribute("dailyDiaryBean");
		
		AuthBean ab;
		try {
			ab = (AuthBean)this.pu.getAttribute("accessInfo");
			if(ab != null) {
				map.put("suCode", ab.getSuCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ddb = this.session.selectOne("getDDFeed", ddb);
		
		ddcb.setDcDdCode(ddb.getDdCode());
		ddcb.setDcDdSuCode(ddb.getSuCode());//suCode == ddSuCode
		ddcb.setDcDdDate(ddb.getDdDate());
		
		System.out.println("피드 코드 체크2 : " + ddcb.getDcDdCode());
		System.out.println("작성자 체크 : " + ddcb.getDcDdSuCode());
		System.out.println("날짜 체크 : " + ddcb.getDcDdDate());
		System.out.println("이미지 체크1 : " + ddb.getDpLink());
		
		if(ddb.getDpLink() == null) {
			ddb.setDpLink("/res/img/non_photo.png");
		}
		
		System.out.println("이미지 체크2 : " + ddb.getDpLink());

		System.out.println("comment check : " + this.session.selectList("getDailyDiaryComment", ddcb));
		
		// 0개 일때 !false=>좋아요 누른적 없음
		if (!this.convertToBoolean(this.session.selectOne("isDdLike", ddb))) {
				// 현재 유저의 좋아요 여부 저장
				ddb.setLike(true);
		} else { // 1개 일때 !true=>좋아요 누른적 있음
				// 현재 유저의 좋아요 여부 저장
				ddb.setLike(false);
		}
		
		map.put("ddFeed", ddb);
		map.put("ddComment", this.session.selectList("getDailyDiaryComment", ddcb));
		model.addAttribute("dailyDiaryFeed", map);
		
	}

	private void insertDailyDiaryCtl(ModelAndView mav) {
		System.out.println("인설트 디디 진입 체크");
		
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
				if (this.session.selectOne("getFbCode") == null) {
					db.setDdCode("1");
				} else {
					
					db.setDdCode(Integer.toString((int)this.session.selectOne("getDdCode")+1));
				}
				
				System.out.println("ddCode : " + db.getDdCode());

				//이미지 파일 설정
				MultipartFile files = ((MultipartFile)mav.getModel().get("files"));
				
				/* 저장 폴더 경로 설정 */
				String path = "C:\\Users\\user\\git\\agi-log\\src\\main\\webapp\\resources\\img\\"+ab.getSuCode()+"\\DD\\";
				
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
						files.transferTo(realPath);//파일 실제 전송

						//이미지 코드 설정. 이미지가 없으면 1로 설정하고 있으면 마지막 게시글 코드+1값으로 설정함
						if (this.session.selectOne("getDdpCode", db) == null) {
							ddpb.setDpCode("1");
						} else {
							ddpb.setDpCode(Integer.toString((int)this.session.selectOne("getDdpCode", db)+1));
						}
						
						//DB에 삽입할 정보 세팅
						System.out.println("ddCode : " + db.getDdCode());
						ddpb.setDpDdCode(db.getDdCode());
						System.out.println("dpDdCode : " + ddpb.getDpDdCode());
						System.out.println(ddpb.getDpDdCode().equals(db.getDdCode()));

						System.out.println("suCode : " + db.getSuCode());
						ddpb.setDpDdSuCode(db.getSuCode());
						System.out.println("dpDdSuCode : " + ddpb.getDpDdSuCode());
						System.out.println(ddpb.getDpDdSuCode().equals(db.getSuCode()));
						
						String ddDate = ((DailyDiaryBean)this.session.selectOne("getDdDate", db)).getDdDate();
						StringBuffer sb = new StringBuffer();
						sb.append(ddDate.substring(0, 4));
						sb.append(ddDate.substring(5, 7));
						sb.append(ddDate.substring(8, 10));
						sb.append(ddDate.substring(11, 13));
						sb.append(ddDate.substring(14, 16));
						sb.append(ddDate.substring(17, 19));
						
						ddpb.setDpDdDate(sb.toString());
						System.out.println(ddpb.getDpDdDate().equals(db.getDdDate()));
						ddpb.setDpLink(realPath.toString());
						
						System.out.println("ddpb : " + ddpb);
						
						//이미지 삽입 성공시 flag를 true로 설정. 실패시 flag를 false로 설정하고 반복문 탈출
						if(this.convertToBoolean(this.session.insert("insDDP", ddpb))) {
							System.out.println("이미지 삽입 성공");
							flag = true;
						} else {
							flag = false;
						}
					
						
						//flag가 트루면 자유게시판으로. flase면 DB에 저장됐던 정보들을 지우고 자유게시판으로.
						if (flag) {
							mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
							mav.setViewName("dailyDiary");
						} else {
							this.session.delete("delDd", db);
							this.session.delete("delDdPhoto", ddpb);
							
							mav.addObject("message", "네트워크가 불안정합니다. 잠시 후 다시 시도해 주세요.");
							mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
							mav.setViewName("dailyDiary");
						}
					} else {
						mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
						mav.setViewName("dailyDiary");
					}
				} else {
					mav.addObject("message", "네트워크가 불안정합니다. 잠시 후 다시 시도해 주세요.");
					mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
					mav.setViewName("dailyDiary");
				}
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		System.out.println("업데이트 디디 진입 체크");
		DailyDiaryBean ddb = (DailyDiaryBean) model.getAttribute("dailyDiaryBean");
		if(this.convertToBoolean(this.session.update("updDDFeed", ddb))) {
			this.getDailyDiaryFeedCtl(model);
		}
	}

	private void deleteDailyDiaryFeedCtl(ModelAndView mav) {
		System.out.println("delete 디디 진입 체크");

		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				//디디빈 세팅
				DailyDiaryBean db = (DailyDiaryBean) mav.getModel().get("dailyDiaryBean");

				System.out.println("코드 체크 : " + db.getDdCode());
				
				if(this.convertToBoolean(this.session.insert("delDd", db))) {
					mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeed")));
					mav.setViewName("dailyDiary");
				}
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			sb.append("<i class='fa-solid fa-xmark closeBtn editBtn' onclick='modalClose('')'>");
			
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