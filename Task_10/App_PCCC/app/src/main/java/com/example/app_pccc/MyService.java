package com.example.app_pccc;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {

    DatabaseReference mData;
    private static final String CHANNEL_ID = "simplified_coding";
    private static final String CHANNEL_NAME = "Simplified Coding";
    private static final String CHANNEL_DESC = "Android Push Notification Tutorial";

    private String val_1, val_2, val_3;
    private Boolean status_1 = false;
    private Boolean status_2 = false;
    private Boolean status_3 = false;

    public MyService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mData = FirebaseDatabase.getInstance().getReference();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        mData.child("canh bao khoi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                val_1 = snapshot.getValue().toString();
                if(val_1.equals("on")) {
                    status_1 = true;
                    if((status_2)||(status_3)) {
                        sendNotification_3();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        History_Data history_data = new History_Data("Cảnh báo cháy","Báo động có cháy", currentDateandTime);
                        mData.child("Alarm_History").push().setValue(history_data);
                    }
                    else {
                        sendNotification_1();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        History_Data history_data = new History_Data("Cảnh báo khói","Cảnh báo phát hiện khói", currentDateandTime);
                        mData.child("Alarm_History").push().setValue(history_data);
                    }

                    mData.child("trang thai den").setValue("on");
                    mData.child("trang thai chuong").setValue("on");
                }
                else {
                    status_1 = false;
                    if ((!status_2)&&(!status_3)) {
                        mData.child("trang thai den").setValue("off");
                        mData.child("trang thai chuong").setValue("off");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("canh bao nhiet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                val_2 = snapshot.getValue().toString();
                if(val_2.equals("on")) {
                    status_2 = true;

                    if((status_1)||(status_3)) {
                        sendNotification_3();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        History_Data history_data = new History_Data("Cảnh báo cháy","Báo động có cháy", currentDateandTime);
                        mData.child("Alarm_History").push().setValue(history_data);
                    }
                    else {
                        sendNotification_2();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        History_Data history_data = new History_Data("Cảnh báo nhiệt","Cảnh báo phát hiện lửa", currentDateandTime);
                        mData.child("Alarm_History").push().setValue(history_data);
                    }

                    mData.child("trang thai den").setValue("on");
                    mData.child("trang thai chuong").setValue("on");
                }
                else {
                    status_2 = false;
                    if ((!status_1)&&(!status_3)) {
                        mData.child("trang thai den").setValue("off");
                        mData.child("trang thai chuong").setValue("off");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("nut nhan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                val_3 = snapshot.getValue().toString();
                if(val_3.equals("on")) {
                    status_3 = true;
                    if((status_1)&&(status_2)) {
                        sendNotification_3();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        History_Data history_data = new History_Data("Cảnh báo cháy","Báo động có cháy", currentDateandTime);
                        mData.child("Alarm_History").push().setValue(history_data);
                    }
                    else {
                        sendNotification_4();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        History_Data history_data = new History_Data("Cảnh báo nhấn nút","Có người nhấn nút báo động", currentDateandTime);
                        mData.child("Alarm_History").push().setValue(history_data);
                    }

                    mData.child("trang thai den").setValue("on");
                    mData.child("trang thai chuong").setValue("on");
                }
                else {
                    status_3 = false;
                    if ((!status_1)&&(!status_2)) {
                        mData.child("trang thai den").setValue("off");
                        mData.child("trang thai chuong").setValue("off");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification_1() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.warning);
        mBuilder.setContentTitle("Cảnh báo khói");
        mBuilder.setContentText("Cảnh báo phát hiện khói");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplication());
        managerCompat.notify(1, mBuilder.build());
    }

    private void sendNotification_2() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.warning);
        mBuilder.setContentTitle("Cảnh báo lửa");
        mBuilder.setContentText("Cảnh báo phát hiện lửa");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplication());
        managerCompat.notify(2, mBuilder.build());
    }

    private void sendNotification_3() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.warning);
        mBuilder.setContentTitle("Cảnh báo cháy");
        mBuilder.setContentText("Báo động có cháy");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplication());
        managerCompat.notify(3, mBuilder.build());
    }
    private void sendNotification_4() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.warning);
        mBuilder.setContentTitle("Cảnh báo nhấn nút");
        mBuilder.setContentText("Có người nhấn nút báo động");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplication());
        managerCompat.notify(4, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}