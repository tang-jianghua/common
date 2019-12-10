package com.tangjianghua.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
    Logger logger = LoggerFactory.getLogger(StringUtil.class);

    private StringUtil(){
        logger.warn("公用类，禁止创建对象！");
    }

    public static String getLeftAbsWidthStr(String str,byte width){
        if(str==null){
            str="";
        }
        return String.format("%-"+width+"s\t",str);
    }

    public static String getRightAbsWidthStr(String str,byte width){
        if(str==null){
            str="";
        }
        return String.format("%"+width+"s",str);
    }

    public static String formatString(String str, int length, String slot){
        StringBuilder sb = new StringBuilder();
        sb.append(str);

        int count = length - str.length();

        while(count > 0){
            sb.append(slot);
            count --;
        }

        return sb.toString();
    }
}
