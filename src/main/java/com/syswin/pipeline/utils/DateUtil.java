package com.syswin.pipeline.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 115477 on 2018/11/28.
 */
public final class DateUtil {

    /**
     * 获取11位时间戳
     *
     * @return
     */
    public static int getDate() {
        Long time = System.currentTimeMillis() / 1000;
        return time.intValue();
    }


    /**
     * 时间转时间搓
     *
     * @return
     */
    public static int switchDate(String  date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int)(d.getTime()/1000);
    }
}
