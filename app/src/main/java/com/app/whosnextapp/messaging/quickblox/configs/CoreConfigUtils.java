package com.app.whosnextapp.messaging.quickblox.configs;

import android.text.TextUtils;

import com.app.whosnextapp.messaging.quickblox.model.QbConfigs;
import com.google.gson.Gson;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CoreConfigUtils {

    private static final String USER_LOGIN_FIELD_NAME = "user_login";
    private static final String USER_PASSWORD_FIELD_NAME = "user_password";

    private static QbConfigs getCoreConfigs(String fileName) throws IOException {
        ConfigParser configParser = new ConfigParser();
        Gson gson = new Gson();
        return gson.fromJson(configParser.getConfigsAsJsonString(fileName), QbConfigs.class);
    }

    static QbConfigs getCoreConfigsOrNull(String fileName) {
        QbConfigs qbConfigs = null;

        try {
            qbConfigs = getCoreConfigs(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return qbConfigs;
    }

    private static String getStringConfigFromFile(String fileName, String fieldName) throws IOException, JSONException {
        JSONObject appConfigs = new ConfigParser().getConfigsAsJson(fileName);
        return appConfigs.getString(fieldName);
    }

    private static String getStringConfigFromFileOrNull(String fileName, String fieldName) {
        String fieldValue = null;

        try {
            fieldValue = getStringConfigFromFile(fileName, fieldName);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static boolean isStringConfigFromFileNotEmpty(String fileName, String fieldName) {
        return !TextUtils.isEmpty(getStringConfigFromFileOrNull(fileName, fieldName));
    }

    public static QBUser getUserFromConfig(String fileName) {
        QBUser qbUser = null;
        String userLogin;
        String userPassword;
        try {
            JSONObject configs = new ConfigParser().getConfigsAsJson(fileName);
            userLogin = configs.getString(USER_LOGIN_FIELD_NAME);
            userPassword = configs.getString(USER_PASSWORD_FIELD_NAME);
            qbUser = new QBUser(userLogin, userPassword);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return qbUser;
    }
}
