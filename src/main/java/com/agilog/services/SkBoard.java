package com.agilog.services;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BoardBean;
import com.agilog.beans.PostBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.Paging;
import com.agilog.utils.ProjectUtils;

@Service
public class SkBoard implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	private Paging page;

	public SkBoard() {
	}

	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		}
	}

	public void backController(Model model, int serviceCode) {
		switch (serviceCode) {
		case 111:
			this.freeBoardLikeCtl(model);
			break;
		case 112:
			this.freeBoardLikeCtl(model);
			break;
		}
	}

	// 자유게시판 좋아요
	@Transactional(rollbackFor = SQLException.class)
	private void freeBoardLikeCtl(Model model) {
		PostBean pb = (PostBean) model.getAttribute("postBean");
		AuthBean ab;
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			if (ab != null) {
				pb.setSuCode(ab.getSuCode());

				//날짜 형식 변환
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				
				Date d = form.parse(pb.getFbDate());
				pb.setFbDate(sdf.format(d));
				
				// 0개 일때 !false=>좋아요 누른적 없음 => 좋아요 등록
				if (!this.convertToBoolean(this.session.selectOne("isFbLike", pb))) {
					if (this.convertToBoolean(this.session.insert("insFbLike", pb))) {
						// 현재 유저의 좋아요 여부 저장
						pb.setLike(true);
						// 해당게시글 전체 좋아요 수 조회
						pb.setLikes(Integer.parseInt(this.session.selectOne("getFbLike", pb)));
						// 해당게시글 좋아요 수 업데이트
						if (this.convertToBoolean(this.session.update("updFbLike", pb))) {
							model.addAttribute("fbLike", pb);
						}
					}
				} else { // 1개 일때 !true=>좋아요 누른적 있음 => 좋아요 삭제
					if (this.convertToBoolean(this.session.delete("delFbLike", pb))) {
						// 현재 유저의 좋아요 여부 저장
						pb.setLike(false);
						// 해당게시글 전체 좋아요 수 조회
						pb.setLikes(Integer.parseInt(this.session.selectOne("getFbLike", pb)));
						// 해당게시글 좋아요 수 업데이트
						if (this.convertToBoolean(this.session.update("updFbLike", pb))) {
							model.addAttribute("fbLike", pb);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 정보게시판 좋아요
	@Transactional(rollbackFor = SQLException.class)
	private void infoBoardLikeCtl(Model model) {
		PostBean pb = (PostBean) model.getAttribute("postBean");
		AuthBean ab;
		try {
			ab = (AuthBean) this.pu.getAttribute("accessInfo");
			if (ab != null) {
				pb.setSuCode(ab.getSuCode());
				
				//날짜 형식 변환
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				
				Date d = form.parse(pb.getIbDate());
				pb.setIbDate(sdf.format(d));
				
				// 0개 일때 !false=>좋아요 누른적 없음 => 좋아요 등록
				if (!this.convertToBoolean(this.session.selectOne("isIbLike", pb))) {
					if (this.convertToBoolean(this.session.insert("insIbLike", pb))) {
						// 현재 유저의 좋아요 여부 저장
						pb.setLike(true);
						// 해당게시글 전체 좋아요 수 조회
						pb.setLikes(this.session.selectOne("getIbLike", pb));
						// 해당게시글 좋아요 수 업데이트
						if (this.convertToBoolean(this.session.update("updIbLike", pb))) {
							model.addAttribute("ibLike", pb);
						}
					}
				} else { // 1개 일때 !true=>좋아요 누른적 있음 => 좋아요 삭제
					if (this.convertToBoolean(this.session.delete("delIbLike", pb))) {
						// 현재 유저의 좋아요 여부 저장
						pb.setLike(false);
						// 해당게시글 전체 좋아요 수 조회
						pb.setLikes(this.session.selectOne("getIbLike", pb));
						// 해당게시글 좋아요 수 업데이트
						if (this.convertToBoolean(this.session.update("updIbLike", pb))) {
							model.addAttribute("ibLike", pb);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean convertToBoolean(int booleanCheck) {
		boolean result = false;

		return result;
	}
}
