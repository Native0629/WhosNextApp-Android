/*
 * Copyright (c) 2018 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.ene.toro.exoplayer;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroPlayer.VolumeChangeListeners;
import im.ene.toro.ToroUtil;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.media.VolumeInfo;

import static im.ene.toro.ToroUtil.checkNotNull;
import static im.ene.toro.exoplayer.ToroExo.with;
import static im.ene.toro.media.PlaybackInfo.INDEX_UNSET;
import static im.ene.toro.media.PlaybackInfo.TIME_UNSET;

/**
 * [20180225]
 * <p>
 * Default implementation of {@link Playable}.
 * <p>
 * Instance of {@link Playable} should be reusable. Retaining instance of Playable across config
 * change must guarantee that all {@link EventListener} are cleaned up on config change.
 *
 * @author eneim (2018/02/25).
 */
@SuppressWarnings("WeakerAccess")
        //
class PlayableImpl implements Playable {

    protected final EventListeners listeners = new EventListeners();  // original listener.
    protected final VolumeChangeListeners volumeChangeListeners = new VolumeChangeListeners();
    protected final ToroPlayer.ErrorListeners errorListeners = new ToroPlayer.ErrorListeners();
    protected final Uri mediaUri; // immutable, parcelable
    protected final ArrayList<Uri> mediaUris; // immutable, parcelable
    protected final String fileExt;
    protected final ExoCreator creator; // required, cached
    private final PlaybackInfo playbackInfo = new PlaybackInfo(); // never expose to outside.
    protected ConcatenatingMediaSource concatenatedSource;
    protected LoopingMediaSource loopingMediaSource;
    protected SimpleExoPlayer player; // on-demand, cached
    protected MediaSource mediaSource;  // on-demand, since we do not reuse MediaSource now.
    protected PlayerView playerView; // on-demand, not always required.
    protected MediaSource[] mediaSources;
    private String TAG = PlayableImpl.class.getSimpleName();
    private boolean sourcePrepared = false;
    private boolean listenerApplied = false;

    PlayableImpl(ExoCreator creator, Uri uri, String fileExt, ArrayList<Uri> videoList) {
        this.creator = creator;
        this.mediaUri = uri;
        this.mediaUris = videoList;
        this.fileExt = fileExt;
    }

    @CallSuper
    @Override
    public void prepare(boolean prepareSource) {
        if (prepareSource) {
            ensureMediaSource();
            ensurePlayerView();
        }
    }

    @Override
    public final PlayerView getPlayerView() {
        return this.playerView;
    }

    @CallSuper
    @Override
    public void setPlayerView(@Nullable PlayerView playerView) {
        if (this.playerView == playerView) return;
        if (playerView == null) {
            this.playerView.setPlayer(null);
        } else {
            if (this.player != null) {
                PlayerView.switchTargetView(this.player, this.playerView, playerView);
            }
        }

        this.playerView = playerView;
    }

    @CallSuper
    @Override
    public void play() {
        ensureMediaSource();
        ensurePlayerView();
        checkNotNull(player, "Playable#play(): Player is null!");
        player.setPlayWhenReady(true);
    }

    @CallSuper
    @Override
    public void pause() {
        // Player is not required to be non-null here.
        if (player != null) player.setPlayWhenReady(false);
    }

    @CallSuper
    @Override
    public void reset() {
        this.playbackInfo.reset();
        if (player != null) {
            // reset volume to default
            ToroExo.setVolumeInfo(this.player, new VolumeInfo(false, 1.f));
            player.stop(true);
        }

        this.mediaSource = null; // so it will be re-prepared when play() is called.
        this.sourcePrepared = false;
    }

    @CallSuper
    @Override
    public void release() {
        this.setPlayerView(null);
        if (this.player != null) {
            // reset volume to default
            ToroExo.setVolumeInfo(this.player, new VolumeInfo(false, 1.f));
            this.player.stop(true);
            if (listenerApplied) {
                player.removeListener(listeners);
                player.removeVideoListener(listeners);
                player.removeTextOutput(listeners);
                player.removeMetadataOutput(listeners);
                if (this.player instanceof ToroExoPlayer) {
                    ((ToroExoPlayer) this.player).removeOnVolumeChangeListener(this.volumeChangeListeners);
                }
                listenerApplied = false;
            }
            with(checkNotNull(creator.getContext(), "ExoCreator has no Context")) //
                    .releasePlayer(this.creator, this.player);
        }

        this.player = null;
        this.mediaSource = null;
        this.sourcePrepared = false;
    }

    @CallSuper
    @NonNull
    @Override
    public PlaybackInfo getPlaybackInfo() {
        updatePlaybackInfo();
        return new PlaybackInfo(playbackInfo.getResumeWindow(), playbackInfo.getResumePosition(),
                playbackInfo.getVolumeInfo());
    }

    @CallSuper
    @Override
    public void setPlaybackInfo(@NonNull PlaybackInfo playbackInfo) {
        this.playbackInfo.setResumeWindow(playbackInfo.getResumeWindow());
        this.playbackInfo.setResumePosition(playbackInfo.getResumePosition());
        this.playbackInfo.setVolumeInfo(playbackInfo.getVolumeInfo());

        if (player != null) {
            ToroExo.setVolumeInfo(player, this.playbackInfo.getVolumeInfo());
            boolean haveResumePosition = this.playbackInfo.getResumeWindow() != INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(this.playbackInfo.getResumeWindow(), this.playbackInfo.getResumePosition());
            }
        }
    }

    @Override
    public final void addEventListener(@NonNull EventListener listener) {
        //noinspection ConstantConditions
        if (listener != null) this.listeners.add(listener);
    }

    @Override
    public final void removeEventListener(EventListener listener) {
        this.listeners.remove(listener);
    }

    @CallSuper
    @Override
    public float getVolume() {
        return checkNotNull(player, "Playable#getVolume(): Player is null!").getVolume();
    }

    @CallSuper
    @Override
    public void setVolume(float volume) {
        checkNotNull(player, "Playable#setVolume(): Player is null!");
        playbackInfo.getVolumeInfo().setTo(volume == 0, volume);
        ToroExo.setVolumeInfo(player, this.playbackInfo.getVolumeInfo());
    }

    @Override
    public boolean setVolumeInfo(@NonNull VolumeInfo volumeInfo) {
        checkNotNull(player, "Playable#setVolumeInfo(): Player is null!");
        boolean changed = !this.playbackInfo.getVolumeInfo().equals(checkNotNull(volumeInfo));
        if (changed) {
            this.playbackInfo.getVolumeInfo().setTo(volumeInfo.isMute(), volumeInfo.getVolume());
            ToroExo.setVolumeInfo(player, this.playbackInfo.getVolumeInfo());
        }
        return changed;
    }

    @NonNull
    @Override
    public VolumeInfo getVolumeInfo() {
        return this.playbackInfo.getVolumeInfo();
    }

    @Override
    public PlaybackParameters getParameters() {
        return checkNotNull(player, "Playable#getParameters(): Player is null").getPlaybackParameters();
    }

    @Override
    public void setParameters(@Nullable PlaybackParameters parameters) {
        checkNotNull(player, "Playable#setParameters(PlaybackParameters): Player is null") //
                .setPlaybackParameters(parameters);
    }

    @Override
    public void addOnVolumeChangeListener(@NonNull ToroPlayer.OnVolumeChangeListener listener) {
        volumeChangeListeners.add(ToroUtil.checkNotNull(listener));
    }

    @Override
    public void removeOnVolumeChangeListener(@Nullable ToroPlayer.OnVolumeChangeListener listener) {
        volumeChangeListeners.remove(listener);
    }

    @Override
    public boolean isPlaying() {
        return player != null && player.getPlayWhenReady();
    }

    @Override
    public void addErrorListener(@NonNull ToroPlayer.OnErrorListener listener) {
        this.errorListeners.add(checkNotNull(listener));
    }

    @Override
    public void removeErrorListener(@Nullable ToroPlayer.OnErrorListener listener) {
        this.errorListeners.remove(listener);
    }

    final void updatePlaybackInfo() {
        if (player == null || player.getPlaybackState() == Player.STATE_IDLE) return;
        playbackInfo.setResumeWindow(player.getCurrentWindowIndex());
        playbackInfo.setResumePosition(player.isCurrentWindowSeekable() ? //
                Math.max(0, player.getCurrentPosition()) : TIME_UNSET);
        playbackInfo.setVolumeInfo(ToroExo.getVolumeInfo(player));
    }

    private void ensurePlayerView() {


        /*Original*/
        /*if (playerView != null && playerView.getPlayer() != player) playerView.setPlayer(player);*/

        /*Set Expect ratio*/

        if (playerView != null && playerView.getPlayer() != player) {
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            playerView.setPlayer(player);
        }

    }

    private void ensureMediaSource() {
       /* if (mediaSource == null) {  // Only actually prepare the source when play() is called.
            sourcePrepared = false;
            mediaSource = creator.createMediaSource(mediaUri, fileExt);
        }



        if (!sourcePrepared) {
            ensurePlayer(); // sourcePrepared is set to false only when player is null.
            player.prepare(mediaSource, playbackInfo.getResumeWindow() == C.INDEX_UNSET, false);
            sourcePrepared = true;
        }*/


        /*  if (mediaSource == null) {  // Only actually prepare the source when play() is called.
            sourcePrepared = false;
            mediaSource = creator.createMediaSource(mediaUri, fileExt);
        }*/


        // static list of multiple videos
/*        String fv = "http://d1ffzjqwjap3w2.cloudfront.net/GroupVideos/a2db3d14-2c66-4add-b05d-1f8f39271930.mp4";
        String sv = "http://d1ffzjqwjap3w2.cloudfront.net/GroupVideos/a40f2819-3db3-4ded-ac72-7c51d52bdee7.mp4";
        String tv = "http://d1ffzjqwjap3w2.cloudfront.net/GroupVideos/1b7dae70-771e-4215-9e9b-0389846121b8.mp4";
        String fov = "http://d1ffzjqwjap3w2.cloudfront.net/GroupVideos/e237c2c6-d178-4a41-9e50-da4234e398a2.mp4";
        String ffv = "http://d1ffzjqwjap3w2.cloudfront.net/GroupVideos/057cf3a3-9920-461b-8581-5d19da320ad2.mp4";


        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(Uri.parse(fv));
        uris.add(Uri.parse(sv));
        uris.add(Uri.parse(tv));
        uris.add(Uri.parse(fov));
        uris.add(Uri.parse(ffv));*/

        if (mediaUris.size() <= 0 && mediaUri != null) {
            if (mediaSource == null) {  // Only actually prepare the source when play() is called.
                sourcePrepared = false;
                mediaSource = creator.createMediaSource(mediaUri, fileExt);

                loopingMediaSource = new LoopingMediaSource(mediaSource, 100);

                if (!sourcePrepared) {
                    ensurePlayer(); // sourcePrepared is set to false only when player is null.
                    /*player.prepare(concatenatedSource, playbackInfo.getResumeWindow() == C.INDEX_UNSET, false);*/
                    player.prepare(loopingMediaSource, playbackInfo.getResumeWindow() == C.INDEX_UNSET, false);
                    sourcePrepared = true;
                }
            }
        } else {
            if (loopingMediaSource == null) {
                sourcePrepared = false;
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(creator.getContext(),
                        Util.getUserAgent(creator.getContext(), "ToroDev-v3"));

                if (mediaUris != null) {
                    Log.e("MediaUris: ", "" + mediaUris.size());
                    if (mediaSources != null) {
                        mediaSources = null;
                    }
                    mediaSources = new MediaSource[mediaUris.size()];

                    for (int k = 0; k < mediaUris.size(); k++) {
                        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUris.get(k));
                        mediaSources[k] = mediaSource;
                    }

                    concatenatedSource = new ConcatenatingMediaSource(mediaSources);
                    loopingMediaSource = new LoopingMediaSource(concatenatedSource, 100);

                    if (!sourcePrepared) {
                        ensurePlayer(); // sourcePrepared is set to false only when player is null.
                        /*player.prepare(concatenatedSource, playbackInfo.getResumeWindow() == C.INDEX_UNSET, false);*/

                        player.prepare(loopingMediaSource, playbackInfo.getResumeWindow() == C.INDEX_UNSET, false);

                      /*  //Play Sepcific video from ConcatenatedMediaSource

                        player.prepare(loopingMediaSource);
                        player.seekTo(2, C.TIME_UNSET);
                        player.setPlayWhenReady(true);*/

                        sourcePrepared = true;
                    }
                }
            }
        }
    }

    private void ensurePlayer() {
        if (player == null) {
            sourcePrepared = false;
            player = with(checkNotNull(creator.getContext(), "ExoCreator has no Context")) //
                    .requestPlayer(creator);
            listenerApplied = false;
        }

        if (!listenerApplied) {
            if (player instanceof ToroExoPlayer) {
                ((ToroExoPlayer) player).addOnVolumeChangeListener(volumeChangeListeners);
            }
            player.addListener(listeners);
            player.addVideoListener(listeners);
            player.addTextOutput(listeners);
            player.addMetadataOutput(listeners);
            listenerApplied = true;
        }

        ToroExo.setVolumeInfo(player, this.playbackInfo.getVolumeInfo());
        boolean haveResumePosition = playbackInfo.getResumeWindow() != C.INDEX_UNSET;
        if (haveResumePosition) {
            player.seekTo(playbackInfo.getResumeWindow(), playbackInfo.getResumePosition());
        }
    }
}
