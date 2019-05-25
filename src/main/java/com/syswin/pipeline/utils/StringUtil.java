package com.syswin.pipeline.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author by Administrator on 2017/10/27.
 */
public class StringUtil {

    /**
     * 判断是否存在空数据
     * @param params 多个输入参数
     * @return 校验结果
     */
    public static <T> Boolean isEmpty(T ... params) {
        for (T obj : params) {
            if(StringUtil.isEmpty (obj)) return true;
        }
        return false;
    }


    public static Boolean isHttpUrl(String url){
        if(isEmpty(url)){
            return false;
        }

        if(!url.startsWith("http://")&&!url.startsWith("https://")){
            return false;
        }

        return true;


    }






    /**
     * 是否全部为空
     * @param params
     * @param <T>
     * @return
     */
    public static<T> Boolean isAllEmpty(T ... params){
        for (T obj : params) {
            if(isNotEmpty (obj))return false;

        }
        return true;
    }




    public static Boolean isNotEmpty(Object obj){
        return obj!=null&&!"".equals(obj);
    }

    public static Boolean isNum(Object obj){
        return  isNotEmpty(obj)&& Pattern.compile("[0-9]*").matcher(obj.toString().trim()).matches();
    }
    public static Boolean isEmpty(Object object){
        return object==null||"".equals(object);
    }

    /**
     * 取随机的32位uuid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }



    public static String getUUIDStr(){
        return UUID.randomUUID().toString().replace("-","");
    }




    /**
     * 获取泛型数据的类型
     *
     * @param raw
     * @param types
     * @return
     */
    protected static ParameterizedType getType(final Type raw, final Type... types) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type getOwnerType() {
                return null;
            }

            public Type[] getActualTypeArguments() {
                return types;
            }
        };
    }


    public static String getMethod(String prefix, String field){
        return prefix+field.substring(0, 1).toUpperCase()+field.substring(1);
    }

    public  static String urlEnocde(Object obj){
        try {
            return	URLEncoder.encode(String.valueOf(obj), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static int strToInt(String s) {
        int res = 0;
        try {
            res = Integer.valueOf(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String null2Str(String source) {
        return source == null?"":source.trim();
    }

    public static String getGetMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static String left(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    public static String right(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    public static String byte2Str(byte[]bytes){
        return new String(bytes,Charset.forName("UTF-8"));
    }


}
