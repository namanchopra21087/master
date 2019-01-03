package com.home.naman.maven.jasper_reports;

import java.util.ArrayList;
import java.util.Iterator;

import com.lowagie.text.List;

public class Test {
	
	public static void main(String[] args) {
		java.util.List<Integer> testL=new ArrayList<>();
		testL.add(1);
		testL.add(2);
		testL.add(3);
		testL.add(4);
		testL.add(5);
		for(Integer i:testL){
			if(i==2)
				testL.add(i,1);
		}
		
		
		System.out.print(testL);
		
	}

}
