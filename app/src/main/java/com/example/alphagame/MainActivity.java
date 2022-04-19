package com.example.alphagame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_MUSIC = "music";
    public static final String APP_PREFERENCES_LANGUAGE = "language";

    public static SharedPreferences mSettings;

    RecyclerView gamesList;

    GamesAdapter gamesAdapter;
    GamesAdapterEng gamesAdapterEng;
    GamesAdapterFre gamesAdapterFre;

    static Intent intent;

    private static SoundPool soundPool;

    AudioManager audioManager;

    // Maximumn sound stream.
    private static final int MAX_STREAMS = 1;

    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;

    private static boolean loaded;
    private static final String TAG_activity = "ActLogs";
    private static int soundIdlose;
    private static int soundIdwin;
    private static float volume;
    public ImageButton musicButton;
    public ImageButton languageButton;
    int imgLang;
    public static boolean music = true;

    static boolean Flag = false;
    static String lang = "rus";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        languageButton = (ImageButton)this.findViewById(R.id.lang);
        languageButton.setOnClickListener(this);

        musicButton = (ImageButton)this.findViewById(R.id.Muzika);
        musicButton.setOnClickListener(this);

        if (mSettings.contains(APP_PREFERENCES_MUSIC)) {
            music = mSettings.getBoolean(APP_PREFERENCES_MUSIC, false);
        }

        if (mSettings.contains(APP_PREFERENCES_LANGUAGE)) {
            lang = mSettings.getString(APP_PREFERENCES_LANGUAGE, "");
        }

        gamesList = (RecyclerView)findViewById(R.id.rv_games);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        gamesList.setLayoutManager(layoutManager);
        gamesList.setHasFixedSize(true);

        if(lang.equals("rus")) {
            gamesAdapter = new GamesAdapter(9, this);
            gamesList.setAdapter(gamesAdapter);
            languageButton.setImageDrawable(null);
            imgLang = getResources().getIdentifier("rus", "drawable", getPackageName());
            languageButton.setImageResource(imgLang);
        }
        else if(lang.equals("eng")){
            gamesAdapterEng = new GamesAdapterEng(9, this);
            gamesList.setAdapter(gamesAdapterEng);
            languageButton.setImageDrawable(null);
            imgLang = getResources().getIdentifier("eng", "drawable", getPackageName());
            languageButton.setImageResource(imgLang);
        }
        else if(lang.equals("fre")){
            gamesAdapterFre = new GamesAdapterFre(9, this);
            gamesList.setAdapter(gamesAdapterFre);
            languageButton.setImageDrawable(null);
            imgLang = getResources().getIdentifier("fre", "drawable", getPackageName());
            languageButton.setImageResource(imgLang);
        }

        Log.d(TAG_activity,"Activity_main: onCreate " + lang);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        Bundle arguments = getIntent().getExtras();

        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        this.volume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        this.setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        // Load sound file (destroy.wav) into SoundPool.
        this.soundIdlose = this.soundPool.load(this, R.raw.lose,1);

        // Load sound file (gun.wav) into SoundPool.
        this.soundIdwin = this.soundPool.load(this, R.raw.win,1);

    }

    public static void playSoundwin(View view)  {
        if(loaded && music)  {
            float leftVolumn = volume;
            float rightVolumn = volume;
            // Play sound of gunfire. Returns the ID of the new stream.
            int streamId = soundPool.play(soundIdwin,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }

    public static void playSoundlose(View view)  {
        if(loaded && music)  {
            float leftVolumn = volume;
            float rightVolumn = volume;

            // Play sound objects destroyed. Returns the ID of the new stream.
            int streamId = soundPool.play(soundIdlose,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG_activity,"Activity_main: onStart");
//        MyService.CurrentlyRunning++;
        Flag = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG_activity,"Activity_main: onStop");
        MyService.CurrentlyRunning--;
        if(music) {
            intent.putExtra("pause", MyService.CurrentlyRunning);
            startService(intent);
        }
        Flag = false;
        //Log.d("huilo", "MA" + MyService.thread.getState().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_activity,"Activity_main: onDestroy");
        if(music) {
            stopService(new Intent(this, MyService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG_activity,"Activity_main: onResume");
        if(MyService.CurrentlyRunning>0)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        MyService.CurrentlyRunning++;
        if(music) {
            intent = new Intent(this, MyService.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Log.d(TAG_activity, "Activity_main: musicStart");
            intent.putExtra("pause", MyService.CurrentlyRunning);
            startService(intent);
        }
    }


    @Override
    public void onClick(View view) {
        if(!SecondActivity.Flag) {
            switch (view.getId()) {
                case R.id.lang:
                    if (lang.equals("rus")) {
                        gamesAdapterEng = new GamesAdapterEng(9, this);
                        gamesList.setAdapter(gamesAdapterEng);
                        //gamesList.swapAdapter(gamesAdapterEng, false);
                        lang = "eng";
                        languageButton.setImageDrawable(null);
                        imgLang = getResources().getIdentifier("eng", "drawable", getPackageName());
                        languageButton.setImageResource(imgLang);
                        mSettings.edit().putString(APP_PREFERENCES_LANGUAGE, lang).apply();
                    } else if (lang.equals("eng")) {
                        gamesAdapterFre = new GamesAdapterFre(9, this);
                        gamesList.setAdapter(gamesAdapterFre);
                        //gamesList.swapAdapter(gamesAdapter, false);
                        lang = "fre";
                        languageButton.setImageDrawable(null);
                        imgLang = getResources().getIdentifier("fre", "drawable", getPackageName());
                        languageButton.setImageResource(imgLang);
                        mSettings.edit().putString(APP_PREFERENCES_LANGUAGE, lang).apply();
                    } else if (lang.equals("fre")) {
                        gamesAdapter = new GamesAdapter(9, this);
                        gamesList.setAdapter(gamesAdapter);
                        //gamesList.swapAdapter(gamesAdapter, false);
                        lang = "rus";
                        languageButton.setImageDrawable(null);
                        imgLang = getResources().getIdentifier("rus", "drawable", getPackageName());
                        languageButton.setImageResource(imgLang);
                        mSettings.edit().putString(APP_PREFERENCES_LANGUAGE, lang).apply();
                    }
                    break;
                case R.id.Muzika:
                    music = !music;
                    mSettings.edit().putBoolean(APP_PREFERENCES_MUSIC, music).apply();
//                editor.putBoolean();
//                editor.apply();
                    if (music) {
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
