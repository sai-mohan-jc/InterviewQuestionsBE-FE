package com.banking.customer.inheritance;

public class Main {
	
	public static void main(String[] args) {
		Parent p=new Child();
		p.add();
		
		Parent p1=new Parent();
		
		Child c=new Child();
		
		Child c1=(Child)p;
		
		
	}

}
