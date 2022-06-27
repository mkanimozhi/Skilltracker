package com.skill.tracker.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TechnicalSkill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int html_Css_Javascript;
	private int angular;
	private int react;
	private int spring;
	private int restful;
	private int hibernate;
	private int git;
	private int docker;
	private int jenkins;
	private int aws;
	
	public int getHtml_Css_Javascript() {
		return html_Css_Javascript;
	}
	public void setHtml_Css_Javascript(int html_Css_Javascript) {
		this.html_Css_Javascript = html_Css_Javascript;
	}
	public int getAngular() {
		return angular;
	}
	public void setAngular(int angular) {
		this.angular = angular;
	}
	public int getReact() {
		return react;
	}
	public void setReact(int react) {
		this.react = react;
	}
	public int getSpring() {
		return spring;
	}
	public void setSpring(int spring) {
		this.spring = spring;
	}
	public int getRestful() {
		return restful;
	}
	public void setRestful(int restful) {
		this.restful = restful;
	}
	public int getHibernate() {
		return hibernate;
	}
	public void setHibernate(int hibernate) {
		this.hibernate = hibernate;
	}
	public int getGit() {
		return git;
	}
	public void setGit(int git) {
		this.git = git;
	}
	public int getDocker() {
		return docker;
	}
	public void setDocker(int docker) {
		this.docker = docker;
	}
	public int getJenkins() {
		return jenkins;
	}
	public void setJenkins(int jenkins) {
		this.jenkins = jenkins;
	}
	public int getAws() {
		return aws;
	}
	public void setAws(int aws) {
		this.aws = aws;
	}
	
}
