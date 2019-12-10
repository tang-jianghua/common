package com.tangjianghua.common.util;

import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Ttest {
	private String a;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public static void main(String[] args) {
		String property = System.getProperty("sun.boot.class.path");
		String dirs = System.getProperty("java.ext.dirs");
		String path = System.getProperty("java.class.path");
		System.out.println(property);
		System.out.println(dirs);
		String[] split = path.split(";");
		System.out.println("\n\n\n");
		Arrays.stream(split).forEach(str ->System.out.println(str));
		ClassLoader classLoader = Ttest.class.getClassLoader();
		System.out.println(classLoader.toString());
		System.out.println(classLoader.getParent().toString());
//		System.out.println(classLoader.getParent().getParent().toString());
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		System.out.println(contextClassLoader.toString());
		String s = DigestUtils.md5DigestAsHex("appid=wxc9d97d58484d06e6&merchantName=&key=bdfc756d07653cd044f28721df1f0b50".getBytes());
		System.out.println(s);

	}

}
