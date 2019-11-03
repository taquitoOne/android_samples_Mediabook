package com.aospstudio.android.apps.mediabook.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class BackgroundService extends Service {

    private static final String TAG = null;
    MediaPlayer mediaplayer;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mediaplayer = new MediaPlayer();
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
}
