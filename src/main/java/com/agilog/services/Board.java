package com.agilog.services;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.agilog.beans.AuthBean;
import com.agilog.beans.BoardBean;
import com.agilog.interfaces.ServiceRule;
import com.agilog.utils.Encryption;
import com.agilog.utils.Paging;
import com.agilog.utils.ProjectUtils;

@Service
public class Board implements ServiceRule {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired
	private ProjectUtils pu;
	private Paging page;

	public Board() {
	}

	public void backController(ModelAndView mav, int serviceCode) {
		switch (serviceCode) {
		}
	}

	public void backController(Model model, int serviceCode) {

	}

	private void changeSortCtl(Model model) {

	}

	private void changeListCtl(ModelAndView mav) {

	}

	private void moveFreeBoardCtl(ModelAndView mav) {

	}

	private void moveMeetingBoardCtl(ModelAndView mav) {

	}

	private void moveInfoBoardCtl(ModelAndView mav) {

	}

	private void moveWritePageCtl(ModelAndView mav) {

	}

	private void moveShowPostCtl(ModelAndView mav) {

	}

	private void searchPostCtl(ModelAndView mav) {

	}

	private void insertPostCommentCtl(Model model) {

	}

	private void updateBoardCommentCtl(Model model) {

	}

	private void deleteBoardCommentCtl(Model model) {

	}

	private void insertPostCtl(ModelAndView mav) {

	}

	private void moveUpdatePostPageCtl(ModelAndView mav) {

	}

	private void updatePostCtl(ModelAndView mav) {

	}

	private String makeBoardList(List<BoardBean> boardList) {
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}

	private boolean convertToBoolean(int booleanCheck) {
		boolean result = false;

		return result;
	}
}
