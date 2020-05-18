package com.app.whosnextapp.messaging.videocall.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.app.whosnextapp.R;
import com.app.whosnextapp.messaging.quickblox.utils.QbDialogHolder;
import com.app.whosnextapp.messaging.videocall.interfaces.IncomeCallFragmentCallbackListener;
import com.app.whosnextapp.messaging.videocall.utils.RingtonePlayer;
import com.app.whosnextapp.messaging.videocall.utils.UiUtils;
import com.app.whosnextapp.utility.Globals;
import com.app.whosnextapp.utility.WebRtcSessionManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class IncomeCallFragment extends Fragment implements Serializable, View.OnClickListener {

    private static final String TAG = IncomeCallFragment.class.getSimpleName();
    private static final long CLICK_DELAY = TimeUnit.SECONDS.toMillis(2);
    Globals globals;
    private TextView callTypeTextView;
    private ImageButton rejectButton;
    private ImageButton takeButton;
    private List<Integer> opponentsIds;
    private Vibrator vibrator;
    private QBRTCTypes.QBConferenceType conferenceType;
    private long lastClickTime = 0l;
    private RingtonePlayer ringtonePlayer;
    private IncomeCallFragmentCallbackListener incomeCallFragmentCallbackListener;
    private QBRTCSession currentSession;
    private TextView alsoOnCallText;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            incomeCallFragmentCallbackListener = (IncomeCallFragmentCallbackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCallEventsController");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_call, container, false);

        initFields();
        hideToolBar();

        if (currentSession != null) {
            initUI(view);
            setDisplayedTypeCall(conferenceType);
            initButtonsListener();
        }

        ringtonePlayer = new RingtonePlayer(getActivity());
        return view;
    }

    private void initFields() {
        currentSession = WebRtcSessionManager.getInstance(getActivity()).getCurrentSession();
        globals = (Globals) Objects.requireNonNull(getActivity()).getApplicationContext();
        if (currentSession != null) {
            opponentsIds = currentSession.getOpponents();
            conferenceType = currentSession.getConferenceType();
            Log.d(TAG, conferenceType.toString() + "From onCreateView()");
        }
    }

    public void hideToolBar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_call);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        startCallNotification();
    }

    private void initButtonsListener() {
        rejectButton.setOnClickListener(this);
        takeButton.setOnClickListener(this);
    }

    private void initUI(View view) {
        callTypeTextView = (TextView) view.findViewById(R.id.call_type);

        ImageView callerAvatarImageView = (ImageView) view.findViewById(R.id.image_caller_avatar);
        callerAvatarImageView.setBackgroundDrawable(getBackgroundForCallerAvatar(currentSession.getCallerID()));

        TextView callerNameTextView = (TextView) view.findViewById(R.id.text_caller_name);
        String callerName = String.valueOf(currentSession.getCallerID());
        ArrayList<QBChatDialog> dialogHolders = new ArrayList<QBChatDialog>(QbDialogHolder.getInstance().getDialogs(true).values());

        if (dialogHolders.size() > 0) {
            for (int i = 0; i < dialogHolders.size(); i++) {
                QBChatDialog qbChatDialog = dialogHolders.get(i);
                if (currentSession.getCallerID().equals(getRecipient(qbChatDialog.getOccupants()))) {
                    callerName = qbChatDialog.getName();
                }

            }
        }

        callerNameTextView.setText(callerName);

        TextView otherIncUsersTextView = (TextView) view.findViewById(R.id.text_other_inc_users);
        otherIncUsersTextView.setText(getOtherIncUsersNames());

        alsoOnCallText = (TextView) view.findViewById(R.id.text_also_on_call);
        setVisibilityAlsoOnCallTextView();

        rejectButton = (ImageButton) view.findViewById(R.id.image_button_reject_call);
        takeButton = (ImageButton) view.findViewById(R.id.image_button_accept_call);
    }

    private void setVisibilityAlsoOnCallTextView() {
        if (opponentsIds.size() < 2) {
            alsoOnCallText.setVisibility(View.INVISIBLE);
        }
    }

    private Drawable getBackgroundForCallerAvatar(int callerId) {
        return UiUtils.getColorCircleDrawable(callerId);
    }

    private Integer getRecipient(List<Integer> opponentsIds) {
        for (Integer id : opponentsIds) {
            if (!String.valueOf(id).equalsIgnoreCase(String.valueOf(globals.getUserDetails().getStatus().getChatId())))
                return id;
        }
        return currentSession.getCallerID();
    }

    public void startCallNotification() {

        ringtonePlayer.play(false);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        long[] vibrationCycle = {0, 1000, 1000};
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(vibrationCycle, 1);
        }

    }

    private void stopCallNotification() {

        if (ringtonePlayer != null) {
            ringtonePlayer.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    private String getOtherIncUsersNames() {
        return "";
    }

    private void setDisplayedTypeCall(QBRTCTypes.QBConferenceType conferenceType) {
        boolean isVideoCall = conferenceType == QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;
        callTypeTextView.setText(isVideoCall ? R.string.text_incoming_video_call : R.string.text_incoming_audio_call);
        takeButton.setImageResource(isVideoCall ? R.drawable.ic_video_white : R.drawable.ic_call);
    }

    @Override
    public void onStop() {
        stopCallNotification();
        super.onStop();
    }

    @Override
    public void onClick(View v) {

        if ((SystemClock.uptimeMillis() - lastClickTime) < CLICK_DELAY) {
            return;
        }
        lastClickTime = SystemClock.uptimeMillis();

        switch (v.getId()) {
            case R.id.image_button_reject_call:
                reject();
                break;

            case R.id.image_button_accept_call:
                accept();
                break;

            default:
                break;
        }
    }

    private void accept() {
        enableButtons(false);
        stopCallNotification();
        incomeCallFragmentCallbackListener.onAcceptCurrentSession();
    }

    private void reject() {
        enableButtons(false);
        stopCallNotification();
        incomeCallFragmentCallbackListener.onRejectCurrentSession();
    }

    private void enableButtons(boolean enable) {
        takeButton.setEnabled(enable);
        rejectButton.setEnabled(enable);
    }
}
