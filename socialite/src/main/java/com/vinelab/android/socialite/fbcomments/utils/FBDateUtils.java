package com.vinelab.android.socialite.fbcomments.utils;

import android.content.res.Resources;

import com.vinelab.android.socialite.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nabil Souk on 7/25/2015.
 *
 * <p>
 *     Class providing utils to format a Facebook date.
 * </p>
 */
public class FBDateUtils {
    static final SimpleDateFormat RELATIVE_DATE_FORMAT;

    public static String getRelativeTimeString(Resources res, long currentTimeMillis, long timestamp) {
        long diff = currentTimeMillis - timestamp;
        if(diff >= 0L) {
            int now1;
            if(diff < 60000L) { // less than 60 sec
                return res.getString(R.string.fb_time_just_now);
            } else if(diff < 3600000L) { // less than 60 min = 60*60
                now1 = (int)(diff / 60000L);
                return res.getQuantityString(R.plurals.fb_time_mins, now1, new Object[]{Integer.valueOf(now1)});
            } else if(diff < 86400000L) { // less than 24 hours = 24*60*60
                now1 = (int) (diff / 3600000L);
                return res.getQuantityString(R.plurals.fb_time_hours, now1, new Object[]{Integer.valueOf(now1)});
            } else { // check if same year or not
                return getDateString(res, currentTimeMillis, timestamp);
            }
        } else { // return default date
            RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.fb_time_default));
            return RELATIVE_DATE_FORMAT.format(new Date(timestamp));
        }
    }

    public static String getDateString(Resources res, long currentTimeMillis, long timestamp) {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTimeMillis);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        Date d = new Date(timestamp);
        if(now.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
            RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.fb_time_same_year));
        } else {
            RELATIVE_DATE_FORMAT.applyPattern(res.getString(R.string.fb_time_another_year));
        }

        return RELATIVE_DATE_FORMAT.format(d);
    }

    static {
        RELATIVE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
    }
}
