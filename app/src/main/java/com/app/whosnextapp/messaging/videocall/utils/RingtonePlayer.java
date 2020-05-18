package com.app.whosnextapp.messaging.videocall.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.IOException;

public class RingtonePlayer {

    private MediaPlayer mediaPlayer;
    private Context context;

    public RingtonePlayer(Context context, int resource) {
        this.context = context;
        Uri beepUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resource);
        mediaPlayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING);

            mediaPlayer.setAudioAttributes(audioAttributesBuilder.build());
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }

        try {
            mediaPlayer.setDataSource(context, beepUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RingtonePlayer(Context context) {
        this.context = context;
        Uri notification = getNotification();
        if (notification != null) {
            mediaPlayer = new MediaPlayer();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE);

                mediaPlayer.setAudioAttributes(audioAttributesBuilder.build());
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            }

            try {
                mediaPlayer.setDataSource(context, notification);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Uri getNotification() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if (notification == null) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (notification == null) {
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }
        return notification;
    }

    public void play(boolean looping) {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.setLooping(looping);
        mediaPlayer.start();
    }

    public synchronized void stop() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
