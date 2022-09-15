package com.agilog.utils;

import java.util.Comparator;

import com.agilog.beans.HealthDiaryBean;

public class ListSort implements Comparator<HealthDiaryBean> {
	@Override
	public int compare(HealthDiaryBean hb, HealthDiaryBean hb2) {
		return hb2.getHdDate().compareTo(hb.getHdDate());
	}

}
