package com.example.controleestoquees;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;

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
                SystemClock.sleep(60_000);
                if (Api.checkAuth()) {
                    System.out.println("Checando produtos...");
                    Api.updateItemArray();
                    for (ProductItem item : Api.getItemArray()) {
                        if (item.countStand <= item.countStandAlert) {
                            System.out.println("Poucos itens de \"" + item.name + "\" nas prateleiras!");
                        }
                        if (item.countStock <= item.countStockAlert) {
                            System.out.println("Poucos itens de \"" + item.name + "\" no estoque!");
                        }
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
}
