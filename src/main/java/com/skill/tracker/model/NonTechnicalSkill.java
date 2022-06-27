package com.skill.tracker.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonTechnicalSkill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int spoken;
	private int communication;		
	private int aptitude;
	public int getSpoken() {
		return spoken;
	}
	public void setSpoken(int spoken) {
		this.spoken = spoken;
	}
	public int getCommunication() {
		return communication;
	}
	public void setCommunication(int communication) {
		this.communication = communication;
	}
	public int getAptitude() {
		return aptitude;
	}
	public void setAptitude(int aptitude) {
		this.aptitude = aptitude;
	}
	
}
