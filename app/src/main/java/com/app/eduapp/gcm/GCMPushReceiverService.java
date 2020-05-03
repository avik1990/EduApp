package com.app.eduapp.gcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.app.eduapp.Dashboard;
import com.app.eduapp.R;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.ModelInbox;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;


public class GCMPushReceiverService extends FirebaseMessagingService {
    static NotificationManager manager;
    static Notification myNotication;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        String from = message.getFrom();
        Map data = message.getData();
        String msg = "";
        String imageurl = "";
        String title = "";
        String subtitle = "";
        String msg_flag = "";
        try {
            if (data != null) {
                try {
                    imageurl = data.get("imgurl").toString();
                } catch (Exception e) {
                    imageurl = "";
                }
                title = data.get("title").toString();
                subtitle = data.get("subtitle").toString();
                msg = data.get("msg").toString();
                msg_flag = "1";
            }
            if (imageurl.isEmpty() || imageurl.equals("")) {
                generateNotification(this, msg, title, msg, msg_flag);
            } else {
                generateNotification(this, msg, imageurl, title, msg, msg_flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateNotification(Context mContext, String message, String title, String subtitle, String flag) {
        EdUtils.setNotificationMessage(mContext, message.trim(), "EPreference");
        setBigPictureStyleNotification(mContext, title, message, flag);
    }

    public void setBigPictureStyleNotification(Context mContext, String title, String msg, String flag) {
        LCDatabaseHandler db = LCDatabaseHandler.getInstance(mContext);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        int year = c.get(Calendar.YEAR);
        String current_time = hour + ":" + minutes + ":" + sec;
        String day = EdUtils.getFormasssttedDate1("" + year + "-" + month + "-" + date);
        String day1 = EdUtils.getFormasssttedDate31("" + year + "-" + month + "-" + date) + " " + current_time;

        db.insertInbox(new ModelInbox("", title, day, EdUtils.getNotificationMessage(mContext), msg, flag, day1, "", "", year + "" + month + "" + date));

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this, Dashboard.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Dashboard.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "EduCare1235";
            String channelName = "EduCareChannel12345";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(this.getString(R.string.app_name))
                    .setContentText(msg)
                    .setTicker("Notification from #EduCare")
                    .setContentIntent(resultPendingIntent)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(m, notification.build());
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("Notification from #EduCare")
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setDefaults(Notification.DEFAULT_ALL);

            mBuilder.setContentIntent(resultPendingIntent);
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(m, mBuilder.build());
        }

        /* NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setTicker("Notification from #EduCare");
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setColor(0xffea00);
        }
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setLargeIcon((BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher)));
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(msg);
        Intent resultIntent = new Intent(this, Dashboard.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Dashboard.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        mNotificationManager.notify(m, mBuilder.build());*/
    }

    public Notification setBigPictureStyleNotification(Context mContext, String imageurl, String title, String subtitle, String flag) {
        Bitmap remote_picture = null;
        LCDatabaseHandler db = LCDatabaseHandler.getInstance(mContext);

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle(title);
        notiStyle.setSummaryText(subtitle);
        try {
            Bitmap bmURL = getBitmapFromURL(imageurl);
            remote_picture = bmURL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        notiStyle.bigPicture(remote_picture);
        Intent resultIntent = new Intent(this, Dashboard.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Dashboard.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        String min;
        if (minutes > 9) {
            min = String.valueOf(minutes);
        } else {
            min = 0 + ":" + String.valueOf(minutes);
        }

        String current_time = "" + hour + ":" + min;
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int year = c.get(Calendar.YEAR);

        String day = EdUtils.getFormasssttedDate1("" + year + "-" + month + "-" + date);
        db.insertInbox(new ModelInbox("", title, day, EdUtils.getNotificationMessage(mContext), subtitle, flag, day, "", imageurl, year + "" + month + "" + date));

        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(subtitle))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(subtitle)
                .setLargeIcon((BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher)))
                .setStyle(notiStyle).build();
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
            //   return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void generateNotification(Context mContext, String message, String imageurl, String title, String subtitle, String flag) {
        EdUtils.setNotificationMessage(mContext, message.trim(), "EPreference");
        //Eutils.setNotificationFlag(mContext, flag.trim());
        long when = System.currentTimeMillis();
        if (!imageurl.isEmpty()) {
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, setBigPictureStyleNotification(mContext, imageurl, title, subtitle, flag));
        } else {
            Intent intent = new Intent(mContext, Dashboard.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, 0);
            Notification.Builder builder = new Notification.Builder(mContext);
            builder.setAutoCancel(true);
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);
            builder.setWhen(when);
            builder.build();
            myNotication = builder.getNotification();
            myNotication.defaults |= Notification.DEFAULT_SOUND;
            myNotication.flags |= Notification.FLAG_AUTO_CANCEL;
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            manager.notify(m, myNotication);
        }
    }

   /* private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_push : R.mipmap.ic_launchers;
    }*/
}
