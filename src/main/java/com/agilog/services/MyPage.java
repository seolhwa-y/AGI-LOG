package com.agilog.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BabyBean;
import com.agilog.beans.HealthDiaryBean;
import com.agilog.beans.MyPageBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.ProjectUtils;

@Service
public class MyPage implements ServiceRule {

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;

	public MyPage() {
	}

	// Controller��
	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		case 9:
			this.moveMyPageCtl(mav);
			break;
		case 68:
			this.changeBabyInfoCtl(mav);
			break;
		case 69:
			this.insertBabyInfoCtl(mav);
			break;
		case 91:
			this.updateProfileCtl(mav);
			break;
		case 108:
			this.uploadParentImageCtl(mav);
			break;
		case 109:
			this.uploadBabyImageCtl(mav);
			break;
		}
	}





	public void backController(Model model, int serviceCode) {
		switch (serviceCode) {
		case 66:
			this.changeThemeCtl(model);
			break;
		case 107:
			this.getBabyInfo(model);
			break;
		}
	}
	/* 테마변경 */
	@Transactional
	private void changeThemeCtl(Model model) {
		// TODO Auto-generated method stub
		
	}

	/* 아이추가 */
	@Transactional
	private void insertBabyInfoCtl(ModelAndView mav) {
		AuthBean ab;
		BabyBean bb = (BabyBean) mav.getModel().get("babyBean");
		try {
			/*인서트 하기 위해서 babyBean에 정보를 담음*/
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			String babyCode = this.session.selectOne("getNewBabyCode",ab);
			bb.setBabyCode(babyCode);
			bb.setSuCode(ab.getSuCode());
			
			/*아이 추가 :: baby 테이블에 인서트*/
			if(this.converToBoolean(this.session.insert("insBabyInfo",bb))) {
				/*아이 추가 :: HealthDiary 테이블에 인서트*/
				/* HealthDiaryBean에 정보 담기 */
				HealthDiaryBean hdb = new HealthDiaryBean();
				hdb.setSuCode(ab.getSuCode());
				hdb.setBabyCode(babyCode);
				hdb.setBabyWeight(bb.getBabyWeight());
				hdb.setHdCaCode("01");
				/* HealthDiaryCode max+1값 가져옴 */
				hdb.setHdCode(this.session.selectOne("getHealthDiaryCode"));
				/* DB :: HealthDiary테이블에 아이 몸무게 INSERT */
				if(this.converToBoolean(this.session.insert("insHDWeight",hdb))) {
					hdb.setBabyHeight(bb.getBabyHeight());
					hdb.setHdCaCode("02");
					/* DB :: HealthDiary테이블에 아이 키 INSERT */
					if(this.converToBoolean(this.session.insert("insHDHeight",hdb))) {
						mav.addObject("message","아이 추가 성공");
						this.moveMyPageCtl(mav);
					}else {
						/* 아이 추가 실패 */
						mav.addObject("message","아이 추가 실패");
						this.moveMyPageCtl(mav);
					}
				}else{
					/* 아이 추가 실패 */
					mav.addObject("message","아이 추가 실패");
					this.moveMyPageCtl(mav);
				}
			}else {
				/* 아이 추가 실패 */
				mav.addObject("message","아이 추가 실패");
				this.moveMyPageCtl(mav);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* 부모 프로필 변경 */
	@Transactional
	private void updateProfileCtl(ModelAndView mav) {

		MyPageBean mb = ((MyPageBean)mav.getModel().get("myPageBean"));
		try {
			mb.setSuCode(((AuthBean)this.pu.getAttribute("accessInfo")).getSuCode());
			/* DB :: SU테이블에 변경된 유저 정보 UPDATE */
			if(this.converToBoolean(this.session.update("updProFile",mb))){
				/*업데이트 성공*/
				/*페이지 이동*/
				this.moveMyPageCtl(mav);
				
			}else {
				/*업데이트 실패*/
				String message = "업데이트 실패";
				mav.addObject("message",message);
				this.moveMyPageCtl(mav);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	/* 내 아이정보 가져오기 */
	@Transactional
	private void getBabyInfo(Model model) {
		AuthBean ab;
		List<BabyBean> bb = new ArrayList<BabyBean>();
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			
			MyPageBean mpb = new MyPageBean();
			/* 내 모든 아이정보 가져오기*/
			bb = this.session.selectList("getBabyInfoList", ab.getSuCode());
			mpb.setBabyList(bb);
			mpb.setSuCode(ab.getSuCode());
			
			/* MODEL에 MYPAGE화면에서 띄워줄 정보 담기*/
			model.addAttribute("myPageBean",mpb);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private String makeMyPageHtlm(AuthBean ab) {
		return null;

	}

	/* 마이페이지 이동 CTL */
	@Transactional
	private void moveMyPageCtl(ModelAndView mav) {
		AuthBean ab;
		List<BabyBean> bb = new ArrayList<BabyBean>();
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			/* 로그인 세션 유효한지 확인*/
			if (ab != null) {
				/* 내 정보 가져오기*/
				MyPageBean mpb = (MyPageBean) this.session.selectList("getMyInfo", ab.getSuCode()).get(0);
				if (ab.getSuCode().length() == 10) {
					//카카오
					ab.setType("kakao");
					//이름 복호화
					mpb.setSuName(this.enc.aesDecode(mpb.getSuName(), ab.getSuCode()));
					} else {
					//네이버
					ab.setType("naver");
					String suCode = ab.getSuCode().substring(0, 24);
					//이름 복호화
					mpb.setSuName(this.enc.aesDecode(mpb.getSuName(),suCode));
					
				}
				mav.addObject("accessInfo", ab);
				mav.setViewName("myPage");
				/* 내 아기정보 전부 가져오기 */
				bb = this.session.selectList("getBabyInfoList", ab.getSuCode());
				mpb.setBabyList(bb);
				mav.addObject("mypageInfo",mpb);
			}else {
				/* 로그인 세션 유효 X, 로그인페이지로 이동 */
				mav.setViewName("login");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void changeThemeCtl(ModelAndView mav) {

	}
	
	/* 아이 프로필변경 */
	private void changeBabyInfoCtl(ModelAndView mav) {
		BabyBean bb = new BabyBean();
		bb = (BabyBean)mav.getModel().get("babyBean");
		/* DB에 UPDATE하기 전에 2022-01-01 => 20220101로 바꿔주기 */
		bb.setBabyBirthday((bb.getBabyBirthday()).replaceAll("-",""));
		try {
			bb.setSuCode(((AuthBean)this.pu.getAttribute("accessInfo")).getSuCode());
			/* DB에 생일 UPDATE*/
			if(this.converToBoolean(this.session.update("updBabyBirthday",bb))){
				String message = "아이생일 업데이트 성공";
				mav.addObject("message",message);
			}else {
				/* 업데이트 실패 */
				String message = "아이생일 업데이트 실패";
				mav.addObject("message",message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.moveMyPageCtl(mav);
	}


	/* 아이 프로필사진 변경 */
	private void uploadBabyImageCtl(ModelAndView mav) {
		MultipartFile file = (MultipartFile)mav.getModel().get("file");
		try {
			BabyBean bb = (BabyBean) mav.getModel().get("babyBean");
			bb.setSuCode(((AuthBean)this.pu.getAttribute("accessInfo")).getSuCode());
			String suCode = bb.getSuCode();
			String babyCode = bb.getBabyCode();
			/* 저장 폴더 경로 설정 */
			String path = "C:\\upload\\profile\\"+suCode+"\\"+babyCode;
			
			/* 확장자 뽑아내서 파일이름(부모코드 만들어주기 */
			int pos = file.getOriginalFilename().lastIndexOf(".");
			String ext = file.getOriginalFilename().substring(pos);
			String fileName = bb.getBabyCode()+ext; //유저코드 + 확장자 
			
			/* 파일저장 */
			File uploadPath = new File(path);
			if (!uploadPath.exists()) uploadPath.mkdirs();//폴더 없다면 생성
			
			File realPath = new File(path,fileName);//최종 경로로 파일 저장
			file.transferTo(realPath);
			
			
			String babyPhoto = "/upload/profile/"+suCode+"/"+babyCode+"/"+fileName;
			bb.setBabyPhoto(babyPhoto);
			/* DB에 파일 저장한 경로 INSERT */
			if(this.converToBoolean((this.session.update("updBabyPhoto",bb)))){
				/* 성공 */
				mav.addObject("message","아이 프로필사진 업데이트 성공");

			}else {
				/* 실패 */
				mav.addObject("message","아이 프로필사진 업데이트 실패");
			}
		
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		this.moveMyPageCtl(mav);
	
	}
	/* 부모 프로필사진 업데이트 */
	private void uploadParentImageCtl(ModelAndView mav) {
		MultipartFile file = (MultipartFile)mav.getModel().get("file");

		MyPageBean mb = (MyPageBean) mav.getModel().get("myPageBean");
		String suCode = mb.getSuCode();
		/* 저장 폴더 경로 설정 */
		String path = "C:\\upload\\profile\\"+suCode;
		
		/* 확장자 뽑아내서 파일이름(아이코드)만들어주기 */
		int pos = file.getOriginalFilename().lastIndexOf(".");
		String ext = file.getOriginalFilename().substring(pos);
		String fileName = mb.getSuCode()+ext; //아이코드 + 확장자
		
		try {
			/* 파일저장 */
			File uploadPath = new File(path);//폴더 없다면 생성
			if (!uploadPath.exists()) uploadPath.mkdirs();
			
			File realPath = new File(path,fileName);//최종 경로로 파일 저장
			file.transferTo(realPath);
			
			/* DB에 파일 저장한 경로 INSERT */
			String suPhoto = "/upload/profile/"+suCode+"/"+fileName;
			AuthBean ab = new AuthBean();
			ab.setSuCode(suCode);
			ab.setSuPhoto(suPhoto);
			if(this.converToBoolean((this.session.update("updSocialUserPhoto",ab)))){
				/* 성공 */
				mav.addObject("message","부모 프로필사진 업데이트 성공");
			}else {
				/* 실패 */
				mav.addObject("message","부모 프로필사진 업데이트 실패");
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.moveMyPageCtl(mav);
	}
	
	// BooleanCheck ��
	private boolean converToBoolean(int booleanCheck) {
		return booleanCheck == 0 ? false : true;
	}

}