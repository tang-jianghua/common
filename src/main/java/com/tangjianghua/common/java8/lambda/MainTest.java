package com.tangjianghua.common.java8.lambda;

public class MainTest {
	public static void main(String[] args) {
		Test addition  = (a,b) ->  a+b;
		int operation = addition.operation(1, 2);
		System.out.println(operation);
	}
}
