package com.app.whosnextapp.snippets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.whosnextapp.R;
import com.app.whosnextapp.model.GetAllDashboardData;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.ConstantEnum;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.TimeUtils;
import com.app.whosnextapp.utility.TouchImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SnippetVideoActivity extends BaseAppCompatActivity implements MediaPlayer.OnPreparedListener, View.OnTouchListener, Player.EventListener {

    @BindView(R.id.vv_snippet_video)
    PlayerView vv_snippet_video;
    @BindView(R.id.iv_snippet_image)
    TouchImageView iv_snippet_image;
    @BindView(R.id.ll_image)
    LinearLayout ll_image;
    @BindView(R.id.ll_video)
    LinearLayout ll_video;
    @BindView(R.id.ll_audio)
    LinearLayout ll_audio;
    @BindView(R.id.iv_audio_image)
    ImageView iv_audio_image;
    @BindView(R.id.tv_timer)
    TextView tv_timer;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    GetAllDashboardData.SnippetsList snippetsList;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean stopped = false;
    long stopPosition = 0, pausePosition = 0;
    SimpleExoPlayer exoPlayer;
    DataSource.Factory dataSourceFactory;
    private Handler handler = new Handler();

    private Runnable mUpdateTimerForAudio = new Runnable() {
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                long currentTime = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
                tv_timer.setText(TimeUtils.milliSecondsToTimer(
                        TimeUnit.MILLISECONDS.toSeconds(currentTime), false));
                handler.postDelayed(this, 100);
            }
        }
    };

    private Runnable mUpdateTimerForVideo = new Runnable() {


        public void run() {
            long currentTime = exoPlayer.getDuration() - exoPlayer.getCurrentPosition();
            tv_timer.setText(TimeUtils.milliSecondsToTimer(
                    TimeUnit.MILLISECONDS.toSeconds(currentTime), false));
            handler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snippet_video);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void init() {
        ButterKnife.bind(this);
        //setData();
    }

    private void initVideoPlayer() {

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        vv_snippet_video.setPlayer(exoPlayer);
        vv_snippet_video.setUseController(false);
        exoPlayer.addListener(this);
        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.text_who_s_next_app)), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(snippetsList.getSnippetsPath()));
        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(true);
        handler.postDelayed(mUpdateTimerForVideo, 0);

    }

    private void initAudioPlayer() {
        if (mediaPlayer != null)
            mediaPlayer.reset();
        mediaPlayer = (MediaPlayer.create(this, Uri.parse(snippetsList.getSnippetsPath())));
        mediaPlayer.setOnPreparedListener(this);
    }

    private void setData() {
        if (getIntent() != null)
            snippetsList = (GetAllDashboardData.SnippetsList) getIntent().getSerializableExtra(Constants.WN_GET_SNIPPET_MODEL);
        if (snippetsList.getSnippetsType() == ConstantEnum.SnippetType.AUDIO.getId()) {
            getSnippetAudio();
        } else if (snippetsList.getSnippetsType() == ConstantEnum.SnippetType.VIDEO.getId()) {
            getSnippetVideo();
        } else if (snippetsList.getSnippetsType() == ConstantEnum.SnippetType.IMAGE.getId()) {
            getSnippetImage();
        }
    }

    private void getSnippetImage() {
        ll_image.setVisibility(View.VISIBLE);
        Glide.with(this).load(snippetsList.getSnippetsPath()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                iv_snippet_image.setImageDrawable(resource);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void getSnippetVideo() {
        ll_video.setVisibility(View.VISIBLE);
        initVideoPlayer();
        vv_snippet_video.setOnTouchListener((v, event) -> {
            if (!stopped) {
                stopPosition = exoPlayer.getCurrentPosition();
                exoPlayer.setPlayWhenReady(false);
                handler.removeCallbacks(mUpdateTimerForVideo);
                stopped = true;
            } else {
                exoPlayer.seekTo((int) stopPosition);
                exoPlayer.setPlayWhenReady(true);
                handler.postDelayed(mUpdateTimerForVideo, 0);
                stopped = false;
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void getSnippetAudio() {
        ll_audio.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        Glide.with(this)
                .load(snippetsList.getSnippetsAudioImagePath())
                .placeholder(R.drawable.whosnextapp)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        iv_audio_image.setImageDrawable(resource);
                    }
                });
        initAudioPlayer();
        iv_audio_image.setOnTouchListener(this);
    }

    @OnClick(R.id.iv_close)
    public void onCloseClick() {
       // if (vv_snippet_video != null) {
           // mediaPlayer.stop();

            Intent intent = new Intent(this, SnippetProfileActivity.class);
            intent.putExtra(Constants.WN_GET_SNIPPET_MODEL, snippetsList);
            startActivity(intent);
            finish();
           // releasePlayer();
      //  }

    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        if (!mediaPlayer.isPlaying() && mediaPlayer != null)
            mediaPlayer.start();
        handler.postDelayed(mUpdateTimerForAudio, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mediaPlayer.isPlaying()) {
            pausePosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            handler.removeCallbacks(mUpdateTimerForAudio);
        } else {
            mediaPlayer.seekTo((int) pausePosition);
            mediaPlayer.start();
            handler.postDelayed(mUpdateTimerForAudio, 0);
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
        releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.stop(true);
            exoPlayer.release();
            exoPlayer = null;
        }
        releasePlayer();
    }

    private void releasePlayer() {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (handler != null) {
            handler.removeCallbacks(mUpdateTimerForAudio);
            handler.removeCallbacks(mUpdateTimerForVideo);
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_BUFFERING) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (playWhenReady && playbackState == Player.STATE_READY) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
