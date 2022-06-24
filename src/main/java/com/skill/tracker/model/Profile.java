package com.skill.tracker.model;

import java.io.Serializable;
import java.util.Date;

public class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String associateId;
	private String mobile;
	private String email;

	//technical skills
	private TechnicalSkill technicalSkills;

	//non-technical skills
	private NonTechnicalSkill nonTechnicalSkills;
	
	private TechnicalSkills[] technicalSkill;
	private NonTechnicalSkills[] nonTechnicalSkill;
	
	private Date createdTimestamp;
	private Date updatedTimestamp;

	public Profile() {
		super();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAssociateId() {
		return associateId;
	}
	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public TechnicalSkill getTechnicalSkills() {
		return technicalSkills;
	}

	public void setTechnicalSkills(TechnicalSkill technicalSkills) {
		this.technicalSkills = technicalSkills;
	}

	public NonTechnicalSkill getNonTechnicalSkills() {
		return nonTechnicalSkills;
	}

	public void setNonTechnicalSkills(NonTechnicalSkill nonTechnicalSkills) {
		this.nonTechnicalSkills = nonTechnicalSkills;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Date getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	public void setUpdatedTimestamp(Date updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}

	public TechnicalSkills[] getTechnicalSkill() {
		return technicalSkill;
	}

	public void setTechnicalSkill(TechnicalSkills[] technicalSkill) {
		this.technicalSkill = technicalSkill;
	}

	public NonTechnicalSkills[] getNonTechnicalSkill() {
		return nonTechnicalSkill;
	}

	public void setNonTechnicalSkill(NonTechnicalSkills[] nonTechnicalSkill) {
		this.nonTechnicalSkill = nonTechnicalSkill;
	}

}
