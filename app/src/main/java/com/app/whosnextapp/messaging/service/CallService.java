package com.app.whosnextapp.messaging.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.app.whosnextapp.utility.ChatPingAlarmManager;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.WebRtcSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSignaling;
import com.quickblox.chat.QBWebRTCSignaling;
import com.quickblox.chat.connections.tcp.QBTcpChatConnectionFabric;
import com.quickblox.chat.connections.tcp.QBTcpConfigurationBuilder;
import com.quickblox.chat.listeners.QBVideoChatSignalingManagerListener;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;

import org.jivesoftware.smackx.ping.PingFailedListener;


public class CallService extends Service {
    private QBChatService chatService;
    private QBRTCClient rtcClient;
    //    private PendingIntent pendingIntent;
    private int currentCommand;
    private QBUser currentUser;

    public static void start(Context context, QBUser qbUser, PendingIntent pendingIntent) {
        Intent intent = new Intent(context, CallService.class);

        intent.putExtra(Constants.EXTRA_COMMAND_TO_SERVICE, Constants.COMMAND_LOGIN);
        intent.putExtra(Constants.EXTRA_QB_USER, qbUser);
        intent.putExtra(Constants.EXTRA_PENDING_INTENT, pendingIntent);

        context.startService(intent);
    }

    public static void start(Context context, QBUser qbUser) {
        start(context, qbUser, null);
    }

    public static void logout(Context context) {
        Intent intent = new Intent(context, CallService.class);
        intent.putExtra(Constants.EXTRA_COMMAND_TO_SERVICE, Constants.COMMAND_LOGOUT);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createChatService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parseIntentExtras(intent);
        startSuitableActions();
        return START_REDELIVER_INTENT;
    }

    private void parseIntentExtras(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            currentCommand = intent.getIntExtra(Constants.EXTRA_COMMAND_TO_SERVICE, Constants.COMMAND_NOT_FOUND);
//            pendingIntent = intent.getParcelableExtra(Constants.EXTRA_PENDING_INTENT);
            currentUser = (QBUser) intent.getSerializableExtra(Constants.EXTRA_QB_USER);
        }
    }

    private void startSuitableActions() {
        if (currentCommand == Constants.COMMAND_LOGIN) {
            startLoginToChat();
        } else if (currentCommand == Constants.COMMAND_LOGOUT) {
            logout();
        }
    }

    private void createChatService() {
        if (chatService == null) {
            QBTcpConfigurationBuilder configurationBuilder = new QBTcpConfigurationBuilder();
            configurationBuilder.setSocketTimeout(0);
            QBChatService.setConnectionFabric(new QBTcpChatConnectionFabric(configurationBuilder));

            QBChatService.setDebugEnabled(true);
            chatService = QBChatService.getInstance();

        }
    }

    private void startLoginToChat() {
        if (!chatService.isLoggedIn()) {
            loginToChat(currentUser);
        } else {
            startActionsOnSuccessLogin();
        }
    }

    private void loginToChat(QBUser qbUser) {
        chatService.login(qbUser, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                startActionsOnSuccessLogin();
            }

            @Override
            public void onError(QBResponseException e) {
                sendResultToActivity(false, e.getMessage() != null
                        ? e.getMessage()
                        : "Login error");
            }
        });
    }

    private void startActionsOnSuccessLogin() {
        initPingListener();
        initQBRTCClient();
        sendResultToActivity(true, null);
    }

    private void initPingListener() {
        ChatPingAlarmManager.onCreate(this);
        ChatPingAlarmManager.getInstanceFor().addPingListener(new PingFailedListener() {
            @Override
            public void pingFailed() {
            }
        });
    }

    private void initQBRTCClient() {
        rtcClient = QBRTCClient.getInstance(getApplicationContext());
        chatService.getVideoChatWebRTCSignalingManager().addSignalingManagerListener(new QBVideoChatSignalingManagerListener() {
            @Override
            public void signalingCreated(QBSignaling qbSignaling, boolean createdLocally) {
                if (!createdLocally) {
                    rtcClient.addSignaling((QBWebRTCSignaling) qbSignaling);
                }
            }
        });
        QBRTCConfig.setDebugEnabled(true);
        rtcClient.addSessionCallbacksListener(WebRtcSessionManager.getInstance(this));
        rtcClient.prepareToProcessCalls();
    }

    private void sendResultToActivity(boolean isSuccess, String errorMessage) {

    }

    private void logout() {
        destroyRtcClientAndChat();
    }

    private void destroyRtcClientAndChat() {
        if (rtcClient != null) {
            rtcClient.destroy();
        }
        ChatPingAlarmManager.onDestroy();
        if (chatService != null) {
            chatService.logout(new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    chatService.destroy();
                }

                @Override
                public void onError(QBResponseException e) {
                    chatService.destroy();
                }
            });
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        destroyRtcClientAndChat();
    }
}