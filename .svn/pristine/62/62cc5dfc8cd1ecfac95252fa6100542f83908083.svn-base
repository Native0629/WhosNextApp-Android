package com.app.whosnextapp.utility;

import android.annotation.SuppressLint;
import android.content.Context;

import com.app.whosnextapp.R;

public class TimeUtils {
    public static String getTimeDiff(Context context, long diff) {
        String result = "";
        long hours = diff / 3600;
        long minute = diff % 3600 / 60;
        long second = diff % 60;

        if (second == 0) {
            result = context.getString(R.string.msg_now);
        } else if (second > 0 && (hours == 0 && minute == 0)) {
            result = String.format(context.getString(R.string.text_seconds), String.valueOf(second));
        } else if (second > 0 && minute > 0 && hours == 0) {
            if (minute == 1) {
                result = String.format(context.getString(R.string.text_min), String.valueOf(minute));
            } else {
                result = String.format(context.getString(R.string.text_minutes), String.valueOf(minute));
            }
        } else {
            result = String.format(context.getString(R.string.text_hr), String.valueOf(hours));
            if (hours > 24) {
                int innerHour = (int) (hours / 24.0);
                if (innerHour == 1) {
                    result = String.format(context.getString(R.string.text_day), String.valueOf(innerHour));
                } else {
                    result = String.format(context.getString(R.string.text_days), String.valueOf(innerHour));
                }
            } /*else {
                if (hours == 1) {
                    result = String.format(context.getString(R.string.text_hr), String.valueOf(hours));
                } else {
                    result = String.format(context.getString(R.string.text_hrs), String.valueOf(hours));
                }
            }*/
        }
        return result;
    }

    public static String getFormattedLastSeenTiming(Context activity, long seconds) {
        long current_milis = System.currentTimeMillis();
        long timeDifInMilliSec = current_milis - seconds;

        long milisOfsec = 1000;
        long milisOfminute = 60 * milisOfsec;
        long milisOfhour = 60 * milisOfminute;
        long milisOfday = 24 * milisOfhour;
        long milisOfweek = 7 * milisOfday;
        long milisOfmonth = 30 * milisOfday;
        long milisOfyear = 12 * milisOfmonth;

        long timeDifSeconds = timeDifInMilliSec / milisOfsec;
        long timeDifMinutes = timeDifInMilliSec / milisOfminute;
        long timeDifHours = timeDifInMilliSec / milisOfhour;
        long timeDifDays = timeDifInMilliSec / milisOfday;
        long timeDifWeeks = timeDifInMilliSec / milisOfweek;
        long timeDifMonths = timeDifInMilliSec / milisOfmonth;
        long timeDifYears = timeDifInMilliSec / milisOfyear;

        String time;

        if (timeDifYears > 0) {
            if (timeDifYears == 1) {
                time = "1 " + activity.getString(R.string.diff_year);
            } else {
                time = timeDifYears + " " + activity.getString(R.string.diff_years);
            }
        } else if (timeDifMonths > 0) {
            if (timeDifMonths == 1) {
                time = "1 " + activity.getString(R.string.diff_month);
            } else {
                time = timeDifMonths + " " + activity.getString(R.string.diff_months);
            }
        } else if (timeDifWeeks > 0) {
            if (timeDifWeeks == 1) {
                time = "1 " + activity.getString(R.string.diff_week);
            } else {
                time = timeDifWeeks + " " + activity.getString(R.string.diff_weeks);
            }
        } else if (timeDifDays > 0) {
            if (timeDifDays == 1) {
                time = "1 " + activity.getString(R.string.diff_day);
            } else {
                time = timeDifDays + " " + activity.getString(R.string.diff_days);
            }
        } else if (timeDifHours > 0) {
            if (timeDifHours == 1) {
                time = "1 " + activity.getString(R.string.diff_hour);
            } else {
                time = timeDifHours + " " + activity.getString(R.string.diff_hours);
            }
        } else if (timeDifMinutes > 0) {
            if (timeDifMinutes == 1) {
                time = "1 " + activity.getString(R.string.diff_minute);
            } else {
                time = timeDifMinutes + " " + activity.getString(R.string.diff_minutes);
            }
        } else if (timeDifSeconds > 0) {
            if (timeDifSeconds == 1) {
                time = "1 " + activity.getString(R.string.diff_second);
            } else {
                time = timeDifSeconds + " " + activity.getString(R.string.diff_seconds);
            }
        } else {
            return activity.getString(R.string.just_now);
        }
        return String.format(activity.getString(R.string.ago), time);
    }


    @SuppressLint("DefaultLocale")
    public static String milliSecondsToTimer(long totalSecs, boolean isShowHours) {
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;

        if (isShowHours || hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

}
