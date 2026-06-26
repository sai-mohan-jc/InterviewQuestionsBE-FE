package com.learn.mapexample;

public class MainClass {

	public static void main(String[] args) {
		InterfaceCustom ef= (a,b) ->{
			System.out.println(a+b);
			return a+b;
					};
		
		ef.add(20, 20);
		
	}
}
