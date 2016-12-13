package com.example.ehar.serviceexample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;

/**
 * Created by ehar on 11/17/2016.
 */

public class MusicService extends Service
        implements MediaPlayer.OnPreparedListener {

    public static final int NOTIFICATION_ID = 123;
    MediaPlayer player = null;

    private static MusicService instance = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MusicService getInstance() {
        return instance;
    }

    public void replay() {
        player.seekTo(0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = super.onStartCommand(intent, flags, startId);
        player = new MediaPlayer();
        File song = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC);
        String path = song.getPath() + "/" + "my_old_friend.mp3";
        try {
            player.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            return i;
        }

        // having media player run in own background thread
        player.setOnPreparedListener(this);
        player.prepareAsync();
        //player.start();
        return i;
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        instance = null;

        NotificationManager nmgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nmgr.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
       mediaPlayer.start();

        // build a notification object

        // create a notification builder object
        Notification.Builder bldr =
            new Notification.Builder(this).
                    setSmallIcon(R.drawable.notification_icon).
                    setContentTitle("My Music App").
                    setContentText("Go to app");

        NotificationManager nmgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // set up code to go back to app with notification action

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        bldr.setContentIntent(pendingIntent);

        nmgr.notify(NOTIFICATION_ID, bldr.build());

    }
}
