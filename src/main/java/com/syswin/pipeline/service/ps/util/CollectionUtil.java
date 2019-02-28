package com.syswin.pipeline.service.ps.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author  by Administrator on 2017/10/22.
 */
public class CollectionUtil {
    public static <K,V> Map<K,V> fastMap(List<K> keys, List<V> values){
        Map<K,V> map=new HashMap<>();
        for(int i=0;i<keys.size();i++){
            map.put(keys.get(i),values.get(i));
        }
        return map;
    }

    /**
     * 快速构建list
     * @param e 参数
     * @param <E> 模板类
     * @return list
     */
    public static <E> List<E> fastList(E...e){
        List<E> list=new ArrayList<>();

        for(E e1:e){
            if(e1!=null){
                list.add(e1);
            }

        }
        return list;
    }


    public static <K,V> Map<K,V> fastMap(K key, V v){
        Map<K,V> map=new HashMap<>();
        map.put(key,v);
        return map;
    }


    public static  Map<String,Object> fastMap(String key, Object v, String key2, Object v2){
        Map<String,Object> map=new HashMap<>();
        map.put(key,v);
        map.put(key2,v2);
        return map;
    }


    public static  Map<String,String> fastMap(String key, String v, String key2, String v2){
        Map<String,String> map=new HashMap<>();
        map.put(key,v);
        map.put(key2,v2);
        return map;
    }



    public static <K,V> Map<K,V> fastMap(K key, V v, K key2, V v2, K key3, V v3){
        Map<K,V> map=new HashMap<>();
        map.put(key,v);
        map.put(key2,v2);
        map.put(key3,v3);
        return map;
    }





    public static <E> Boolean isEmpty(Collection<E> collection){
      return collection==null||collection.isEmpty();
    }


    public static <K,V> Boolean isEmpty(Map<K,V> map){
        return map==null||map.isEmpty();
    }


    /**
     * change bean
     * @param bean bean对象
     * @return
     */
    public static Map<String,Object> changeBeanToMap(Object bean){
        Map<String,Object> params=new HashMap<>();
        Class<?> beanClass=bean.getClass();
        for(Field field:beanClass.getDeclaredFields()){
            String stringLetter = field.getName().substring(0, 1).toUpperCase();
            String getterName = "get" + stringLetter + field.getName().substring(1);
            try {
                Method method= beanClass.getMethod(getterName,null);
                params.put(field.getName(),method.invoke(bean,null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }


    public static Map<String, String> changeBean2MapString(Object paramInut) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtil.isEmpty(paramInut)) {
            return map;
        }
        Class<?> cls = paramInut.getClass();

        try {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                Method getMethod = cls.getDeclaredMethod(StringUtil.getGetMethodName(field.getName()));
                Object value = getMethod.invoke(paramInut);
                if (StringUtil.isEmpty(value)) {
                    continue;
                }
                map.put(field.getName(), String.valueOf(value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
