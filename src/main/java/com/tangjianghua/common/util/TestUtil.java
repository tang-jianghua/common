package com.tangjianghua.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tangjianghua.common.payload.OrderPayload;

import java.awt.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtil {


	public static void main(String[] args) throws  Exception{

/*
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date parse = format.parse("2019/01/09hjkg");
		System.out.println(format.format(parse));

		Date parse1 = DateUtils.parse("2019/01/09hjkg", "yyyy/MM/dd");
		System.out.println(DateUtils.format(parse1, "yyyy/MM/dd"));
*/


		String s = "{\n" +
				"\t\"type\": \"object\",\n" +
				"\t\"title\": \"empty object\",\n" +
				"\t\"properties\": {\n" +
				"\t\t\"platCode\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"平台编号\"\n" +
				"\t\t},\n" +
				"\t\t\"mchntNo\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"用户商户号\"\n" +
				"\t\t},\n" +
				"\t\t\"mchntName\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"商户名称\"\n" +
				"\t\t},\n" +
				"\t\t\"orderNo\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"交易单号\"\n" +
				"\t\t},\n" +
				"\t\t\"amoutMin\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"金额大于等于 单位分\"\n" +
				"\t\t},\n" +
				"\t\t\"amoutMax\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"金额小于 单位分\"\n" +
				"\t\t},\n" +
				"\t\t\"paySt\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"订单状态\"\n" +
				"\t\t},\n" +
				"\t\t\"createdBegin\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"创建时间\"\n" +
				"\t\t},\n" +
				"\t\t\"createdEnd\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"创建结束时间\"\n" +
				"\t\t},\n" +
				"\t\t\"timePaidBegin\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"支付时间\"\n" +
				"\t\t},\n" +
				"\t\t\"timePaidEnd\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"支付结束时间\"\n" +
				"\t\t},\n" +
				"\t\t\"appid\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"行业应用\"\n" +
				"\t\t},\n" +
				"\t\t\"productCode\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"支付方式\"\n" +
				"\t\t},\n" +
				"\t\t\"pageNum\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"页数\"\n" +
				"\t\t},\n" +
				"\t\t\"pageSize\": {\n" +
				"\t\t\t\"type\": \"string\",\n" +
				"\t\t\t\"description\": \"每页条数\"\n" +
				"\t\t}\n" +
				"\t},\n" +
				"\t\"required\": [\"pageSize\", \"pageNum\"]\n" +
				"}";

		JSONObject jsonObject1 = new JSONObject();
		JSONObject jsonObject = JSONObject.parseObject(s);
		JSONObject json = jsonObject.getJSONObject("properties");
		json.keySet().parallelStream().forEach(key ->{
			String description = json.getJSONObject(key).getString("description");
			jsonObject1.put(key,description);
		});
		System.out.println(jsonObject1);
	}



}
