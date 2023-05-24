package com.example.app_sk;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
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
    private String device_Name;

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
        realTimeData();

        return super.onStartCommand(intent, flags, startId);
    }

    private void realTimeData() {
        read_Name();
        mData.child(device_Name + "/present_val").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                History_Data history_data = snapshot.getValue(History_Data.class);
                if(history_data != null) {
                    if(history_data.hr <= 40) sendNotification_1();
                    else if(history_data.hr >= 140) sendNotification_2();

                    if(history_data.spo2 <= 95) sendNotification_3();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateandTime = sdf.format(new Date());

        mData.child(device_Name + "/" + currentDateandTime + "/2").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                History_Data history = snapshot.getValue(History_Data.class);
                assert history != null;
                History_Data history_data = new History_Data(history.hr, history.spo2, history.time + " " + currentDateandTime);
                mData.child(device_Name + "/present_val").setValue(history_data);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Lấy tên thiết bị
    private void read_Name() {
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            for (UserInfo profile : user1.getProviderData()) {
                String name_1 = profile.getEmail();

                if (name_1 != null) {
                    if(name_1.equals("hung49696@donga.edu.vn")) {
                        device_Name = "devices_1";
                    }
                    else if (name_1.equals("an49966@donga.edu.vn")) {
                        device_Name = "devices";
                    }
                }
            }
        }
    }

    private void sendNotification_1() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.icon_app);
        mBuilder.setContentTitle("Cảnh báo nhịp tim thấp");
        mBuilder.setContentText("Cảnh báo nhịp tim của bạn đang thấp");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplication());
        managerCompat.notify(1, mBuilder.build());
    }

    private void sendNotification_2() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.icon_app);
        mBuilder.setContentTitle("Cảnh báo nhịp tim cao");
        mBuilder.setContentText("Cảnh báo nhịp tim của bạn đang cao");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplication());
        managerCompat.notify(2, mBuilder.build());
    }

    private void sendNotification_3() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.icon_app);
        mBuilder.setContentTitle("Cảnh báo spo2");
        mBuilder.setContentText("Cảnh báo nồng độ spo2 của bạn đang thấp");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplication());
        managerCompat.notify(3, mBuilder.build());
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