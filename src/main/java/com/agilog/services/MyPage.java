package com.agilog.services;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
			this.changeParentInfoCtl(mav);
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
		AuthBean ab;
		MyPageBean mpb = (MyPageBean)model.getAttribute("myPageBean");
		
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			ab.setSuTheme(mpb.getSuTheme());
			if(this.converToBoolean(this.session.update("updTheme",ab))) {
				//테마변경 성공
			}else {
				model.addAttribute("message","테마변경 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/* 아이추가 */
	@Transactional(rollbackFor = SQLException.class)
	private void insertBabyInfoCtl(ModelAndView mav) {
		AuthBean ab;
		BabyBean bb = (BabyBean) mav.getModel().get("babyBean");
		try {
			/*인서트 하기 위해서 babyBean에 정보를 담음*/
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			String babyCode = this.session.selectOne("getNewBabyCode",ab);
			bb.setBbCode(babyCode);
			bb.setSuCode(ab.getSuCode());
			
			/*아이 추가 :: baby 테이블에 인서트*/
			if(this.converToBoolean(this.session.insert("insBabyInfo",bb))) {
				/*아이 추가 :: HealthDiary 테이블에 인서트*/
				/* HealthDiaryBean에 정보 담기 */
				HealthDiaryBean hdb = new HealthDiaryBean();
				hdb.setSuCode(ab.getSuCode());
				hdb.setBbCode(babyCode);
				hdb.setBbWeight(bb.getBbWeight());
				hdb.setCaCode("01");
				
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				hdb.setHdDate(sdf.format(date));
				
				/* HealthDiaryCode max+1값 가져옴 */
				hdb.setHdCode(this.session.selectOne("getHealthDiaryCode",hdb));
				/* DB :: HealthDiary테이블에 아이 몸무게 INSERT */
				if(this.converToBoolean(this.session.insert("insHDWeight",hdb))) {
					hdb.setBbHeight(bb.getBbHeight());
					hdb.setCaCode("02");
					/* DB :: HealthDiary테이블에 아이 키 INSERT */
					if(this.converToBoolean(this.session.insert("insHDHeight",hdb))) {
						mav.addObject("message","아이 추가를 성공하였습니다!");
						mav.addObject("title","아이 추가");
						this.moveMyPageCtl(mav);
					}else {
						/* 아이 추가 실패 */
						mav.addObject("message","아이 추가를 실패하였습니다!");
						mav.addObject("title","아이 추가");
						this.moveMyPageCtl(mav);
					}
				}else{
					/* 아이 추가 실패 */
					mav.addObject("message","아이 추가를 실패하였습니다!");
					mav.addObject("title","아이 추가");
					this.moveMyPageCtl(mav);
				}
			}else {
				/* 아이 추가 실패 */
				mav.addObject("message","아이 추가를 실패하였습니다!");
				mav.addObject("title","아이 추가");
				this.moveMyPageCtl(mav);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* 부모 프로필 변경 */
	@Transactional(rollbackFor = SQLException.class)
	private void changeParentInfoCtl(ModelAndView mav) {
		MyPageBean mb = ((MyPageBean)mav.getModel().get("myPageBean"));
		try {
			boolean isUpd=false;
			mb.setSuCode(((AuthBean)this.pu.getAttribute("accessInfo")).getSuCode());
			/* DB :: SU테이블에 변경된 유저 정보 UPDATE */
			//닉네임
			if(mb.getSuNickName()!=""&&mb.getSuNickName()!=null) {
				if(this.converToBoolean(this.session.update("updProFile",mb))){
					/*업데이트 성공*/
					/*페이지 이동*/
					//this.moveMyPageCtl(mav);
					isUpd = true;
				}else {
					/*업데이트 실패*/
					isUpd = false;
				}
			}
			if(mb.getSuAddress()!=""&&mb.getSuAddress()!=null) {
				if (mb.getSuCode().length() > 10) {
					mb.setSuAddress(this.enc.aesEncode(mb.getSuAddress(), mb.getSuCode().substring(0, 10)));
				} else {
					mb.setSuAddress(this.enc.aesEncode(mb.getSuAddress(), mb.getSuCode()));
				}				
				if(this.converToBoolean(this.session.update("updAddress",mb))) {
					/* 업데이트 성공 */
					/* 페이지 이동 */
					//this.moveMyPageCtl(mav);
					isUpd = true;
				}else {
					/*업데이트 실패*/
					isUpd = false;
				}
			}
			
			if(isUpd) {
				this.moveMyPageCtl(mav);
				mav.addObject("message","부모 프로필 업데이트를 성공하였습니다!");
				mav.addObject("title","부모 프로필 업데이트");
			} else {
				mav.addObject("message","부모 프로필 업데이트를 실패하였습니다!");
				mav.addObject("title","부모 프로필 업데이트");
				this.moveMyPageCtl(mav);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	/* 내 아이정보 가져오기 */
	@Transactional(rollbackFor = SQLException.class)
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
	@Transactional(rollbackFor = SQLException.class)
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
					//주소 복호화
					mpb.setSuAddress(this.enc.aesDecode(mpb.getSuAddress(), ab.getSuCode()));
				} else {
					//네이버
					ab.setType("naver");
					String suCode = ab.getSuCode().substring(0, 10);
					//이름 복호화
					mpb.setSuName(this.enc.aesDecode(mpb.getSuName(),suCode));
					//주소 복호화
					mpb.setSuAddress(this.enc.aesDecode(mpb.getSuAddress(), suCode));
				}
				ab.setSuNickName(mpb.getSuNickName());
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
	@Transactional(rollbackFor = SQLException.class)
	private void changeBabyInfoCtl(ModelAndView mav) {
		BabyBean bb = new BabyBean();
		bb = (BabyBean)mav.getModel().get("babyBean");
		/* DB에 UPDATE하기 전에 2022-01-01 => 20220101로 바꿔주기 */
		bb.setBbBirthday((bb.getBbBirthday()).replaceAll("-",""));
		try {
			bb.setSuCode(((AuthBean)this.pu.getAttribute("accessInfo")).getSuCode());
			/* DB에 생일 UPDATE*/
			if(this.converToBoolean(this.session.update("updBabyBirthday",bb))){
				mav.addObject("message","아이 프로필 업데이트를 성공하였습니다!");
				mav.addObject("title","아이 프로필 업데이트");
			}else {
				/* 업데이트 실패 */
				mav.addObject("message","아이 프로필 업데이트를 실패하였습니다!");
				mav.addObject("title","아이 프로필 업데이트");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.moveMyPageCtl(mav);
	}


	/* 아이 프로필사진 변경 */
	@Transactional(rollbackFor = SQLException.class)
	private void uploadBabyImageCtl(ModelAndView mav) {
		MultipartFile file = (MultipartFile)mav.getModel().get("file");
		HttpServletRequest req = (HttpServletRequest)mav.getModel().get("req");
		try {
			BabyBean bb = (BabyBean) mav.getModel().get("babyBean");
			bb.setSuCode(((AuthBean)this.pu.getAttribute("accessInfo")).getSuCode());
			String suCode = bb.getSuCode();
			String babyCode = bb.getBbCode();
			//ServletContext context = request.getSession().getServletContext();
			/* 저장 폴더 경로 설정 */
			String path = req.getSession().getServletContext().getRealPath("/resources/img/")+suCode+"\\profile\\";
			
			/* 확장자 뽑아내서 파일이름(아이코드) 만들어주기 */
			int pos = file.getOriginalFilename().lastIndexOf(".");
			String ext = file.getOriginalFilename().substring(pos);
			String fileName = bb.getBbCode()+ext; //유저코드 + 확장자 
			
			/* 파일저장 
			File uploadPath = new File(path);
			if (!uploadPath.exists()) uploadPath.mkdirs();//폴더 없다면 생성*/
			
			File realPath = new File(path,fileName);//최종 경로로 파일 저장
			file.transferTo(realPath);
			
			
			String babyPhoto = "/res/img/"+suCode+"/profile/"+fileName;
			bb.setBbPhoto(babyPhoto);
			/* DB에 파일 저장한 경로 INSERT */
			if(this.converToBoolean((this.session.update("updBabyPhoto",bb)))){
				/* 성공 */
				mav.addObject("message","아이 프로필사진 업데이트를 실패하였습니다!");
				mav.addObject("title","프로필사진 업데이트");
			}else {
				/* 실패 */
				mav.addObject("message","아이 프로필사진 업데이트를 실패하였습니다!");
				mav.addObject("title","프로필사진 업데이트");
			}
		
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		this.moveMyPageCtl(mav);
	
	}
	/* 부모 프로필사진 업데이트 */
	@Transactional(rollbackFor = SQLException.class)
	private void uploadParentImageCtl(ModelAndView mav) {
		MultipartFile file = (MultipartFile)mav.getModel().get("file");
		HttpServletRequest req = (HttpServletRequest)mav.getModel().get("req");
		
		MyPageBean mb = (MyPageBean) mav.getModel().get("myPageBean");
		String suCode = mb.getSuCode();
		/* 저장 폴더 경로 설정 */
		//String path = "C:\\upload\\profile\\"+suCode;
		String path = req.getSession().getServletContext().getRealPath("/resources/img/")+suCode+"\\profile\\";
		System.out.println("path:"+path);
		//String path = "/res/img/"+suCode;
		
		/* 확장자 뽑아내서 파일이름 만들어주기 */
		int pos = file.getOriginalFilename().lastIndexOf(".");
		String ext = file.getOriginalFilename().substring(pos);
		String fileName = mb.getSuCode()+ext; //유저코드 + 확장자
		
		try {
			/* 파일저장 
			File uploadPath = new File(path);//폴더 없다면 생성
			if (!uploadPath.exists()) uploadPath.mkdirs();*/
			
			File realPath = new File(path,fileName);//최종 경로로 파일 저장
			file.transferTo(realPath);
			
			/* DB에 파일 저장한 경로 INSERT */
			String suPhoto = "/res/img/"+suCode+"/profile/"+fileName;
			AuthBean ab = new AuthBean();
			ab.setSuCode(suCode);
			ab.setSuPhoto(suPhoto);
			if(this.converToBoolean((this.session.update("updSocialUserPhoto",ab)))){
				/* 성공 */
				mav.addObject("message","부모 프로필사진 업데이트를 성공하였습니다!");
				mav.addObject("title","프로필사진 업데이트");
			}else {
				/* 실패 */
				mav.addObject("message","부모 프로필사진 업데이트를 실패하였습니다!");
				mav.addObject("title","프로필사진 업데이트");
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
