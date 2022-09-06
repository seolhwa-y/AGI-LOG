package com.agilog.services;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.DailyDiaryBean;
import com.agilog.beans.DailyDiaryPhotoBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.ProjectUtils;

@Service
public class DashBoard implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private ProjectUtils pu;

	public DashBoard() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 4:
			this.moveMainCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {

	}
	
	// 메인페이지 :: 감성일기 피드 사진 불러오기
	private void moveMainCtl(ModelAndView mav) {
		StringBuffer sb = new StringBuffer();
		AuthBean ab;
		
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			if (ab != null) {
				mav.addObject("accessInfo", ab);
			}
			
			List<DailyDiaryPhotoBean> ddpList = this.session.selectList("getDairyDiaryPhoto");
			
			for(int i = 0; i < 9; i++) {
				sb.append("<img src='"+ ddpList.get(i).getDpLink() +"' onclick=\"me('"+ ddpList.get(i).getDdCode() +"')\" class=\"todayPhoto\">");
			}
			
			mav.addObject("dailyDiaryPhoto", sb.toString());
			mav.setViewName("dashBoard");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;

	}
}
