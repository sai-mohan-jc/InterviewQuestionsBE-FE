package com.learn.mapexample;

import java.util.Objects;

public class Employee {
	
	int id;
	
	String name;

	public Employee(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	
	
	
	
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + "]";
	}






	public int getId() {
		return id;
	}





	public void setId(int id) {
		this.id = id;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	@Override
	public boolean equals(Object o) {
		Employee e = (Employee) o;
		return this.id == e.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	

}
