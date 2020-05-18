package com.app.whosnextapp.messaging.videocall.interfaces;

import com.app.whosnextapp.messaging.videocall.fragment.CallActivity;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionEventsCallback;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionStateCallback;

import org.webrtc.CameraVideoCapturer;

public interface ConversationFragmentCallbackListener {

    void addTCClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks);

    void removeRTCClientConnectionCallback(QBRTCSessionStateCallback clientConnectionCallbacks);

    void addRTCSessionEventsCallback(QBRTCSessionEventsCallback eventsCallback);

    void removeRTCSessionEventsCallback(QBRTCSessionEventsCallback eventsCallback);

    void addCurrentCallStateCallback(CallActivity.CurrentCallStateCallback currentCallStateCallback);

    void removeCurrentCallStateCallback(CallActivity.CurrentCallStateCallback currentCallStateCallback);

    void onSetAudioEnabled(boolean isAudioEnabled);

    void onSetVideoEnabled(boolean isNeedEnableCam);

    void onHangUpCurrentSession();

    void onStartScreenSharing();

    void onSwitchCamera(CameraVideoCapturer.CameraSwitchHandler cameraSwitchHandler);
}
