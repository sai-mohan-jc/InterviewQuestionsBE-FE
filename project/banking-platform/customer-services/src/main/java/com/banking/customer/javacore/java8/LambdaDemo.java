package com.banking.customer.javacore.java8;

import java.util.HashMap;

public class LambdaDemo {
	
	public static void main(String[] args) {
        
        FeeCalculator imps=a->a*00.2;
        FeeCalculator neft=a->a*00.1;
        FeeCalculator rtgs=a->a*00.5;
        
	     double impsResult = imps.calculate(10);
	     double neftResult = neft.calculate(10);
	     double rtgsResult = rtgs.calculate(10);
	     
	     System.out.println(imps.getClass());
	     
	     HashMap<String, String> map=new HashMap<>();
	     map.putIfAbsent("", "");
	     

        System.out.println("Imps Result: " + impsResult+" "+"Neft Result "+neftResult+" rtgs Result "+rtgsResult);
	}

	@FunctionalInterface
	 interface FeeCalculator {

	    double calculate(double a);
}
	
	
}
