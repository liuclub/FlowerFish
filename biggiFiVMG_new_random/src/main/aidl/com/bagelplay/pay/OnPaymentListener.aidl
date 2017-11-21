package com.bagelplay.pay;

interface OnPaymentListener
{
	void OnPayment(in int result,in String resultsJson);
	
	void OnOnlyUploadLog(in int result,in String resultsJson);
}