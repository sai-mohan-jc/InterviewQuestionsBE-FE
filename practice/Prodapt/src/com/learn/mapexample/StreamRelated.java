package com.learn.mapexample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamRelated {
	
	public static void main(String[] args) {
		List<String> names = Arrays.asList("SAI");
		
		Employee e1=new Employee(1, "SAI");
		Employee e2=new Employee(2, "SAIMohan");

		List<Employee> employees = new ArrayList<>();
		employees.add(e1);
		employees.add(e2);
		
		List<Employee> fil=employees.stream().filter(e->names.contains(e.getName())).collect(Collectors.toList());
		System.out.println(fil);
		fil.stream().forEach(a->a.setName(a.getName()+"S"));
		System.out.println(fil);

	}

}
