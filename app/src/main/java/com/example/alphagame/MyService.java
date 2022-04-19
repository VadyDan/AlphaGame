package com.example.alphagame;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "ActLogs";
    static int CurrentlyRunning = 0;
    MediaPlayer player;

    public void onCreate() {
        player = MediaPlayer.create(this, R.raw.fon);
        player.setLooping(true); // зацикливаем
        player.setVolume(0.1f, 0.1f);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService: Bind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getIntExtra("pause", 0) == 0) {
            Log.d(TAG, "MyService: Pause");
            player.pause();
        }
        if(intent.getIntExtra("pause", 0) > 0) {
            Log.d(TAG, "MyService: Start");
            player.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        Log.d(TAG, "MyService: Destroy");
        CurrentlyRunning = 0;
        player.stop();
    }
}
