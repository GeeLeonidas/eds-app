package com.example.controleestoquees;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.concurrent.Semaphore;

public class NotificationService extends Service {

    Thread thread;
    private boolean wasDestroyed;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wasDestroyed = false;
        thread = new Thread(() -> {
            while (!wasDestroyed) {
                boolean wasHidden = false;
                final long delay = 5;
                int cooldown = 60_000;
                while (cooldown > 0) {
                    SystemClock.sleep(delay);
                    if (!wasHidden && isForegrounded()) {
                        hideNotification(0);
                        hideNotification(1);
                        wasHidden = true;
                    }
                    cooldown -= delay;
                }

                if (!isForegrounded() && Api.checkAuth()) {
                    System.out.println("Checando produtos...");
                    Api.updateItemArray();
                    StringBuilder standContent = new StringBuilder();
                    StringBuilder stockContent = new StringBuilder();
                    for (ProductItem item : Api.getItemArray()) {
                        if (item.countStand <= item.countStandAlert) {
                            System.out.println("Poucos itens de \"" + item.name + "\" nas prateleiras!");
                            standContent.append(item.name).append('\n');
                        }
                        if (item.countStock <= item.countStockAlert) {
                            System.out.println("Poucos itens de \"" + item.name + "\" no estoque!");
                            stockContent.append(item.name).append('\n');
                        }
                    }
                    if (standContent.length() > 0) {
                        standContent.deleteCharAt(standContent.length() - 1);
                        showNotification(0, "Baixo nível nas prateleiras!", standContent.toString());
                    }
                    if (stockContent.length() > 0) {
                        stockContent.deleteCharAt(stockContent.length() - 1);
                        showNotification(1, "Baixo nível no estoque!", stockContent.toString());
                    }
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread.start();
        System.out.println("Iniciando serviço de checagem da lista!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        wasDestroyed = true;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Not an error
        }
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(int notificationId, String title, String content) {
        String CHANNEL_ID = "notif_channel";
        NotificationChannel notificationChannel =
                new NotificationChannel(CHANNEL_ID,"Notificações de estoque/prateleiras", NotificationManager.IMPORTANCE_HIGH);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 1, new Intent(), PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(notificationId, notification);
    }

    private void hideNotification(int notificationId) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    private boolean isForegrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return appProcessInfo.importance == IMPORTANCE_FOREGROUND ||
                        appProcessInfo.importance == IMPORTANCE_VISIBLE;

    }
}
