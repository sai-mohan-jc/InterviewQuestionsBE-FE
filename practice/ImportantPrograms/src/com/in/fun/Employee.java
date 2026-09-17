package com.in.fun;



import java.math.BigDecimal;

public class Employee {
	
	
	
	public Employee(String name, Integer sal, String city) {
		super();
		this.name = name;
		this.sal = sal;
		this.city = city;
	}
	private String name;
	private Integer sal;
	private String city;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSal() {
		return sal;
	}
	public void setSal(Integer sal) {
		this.sal = sal;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", sal=" + sal + ", city=" + city + "]";
	}
	
	
	
	

}
