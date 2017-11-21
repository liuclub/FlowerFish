package com.bagelplay.pay;
import com.bagelplay.pay.OnPaymentListener;

interface PayHandler
{		
	void pay(in String token,in String argsJson,in OnPaymentListener opl);  
}