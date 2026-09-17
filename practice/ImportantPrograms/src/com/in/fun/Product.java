package com.in.fun;

public class Product {
	
	
	
	public Product(String name, String poductType) {
		super();
		this.name = name;
		this.poductType = poductType;
	}
	String name;
	String poductType;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoductType() {
		return poductType;
	}
	public void setPoductType(String poductType) {
		this.poductType = poductType;
	}
	@Override
	public String toString() {
		return "Product [name=" + name + ", poductType=" + poductType + "]";
	}
	
	
	
	

}
