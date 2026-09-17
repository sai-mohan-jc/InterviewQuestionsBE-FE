package com.epam;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ShiftZeroToRight {
	
	public static void main(String[] args) {
		
		int arr[]= {0, 4, 0, 7, 2, 0, 9};
		
		int shiftedValue=0;
		
		
		
		int j = 0;

		for (int i = 0; i < arr.length; i++) {
		    if (arr[i] != 0) {
		    	System.out.println(arr[i]+i+" "+j);
		        int temp = arr[i];
		        arr[i] = arr[j];
		        arr[j] = temp;
		        j++;
		    }else {
		    	shiftedValue++;
		    }
		}
		System.out.println(shiftedValue);
		System.out.println(Arrays.toString(arr));
		
		String s="I am getting understand";
		
		String a[]=s.split(" ");
		
		System.out.println(Arrays.toString(a));
		
		List<String> list=List.of(a);
		
		Optional<String>  ss=list.stream().sorted(Comparator.comparingInt(String::length).reversed()).toList().stream().skip(1).findFirst();
		
		System.out.println(ss);
		
		
			
		}
		
		
	

}
