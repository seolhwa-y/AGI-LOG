package com.agilog.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	}

	public void backController(Model model, int serviceCode) {
		switch (serviceCode) {
		case 48:
			this.dateDetailCtl(model,0);
			break;
		case 49:
			this.deleteScheduleCtl(model);
			break;
		case 50:
			this.updateScheduleCtl(model);
			break;
		case 51:
			this.insertScheduleCtl(model);
			break;
		case 75:
			this.deleteReservationCtl(model);
			break;
		}
	}
	// 캘린더에서 특정일자 클릭 시 모달창 띄워주고 안에 내용 보여주기
	private void dateDetailCtl(Model model,int num) {
		BebeCalendarBean bcb = (BebeCalendarBean)model.getAttribute("bebeCalendarBean");
		try {
			bcb.setSuCode(((AuthBean)this.pu.getAttribute("accessInfo")).getSuCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//bcb.setDate((bcb.getDate().replace("-", "")).substring(2));
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

		
			/* 예약일정 가져오기 */
			List<ReservationBean> rbList = new ArrayList<ReservationBean>();
			
			rbList = this.session.selectList("getReservationInfo", bcb);
			
			if(rbList.size()!=0) {
				bcb.setReservationInfo(rbList);
			}
			
			/* 일반일정 가져오기 */
			List<ScheduleBean> scheduleList = new ArrayList<ScheduleBean>();
			
			scheduleList = this.session.selectList("getScheduleList",bcb);

			if(scheduleList.size()!=0) {
				bcb.setScheduleList(scheduleList);
			}
		if(this.converToBoolean(num)) {
			bcb.setMessage("성공");
		}
		model.addAttribute(bcb);
	}

	private void moveMonthCtl(Model model) {

	}
	
	//일정삭제
	@Transactional(rollbackFor = SQLException.class)
	private void deleteScheduleCtl(Model model) {
		ScheduleBean sb = (ScheduleBean)model.getAttribute("scheduleBean");
		
		try {
		
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			sb.setSuCode(ab.getSuCode());

			/* 일정 update */
			if(this.converToBoolean(this.session.update("delSchedule",sb))) {
				//insert 성공
				//모달창 안의 정보 새로 가져가기
				this.dateDetailCtl(model,1);
			}else {
				//insert 실패
				//moveCalendarCtl
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//일정수정
	@Transactional(rollbackFor = SQLException.class)
	private void updateScheduleCtl(Model model) {
		ScheduleBean sb = (ScheduleBean)model.getAttribute("scheduleBean");
	
		try {
		
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			sb.setSuCode(ab.getSuCode());

			/* 일정 update */
			if(this.converToBoolean(this.session.update("updSchedule",sb))) {
				//insert 성공
				//모달창 안의 정보 새로 가져가기
				this.dateDetailCtl(model,1);
			}else {
				//insert 실패
				//moveCalendarCtl
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//일정추가
	@Transactional(rollbackFor = SQLException.class)
	private void insertScheduleCtl(Model model) {
		ScheduleBean sb = (ScheduleBean)model.getAttribute("scheduleBean");
		
		try {
			/* 일정코드 max+1가져오기 or YYYYMMDD01 */
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			sb.setSuCode(ab.getSuCode());
			String schCode = (this.session.selectOne("getNewScheduleCode", sb));
			sb.setScheduleCode(schCode);

			/* 일정 insert */
			if(this.converToBoolean(this.session.insert("insSchedule",sb))) {
				//insert 성공
				//모달창 안의 정보 새로 가져가기
				this.dateDetailCtl(model,1);
			}else {
				//insert 실패
				//moveCalendarCtl
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//예약삭제
	@Transactional(rollbackFor = SQLException.class)
	private void deleteReservationCtl(Model model) {
		ReservationBean rb = (ReservationBean)model.getAttribute("reservationBean");
		
		try {
		
			AuthBean ab = (AuthBean)this.pu.getAttribute("accessInfo");
			rb.setResSuCode(ab.getSuCode());
			// CC => 예약취소
			rb.setRcCode("CC");
			/* 예약취소 :: 예약완료 => 예약취소로 변경*/
			if(this.converToBoolean(this.session.update("updateReservationStatus",rb))) {
				//update 성공
				//모달창 안의 정보 새로 가져가기
				this.dateDetailCtl(model,1);
			}else {
				//update 실패
				//moveCalendarCtl
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;
	}
}
