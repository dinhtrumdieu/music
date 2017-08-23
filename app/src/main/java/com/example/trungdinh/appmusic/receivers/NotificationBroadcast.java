package com.example.trungdinh.appmusic.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.example.trungdinh.appmusic.controls.Controls;
import com.example.trungdinh.appmusic.services.MusicService;


public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;
        } else {
            if (intent.getAction().equals(MusicService.NOTIFY_PLAY)) {
                Controls.playControl(context);
            } else if (intent.getAction().equals(MusicService.NOTIFY_PAUSE)) {
                Controls.pauseControl(context);
            } else if (intent.getAction().equals(MusicService.NOTIFY_NEXT)) {
                Controls.nextControl(context);
            } else if (intent.getAction().equals(MusicService.NOTIFY_DELETE)) {
                Intent i = new Intent(context, MusicService.class);
                context.stopService(i);
               // Intent in = new Intent(context, MainActivity.class);
               // in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // context.startActivity(in);
            } else if (intent.getAction().equals(MusicService.NOTIFY_PREVIOUS)) {
                Controls.previousControl(context);
            }
        }
    }



    public String ComponentName() {
        return this.getClass().getName();
    }
}
