package com.skill.tracker.demo;

import com.skill.tracker.model.Profiles;
import com.skill.tracker.model.ResponseHeader;

public class ProfileResponse {

	private ResponseHeader responseHeader;
	private Profiles responseBody;
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	public Profiles getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(Profiles responseBody) {
		this.responseBody = responseBody;
	}
	
}
