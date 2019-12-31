package com.huiyh.util;

/**
 * Created by huiyh on 2016/7/29.
 */
public class Log {

    public static final int LEVEL_CLOSE = 0;
    public static final int LEVEL_ERROR = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_DEBUG = 3;

    public static final int LEVEL = LEVEL_ERROR;

    public static void d(String msg){
        if(!hasShow(LEVEL_DEBUG)){
            return;
        }
        System.out.println(msg);
    }

    public static void i(String msg){
        if(!hasShow(LEVEL_INFO)){
            return;
        }
        System.out.println(msg);
    }

    public static void e(String msg){
        if(!hasShow(LEVEL_ERROR)){
            return;
        }
        System.err.println(msg);
    }

    public static void e(Throwable e){
        if(!hasShow(LEVEL_ERROR)){
            return;
        }
        e.printStackTrace();
    }

    public static void e(String msg, Throwable t){
        if(!hasShow(LEVEL_ERROR)){
            return;
        }
        Exception e = new Exception(msg, t);
        e.printStackTrace();
    }

    public static boolean hasShow(int level){
        return level <= LEVEL;
    }
}
