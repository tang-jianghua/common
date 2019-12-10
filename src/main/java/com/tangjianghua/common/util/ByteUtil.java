package com.tangjianghua.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteUtil {

    Logger logger = LoggerFactory.getLogger(ByteUtil.class);

    private ByteUtil(){
        logger.warn("公用类，禁止创建对象！");
    }

    public static byte hexChar2Byte(char c) {
        if (c>='0' && c<='9') {
            return (byte)(c-'0');
        }
        if (c>='a' && c<='f') {
            return (byte)(c-'a'+10);
        }
        if (c>='A' && c<='F') {
            return (byte)(c-'A'+10);
        }
        return -1;
    }

    /**
     * 获取一个int（范围在0-255）的单个字节
     * @param num
     * @return
     */
    public static byte getByteOfInt(int num){
       for (int i=0;i<4;i++){
           byte tempByte=(byte)(num>>8*i&0xff);
           if(tempByte!=0){
               return tempByte;
           }
       }
        return 0;
    }
}
