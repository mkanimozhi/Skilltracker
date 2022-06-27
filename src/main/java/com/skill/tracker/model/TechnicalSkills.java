package com.skill.tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TechnicalSkills implements Comparable<TechnicalSkills> {

	private String skillName;
	private int expertiseLevel;
	
	public TechnicalSkills() {
		super();
	}

	public TechnicalSkills(String skillName, int expertiseLevel) {
		super();
		this.skillName = skillName;
		this.expertiseLevel = expertiseLevel;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public int getExpertiseLevel() {
		return expertiseLevel;
	}

	public void setExpertiseLevel(int expertiseLevel) {
		this.expertiseLevel = expertiseLevel;
	}

	@Override
	public int compareTo(TechnicalSkills technicalSkills) {
		int compareExpertiseLevel = ((TechnicalSkills) technicalSkills).getExpertiseLevel();
		//descending order
		return compareExpertiseLevel - this.expertiseLevel;
	}

}
