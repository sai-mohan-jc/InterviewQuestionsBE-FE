package com.sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.in.fun.Employee;
import com.in.model.Employe;

public class SortingTechnique {
	
	public static void main(String[] args) {
		
		
		List<Integer> numbers=List.of(40,32,12,89,23);
		
		List<Integer> res=numbers.stream().sorted().toList();
		
		System.out.println(res);
		
		System.out.println(numbers.stream().sorted(Comparator.reverseOrder()).toList());
		
		List<String> names=List.of("SAI","MOHAN","AJAY","KUMAR");
		
		List<String> namesAce=names.stream().sorted().toList();
		System.out.println(namesAce);
		List<String> namesDes=names.stream().sorted(Comparator.reverseOrder()).toList();
	    System.out.println(namesDes);
	    List<String> nameslength= names.stream().sorted(Comparator.comparingInt(String::length)).toList();
	    Optional<String> namesecon= nameslength.stream().skip(1).limit(1).findFirst();
	    System.out.println(nameslength);
	    
	    System.out.println(namesecon);
	    
	    
	    List<Employee> list=new ArrayList<>();
		Employee e1=new Employee("SAI",380,"Nellore");
		Employee e2=new Employee("SAIMohan",400,"Hyderabad");
		Employee e3=new Employee("MohanSAI1",420,"Gudur");
		list.add(e1);
		list.add(e2);
		list.add(e3);
		
		List<Employee> listAsc = list.stream().sorted(Comparator.comparing(emp -> emp.getSal())).toList();

		System.out.println(listAsc);

		List<Employee> listDesc = list.stream().sorted(Comparator.comparing(Employee::getSal).reversed()).toList();

		System.out.println(listDesc);
		
	    
	    
	}

}
