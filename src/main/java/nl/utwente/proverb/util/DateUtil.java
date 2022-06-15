package nl.utwente.proverb.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String getDate(Date date) {
        var sdfTimeFormat = new SimpleDateFormat(DATE_FORMAT);
        return sdfTimeFormat.format(date);
    }

    public static String getTime(Date date) {
        var sdfTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        return sdfTimeFormat.format(date);
    }
}
