package com.in;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.in.fun.Employee;
import com.in.model.Employe;

public class StreamRelated {
	
	//1.List of employees and name ,sal are two properties.
	//Find the sum of the sal for all employees?
	
	public static BigDecimal getAllSal(List<Employe> empList) {
		
		BigDecimal totalSal= empList.stream().map(emp->emp.sal()).reduce(new BigDecimal("0"),(a,b)->a.add(b));
		
		//float totalSal=(float)empList.stream().map(Employe::sal).sum();
		
		String name=empList.stream().map(emp->emp.name()).reduce("", (a, b) ->
                                   a.length() >= b.length() ? a : b);
		System.out.println(name);
		
		return totalSal;
	}
	
	
	public static void findCities() {
		
		List<Employee> list=new ArrayList<>();
		Employee e1=new Employee("SAI",380,"Nellore");
		Employee e2=new Employee("SAIMohan",400,"Hyderabad");
		Employee e3=new Employee("MohanSAI1",420,"Gudur");
		list.add(e1);
		list.add(e2);
		list.add(e3);
		
		List<String> cities=List.of("Nellore","Hyderabad","Guntur");
		
		List<Employee> list1=list.stream().filter(empl->cities.contains(empl.getCity())).toList();
		
		System.out.println(list1);
		
		//list1.stream().map(emp->emp.getSal() + ((emp.getSal() * 10) / 100)).toList();
		
		list1.stream()
	    .filter(emp -> cities.contains(emp.getCity()))
	    .forEach(emp ->
	        emp.setSal(emp.getSal() + ((emp.getSal() * 10) / 100))
	    );
		
		System.out.println(list1);
		
	}
	
	
	public static void main(String[] args) {
		List<Employe> list=new ArrayList<>();
		Employe e1=new Employe("SAI",new BigDecimal("380.2"),"");
		Employe e2=new Employe("SAIMohan",new BigDecimal("400"),"");
		Employe e3=new Employe("MohanSAI1",new BigDecimal("420"),"");
		list.add(e1);
		list.add(e2);
		list.add(e3);
		
		System.out.println(getAllSal(list));
		
		findCities();
		
	}
	
	

}
