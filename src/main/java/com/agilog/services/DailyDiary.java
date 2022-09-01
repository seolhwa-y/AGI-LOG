package com.agilog.services;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class DailyDiary implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;

	public DailyDiary() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 5:
			this.moveDailyDiaryPageCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {

	}

	private void moveDailyDiaryPageCtl(ModelAndView mav) {
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
		mav.setViewName("dailyDiary");
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
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
