package com.agilog.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilog.beans.BebeMapCommentBean;
import com.agilog.beans.CompanyBean;
import com.agilog.beans.DailyDiaryCommentBean;
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.PostCommentBean;
import com.agilog.beans.ReservationBean;
import com.agilog.services.Authentication;
import com.agilog.services.BebeCalendar;
import com.agilog.services.BebeMap;
import com.agilog.services.Board;
import com.agilog.services.Company;
import com.agilog.services.DailyDiary;
import com.agilog.services.DashBoard;
import com.agilog.services.HealthDiary;
import com.agilog.services.MyPage;
import com.agilog.services2.HealthDiary2;
import com.agilog.services3.Board3;
import com.agilog.services3.DailyDiary3;

@RestController
public class ShAPIController {
	@Autowired
	Authentication auth;
	@Autowired
	DashBoard dashBoard;
	@Autowired
	Company company;
	@Autowired
	DailyDiary dailyDiary;
	@Autowired
	HealthDiary healthDiary;
	@Autowired
	BebeMap bebeMap;
	@Autowired
	BebeCalendar bebeCalendar;
	@Autowired
	Board board;
	@Autowired
	MyPage myPage;
	@Autowired
	HealthDiary2 healthDiary2;
	@Autowired
	Board3 board3;
	@Autowired
	DailyDiary3 dailyDiary3;

	// 건강일기 추세 아이 변경
	@SuppressWarnings("unchecked")
	@PostMapping("/SelectBaby")
	public HashMap<String, Object> selectBaby(Model model, @ModelAttribute HealthDiaryBean hb) {
		model.addAttribute(hb);
		this.healthDiary2.backController(model, 95);

		return (HashMap<String, Object>)model.getAttribute("babyStatusList");
	}
	
	// 게시판 댓글 등록
	@SuppressWarnings("unchecked")
	@PostMapping("/InsertBoardComment")
	public HashMap<String, Object> insertBoardComment(Model model, @ModelAttribute PostCommentBean pcb) {
		model.addAttribute(pcb);
		this.board3.backController(model, 60);
		
		return (HashMap<String, Object>)model.getAttribute("insfbComment");
	}
	
	// 게시판 댓글 수정
	@SuppressWarnings("unchecked")
	@PostMapping("/UpdateBoardComment")
	public HashMap<String, Object> updateBoardComment(Model model, @ModelAttribute PostCommentBean pcb) {
		model.addAttribute(pcb);
		this.board3.backController(model, 61);
		
		return (HashMap<String, Object>) model.getAttribute("updfbComment");
	}
	
	// 게시판 댓글 삭제
	@SuppressWarnings("unchecked")
	@PostMapping("/DeleteBoardComment")
	public HashMap<String, Object> deleteBoardComment(Model model, @ModelAttribute PostCommentBean pcb) {
		model.addAttribute(pcb);
		this.board3.backController(model, 62);
		
		return (HashMap<String, Object>) model.getAttribute("delfbComment");
	}
	
	// 감성일기 댓글 조회
	@SuppressWarnings("unchecked")
	@PostMapping("/ShowDailyDiary")
	public HashMap<String, Object> showDailyDiary(Model model, @ModelAttribute DailyDiaryCommentBean ddcb){
		model.addAttribute(ddcb);
		this.dailyDiary3.backController(model, 31);
		
		return (HashMap<String, Object>)model.getAttribute("ddCommentList");
	}
	
	// 감성일기 댓글 등록
	@SuppressWarnings("unchecked")
	@PostMapping("/InsertDailyDiaryComment")
	public HashMap<String, Object> insertDailyDiaryComment(Model model, @ModelAttribute DailyDiaryCommentBean ddcb){
		model.addAttribute(ddcb);
		this.dailyDiary3.backController(model, 36);
		
		return (HashMap<String, Object>)model.getAttribute("insDdComment");
	}
	
	// 감성일기 댓글 수정
	@SuppressWarnings("unchecked")
	@PostMapping("/UpdateDailyDiaryComment")
	public HashMap<String, Object> updateDailyDiaryComment(Model model, @ModelAttribute DailyDiaryCommentBean ddcb){
		model.addAttribute(ddcb);
		this.dailyDiary3.backController(model, 63);
		
		return (HashMap<String, Object>)model.getAttribute("updDdComment");
	}
	
	// 감성일기 댓글 삭제
	@SuppressWarnings("unchecked")
	@PostMapping("/DeleteDailyDiaryComment")
	public HashMap<String, Object> deleteDailyDiaryComment(Model model, @ModelAttribute DailyDiaryCommentBean ddcb){
		model.addAttribute(ddcb);
		this.dailyDiary3.backController(model, 64);
		
		return (HashMap<String, Object>)model.getAttribute("delDdComment");
	}
	
	// 지도 정보 확인 후 댓글 불러오기
	@SuppressWarnings("unchecked")
	@PostMapping("/ViewCompanyInfo")
	public HashMap<String, Object> viewCompanyInfo(Model model, @ModelAttribute CompanyBean cb){
		model.addAttribute(cb);
		this.bebeMap.backController(model, 42);
		return (HashMap<String, Object>)model.getAttribute("mcCommentList");
	}
	
	// 지도 댓글 등록
	@SuppressWarnings("unchecked")
	@PostMapping("/InsertCompanyComment")
	public HashMap<String, Object> insertCompanyComment(Model model, @ModelAttribute BebeMapCommentBean bmcb){
		model.addAttribute(bmcb);
		this.bebeMap.backController(model, 45);
		
		return (HashMap<String, Object>)model.getAttribute("insMapComment");
	}
	
	// 지도 댓글 수정
	@SuppressWarnings("unchecked")
	@PostMapping("/UpdateCompanyComment")
	public HashMap<String, Object> updateCompanyComment(Model model, @ModelAttribute BebeMapCommentBean bmcb){
		model.addAttribute(bmcb);
		this.bebeMap.backController(model, 97);
		
		return (HashMap<String, Object>)model.getAttribute("updMapComment");
	}
	
	// 지도 댓글 삭제
	@SuppressWarnings("unchecked")
	@PostMapping("/DeleteCompanyComment")
	public HashMap<String, Object> deleteCompanyComment(Model model, @ModelAttribute BebeMapCommentBean bmcb){
		model.addAttribute(bmcb);
		this.bebeMap.backController(model, 85);
		
		return (HashMap<String, Object>)model.getAttribute("delMapComment");
	}
	
	// 해당 병원에 예약 가능여부 불러오기
	@SuppressWarnings("unchecked")
	@PostMapping("/ShowReservation")
	public HashMap<String, Object> showReservation(Model model, @ModelAttribute ReservationBean rb){
		model.addAttribute(rb);
		this.bebeMap.backController(model, 43);
		
		return (HashMap<String, Object>) model.getAttribute("resInfo");
	}
	
	// 예약 가능한 시간대 불러오기
	@SuppressWarnings("unchecked")
	@PostMapping("/GetResTime")
	public List<ReservationBean> getBabyListInfo(Model model, @ModelAttribute ReservationBean rb){
		System.out.println(rb);
		model.addAttribute(rb);
		this.bebeMap.backController(model, 200);
	
		return (List<ReservationBean>)model.getAttribute("resTime");
	}
	
	// 지도 예약완료
	@PostMapping("/Reservation")
	public void reservation(Model model, @ModelAttribute ReservationBean rb){
		System.out.println(rb);
		model.addAttribute(rb);
		this.bebeMap.backController(model, 44);
	}
}
