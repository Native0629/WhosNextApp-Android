package com.app.whosnextapp.messaging.quickblox.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class AssetsUtils {

    public static String getJsonAsString(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        StringBuilder buf = new StringBuilder();
        InputStream json = manager.open(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
        String str;

        while ((str = in.readLine()) != null) {
            buf.append(str);
        }
        in.close();
        return buf.toString();
    }

    public static String toStringFromChatDialog(QBChatDialog params) {
        if (params == null) {
            return null;
        }
        Type mapType = new TypeToken<QBChatDialog>() {
        }.getType();
        Gson gson = new Gson();
        String postData = gson.toJson(params, mapType);
        return postData;
    }

    public static QBChatDialog toChatDialogFromString(String params) {
        if (params == null)
            return null;

        Type mapType = new TypeToken<QBChatDialog>() {
        }.getType();
        Gson gson = new Gson();
        QBChatDialog postData = gson.fromJson(params, mapType);
        return postData;
    }

    public static String toStringFromQBChatMessage(QBChatMessage params) {
        if (params == null) {
            return null;
        }
        Type mapType = new TypeToken<QBChatMessage>() {
        }.getType();
        Gson gson = new Gson();
        String postData = gson.toJson(params, mapType);
        return postData;
    }

    public static QBChatMessage toQBChatMessageFromString(String params) {
        if (params == null)
            return null;

        Type mapType = new TypeToken<QBChatMessage>() {
        }.getType();
        Gson gson = new Gson();
        QBChatMessage postData = gson.fromJson(params, mapType);
        return postData;
    }
}
