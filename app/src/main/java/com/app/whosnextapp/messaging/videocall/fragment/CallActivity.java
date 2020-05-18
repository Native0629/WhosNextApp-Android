package com.app.whosnextapp.messaging.videocall.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.app.whosnextapp.R;
import com.app.whosnextapp.apis.ConnectionDetector;
import com.app.whosnextapp.messaging.PermissionsActivity;
import com.app.whosnextapp.messaging.videocall.interfaces.ConversationFragmentCallbackListener;
import com.app.whosnextapp.messaging.videocall.interfaces.IncomeCallFragmentCallbackListener;
import com.app.whosnextapp.messaging.videocall.utils.FragmentExecuotr;
import com.app.whosnextapp.messaging.videocall.utils.RingtonePlayer;
import com.app.whosnextapp.messaging.videocall.utils.SettingsUtil;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.PermissionsChecker;
import com.app.whosnextapp.utility.WebRtcSessionManager;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.AppRTCAudioManager;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBRTCCameraVideoCapturer;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCConfig;
import com.quickblox.videochat.webrtc.QBRTCScreenCapturer;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.QBSignalingSpec;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSignalingCallback;
import com.quickblox.videochat.webrtc.exception.QBRTCSignalException;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.webrtc.CameraVideoCapturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CallActivity extends BaseAppCompatActivity implements QBRTCClientSessionCallbacks, QBRTCSessionStateCallback<QBRTCSession>, QBRTCSignalingCallback, ConnectionDetector.OnConnectivityChangedListener,
        QBRTCSessionEventsCallback, IncomeCallFragmentCallbackListener, ConversationFragmentCallbackListener, CameraVideoCapturer.CameraEventsHandler {

    public static final String INCOME_CALL_FRAGMENT = "income_call_fragment";
    private WebRtcSessionManager sessionManager;
    private QBRTCSession currentSession;
    private boolean callStarted;
    private Runnable showIncomingCallWindowTask;
    private Handler showIncomingCallWindowTaskHandler;
    private boolean isInComingCall;
    private QBRTCClient rtcClient;
    private ConnectionListener connectionListener;
    private AppRTCAudioManager audioManager;
    private ArrayList<CurrentCallStateCallback> currentCallStateCallbackList = new ArrayList<>();
    private boolean isVideoCall;
    private long expirationReconnectionTime;
    private int reconnectHangUpTimeMillis;
    private PermissionsChecker checker;
    private RingtonePlayer ringtonePlayer;


    public static void start(Context context,
                             boolean isIncomingCall) {

        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(Constants.EXTRA_IS_INCOMING_CALL, isIncomingCall);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        init();
    }


    public void init() {
        parseIntentExtras();
        sessionManager = WebRtcSessionManager.getInstance(this);
        if (!currentSessionExist()) {
            finish();
            return;
        }
        ringtonePlayer = new RingtonePlayer(this, R.raw.beep);
        initCurrentSession(currentSession);
        initQBRTCClient();
        initAudioManager();
        checker = new PermissionsChecker(getApplicationContext());

        if (!isInComingCall) {
            ringtonePlayer.play(true);
        }
        startSuitableFragment(isInComingCall);
    }

    private boolean currentSessionExist() {
        currentSession = sessionManager.getCurrentSession();
        return currentSession != null;
    }

    public void initCurrentSession(QBRTCSession session) {
        if (session != null) {
            this.currentSession = session;
            this.currentSession.addSessionCallbacksListener(CallActivity.this);
            this.currentSession.addSignalingCallback(CallActivity.this);
        }
    }

    private void initQBRTCClient() {
        rtcClient = QBRTCClient.getInstance(this);
        rtcClient.setCameraErrorHandler(this);
        QBRTCConfig.setMaxOpponentsCount(Constants.MAX_OPPONENTS_COUNT);
        SettingsUtil.configRTCTimers(CallActivity.this);
        QBRTCConfig.setDebugEnabled(true);
        rtcClient.addSessionCallbacksListener(this);
        rtcClient.prepareToProcessCalls();
        connectionListener = new ConnectionListener();
        QBChatService.getInstance().addConnectionListener(connectionListener);
    }


    private void parseIntentExtras() {
        isInComingCall = getIntent().getExtras().getBoolean(Constants.EXTRA_IS_INCOMING_CALL);
    }

    private void initAudioManager() {
        audioManager = AppRTCAudioManager.create(this);

        isVideoCall = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(currentSession.getConferenceType());
        if (isVideoCall) {
            audioManager.setDefaultAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
        } else {
            audioManager.setDefaultAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
            audioManager.setManageSpeakerPhoneByProximity(false);
        }

        audioManager.setOnWiredHeadsetStateListener((plugged, hasMicrophone) -> {
            if (callStarted) {
            }
        });

        audioManager.setBluetoothAudioDeviceStateListener(connected -> {
            if (callStarted) {
            }
        });
    }

    private void startSuitableFragment(boolean isInComingCall) {
        if (isInComingCall) {
            initIncomingCallTask();
            addIncomeCallFragment();
            checkPermission();

        } else {
            addConversationFragment(isInComingCall);
        }
    }

    private void checkPermission() {
        if (checker.lacksPermissions(Constants.PERMISSIONS)) {
            startPermissionsActivity(!isVideoCall);
        }
    }

    private void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Constants.PERMISSIONS);
    }

    private void addConversationFragment(boolean isIncomingCall) {
        BaseConversationFragment conversationFragment = BaseConversationFragment.newInstance(
                isVideoCall
                        ? new VideoConversationFragment()
                        : new VideoConversationFragment(),
                isIncomingCall);
        FragmentExecuotr.addFragment(getSupportFragmentManager(), R.id.fragment_container, conversationFragment, conversationFragment.getClass().getSimpleName());
    }

    private void initIncomingCallTask() {
        showIncomingCallWindowTaskHandler = new Handler(Looper.myLooper());
        showIncomingCallWindowTask = new Runnable() {
            @Override
            public void run() {
                if (currentSession == null) {
                    return;
                }

                QBRTCSession.QBRTCSessionState currentSessionState = currentSession.getState();
                if (QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_NEW.equals(currentSessionState)) {
                    rejectCurrentSession();
                } else {
                    ringtonePlayer.stop();
                    hangUpCurrentSession();
                }
            }
        };
    }

    public void rejectCurrentSession() {
        if (getCurrentSession() != null) {
            getCurrentSession().rejectCall(new HashMap<String, String>());
        }
    }

    private void addIncomeCallFragment() {

        if (currentSession != null) {
            IncomeCallFragment fragment = new IncomeCallFragment();
            FragmentExecuotr.addFragment(getSupportFragmentManager(), R.id.fragment_container, fragment, INCOME_CALL_FRAGMENT);
        }
    }

    public void hangUpCurrentSession() {
        ringtonePlayer.stop();
        if (getCurrentSession() != null) {
            getCurrentSession().hangUp(new HashMap<String, String>());
        }
    }

    @Override
    public void addTCClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks) {
        if (currentSession != null) {
            currentSession.addSessionCallbacksListener(clientConnectionCallbacks);
        }
    }

    @Override
    public void removeRTCClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks) {
        if (currentSession != null) {
            currentSession.removeSessionCallbacksListener(clientConnectionCallbacks);
        }
    }

    @Override
    public void addRTCSessionEventsCallback(QBRTCSessionEventsCallback eventsCallback) {
        QBRTCClient.getInstance(this).addSessionCallbacksListener(eventsCallback);
    }

    @Override
    public void removeRTCSessionEventsCallback(QBRTCSessionEventsCallback eventsCallback) {
        QBRTCClient.getInstance(this).removeSessionsCallbacksListener(eventsCallback);
    }

    @Override
    public void addCurrentCallStateCallback(CurrentCallStateCallback currentCallStateCallback) {
        currentCallStateCallbackList.add(currentCallStateCallback);
    }

    @Override
    public void removeCurrentCallStateCallback(CurrentCallStateCallback currentCallStateCallback) {
        currentCallStateCallbackList.remove(currentCallStateCallback);
    }

    @Override
    public void onSetAudioEnabled(boolean isAudioEnabled) {
        setAudioEnabled(isAudioEnabled);
    }

    private void setAudioEnabled(boolean isAudioEnabled) {
        if (currentSession != null && currentSession.getMediaStreamManager() != null) {
            currentSession.getMediaStreamManager().getLocalAudioTrack().setEnabled(isAudioEnabled);
        }
    }

    @Override
    public void onSetVideoEnabled(boolean isNeedEnableCam) {
        setVideoEnabled(isNeedEnableCam);
    }


    private void setVideoEnabled(boolean isVideoEnabled) {
        if (currentSession != null && currentSession.getMediaStreamManager() != null) {
            currentSession.getMediaStreamManager().getLocalVideoTrack().setEnabled(isVideoEnabled);
        }
    }

    @Override
    public void onHangUpCurrentSession() {
        hangUpCurrentSession();
    }

    @Override
    public void onStartScreenSharing() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        QBRTCScreenCapturer.requestPermissions(CallActivity.this);
    }

    @Override
    public void onSwitchCamera(CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler) {
        ((QBRTCCameraVideoCapturer) (currentSession.getMediaStreamManager().getVideoCapturer()))
                .switchCamera(cameraSwitchHandler);
    }

    @Override
    public void onAcceptCurrentSession() {
        if (currentSession != null) {
            addConversationFragment(true);
        }
    }

    @Override
    public void onRejectCurrentSession() {
        rejectCurrentSession();
    }

    @Override
    public void connectivityChanged(boolean availableNow) {

    }

    @Override
    public void onReceiveNewSession(QBRTCSession qbrtcSession) {
        if (getCurrentSession() != null) {
            qbrtcSession.rejectCall(null);
        }
    }

    @Override
    public void onUserNoActions(QBRTCSession qbrtcSession, Integer integer) {
        startIncomeCallTimer(0);
    }

    private void startIncomeCallTimer(long time) {
        showIncomingCallWindowTaskHandler.postAtTime(showIncomingCallWindowTask, SystemClock.uptimeMillis() + time);
    }

    @Override
    public void onSessionStartClose(QBRTCSession qbrtcSession) {
        if (qbrtcSession.equals(getCurrentSession())) {
            qbrtcSession.removeSessionCallbacksListener(CallActivity.this);
            notifyCallStateListenersCallStopped();
        }
    }

    @Override
    public void onStateChanged(QBRTCSession qbrtcSession, BaseSession.QBRTCSessionState qbrtcSessionState) {

    }

    @Override
    public void onConnectedToUser(QBRTCSession qbrtcSession, Integer integer) {
        callStarted = true;
        notifyCallStateListenersCallStarted();
        if (isInComingCall) {
            stopIncomeCallTimer();
        }
    }

    @Override
    public void onDisconnectedFromUser(QBRTCSession qbrtcSession, Integer integer) {

    }

    @Override
    public void onConnectionClosedForUser(QBRTCSession qbrtcSession, Integer integer) {
    }

    @Override
    public void onCameraError(String s) {

    }

    @Override
    public void onCameraDisconnected() {

    }

    @Override
    public void onCameraFreezed(String s) {
        hangUpCurrentSession();
    }

    @Override
    public void onCameraOpening(String s) {

    }

    @Override
    public void onFirstFrameAvailable() {

    }

    @Override
    public void onCameraClosed() {

    }

    @Override
    public void onUserNotAnswer(QBRTCSession qbrtcSession, Integer integer) {
        if (!qbrtcSession.equals(getCurrentSession())) {
            return;
        }
        ringtonePlayer.stop();

    }

    private QBRTCSession getCurrentSession() {
        return currentSession;
    }

    @Override
    public void onCallRejectByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
        if (!qbrtcSession.equals(getCurrentSession())) {
            return;
        }

        ringtonePlayer.stop();
    }

    @Override
    public void onCallAcceptByUser(QBRTCSession qbrtcSession, Integer integer, Map<String, String> map) {
        if (!qbrtcSession.equals(getCurrentSession())) {
            return;
        }
        ringtonePlayer.stop();
    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession qbrtcSession, Integer userID, Map<String, String> map) {
        if (qbrtcSession.equals(getCurrentSession())) {
            if (userID.equals(qbrtcSession.getCallerID())) {
                hangUpCurrentSession();
            }
        }
    }

    @Override
    public void onSessionClosed(QBRTCSession qbrtcSession) {

        if (qbrtcSession.equals(getCurrentSession())) {
            if (audioManager != null) {
                audioManager.stop();
            }
            releaseCurrentSession();
            finish();
        }
    }

    public void releaseCurrentSession() {
        if (currentSession != null) {
            this.currentSession.removeSessionCallbacksListener(CallActivity.this);
            this.currentSession.removeSignalingCallback(CallActivity.this);
            rtcClient.removeSessionsCallbacksListener(CallActivity.this);
            this.currentSession = null;
        }
    }

    @Override
    public void onSuccessSendingPacket(QBSignalingSpec.QBSignalCMD qbSignalCMD, Integer integer) {

    }

    @Override
    public void onErrorSendingPacket(QBSignalingSpec.QBSignalCMD qbSignalCMD, Integer integer, QBRTCSignalException e) {
        Globals.showToast(this, getString(R.string.dlg_signal_error));

    }

    private void notifyCallStateListenersCallStarted() {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onCallStarted();
        }
    }

    private void notifyCallStateListenersCallStopped() {
        for (CurrentCallStateCallback callback : currentCallStateCallbackList) {
            callback.onCallStopped();
        }
    }

    private void stopIncomeCallTimer() {
        showIncomingCallWindowTaskHandler.removeCallbacks(showIncomingCallWindowTask);
    }

    private void setExpirationReconnectionTime() {
        reconnectHangUpTimeMillis = 10 * 1000;
        expirationReconnectionTime = System.currentTimeMillis() + reconnectHangUpTimeMillis;
    }

    private void hangUpAfterLongReconnection() {
        if (expirationReconnectionTime < System.currentTimeMillis()) {
            hangUpCurrentSession();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == QBRTCScreenCapturer.REQUEST_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK) {
                startScreenSharing(data);
            }
        }
    }

    private void startScreenSharing(final Intent data) {
        currentSession.getMediaStreamManager().setVideoCapturer(new QBRTCScreenCapturer(data, null));
    }

    public interface CurrentCallStateCallback {
        void onCallStarted();

        void onCallStopped();

        void onOpponentsListUpdated(ArrayList<QBUser> newUsers);
    }

    private class ConnectionListener extends AbstractConnectionListener {
        @Override
        public void connectionClosedOnError(Exception e) {
            setExpirationReconnectionTime();
        }

        @Override
        public void reconnectionSuccessful() {

        }

        @Override
        public void reconnectingIn(int seconds) {
            if (!callStarted) {
                hangUpAfterLongReconnection();
            }
        }
    }

}
