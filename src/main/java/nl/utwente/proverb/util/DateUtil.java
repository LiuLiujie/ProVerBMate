package nl.utwente.proverb.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final String TIME_FORMAT = "dd MMM yyyy HH:mm:ss";

    private static final String DATE_FORMAT = "dd MMM yyyy";

    public static String getDate(Date date) {
        var sdfTimeFormat = new SimpleDateFormat(DATE_FORMAT);
        return sdfTimeFormat.format(date);
    }

    public static String getTime(Date date) {
        var sdfTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        return sdfTimeFormat.format(date);
    }

    /**
     * Format date string info date type
     *
     * @param date String in yyyy-MM-dd format
     * @return Date type in yyyy-MM-dd
     */
    public static Date formatDate(String date) {
        var sdfDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdfDateFormat.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
