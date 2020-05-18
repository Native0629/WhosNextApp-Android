package com.app.whosnextapp.messaging.quickblox.configs;

import android.content.Context;

import com.app.whosnextapp.messaging.quickblox.utils.AssetsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ConfigParser {

    private Context context;

    ConfigParser() {
        context = CoreApp.getInstance().getApplicationContext();
    }

    String getConfigsAsJsonString(String fileName) throws IOException {
        return AssetsUtils.getJsonAsString(fileName, context);
    }

    JSONObject getConfigsAsJson(String fileName) throws IOException, JSONException {
        return new JSONObject(getConfigsAsJsonString(fileName));
    }

    public String getConfigByName(JSONObject jsonObject, String fieldName) throws JSONException {
        return jsonObject.getString(fieldName);
    }
}
