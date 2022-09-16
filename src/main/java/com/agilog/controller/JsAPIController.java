package com.agilog.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agilog.beans.BabyBean;
import com.agilog.beans.BebeCalendarBean;
import com.agilog.beans.MyPageBean;
import com.agilog.beans.ReservationBean;
import com.agilog.beans.ScheduleBean;
import com.agilog.services.BebeCalendar;
import com.agilog.services.MyPage;
import com.agilog.services3.Company3;

@RestController
public class JsAPIController {
	@Autowired
	MyPage myPage;
	@Autowired
	BebeCalendar calendar;
	@Autowired
	Company3 company;
	
	@SuppressWarnings("unchecked")
	@PostMapping("/GetBabyInfo")
	public MyPageBean getBabyInfo(Model model, @ModelAttribute BabyBean bb){

		myPage.backController(model, 107);
		return (MyPageBean)model.getAttribute("myPageBean");
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/ChangeTheme")
	public String changeTheme(Model model, @ModelAttribute MyPageBean mpb){
		model.addAttribute(mpb);
		myPage.backController(model, 66);
		return ((MyPageBean)model.getAttribute("myPageBean")).getSuTheme();
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/DateDetail")
	public BebeCalendarBean dateDetail(Model model, @ModelAttribute BebeCalendarBean bcb){
		model.addAttribute(bcb);
		calendar.backController(model, 48);
		return (BebeCalendarBean)model.getAttribute("bebeCalendarBean");
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/InsertSchedule")
	public BebeCalendarBean insertSchedule(Model model, @ModelAttribute ScheduleBean sb){
		model.addAttribute(sb);
		/* 개인일정 인서트해주고 특정일 일정정보 불러오는 메소드(서비스코드 48번) 재탕하려고 빌드업 */
		BebeCalendarBean bcb = new BebeCalendarBean();
		bcb.setDate(sb.getScheduleDate());
		model.addAttribute(bcb);
		/* 개인일정 추가 */
		calendar.backController(model, 51);
		
		return (BebeCalendarBean)model.getAttribute("bebeCalendarBean");
	}
	@PostMapping("/UpdateSchedule")
	public BebeCalendarBean updateSchedule(Model model, @ModelAttribute ScheduleBean sb){
		model.addAttribute(sb);
		/* 개인일정 업데이트해주고 특정일 일정정보 불러오는 메소드(서비스코드 48번) 재탕하려고 빌드업 */
		BebeCalendarBean bcb = new BebeCalendarBean();
		bcb.setDate(sb.getScheduleDate());
		model.addAttribute(bcb);
		/* 개인일정 수정 */
		calendar.backController(model, 50);
		
		return (BebeCalendarBean)model.getAttribute("bebeCalendarBean");
	}
	//하이
	@PostMapping("/DeleteSchedule")
	public BebeCalendarBean deleteScheduleCtl(Model model, @ModelAttribute ScheduleBean sb){
		model.addAttribute(sb);
		/* 개인일정 업데이트해주고 특정일 일정정보 불러오는 메소드(서비스코드 48번) 재탕하려고 빌드업 */
		BebeCalendarBean bcb = new BebeCalendarBean();
		bcb.setDate(sb.getScheduleDate());
		model.addAttribute(bcb);
		/* 개인일정 수정 */
		calendar.backController(model, 49);
		
		return (BebeCalendarBean)model.getAttribute("bebeCalendarBean");
	}
	@PostMapping("/DeleteReservation")
	public BebeCalendarBean deleteReservation(Model model, @ModelAttribute ReservationBean rb){
		model.addAttribute(rb);
		/* 예약내역 삭제해주고 특정일 일정정보 불러오는 메소드(서비스코드 48번) 재탕하려고 빌드업 */
		BebeCalendarBean bcb = new BebeCalendarBean();
		bcb.setDate(rb.getResDate());
		model.addAttribute(bcb);
		/* 예약취소 */
		calendar.backController(model, 75);
		
		return (BebeCalendarBean)model.getAttribute("bebeCalendarBean");
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/CoResDetail")
    public HashMap<String,Object> coResDetail(Model model, @ModelAttribute ReservationBean rb){
        model.addAttribute(rb);

        company.backController(model, 118);

        return (HashMap<String,Object>)model.getAttribute("dateInfo");
    }
    @PostMapping("/SetResTime")
    public ReservationBean setResTime(Model model, @ModelAttribute ReservationBean rb){
        model.addAttribute(rb);
        System.out.println("api에서의rb:"+rb);
        company.backController(model, 119);

        return (ReservationBean)model.getAttribute("reservationBean");
    }
}