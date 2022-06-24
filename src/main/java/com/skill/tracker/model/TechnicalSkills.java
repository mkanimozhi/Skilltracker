package com.skill.tracker.model;

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
		// TODO Auto-generated method stub
//		return 0;
		int compareExpertiseLevel = ((TechnicalSkills) technicalSkills).getExpertiseLevel();
		
		//ascending order
//		return this.expertiseLevel - compareExpertiseLevel;
		
		//descending order
		return compareExpertiseLevel - this.expertiseLevel;
	}

	@Override
	public String toString() {
		return "TechnicalSkills [skillName=" + skillName + ", expertiseLevel=" + expertiseLevel + "]";
	}
	

}
