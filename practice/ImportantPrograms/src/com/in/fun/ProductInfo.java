
package com.in.fun;

import java.security.Identity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductInfo {
	
	public static void main(String[] args) {
		List<Product> products=new ArrayList<>();
		Product p1=new Product("p1", "iron");
		Product p2=new Product("p1", "iron");
		Product p3=new Product("p2", "iron");
		Product p4=new Product("p1", "iron");
		
		Product p5=new Product("p3", "iron");
		Product p6=new Product("p2", "iron");
		Product p7=new Product("p2", "iron");
		Product p8=new Product("p2", "iron");
		
		
		products = List.of(p1,p2,p3,p4,p5,p6,p7,p8);
		
		
		Map<String,Long> pro= products.stream().collect(Collectors.groupingBy(Product::getName,Collectors.counting()));
		
		Map.Entry<String, Long> val=pro.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow();
		
		System.out.println(val);
		
		
		
	}
	

}
