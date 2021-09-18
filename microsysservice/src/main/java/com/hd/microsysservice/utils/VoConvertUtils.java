package com.hd.microsysservice.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
public class VoConvertUtils<T1,T2> {
    /**
     * 拷贝对象属性
     *
     * @param from 源对象
     * @param to   目标对象
     */
    public static void copyObjectProperties(Object from, Object to) {
        Assert.notNull(from, "源对象不能为空！");
        Assert.notNull(to, "目标对象不能为空！");
        BeanUtils.copyProperties(from, to);
    }

    public  T2 convertToT2(T1 t1){
        T2 t2=createT2();
        BeanUtils.copyProperties(t1, t2);
        return  t2;
    }
    public  T1 convertToT1(T2 t2){
        T1 t1=createT1();
        BeanUtils.copyProperties(t2, t1);
        return  t1;
    }
    public List<T2> convertToListT2(List<T1> fromList) {
        Assert.notNull(fromList, "源对象不能为空！");
        //Assert.notNull(toList, "目标对象不能为空！");
        List<T2> toList=new ArrayList<>();
        for(T1 t1:fromList){
            T2 t2=createT2();
            BeanUtils.copyProperties(t1, t2);
            toList.add(t2);
        }
        return toList;
    }
    public List<T1> convertToListT1(List<T2> fromList) {
        Assert.notNull(fromList, "源对象不能为空！");
        //Assert.notNull(toList, "目标对象不能为空！");
        List<T1> toList=new ArrayList<>();
        for(T2 t2:fromList){
            T1 t1=createT1();
            BeanUtils.copyProperties(t2, t1);
            toList.add(t1);
        }
        return toList;
    }
    private  Class<T1> clazz1;
    private  Class<T2> clazz2;
    public VoConvertUtils(){
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = null;
        if (superclass instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType) superclass;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            if (typeArray != null && typeArray.length > 0) {
                clazz1 = (Class<T1>) typeArray[0];
                clazz2 = (Class<T2>) typeArray[1];
            }
        }
    }

    public T2 createT2() {
        try {
            return  clazz2.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public T1 createT1() {
        try {
            return  clazz1.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
