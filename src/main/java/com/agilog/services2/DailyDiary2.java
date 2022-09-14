package com.agilog.services2;

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
		default:
			break;
		}	
	}

	public void backController(Model model, int serviceCode) {

	}

	private void moveDailyDiaryPageCtl(ModelAndView mav) {

	}

	private void showDailyDiaryCtl(Model model) {

	}

	private void moveMyDailyDiaryPageCtl(ModelAndView mav) {

	}

	private void insertDailyDiaryCtl(ModelAndView mav) {
		System.out.print("인설트 디디 진입 체크");
		
		try {
			AuthBean ab = ((AuthBean) this.pu.getAttribute("accessInfo"));
			if(ab != null) {
				//디디빈 세팅
				DailyDiaryBean db = (DailyDiaryBean) mav.getModel().get("dailyDiaryBean");

				System.out.println("코드 체크 : " + ab.getSuCode());
				db.setDdSuCode(ab.getSuCode());

				if (this.session.selectOne("getFbCode") == null) {
					db.setDdCode("1");
				} else {
					System.out.println("코드 체크1 : " + (this.session.selectOne("getDdCode")));
					db.setDdCode(Integer.toString((int)this.session.selectOne("getDdCode")+1));
				}
				System.out.println("코드 체크2 : " + db.getDdCode());
				System.out.println("상태 체크 : " + db.getDdStatus());
				System.out.println("유저 코드 체크 : " + db.getDdSuCode());
				System.out.println("컨텐츠 체크 : " + db.getDdContent());
				
				if(this.convertToBoolean(this.session.insert("insDd", db))) {
					mav.setViewName("freeBoard");
				}
			} else {
				mav.addObject("message", "세션이 만료되었습니다. 다시 로그인 해주세요");
				mav.setViewName("dashBoard");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mav.setViewName("dailyDiary");

	}

	private void insertDailyDiaryCommentCtl(Model model) {

	}

	private void updateDailyDiaryCommentCtl(Model model) {

	}

	private void deleteDailyDiaryComentCtl(Model model) {

	}

	private void selectFeedSortCtl(ModelAndView mav) {

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

		return sb.toString();
	}

	// BooleanCheck ��
	private boolean convertToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
