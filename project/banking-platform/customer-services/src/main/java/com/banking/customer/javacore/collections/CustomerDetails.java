package com.banking.customer.javacore.collections;

import java.util.List;

public class CustomerDetails {
	
	record Customer(String name,List<String> accounts) {}
	
	public static void main(String[] args) {
		Customer c1=new Customer("SAI", List.of("A1","A2","A3")) ;
		
		Customer c2=new Customer("SAIMohan", List.of("A4","A5","A6")) ;
		
		Customer c3=new Customer("MohanSAI", List.of("A7","A8","A9")) ;
		
		
		List<Customer> cus=List.of(c1,c2,c3);
		
		List<List<String>> ac=cus.stream().map(a-> a.accounts).toList();
		
		List<String> acfm= cus.stream().flatMap(a->a.accounts.stream()).toList();
		
		System.out.println(ac);
		System.out.println(acfm);
		
	}
	

}
