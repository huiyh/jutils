package com.huiyh.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassUtils {

    /**
     * @param className 根据class名称,需要转换为entryName
     *                  转换方法是将"."替换为"/",并在最后添加".class"
     * @return entryName
     */
    public static String getClassEntryName(String className) {
        String entryName = className.replace(".", "/") + ".class";
        return entryName;
    }

    public static boolean isAbstract(Class aclass) {
        boolean isAbstract = Modifier.isAbstract(aclass.getModifiers());
        return isAbstract;
    }

    public static boolean isAbstract(Method method) {
        boolean isAbstract = Modifier.isAbstract(method.getModifiers());
        return isAbstract;
    }
}
