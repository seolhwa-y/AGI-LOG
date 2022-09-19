package com.agilog.services2;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.DailyDiaryCommentBean;
import com.agilog.beans.DailyDiaryPhotoBean;
import com.agilog.beans.PostBean;
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
				//디디빈 세팅
				DailyDiaryBean db = (DailyDiaryBean) mav.getModel().get("dailyDiaryBean");

				System.out.println("코드 체크 : " + ab.getSuCode());
				db.setSuCode(ab.getSuCode());

				if (this.session.selectOne("getFbCode") == null) {
					db.setDdCode("1");
				} else {
					System.out.println("코드 체크1 : " + (this.session.selectOne("getDdCode")));
					db.setDdCode(Integer.toString((int)this.session.selectOne("getDdCode")+1));
				}
				System.out.println("코드 체크2 : " + db.getDdCode());
				System.out.println("상태 체크 : " + db.getDdStatus());
				System.out.println("유저 코드 체크 : " + db.getSuCode());
				System.out.println("컨텐츠 체크 : " + db.getDdContent());
				
				if(this.convertToBoolean(this.session.insert("insDd", db))) {
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