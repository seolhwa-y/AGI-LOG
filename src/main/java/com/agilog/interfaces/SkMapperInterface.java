package com.agilog.interfaces;

import com.agilog.beans.HealthDiaryBean;

public interface SkMapperInterface {
	public String getHealthDiaryCode(HealthDiaryBean hb);
	
	public int insHDWeight(HealthDiaryBean hb);
	public int insHDHeight(HealthDiaryBean hb);
	public int insHDHead(HealthDiaryBean hb);
	public int insHDFoot(HealthDiaryBean hb);
	public int insHDTemp(HealthDiaryBean hb);
	public int insHDSleep(HealthDiaryBean hb);
	public int insHDdefecation(HealthDiaryBean hb);
	public int insHDDefstatus(HealthDiaryBean hb);
	public int insHDMeal(HealthDiaryBean hb);
	public int insHDMemo(HealthDiaryBean hb);

	public HealthDiaryBean getHealthDiaryDetail(HealthDiaryBean hb);
	
}
