package com.agilog.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BebeCalendarBean;
import com.agilog.beans.ReservationBean;
import com.agilog.beans.ScheduleBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class BebeCalendar implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;

	public BebeCalendar() {
	}

	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 7:
			this.moveCalendarCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {
		switch (serviceCode) {
		case 48:
			this.dateDetailCtl(model);
			break;
		}
	}
	// 캘린더에서 특정일자 클릭 시 모달창 띄워주고 안에 내용 보여주기
	private void dateDetailCtl(Model model) {
		BebeCalendarBean bcb = (BebeCalendarBean)model.getAttribute("bebeCalendarBean");
		try {
			bcb.setSuCode(((AuthBean)this.pu.getAttribute("accessInfo")).getSuCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		bcb.setDate((bcb.getDate().replace("-", "")).substring(2));
		/* 건강일기 작성여부 검색 */
		if(!this.session.selectOne("isWriteHd",bcb).equals("0")) {
			//작성했음
			bcb.setHealthDiary(true);
		}else {
			//작성안했음
			bcb.setHealthDiary(false);
		}

		/* 공유일기 작성여부 검색 */
		if(!this.session.selectOne("isWriteDd",bcb).equals("0")) {
			//작성했음
			bcb.setDailyDiary(true);
		}else {
			//작성안했음
			bcb.setDailyDiary(false);
		}

		try {
			/* 일반일정 가져오기 */
			List<ReservationBean> rbList = new ArrayList<ReservationBean>();
			rbList = this.session.selectList("getReservationInfo", bcb);
			bcb.setReservationInfo(rbList);
			//병원이름 복호화
			bcb.getReservationInfo().get(0).setResCoName(this.enc.aesDecode(rbList.get(0).getResCoName(),rbList.get(0).getResCoCode()));
			
			/* 예약일정 가져오기 */
			List<ScheduleBean> scheduleList = new ArrayList<ScheduleBean>();
			scheduleList = this.session.selectList("getScheduleList",bcb);
			bcb.setScheduleList(scheduleList);
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
	
			e.printStackTrace();
		}
		model.addAttribute(bcb);

		System.out.println("최종"+bcb);
	}

	private void moveCalendarCtl(ModelAndView mav) {
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
		mav.setViewName("bebeCalendar");
	}

	private void moveMonthCtl(Model model) {

	}

	private void dateDetailCtl(ModelAndView mav) {

	}

	private void deleteScheduleCtl(Model model) {

	}

	private void updateScheduleCtl(Model model) {

	}

	private void insertScheduleCtl(Model model) {

	}

	private void deleteReservationCtl(ModelAndView mav) {

	}

	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;
	}
}
