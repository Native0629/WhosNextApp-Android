package com.app.whosnextapp.loginregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.whosnextapp.R;
import com.app.whosnextapp.customVideoViews.BackgroundTask;
import com.app.whosnextapp.customVideoViews.BarThumb;
import com.app.whosnextapp.customVideoViews.CustomRangeSeekBar;
import com.app.whosnextapp.customVideoViews.OnRangeSeekBarChangeListener;
import com.app.whosnextapp.customVideoViews.OnVideoTrimListener;
import com.app.whosnextapp.customVideoViews.TileView;
import com.app.whosnextapp.navigationmenu.EditProfileActivity;
import com.app.whosnextapp.settings.AddSnippetActivity;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;
import com.app.whosnextapp.utility.Utility;
import com.app.whosnextapp.videos.ShareToActivity;
import com.app.whosnextapp.videos.groupvideo.GroupVideoActivity;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoTrimmerActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @BindView(R.id.txtVideoCancel)
    TextView txtVideoCancel;
    @BindView(R.id.txtVideoUpload)
    TextView txtVideoUpload;
    @BindView(R.id.txtVideoTrimSeconds)
    TextView txtVideoTrimSeconds;
    @BindView(R.id.timeLineView)
    TileView timeLineView;
    @BindView(R.id.timeLineBar)
    CustomRangeSeekBar timeLineBar;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.imgPlay)
    ImageView imgPlay;
    @BindView(R.id.seekBarVideo)
    SeekBar seekBarVideo;
    @BindView(R.id.txtVideoLength)
    TextView txtVideoLength;

    String srcFile, dstFile;
    private int mDuration = 0;
    private int mTimeVideo = 0;
    private int mStartPosition = 0;
    private int mEndPosition = 0;
    private int mMaxDuration;
    private Handler mHandler = new Handler();
    private ProgressDialog mProgressDialog;

    OnVideoTrimListener mOnVideoTrimListener = new OnVideoTrimListener() {
        @Override
        public void onTrimStarted() {
            // Create an indeterminate progress dialog
            mProgressDialog = new ProgressDialog(VideoTrimmerActivity.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle(getString(R.string.text_saving));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        public void getResult(Uri uri) {
            mProgressDialog.dismiss();
            Bundle conData = new Bundle();
            conData.putString(Constants.WN_INTENT_VIDEO_FILE, uri.getPath());
            Intent intent = new Intent();
            intent.putExtras(conData);

            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getInt(Constants.WN_REQUEST_CODE) == Constants.WN_VIDEO_TRIM_REG) {
                    sendDataToRegistration(conData);
                    finish();
                } else if (getIntent().getExtras().getInt(Constants.WN_REQUEST_CODE) == Constants.WN_VIDEO_TRIM_Edit) {
                    sendDataToEditProfile(conData);
                    finish();
                } else if (getIntent().getExtras().getInt(Constants.WN_REQUEST_CODE) == Constants.WN_VIDEO_TRIM) {
                    sendDataToShare(conData);
                    finish();
                } else if (getIntent().getExtras().getInt(Constants.WN_REQUEST_CODE) == Constants.WN_VIDEO_TRIM_SNIPPET) {
                    sendDataToSnippet(conData);
                    finish();
                } else if (getIntent().getExtras().getInt(Constants.WN_REQUEST_CODE) == Constants.WN_VIDEO_TRIM_GROUP) {
                    sendDataToGroupVideo(conData);
                    finish();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }

        @Override
        public void cancelAction() {
            mProgressDialog.dismiss();
        }

        @Override
        public void onError(String message) {
            mProgressDialog.dismiss();
        }
    };

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (seekBarVideo.getProgress() >= seekBarVideo.getMax()) {
                seekBarVideo.setProgress((videoView.getCurrentPosition() - mStartPosition * 1000));
                txtVideoLength.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                videoView.seekTo(mStartPosition * 1000);
                videoView.pause();
                seekBarVideo.setProgress(0);
                txtVideoLength.setText(R.string.time_00);
                imgPlay.setBackgroundResource(R.drawable.ic_white_play);
            } else {
                seekBarVideo.setProgress((videoView.getCurrentPosition() - mStartPosition * 1000));
                txtVideoLength.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                mHandler.postDelayed(this, 100);
            }
        }
    };

    private void sendDataToRegistration(Bundle data) {
        startActivity(new Intent(this, RegistrationActivity.class).putExtra(Constants.WN_EXTRA_VIDEO_TRIM_VIDEO, data));
        overridePendingTransition(0, 0);
    }

    private void sendDataToEditProfile(Bundle data) {
        startActivity(new Intent(this, EditProfileActivity.class).putExtra(Constants.WN_EXTRA_VIDEO_TRIM_VIDEO, data));
        overridePendingTransition(0, 0);
    }

    private void sendDataToShare(Bundle data) {
        startActivity(new Intent(this, ShareToActivity.class).putExtra(Constants.WN_EXTRA_VIDEO_TRIM_VIDEO, data));
        overridePendingTransition(0, 0);
    }

    private void sendDataToSnippet(Bundle data) {
        startActivity(new Intent(this, AddSnippetActivity.class).putExtra(Constants.WN_EXTRA_VIDEO_TRIM_VIDEO, data));
        overridePendingTransition(0, 0);
    }

    private void sendDataToGroupVideo(Bundle data) {
        startActivity(new Intent(this, GroupVideoActivity.class).putExtra(Constants.WN_EXTRA_VIDEO_TRIM_VIDEO, data));
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_trim);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            srcFile = getIntent().getExtras().getString(Constants.WN_EXTRA_VIDEO_PATH);
            if (getIntent().getExtras().getInt(Constants.WN_VIDEO_DURATION) != 0) {
                mMaxDuration = getIntent().getExtras().getInt(Constants.WN_VIDEO_DURATION);
            } else {
                mMaxDuration = Constants.WN_BIO_VIDEO_DURATION; /*Default 30 Seconds*/
            }
        }
        dstFile = Utility.createFile(Constants.WN_VIDEO_FOLDER_PATH, Constants.WN_VIDEO_TRIM_FILE + System.currentTimeMillis(), Constants.WN_VIDEO_MP4);
        timeLineView.post(new Runnable() {
            @Override
            public void run() {
                setBitmap(Uri.parse(srcFile));
                videoView.setVideoURI(Uri.parse(srcFile));
            }
        });

        txtVideoCancel.setOnClickListener(this);
        txtVideoUpload.setOnClickListener(this);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                onVideoPrepared(mp);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onVideoCompleted();
            }
        });

        // handle your range seekbar changes
        timeLineBar.addOnRangeSeekBarListener(new OnRangeSeekBarChangeListener() {
            @Override
            public void onCreate(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                // Do nothing
            }

            @Override
            public void onSeek(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                onSeekThumbs(index, value);
            }

            @Override
            public void onSeekStart(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                if (videoView != null) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seekBarVideo.setProgress(0);
                    videoView.seekTo(mStartPosition * 1000);
                    videoView.pause();
                    imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                }
            }

            @Override
            public void onSeekStop(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
            }
        });
        imgPlay.setOnClickListener(this);
        // handle changes on seekbar for video play
        seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (videoView != null) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seekBarVideo.setMax(mTimeVideo * 1000);
                    seekBarVideo.setProgress(0);
                    videoView.seekTo(mStartPosition * 1000);
                    videoView.pause();
                    imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                videoView.seekTo((mStartPosition * 1000) - seekBarVideo.getProgress());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == txtVideoCancel) {
            finish();
        } else if (view == txtVideoUpload) {
            int diff = mEndPosition - mStartPosition;

            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(VideoTrimmerActivity.this, Uri.parse(srcFile));
            final File file = new File(srcFile);

            //notify that video trimming started
            if (mOnVideoTrimListener != null)
                mOnVideoTrimListener.onTrimStarted();

            BackgroundTask.execute(
                    new BackgroundTask.Task("", 0L, "") {
                        @Override
                        public void execute() {
                            try {
                                Utility.startTrim(file, dstFile, mStartPosition * 1000, mEndPosition * 1000, mOnVideoTrimListener);
                            } catch (final Throwable e) {
                                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                            }
                        }
                    }
            );

        } else if (view == imgPlay) {
            if (videoView.isPlaying()) {
                if (videoView != null) {
                    videoView.pause();
                    imgPlay.setBackgroundResource(R.drawable.ic_white_play);
                }
            } else {
                if (videoView != null) {
                    videoView.start();
                    imgPlay.setBackgroundResource(R.drawable.ic_white_pause);
                    if (seekBarVideo.getProgress() == 0) {
                        txtVideoLength.setText(R.string.time_00);
                        updateProgressBar();
                    }
                }
            }
        }
    }

    private void setBitmap(Uri mVideoUri) {
        timeLineView.setVideo(mVideoUri);
    }

    private void onVideoPrepared(@NonNull MediaPlayer mp) {
        mDuration = videoView.getDuration() / 1000;
        setSeekBarPosition();
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private void setSeekBarPosition() {
        if (mDuration >= mMaxDuration) {
            mStartPosition = 0;
            mEndPosition = mMaxDuration;

            timeLineBar.setThumbValue(0, (mStartPosition * 100) / mDuration);
            timeLineBar.setThumbValue(1, (mEndPosition * 100) / mDuration);
        } else {
            mStartPosition = 0;
            mEndPosition = mDuration;
        }

        mTimeVideo = mDuration;
        timeLineBar.initMaxWidth();
        seekBarVideo.setMax(mMaxDuration * 1000);
        videoView.seekTo(mStartPosition * 1000);

        String mStart = mStartPosition + "";
        if (mStartPosition < 10)
            mStart = "0" + mStartPosition;

        int startMin = Integer.parseInt(mStart) / 60;
        int startSec = Integer.parseInt(mStart) % 60;

        String mEnd = mEndPosition + "";
        if (mEndPosition < 10)
            mEnd = "0" + mEndPosition;

        int endMin = Integer.parseInt(mEnd) / 60;
        int endSec = Integer.parseInt(mEnd) % 60;

        txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
    }

    private void onVideoCompleted() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        seekBarVideo.setProgress(0);
        videoView.seekTo(mStartPosition * 1000);
        videoView.pause();
        imgPlay.setBackgroundResource(R.drawable.ic_white_play);
    }

    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case BarThumb.LEFT: {
                mStartPosition = (int) ((mDuration * value) / 100L);
                videoView.seekTo(mStartPosition * 1000);
                break;
            }
            case BarThumb.RIGHT: {
                mEndPosition = (int) ((mDuration * value) / 100L);
                break;
            }
        }
        mTimeVideo = (mEndPosition - mStartPosition);
        seekBarVideo.setMax(mTimeVideo * 1000);
        seekBarVideo.setProgress(0);
        videoView.seekTo(mStartPosition * 1000);

        String mStart = mStartPosition + "";
        if (mStartPosition < 10)
            mStart = "0" + mStartPosition;

        int startMin = Integer.parseInt(mStart) / 60;
        int startSec = Integer.parseInt(mStart) % 60;

        String mEnd = mEndPosition + "";
        if (mEndPosition < 10)
            mEnd = "0" + mEndPosition;
        int endMin = Integer.parseInt(mEnd) / 60;
        int endSec = Integer.parseInt(mEnd) % 60;

        txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;
        String minutesString;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        return finalTimerString;
    }
}
