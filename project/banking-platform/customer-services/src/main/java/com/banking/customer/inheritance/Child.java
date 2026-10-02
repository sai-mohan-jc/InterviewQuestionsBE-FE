package com.banking.customer.inheritance;

public class Child extends Parent{
	
	Child(){
		System.out.println("Child constructor");
	}
	
	@Override
	public void add() {
		System.out.println("Cheld add");
	}
	
	public void getName() {
		System.out.println("get name child");
	}

}
