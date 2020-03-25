package com.tangjianghua.common.java.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author tangjianghua
 * date 2020/3/24
 * time 14:21
 */
public class ParameterizedTypeTest {
    Map<String,String> map1;
    Map<String,Map<String,Object>> map2;
    Map<String,? extends Collection> map3;
    HashMap.Entry<String,?> entry;

    public static void main(String[] args) {
        Field[] fields = ParameterizedTypeTest.class.getDeclaredFields();
        for(Field f:fields){
            //是否是ParameterizedType
            System.out.println(f.getName()+"是否是ParameterizedType:"+(f.getGenericType() instanceof ParameterizedType));
            ParameterizedType genericType = (ParameterizedType) f.getGenericType();
            Type[] actualTypeArguments = genericType.getActualTypeArguments();
            System.out.println("getActualTypeArguments:"+ Arrays.toString(actualTypeArguments));
            Type rawType = genericType.getRawType();
            System.out.println("getRawType:"+ rawType);
            Type ownerType = genericType.getOwnerType();
            System.out.println("getOwnerType:"+ ownerType+"\n");
        }
    }
}
