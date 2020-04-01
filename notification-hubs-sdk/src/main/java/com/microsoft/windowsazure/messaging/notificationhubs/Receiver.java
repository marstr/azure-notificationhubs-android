package com.microsoft.windowsazure.messaging.notificationhubs;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Receiver extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;
    private final NotificationHub notificationHub;

    public Receiver() {
        this(NotificationHub.getInstance());
    }

    public Receiver(NotificationHub hub) {
        notificationHub = hub;
    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()) {
                            return;
                        }

                        notificationHub.setInstancePushChannel(task.getResult().getToken());
                    }
                }
        );
    }

    @Override
    public void onNewToken(String token) {
        notificationHub.setInstancePushChannel(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Intent i = new Intent("notificationReceived");
        i.putExtra("message", message);
        broadcaster.sendBroadcast(i);
    }

    @Override
    public void onDeletedMessages(){

    }
}
