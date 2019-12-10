package com.tangjianghua.common.java8.steam;

//import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {

	public static void main(String[] args) {

		List<Model1> list = Arrays.asList(new Model1("one", "1"), new Model1("two", "2"));
		Map<String, List<Model1>> collect = list.parallelStream().collect(Collectors.groupingBy(Model1::getName));
//		System.out.println(JSON.toJSONString(collect));
	}
}
