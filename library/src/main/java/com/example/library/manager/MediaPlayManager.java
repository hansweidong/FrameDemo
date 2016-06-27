package com.example.library.manager;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by bjhl on 16/6/27.
 */
public class MediaPlayManager {

    private MediaPlayer mMediaPlayer;
    private onCompleteListener mListener;

    /**
     *
     * @param act
     * @param filepath
     * @param listener 播放完成的Listener
     * @throws Exception
     */
    public MediaPlayManager(Activity act, String filepath, onCompleteListener listener) throws Exception {
        mListener = listener;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        act.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(filepath);
        mMediaPlayer.prepare();
    }

    public void startPlay() {
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                mMediaPlayer.release();
                mMediaPlayer = null;
                if (mListener != null) {
                    mListener.onComplete(mp);
                }
            }
        });
    }

    public void stopPlay() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public interface onCompleteListener {
        void onComplete(MediaPlayer mp);
    }
}
