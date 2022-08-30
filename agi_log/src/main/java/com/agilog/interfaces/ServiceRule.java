package com.agilog.interfaces;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public interface ServiceRule {
	public void backController(ModelAndView mav, int serviceCode);
	public void backController(Model model, int serviceCode);
}
