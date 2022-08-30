package com.agilog.services;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class Authentication implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	private JavaMailSenderImpl mail;
	
	public Authentication() {}
	
	public void backController(ModelAndView mav, int serviceCode) {
		
	}
	
	public void backController(Model model, int serviceCode) {
		
	}
	
	private void moveLoginCtl(ModelAndView mav) {
		
	}
	
	private void moveJoinCtl(ModelAndView mav) {
		
	}
	
	private void moveCompanyPageCtl(ModelAndView mav) {
		
	}
 	
	private void moveJoinForPageCtl(ModelAndView mav) {
		
	}
	
	private void naverJoinCtl(ModelAndView mav) {
		
	}
	
	private void insertNaverMemberCtl(ModelAndView mav) {
		
	}
	
	private void kakaoJoinCtl(ModelAndView mav) {
		
	}
	
	private void insertKakaoMemberCtl(ModelAndView mav) {
		
	}
	
	private void moveCompanyJoinPageCtl(ModelAndView mav) {
		
	}
	
	private void naverLoginCtl(ModelAndView mav) {
		
	}
	
	private void kakaoLoginCtl(ModelAndView mav) {
		
	}
	
	private void moveFindCompanyPwCtl(ModelAndView mav) {
		
	}
	
	private void findIdCtl(ModelAndView mav) {
		
	}
	
	private void sendEmail(AuthBean ab, String title, String content) {
		
	}
	
	private void findPersonalPwCtl(ModelAndView mav) {
		
	}
	
	private void findCompanyPwCtl(ModelAndView mav) {
		
	}
	
	private String makeRandomPw(String random) {
		String randomPw=null;
		
		return randomPw;
	}
	
	private boolean convertToBoolean(int booleanCheck) {
		boolean result=false;
		
		return result;
	}
}
