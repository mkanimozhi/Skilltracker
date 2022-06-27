package com.skill.tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseHeader {

	private TransactionNotification transactionNotification = new TransactionNotification();

	public TransactionNotification getTransactionNotification() {
		return transactionNotification;
	}

	public void setTransactionNotification(TransactionNotification transactionNotification) {
		this.transactionNotification = transactionNotification;
	}
	
}
