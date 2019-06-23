package org.aospstudio.webbrowser.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.media.audiofx.PresetReverb;
import android.os.IBinder;

import java.io.IOException;

public class MPlayer extends Service implements MediaPlayer.OnCompletionListener {

    private static final String TAG = null;
    MediaPlayer mediaplayer;
    AudioEffect audioEffect;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mediaplayer = new MediaPlayer();
        mediaplayer.setLooping(true);
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaplayer.setVolume(15.0f,15.0f);
        mediaplayer.getAudioSessionId();
        mediaplayer.setOnCompletionListener(this);

        PresetReverb mReverb = new PresetReverb(0, mediaplayer.getAudioSessionId());
        mReverb.setPreset(PresetReverb.PRESET_LARGEROOM);
        mReverb.setEnabled(true);
        mediaplayer.attachAuxEffect(mReverb.getId());

        int sessionID = mediaplayer.getAudioSessionId();
        LoudnessEnhancer effect = new LoudnessEnhancer(sessionID);
        effect.setTargetGain(100);
        effect.setEnabled(true);

        BassBoost bassBoost = new BassBoost(0, 0);
        BassBoost.Settings bassBoostSettingTemp =  bassBoost.getProperties();
        BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
        bassBoostSetting.strength = 1000;
        bassBoost.setProperties(bassBoostSetting);
        bassBoost.setEnabled(true);

        bassBoost.setStrength((short) 1000);
        mediaplayer.attachAuxEffect(bassBoost.getId());
        mediaplayer.setAuxEffectSendLevel(bassBoost.getId());
        try {
            mediaplayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaplayer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mediaplayer.isPlaying()) {
            mediaplayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaplayer.release();
    }

    public void onCompletion(MediaPlayer _mediaPlayer) {
        stopSelf();
    }
}
