package com.skill.tracker.model;

public class NonTechnicalSkills implements Comparable<NonTechnicalSkills> {
	private String skillName;
	private int expertiseLevel;
	
	public NonTechnicalSkills() {
		super();
	}
	public NonTechnicalSkills(String skillName, int expertiseLevel) {
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
	public int compareTo(NonTechnicalSkills nonTechnicalSkills) {
		// TODO Auto-generated method stub
//		return 0;
		int compareExpertiseLevel = ((NonTechnicalSkills) nonTechnicalSkills).getExpertiseLevel();
		
		//ascending order
//		return this.expertiseLevel - compareExpertiseLevel;
		
		//descending order
		return compareExpertiseLevel - this.expertiseLevel;
	}
	
}
