package com.tangjianghua.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

	static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	private DateUtils(){
		logger.warn("公用类，禁止创建对象！");
	}

	public static final String BEGIN_YEAR_SUF="-01-01 00:00:00";

	public static final String END_YEAR_SUF="-12-31 23:59:59";

	public static final String BEGIN_SUF=" 00:00:00";

	public static final String END_SUF=" 23:59:59";

	public static final String FORMAT_DEFAULT="yyyy-MM-dd HH:mm:ss";

	public static final String FORMAT_SSS_NUMBER="yyyyMMddHHmmssSSS";

	public static final String FORMAT_NUMBER="yyyyMMddHHmmss";

	public static final String FORMAT_DEFAULT_DATE="yyyy-MM-dd";

	public static String format(Date date,String format){
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern(format));
	}

	public static Date parse(String time,String format){
		LocalDateTime parse = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format));
		return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static String formatSSSNumberNow(){
		LocalDateTime localDateTime = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern(FORMAT_SSS_NUMBER));
	}

	public static String formatDefault(Date date){
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern(FORMAT_DEFAULT));
	}

	public static Date parseDefault(String time){
		LocalDateTime parse = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(FORMAT_DEFAULT));
		return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
	}
	public static Date parseNumber(String time){
		LocalDateTime parse = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(FORMAT_NUMBER));
		return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
	}
	public static String formatNumber(Date date){
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern(FORMAT_NUMBER));
	}

	public static String formatDefaultDate(Date date){
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.format(DateTimeFormatter.ofPattern(FORMAT_DEFAULT_DATE));
	}

	public static Date parseDefaultDate(String time){
		LocalDateTime parse = LocalDateTime.parse(time+BEGIN_SUF, DateTimeFormatter.ofPattern(FORMAT_DEFAULT));
		return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
	}
	public static Date parseDefaultDateBegin(String time){
		LocalDateTime parse = LocalDateTime.parse(time+BEGIN_SUF, DateTimeFormatter.ofPattern(FORMAT_DEFAULT));
		return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
	}
	public static Date parseDefaultDateEnd(String time){
		LocalDateTime parse = LocalDateTime.parse(time+END_SUF, DateTimeFormatter.ofPattern(FORMAT_DEFAULT));
		return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
	}
	public static Date parseDefaultYearBegin(String year){
		LocalDateTime parse = LocalDateTime.parse(year+BEGIN_YEAR_SUF, DateTimeFormatter.ofPattern(FORMAT_DEFAULT));
		return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
	}
	public static Date parseDefaultYearEnd(String year){
		LocalDateTime parse = LocalDateTime.parse(year+END_YEAR_SUF, DateTimeFormatter.ofPattern(FORMAT_DEFAULT));
		return Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
	}

}
