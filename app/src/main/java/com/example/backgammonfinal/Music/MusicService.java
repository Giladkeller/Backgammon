package com.example.backgammonfinal.Music;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.core.app.NotificationCompat;
import androidx.media.app.NotificationCompat.MediaStyle;

import com.example.backgammonfinal.R;

import java.util.ArrayList;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new MusicBinder();
    private MediaSessionCompat mediaSession;
    private ArrayList<Uri> playlist = new ArrayList<>();
    private ArrayList<String> songTitles = new ArrayList<>();
    private int currentTrackIndex = 0;

    // ממשק (Interface) שיאפשר לעדכן את האקטיביטי כשהשיר מתחלף אוטומטית ברקע
    public interface OnTrackChangedListener {
        void onTrackChanged(Uri uri, String title);
    }
    private OnTrackChangedListener trackChangedListener;

    public void setOnTrackChangedListener(OnTrackChangedListener listener) {
        this.trackChangedListener = listener;
    }

    public static final String ACTION_PLAY_PAUSE = "action_play_pause";
    private static final String CHANNEL_ID = "music_channel";

    public class MusicBinder extends Binder {
        MusicService getService() { return MusicService.this; }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(this, "MusicService");
        createNotificationChannel();
    }

    public void setPlaylist(ArrayList<Uri> uris, ArrayList<String> titles) {
        this.playlist = uris;
        this.songTitles = titles;
        this.currentTrackIndex = 0;
        if (!playlist.isEmpty()) {
            playCurrentTrack();
        }
    }

    // מתודת הניגון הפנימית של הרצועה הנוכחית
    private void playCurrentTrack() {
        if (playlist.isEmpty() || currentTrackIndex >= playlist.size()) {
            // אם הגענו לסוף הרשימה, נחזור להתחלה
            currentTrackIndex = 0;
            if (playlist.isEmpty()) return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        Uri uri = playlist.get(currentTrackIndex);
        String title = songTitles.get(currentTrackIndex);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setLooping(false);

            // מנגנון המעבר האוטומטי: כשהשיר מסתיים, המשתנה מקודם והשיר הבא מנוגן
            mediaPlayer.setOnCompletionListener(mp -> {
                currentTrackIndex++;
                playCurrentTrack();
            });

            // עדכון ההתראה בוילון
            showNotification(title);

            // עדכון ה-UI באקטיביטי במידה והיא פתוחה ומחוברת
            if (trackChangedListener != null) {
                trackChangedListener.onTrackChanged(uri, title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null && ACTION_PLAY_PAUSE.equals(intent.getAction())) {
//            togglePlayPause();
//            // עדכון ההתראה כדי לשקף את המצב החדש (Play/Pause)
//            showNotification(lastSongTitle);
//        }
//        return START_STICKY;
//    }

    // פונקציה שנקראת כשהמשתמש סוגר את האפליקציה מהתפריט הראשי
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // מפסיק את השירות ומשחרר את הנגן כשהאפליקציה נסגרת לגמרי
        stopSelf();
    }

//    public void playSong(Uri uri, String songName) {
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//        }
//        currentUri = uri;
//        lastSongTitle = songName;
//        mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(this, uri);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//            mediaPlayer.setLooping(true);
//            showNotification(songName);
//        } catch (Exception e) { e.printStackTrace(); }
//    }

    public boolean isPlaying() { return mediaPlayer != null && mediaPlayer.isPlaying(); }
    public Uri getCurrentUri() {
        if (!playlist.isEmpty() && currentTrackIndex < playlist.size()) {
            return playlist.get(currentTrackIndex);
        }
        return null;
    }

    public String getCurrentTitle() {
        if (!songTitles.isEmpty() && currentTrackIndex < songTitles.size()) {
            return songTitles.get(currentTrackIndex);
        }
        return "שש-בש מוזיקה";
    }

    public void togglePlayPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                stopForeground(false);
            } else {
                mediaPlayer.start();
                showNotification(getCurrentTitle());
            }
        }
    }

    private void showNotification(String title) {
        Intent notificationIntent = new Intent(this, MusicActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent playPauseIntent = new Intent(this, MusicService.class).setAction(ACTION_PLAY_PAUSE);
        PendingIntent playPausePending = PendingIntent.getService(this, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE);

        int icon = isPlaying() ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Backgammon Music: " + title)
                .setOngoing(isPlaying())
                .setContentIntent(pendingIntent)
                .addAction(new NotificationCompat.Action(icon, isPlaying() ? "Pause" : "Play", playPausePending))
                .setStyle(new MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0)) // מציג את כפתור ה-Play/Pause גם בהתראה המצומצמת
                .setPriority(NotificationCompat.PRIORITY_LOW);

        // אם מנגן - מפעיל Foreground Service אם לא - רק מעדכן את ההתראה
        if (isPlaying()) {
            startForeground(1, builder.build());
        } else {
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.notify(1, builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Music", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) { return binder; }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaSession.release();
        super.onDestroy();
    }
}