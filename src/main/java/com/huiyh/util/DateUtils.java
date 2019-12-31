package com.huiyh.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huiyh on 2016/9/8.
 */

public class DateUtils {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    public static boolean isEmpty(String string){
        return string == null || string.length() == 0;
    }

    public static String getDate() {
        Date d = new Date();
        String dateNowStr = DATE_FORMAT.format(d);
        return dateNowStr;
    }
}
