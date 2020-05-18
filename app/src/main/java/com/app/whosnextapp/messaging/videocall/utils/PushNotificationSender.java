package com.app.whosnextapp.messaging.videocall.utils;

import android.os.Bundle;

import com.app.whosnextapp.R;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;

import java.util.ArrayList;

public class PushNotificationSender {
    public static void sendPushMessage(ArrayList<Integer> recipients, String senderName) {
        String outMessage = String.format(String.valueOf(R.string.text_push_notification_message), senderName);
        QBEvent qbEvent = new QBEvent();
        qbEvent.setNotificationType(QBNotificationType.PUSH);
        qbEvent.setEnvironment(QBEnvironment.DEVELOPMENT);
        qbEvent.setMessage(outMessage);
        StringifyArrayList<Integer> userIds = new StringifyArrayList<>(recipients);
        qbEvent.setUserIds(userIds);
        QBPushNotifications.createEvent(qbEvent).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }
}
