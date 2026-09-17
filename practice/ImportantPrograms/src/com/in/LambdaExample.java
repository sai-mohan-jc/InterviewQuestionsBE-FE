package com.in;

import java.util.stream.Stream;

public class LambdaExample {
	
	public static void main(String[] args) {
		
		Runnable r=()->System.out.println("JAVA WORLD");
		
		r.run();
		
		
		
		/*
		 * 4. What is the output for below programs?
 a. System.out.println("GOOGLE".split(3));
B. StringBuilder sb=new StringBuilder("2");
StringBuilder sb1=new StringBuilder("3");
  if(sb==b1)
c. Stream s=stream.iteraate("",str->str+"M");
  s.limit(5).forEach(System.out::println);
		 */
		//System.out.println("GOOGLE".split(3)); -exception
		
		StringBuilder sb=new StringBuilder("2");
		StringBuilder sb1=new StringBuilder("3");
		  System.out.println(sb==sb1) ;
		  
		  Stream s=Stream.iterate(0,str->str+1);
		  s.limit(5).forEach(System.out::println);
		
	}

}
