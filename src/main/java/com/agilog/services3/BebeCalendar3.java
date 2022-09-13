package com.agilog.services3;

import java.time.LocalDate;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BebeCalendarBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class BebeCalendar3 implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;

	public BebeCalendar3() {
	}

	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 7:
			this.moveCalendarCtl(mav);
			break;
		}
	}

	public void backController(Model model, int serviceCode) {

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

				LocalDate todaysDate = LocalDate.now();
				BebeCalendarBean bcb = (BebeCalendarBean) mav.getModel().get("bebeCalendarBean");
				bcb.setSuCode(ab.getSuCode());
				bcb.setDate(todaysDate.toString());
				List<BebeCalendarBean> birthList = this.session.selectList("getBabyBirth", bcb);
				List<BebeCalendarBean> ddList = this.session.selectList("checkWriteDD", bcb);
				List<BebeCalendarBean> hdList = this.session.selectList("checkWriteHD", bcb);
				List<BebeCalendarBean> reList = this.session.selectList("checkReservation", bcb);
				List<BebeCalendarBean> scList = this.session.selectList("checkSchedule", bcb);
				mav.addObject("birthList", this.makeEventBirth(birthList));
				mav.addObject("ddList", this.makeEventDD(ddList));
				mav.addObject("hdList", this.makeEventHD(hdList));
				mav.addObject("reList", this.makeEventRe(reList));
				mav.addObject("scList", this.makeEventSc(scList));
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

	private String makeEventBirth(List<BebeCalendarBean> birthList) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		for (BebeCalendarBean bcb : birthList) {
			String year = bcb.getBabyBirthDay().split("-")[0];
			String month = bcb.getBabyBirthDay().split("-")[1];
			String day = bcb.getBabyBirthDay().split("-")[2];
			for (int y = Integer.parseInt(year); y < Integer.parseInt(year) + 200; y++) {
				sb.append("{start:'" + Integer.toString(y) + "-" + month + "-" + day + "'");
				sb.append(",title:'" + bcb.getBabyName() + " 생일'");
				sb.append(",display:'list-item'");
				sb.append(",borderColor:'rgb(255,145,145)'");
				sb.append(",classNames:'birthDay'}");
				if (y != (Integer.parseInt(year) + 200) - 1) {
					sb.append(",");
				}
			}
			if (idx++ != birthList.size() - 1) {
				sb.append(",");
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	private String makeEventDD(List<BebeCalendarBean> ddList) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		if (ddList.size() != 0) {
			sb.append(",");
			for (BebeCalendarBean bcb : ddList) {
				sb.append("{start:'" + bcb.getDdDate() + "'");
				sb.append(",display:'list-item'");
				sb.append(",borderColor:'#ff9e80'");
				sb.append(",classNames:'dailyDiary'}");
				if (idx++ != ddList.size() - 1) {
					sb.append(",");
				}
			}
		}

		return sb.toString();
	}

	private String makeEventHD(List<BebeCalendarBean> hdList) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		if (hdList.size() != 0) {
			sb.append(",");
			for (BebeCalendarBean bcb : hdList) {
				sb.append("{start:'" + bcb.getHdDate() + "'");
				sb.append(",display:'list-item'");
				sb.append(",borderColor:'#8980ff'");
				sb.append(",classNames:'healthDiary'}");
				if (idx++ != hdList.size() - 1) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	private String makeEventRe(List<BebeCalendarBean> reList) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		if (reList.size() != 0) {
			sb.append(",");
			for (BebeCalendarBean bcb : reList) {
				sb.append("{start:'" + bcb.getResDate() + "'");
				sb.append(",display:'list-item'");
				sb.append(",borderColor:'#ff1717'");
				sb.append(",classNames:'reservation'}");
				if (idx++ != reList.size() - 1) {
					sb.append(",");
				}
			}
		}

		return sb.toString();
	}

	private String makeEventSc(List<BebeCalendarBean> scList) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		if (scList.size() != 0) {
			sb.append(",");
			for (BebeCalendarBean bcb : scList) {
				sb.append("{start:'" + bcb.getScDate() + "'");
				sb.append(",display:'list-item'");
				sb.append(",borderColor:'#2dc347'");
				sb.append(",classNames:'schedule'}");
				if (idx++ != scList.size() - 1) {
					sb.append(",");
				}
			}
		}

		return sb.toString();
	}

	private boolean convertToBoolean(int booleanCheck) {
		boolean result = false;

		return result;
	}
}
