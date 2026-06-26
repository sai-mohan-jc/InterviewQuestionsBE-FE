package com.learn.mapexample;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HashMapExample {
	
	public static void main(String[] args) {
		Employee e1=new Employee(100, "SAI");
		Employee e2=new Employee(100, "SAI");
		
		Map<Employee, String> map=new HashMap<>(10000);
		map.put(e1, "Creator");
		map.put(e2, "Creator");
		
		System.out.println(map.size());
		System.out.println(e1.hashCode());
		System.out.println(e2.hashCode());
		
		Stream.generate(() -> "Hello")
	      .limit(3)
	      .forEach(System.out::println);
		
		
		Integer list[]= {1,2,3,4,5,6,7,9};
		
		List<Integer> numList=Arrays.asList(list);
		
		//Function map(Funtion)
		numList.stream().map(a->a*a).collect(Collectors.toList());
		//Predicate filter(Predicate)
		numList.stream().filter(a->a%2==0).collect(Collectors.toList());
		//Consumer
		numList.stream().forEach(System.out::println);
		//Supplier generate(Supplier)
		Stream.generate(()->"SAI").limit(3).forEach(System.out::println);
		//display value incrementally
		AtomicInteger count=new AtomicInteger(1);
		Stream.generate(count::getAndIncrement).limit(3).forEach(System.out::println);
		//BinaryOperator
		numList.stream().reduce((a,b)->a+b);
		//Comparator
		List<Integer> ss=numList.stream().sorted((a,b)->a+b).toList();
		System.out.println("te "+ss);
		//UnaryOperator
		numList.replaceAll(x->x*2);
		System.out.println(numList);
		//Stream.iterate("n", val->val+"n").limit(6).forEach(System.out::println);
		
		List<Integer> listnum=  Stream.iterate(1, a->a+1).limit(5).toList();
		System.out.println(listnum);
		
		
	}

}
