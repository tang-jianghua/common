package com.tangjianghua.common.java8;

import java.util.Arrays;
import java.util.List;

public class Test {

	public static void  printValur(String str){
		System.out.println("print value : "+str);
	}public static void  printValurs(String str){
		System.out.println("print value2 : "+str);
	}
	public static void main(String[] args) {
		List<String> al = Arrays.asList("a","b","c","d");
		for (String a: al) {
			printValur(a);
		}
		//下面的for each循环和上面的循环是等价的
		al.forEach(x->{
			printValurs(x);
		});
	}


}
