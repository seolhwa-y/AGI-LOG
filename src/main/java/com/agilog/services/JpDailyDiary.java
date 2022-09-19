package com.agilog.services;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.PostBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class JpDailyDiary implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;

	public JpDailyDiary() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 86:
			this.selectFeedSortCtl(mav);
			break;
		case 121:
			this.selectMyFeedSortCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {

	}
	// 감성일기-전체피드 페이지로 이동
	private void moveDailyDiaryPageCtl(ModelAndView mav) {

	}

	private void showDailyDiaryCtl(Model model) {

	}

	private void moveMyDailyDiaryPageCtl(ModelAndView mav) {

	}

	private void insertDailyDiaryCtl(ModelAndView mav) {

	}

	private void insertDailyDiaryCommentCtl(Model model) {

	}

	private void updateDailyDiaryCommentCtl(Model model) {

	}

	private void deleteDailyDiaryComentCtl(Model model) {

	}

	private void selectFeedSortCtl(ModelAndView mav) {
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
		//캘린더에서 글쓰기 버튼 눌렀을때
		DailyDiaryBean d = (DailyDiaryBean)mav.getModel().get("dailyDiaryBean");
		if(d!=null&&d.getMoveWrite()!=null&&d.getMoveWrite().equals("1")) {
			mav.addObject("isWrite",true);
		}
		
		//최신순
			if(d.getDdSort().equals("newList")){
				mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeedNew")));
		//오래된순
			}else if(d.getDdSort().equals("oldList")){
				//오래된순 가져오기
				mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeedOld")));
		//좋아요순
			}else if(d.getDdSort().equals("likeList")){	
				//좋아요순 가져오기
				mav.addObject("allDailyDiaryList", this.makeDialyFeed(this.session.selectList("getDailyDiaryFeedLike")));
			}

		mav.setViewName("dailyDiary");
		
	}
	private void selectMyFeedSortCtl(ModelAndView mav) {
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
				DailyDiaryBean d = (DailyDiaryBean)mav.getModel().get("dailyDiaryBean");
				d.setDdSuCode(ab.getSuCode());
					//최신순
				if(d.getDdSort().equals("newList")){
					mav.addObject("myDailyDiaryFeed", this.makeDialyFeed(this.session.selectList("getMyDailyDiaryFeedNew",d)));
					//오래된순
				}else if(d.getDdSort().equals("oldList")){
					//오래된순 가져오기
					mav.addObject("myDailyDiaryFeed", this.makeDialyFeed(this.session.selectList("getMyDailyDiaryFeedOld",d)));
					//좋아요순
				}else if(d.getDdSort().equals("likeList")){	
					//좋아요순 가져오기
					mav.addObject("myDailyDiaryFeed", this.makeDialyFeed(this.session.selectList("getMyDailyDiaryFeedLike",d)));
				}
				
				mav.setViewName("myDailyDiary");
				//캘린더에서 해당 날짜 감성일기 부분을 눌렀을때
				if(d!=null&&d.getDdDate()!=null) {
					mav.addObject("showDdDate",d.getDdDate());
				}
			}else {
				mav.setViewName("login");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	
	}

	private void updateMyDailyDiaryCtl(Model model) {

	}

	private void deleteMyDailyDiaryCtl(ModelAndView mav) {

	}

	private void trendHashTagCtl(Model model) {

	}

	private void searchHashTagCtl(Model model) {

	}

	private String makeDialyFeed(List<DailyDiaryBean> feedList) {
		StringBuffer sb = new StringBuffer();

		for(DailyDiaryBean fl : feedList) {
			sb.append("<li class=\'feed\' style=\'cursor : pointer;\' onClick=\'getFeed(" + fl.getDdCode() + ")\'>");
			sb.append("<div class=\'feed_top\'>");
			
			if(fl.getDpLink()!=null) {
				sb.append("<img src=\'"+fl.getDpLink()+"\'>");
			}else {
				sb.append("<img src=\'/res/img/non_photo.png\'>");
			}
	
			sb.append("<div class=\'like\'>❤ "+fl.getLikes()+"</div>");
			sb.append("</div>");
			sb.append("<div class=\'feed_bottom\'>");
			sb.append("<div class=\'feedDate\'>"+ fl.getDdDate()+"</div>");
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
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
