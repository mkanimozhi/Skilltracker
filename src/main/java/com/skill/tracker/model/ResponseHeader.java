package com.skill.tracker.model;

public class ResponseHeader {

	private TransactionNotification transactionNotification = new TransactionNotification();

	public TransactionNotification getTransactionNotification() {
		return transactionNotification;
	}

	public void setTransactionNotification(TransactionNotification transactionNotification) {
		this.transactionNotification = transactionNotification;
	}
	
}
