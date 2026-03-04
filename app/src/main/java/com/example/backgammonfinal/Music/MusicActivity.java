package com.example.backgammonfinal.Music;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.backgammonfinal.StartActivities.MainActivity;
import com.example.backgammonfinal.R;

public class MusicActivity extends AppCompatActivity {

    private MusicService musicService;
    private boolean isBound = false;
    private AudioManager audioManager;
    private TextView tvSongName;
    private ImageView btnPlayPause;
    private ActivityResultLauncher<Intent> songPickerLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    // חיבור ל-Service
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            isBound = true;

            // עדכון ה-UI אם שיר כבר מתנגן ברקע
            if (musicService.getCurrentUri() != null) {
                updateUIForTrack(musicService.getCurrentUri());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        // אתחול הרשאות להתראות (עבור Android 13+)
        initPermissionLauncher();
        checkAndRequestNotificationPermission();

        // הפעלת ה-Service וחיבור אליו
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // קישור רכיבי UI
        tvSongName = findViewById(R.id.tvSongName);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        Button btnPick = findViewById(R.id.btnPickSong);
        Button btnBack = findViewById(R.id.btnBack);
        SeekBar volumeSeekBar = findViewById(R.id.volumeSeekBar);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // הגדרת SeekBar לווליום
        setupVolumeControl(volumeSeekBar);

        // אתחול בחירת קבצים
        initPicker();

        btnPick.setOnClickListener(v -> {
            Intent pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
            pickIntent.setType("audio/*");
            pickIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            songPickerLauncher.launch(pickIntent);
        });

        btnPlayPause.setOnClickListener(v -> {
            if (isBound) {
                musicService.togglePlayPause();
                updatePlayPauseIcon();
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent mainIntent = new Intent(MusicActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });
    }

    private void setupVolumeControl(SeekBar volumeSeekBar) {
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVol);
        volumeSeekBar.setProgress(curVol);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void initPicker() {
        songPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null && isBound) {
                            String title = getSongTitle(uri);
                            musicService.playSong(uri, title);
                            updateUIForTrack(uri);
                        }
                    }
                }
        );
    }

    private void updateUIForTrack(Uri uri) {
        displaySongName(uri);
        updatePlayPauseIcon();
    }

    private void updatePlayPauseIcon() {
        if (musicService != null && musicService.isPlaying()) {
            btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void displaySongName(Uri uri) {
        tvSongName.setText(getSongTitle(uri));
    }

    private String getSongTitle(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        String title = "Unknown Track";
        try {
            retriever.setDataSource(this, uri);
            title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (title == null) title = "Selected Track";
            retriever.release();
        } catch (Exception e) {
            title = "Music Playing";
        }
        return title;
    }

    // ניהול הרשאות התראה (Android 13 ומעלה)
    private void initPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(this, "Notification permission is required for the music player controls", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}