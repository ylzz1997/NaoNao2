package com.naonao.lhy.naonao2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.naonao.lhy.naonao2.bean.Nao;

import java.io.FileNotFoundException;

public class Notice extends Service {

    private String channelID = "1";
    private String channelName = "Nao";
    private Bitmap bigPicture = null;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyApplication application = (MyApplication) this.getApplication();
        Nao nao = application.getNaoList().get(intent.getIntExtra("id",0));
        Log.d("Nao", "onStart: 闹:"+nao.name);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
/*
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(this,channelID)
                .setContentTitle(nao.Title)
                .setContentText(nao.Brief)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build();
        manager.notify(1, notification);

    */

        if(nao.bigPicture!=null){
            try {
                bigPicture = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(nao.bigPicture)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT>=26){
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            Notification.Builder notify = getChannelNotification(nao.Title, nao.Brief);
            if(bigPicture!=null){
                notify.setStyle(new Notification.BigPictureStyle().bigPicture(bigPicture));
            }
            Notification notification = notify.build();
            manager.notify(1, notification);
        }else{
            NotificationCompat.Builder notify = getNotification_25(nao.Title, nao.Brief);
            if(bigPicture!=null){
                notify.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture));
            }
            Notification notification = notify.build();
            manager.notify(1, notification);
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content){
        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setChannelId(channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
    }

    public NotificationCompat.Builder getNotification_25(String title, String content){
        return new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
    }
}
