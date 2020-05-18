package com.app.whosnextapp.messaging.quickblox.configs;

import android.text.TextUtils;

import androidx.multidex.MultiDexApplication;

import com.app.whosnextapp.messaging.quickblox.model.QbConfigs;
import com.app.whosnextapp.utility.Constants;
import com.orhanobut.logger.Logger;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;

public class CoreApp extends MultiDexApplication {
    public static final String TAG = CoreApp.class.getSimpleName();
    private static CoreApp instance;
    private QbConfigs qbConfigs;

    public static synchronized CoreApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initQbConfigs();
        initCredentials();
    }

    private void initQbConfigs() {
        Logger.e(TAG, "QB CONFIG FILE NAME: " + getQbConfigFileName());
        qbConfigs = CoreConfigUtils.getCoreConfigsOrNull(getQbConfigFileName());
    }

    public void initCredentials() {
        if (qbConfigs != null) {
            QBSettings.getInstance().init(getApplicationContext(), qbConfigs.getAppId(), qbConfigs.getAuthKey(), qbConfigs.getAuthSecret());
            QBSettings.getInstance().setAccountKey(qbConfigs.getAccountKey());

            if (!TextUtils.isEmpty(qbConfigs.getApiDomain()) && !TextUtils.isEmpty(qbConfigs.getChatDomain())) {
                QBSettings.getInstance().setEndpoints(qbConfigs.getApiDomain(), qbConfigs.getChatDomain(), ServiceZone.PRODUCTION);
                QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
            }
        }
    }

    public QbConfigs getQbConfigs() {
        return qbConfigs;
    }

    protected String getQbConfigFileName() {
        return Constants.WN_QB_CONFIG_FILE_NAME;
    }
}