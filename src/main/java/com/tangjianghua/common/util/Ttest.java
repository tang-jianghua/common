package com.tangjianghua.common.util;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Ttest {

    private static InstanceBean instanceBean;

    public synchronized static InstanceBean getInstanceBean() {
        if (instanceBean == null) {
            instanceBean = new InstanceBean();
        }
        return instanceBean;
    }

    static class InstanceBean {
        void sou() {
            System.out.println("run------------");
            try {
                Thread.currentThread().sleep(10000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("end------------");
        }
    }

    private String a;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }


    public static void main(String[] args) {
		/*Map<String,Object> map = new HashMap<>();
		map.put("key", "asf");
		map.put("saf", "asf");
		map.put("xz", "asf");
		map.put("et", "asf");
		map.put("sdfg", "asf");
		map.put("xfgvd", "asf");
		map.put("adfasd", "asf");
		map.put("123", "asf");
		map.put("32", null);
		map.put("234234", "");
		long t1 = System.currentTimeMillis();
		TreeMap<String, Object> tem = new TreeMap<>(map);
		Iterator<Map.Entry<String, Object>> iterator = tem.entrySet().iterator();
		StringBuffer source = new StringBuffer();
		String paramSplit="&";
		while (iterator.hasNext()){
			Map.Entry<String, Object> next = iterator.next();
			source.append(next.toString()).append(paramSplit);
		}
		long t2 = System.currentTimeMillis();
		System.out.println(source.toString());
		System.out.println(t2-t1);
		String hex = hex(map);
		long t3 = System.currentTimeMillis();
		System.out.println(hex);
		System.out.println(t3-t2);*/
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).forEach(x ->
                executorService.submit(() ->
                        Ttest.getInstanceBean().sou()
                )
        );
        executorService.shutdown();


    }

    public static String hex(Map<String, Object> map) {
        String[] strs = new String[map.size()];
        map.keySet().toArray(strs);
        Arrays.sort(strs);
        StringBuffer source = new StringBuffer();
        for (String str : strs) {
            source.append(str + "=" + map.get(str) + "&");
        }
        String bigstr = source.substring(0, source.length() - 1);
        return bigstr;
    }

}
