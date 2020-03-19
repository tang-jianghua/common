package com.tangjianghua.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tangjianghua.common.payload.OrderPayload;

import java.awt.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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


		System.out.println(LocalDate.now().toString().replace("-", ""));
	}



}
