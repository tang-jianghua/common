package com.tangjianghua.common.java8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Test8 {
	public static void  printValur(String str){
		System.out.println("print value : "+str);
	}

	public static void main(String[] args) throws Exception{
	/*	List<String> al = Arrays.asList("a", "b", "c", "d");
		al.forEach(x ->printValur(x));
//		Test88 test88 = new Test88();
//		al.forEach(test88::printValur);
		//下面的方法和上面等价的
		Consumer<String> methodParam = Test8::printValur; //方法参数
		al.forEach(x -> methodParam.accept(x));//方法执行accept*/

	String str = "进口量将sdfsdfasfd";
		int length = str.length();
		System.out.println(length);
		BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
		char[] c = new char[length];
		System.out.println(String.copyValueOf(c));
		System.out.println(bufferedReader.read(c,2,length-2));
		System.out.println(String.copyValueOf(c));
		System.out.println(new String[]{null,null}.length);
	}
}
