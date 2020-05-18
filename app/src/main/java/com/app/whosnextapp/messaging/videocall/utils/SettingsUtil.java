package com.app.whosnextapp.messaging.videocall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.whosnextapp.R;
import com.quickblox.videochat.webrtc.QBRTCConfig;

public class SettingsUtil {


    public static void configRTCTimers(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        long answerTimeInterval = getPreferenceInt(sharedPref, context,
                R.string.pref_answer_time_interval_key,
                R.string.pref_answer_time_interval_default_value);
        QBRTCConfig.setAnswerTimeInterval(answerTimeInterval);

        int disconnectTimeInterval = getPreferenceInt(sharedPref, context,
                R.string.pref_disconnect_time_interval_key,
                R.string.pref_disconnect_time_interval_default_value);
        QBRTCConfig.setDisconnectTime(disconnectTimeInterval);

        long dialingTimeInterval = getPreferenceInt(sharedPref, context,
                R.string.pref_dialing_time_interval_key,
                R.string.pref_dialing_time_interval_default_value);
        QBRTCConfig.setDialingTimeInterval(dialingTimeInterval);
    }

    public static int getPreferenceInt(SharedPreferences sharedPref, Context context, int strResKey, int strResDefValue) {
        return sharedPref.getInt(context.getString(strResKey), Integer.valueOf(context.getString(strResDefValue)));
    }


}
