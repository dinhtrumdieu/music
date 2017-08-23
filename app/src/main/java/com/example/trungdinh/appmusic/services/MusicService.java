package com.example.trungdinh.appmusic.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.activitys.MainActivity;
import com.example.trungdinh.appmusic.activitys.PlaySongActivity;
import com.example.trungdinh.appmusic.controls.Controls;
import com.example.trungdinh.appmusic.fragments.PlaySongFragment;
import com.example.trungdinh.appmusic.models.Music;
import com.example.trungdinh.appmusic.receivers.NotificationBroadcast;
import com.example.trungdinh.appmusic.utils.PlayerConstants;
import com.example.trungdinh.appmusic.utils.UtilFunctions;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service implements AudioManager.OnAudioFocusChangeListener {

    public static MediaPlayer mp;
    int NOTIFICATION_ID = 1111;
    public static final String NOTIFY_PREVIOUS = "com.tutorialsface.audioplayer.previous";
    public static final String NOTIFY_DELETE = "com.tutorialsface.audioplayer.delete";
    public static final String NOTIFY_PAUSE = "com.tutorialsface.audioplayer.pause";
    public static final String NOTIFY_PLAY = "com.tutorialsface.audioplayer.play";
    public static final String NOTIFY_NEXT = "com.tutorialsface.audioplayer.next";
    AudioManager audioManager;
    private ComponentName remoteComponentName;
    private RemoteControlClient remoteControlClient;
    private static boolean currentVersionSupportLockScreenControls = false;
    private static Timer timer;

    @Override
    public void onCreate() {
        mp = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        timer = new Timer();
        //
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Controls.nextControl(getApplicationContext());
            }
        });
        currentVersionSupportLockScreenControls = UtilFunctions.currentVersionSupportLockScreenControls();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Music data = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
        if (currentVersionSupportLockScreenControls) {
            RegisterRemoteClient();
        }
        String songPath = data.getData();
        playSong(songPath, data);
        newsNotification();

        PlayerConstants.SONG_CHANGE_HANDLER = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.d("xxv", "xx");
                Music music = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
                String path = music.getData();
                newsNotification();
                try {
                    playSong(path, music);
                    MainActivity.updateUI();
                    MainActivity.changeButton();
                    PlaySongActivity.changeUI();
                    PlaySongFragment.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        PlayerConstants.PLAY_PAUSE_HANDLER = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                String message = (String) msg.obj;
                if (mp == null)
                    return false;
                if (message.equalsIgnoreCase(getResources().getString(R.string.play))) {
                    PlayerConstants.SONG_PAUSED = false;
                    if (currentVersionSupportLockScreenControls) {
                        remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
                    }
                    mp.start();
                } else if (message.equalsIgnoreCase(getResources().getString(R.string.pause))) {
                    PlayerConstants.SONG_PAUSED = true;
                    if (currentVersionSupportLockScreenControls) {
                        remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
                    }
                    mp.pause();
                }
                newsNotification();
                try {
                    MainActivity.changeButton();
                    PlaySongActivity.changeButton();
                    PlaySongFragment.animate();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("TAG", "TAG Pressed: " + message);
                return false;
            }
        });

        return START_STICKY;
    }

    private class MainTask extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mp != null) {
                int progress = (mp.getCurrentPosition() * 100) / mp.getDuration();
                Integer i[] = new Integer[3];
                i[0] = mp.getCurrentPosition();
                i[1] = mp.getDuration();
                i[2] = progress;
                try {
                    PlayerConstants.PROGRESSBAR_HANDLER.sendMessage(PlayerConstants.PROGRESSBAR_HANDLER.obtainMessage(0, i));
                } catch (Exception e) {
                }
            }
        }
    };

    private void playSong(String songPath, Music music) {
        try {
            if (currentVersionSupportLockScreenControls) {
                UpdateMetadata(music);
                remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
            }

            mp.reset();
            mp.setDataSource(songPath);
            mp.prepare();
            mp.start();
            timer.scheduleAtFixedRate(new MainTask(), 0, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void newsNotification() {
        String songName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getNameSong();
        String albumName = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getAbum();
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);

        Notification notification = new NotificationCompat.Builder(getApplicationContext()).setContentIntent(createContentIntent())
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(songName).build();

        setListeners(simpleContentView);
        notification.contentView = simpleContentView;

        if (PlayerConstants.SONG_PAUSED) {
            notification.contentView.setViewVisibility(R.id.btnPause, View.GONE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
        } else {
            notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);
        }

        notification.contentView.setTextViewText(R.id.textSongName, songName);
        notification.contentView.setTextViewText(R.id.textAlbumName, albumName);

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(NOTIFICATION_ID, notification);
    }

    public void setListeners(RemoteViews view) {
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);
        Intent previous = new Intent(NOTIFY_PREVIOUS);

        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

        PendingIntent pRrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pRrevious);

    }

    private PendingIntent createContentIntent() {
        //có intent rồi thì lấy Bundle dựa vào MyPackage
        Intent notIntent = new Intent(this, PlaySongActivity.class);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendInt;
    }

    @Override
    public void onDestroy() {
        if (mp != null) {
            mp.stop();
            mp = null;
        }
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void RegisterRemoteClient() {
        remoteComponentName = new ComponentName(getApplicationContext(), new NotificationBroadcast().ComponentName());
        try {
            if (remoteControlClient == null) {
                audioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                audioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(
                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE |
                            RemoteControlClient.FLAG_KEY_MEDIA_STOP |
                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
        } catch (Exception ex) {
        }
    }

    @SuppressLint("NewApi")
    private void UpdateMetadata(Music data) {

        if (remoteControlClient == null)
            return;
        RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, data.getAbum());
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, data.getNameSinger());
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, data.getData());

        metadataEditor.apply();
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }


}
