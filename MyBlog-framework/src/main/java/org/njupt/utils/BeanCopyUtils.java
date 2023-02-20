package org.njupt.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {
    }

    /**
     * 单个对象拷贝
     * 前面一个v说明是泛型方法,后面一个v说明返回值类型是v
     *
     * @param source 需要被转换的对象（源头）
     * @param clazz  需要转换成的对象类型（目标）
     */
    public static <V> V copyBean(Object source, Class<V> clazz) {
        V result = null;
        try {
            //创建目标对象
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }

    /**
     * 多个对象的拷贝
     *
     * @param source 需要被转换的多个对象（源头）
     * @param clazz  需要转换成的对象类型（目标）
     * @version 1.0
     */
    /*
    public static <V, O> List<V> copyBeanList(List<O> source, Class<V> clazz) {
        List<V> list = new ArrayList<>();
        try {
            for (O obj : source) {
                V v = clazz.newInstance();
                BeanUtils.copyProperties(obj, v);
                list.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }*/

    /**
     * 多个对象的拷贝（采用stream)
     * @version 2.0
     */
    public static <V, O> List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

}
