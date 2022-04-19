package com.example.alphagame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    static boolean Flag = false;
    static Intent intent;
    private static final String TAG_activity = "ActLogs";
    public ImageButton returnButton;
    public ImageButton musicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(TAG_activity,"Activity_Second: onCreate");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, new Fragment1()).commit();

        returnButton = (ImageButton)findViewById(R.id.backbutton);
        returnButton.setOnClickListener(this);

        musicButton = (ImageButton)findViewById(R.id.MusicInGame);
        musicButton.setOnClickListener(this);

        intent = new Intent(this, MyService.class);
        Flag = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG_activity,"Activity_Second: onStart");
        MyService.CurrentlyRunning++;
        if(MainActivity.music) {
            intent.putExtra("pause", MyService.CurrentlyRunning);
            startService(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG_activity,"Activity_Second: onStop");
        MyService.CurrentlyRunning--;
        if (MainActivity.music) {
            intent.putExtra("pause", MyService.CurrentlyRunning);
            startService(intent);
        }
        Flag = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_activity,"Activity_Second: onDestroy");
    }

    public void onClick(View view) {
        if(!MainActivity.Flag) {
            switch (view.getId()) {
                case R.id.backbutton:
                    Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP); //
                    startActivity(intent);
                    view.setEnabled(false);
                    this.finish();
                    break;
                case R.id.MusicInGame:
                    MainActivity.music = !MainActivity.music;
                    SharedPreferences.Editor editor = MainActivity.mSettings.edit();
                    editor.putBoolean(MainActivity.APP_PREFERENCES_MUSIC, MainActivity.music);
                    editor.apply();
                    if (MainActivity.music) {
                        intent = new Intent(this, MyService.class);
                        intent.putExtra("pause", MyService.CurrentlyRunning);
                        startService(intent);
                    } else {
                        intent = new Intent(this, MyService.class);
                        intent.putExtra("pause", 0);
                        startService(intent);
                    }
                    break;

            }
        }
    }

}
