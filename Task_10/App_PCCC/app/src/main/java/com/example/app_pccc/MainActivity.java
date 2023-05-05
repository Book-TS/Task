package com.example.app_pccc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView textAlarm;
    private ImageView imageBell;
    private ImageView imageLight;
    private String val_1, val_2, val_3;
    private boolean status_1 = false;
    private boolean status_2 = false;
    private boolean status_3 = false;

    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar_1 = findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar_1);

        textAlarm = findViewById(R.id.text_alarm);
        imageBell = findViewById(R.id.image_bell);
        imageLight = findViewById(R.id.image_light);

        Intent intent_Service = new Intent(MainActivity.this, MyService.class);
        startService(intent_Service);

        mData.child("canh bao khoi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                val_1 = snapshot.getValue().toString();
                if(val_1.equals("on")) {
                    status_1 = true;
                    if((status_2)||(status_3)) {
                        textAlarm.setText("Cảnh báo phát hiện cháy");
                        textAlarm.setTextColor(Color.RED);
                    }
                    else {
                        textAlarm.setText("Cảnh báo phát hiện khói");
                        textAlarm.setTextColor(Color.rgb(221, 132, 0));
                    }

                    imageLight.setImageResource(R.drawable.alarm_2);
                    mData.child("trang thai den").setValue("on");
                    imageBell.setImageResource(R.drawable.bell_ring_2);
                    mData.child("trang thai chuong").setValue("on");

                    YoYo.with(Techniques.Swing)
                            .duration(400)
                            .repeat(10)
                            .playOn(imageBell);
                    YoYo.with(Techniques.FadeIn)
                            .duration(400)
                            .repeat(10)
                            .playOn(imageLight);
                }
                else {
                    status_1 = false;
                    if ((!status_2)&&(!status_3)) {
                        textAlarm.setText("Bình thường");
                        textAlarm.setTextColor(Color.rgb(1, 99, 90));
                        imageLight.setImageResource(R.drawable.alarm_1);
                        mData.child("trang thai den").setValue("off");
                        imageBell.setImageResource(R.drawable.bell_ring_1);
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
                        textAlarm.setText("Cảnh báo phát hiện cháy");
                        textAlarm.setTextColor(Color.RED);
                    }
                    else {
                        textAlarm.setText("Cảnh báo phát hiện lửa");
                        textAlarm.setTextColor(Color.rgb(221, 132, 0));
                    }

                    imageLight.setImageResource(R.drawable.alarm_2);
                    mData.child("trang thai den").setValue("on");
                    imageBell.setImageResource(R.drawable.bell_ring_2);
                    mData.child("trang thai chuong").setValue("on");

                    YoYo.with(Techniques.Swing)
                            .duration(400)
                            .repeat(10)
                            .playOn(imageBell);
                    YoYo.with(Techniques.FadeIn)
                            .duration(400)
                            .repeat(10)
                            .playOn(imageLight);
                }
                else {
                    status_2 = false;
                    if ((!status_1)&&(!status_3)) {
                        textAlarm.setText("Bình thường");
                        textAlarm.setTextColor(Color.rgb(1, 99, 90));
                        imageLight.setImageResource(R.drawable.alarm_1);
                        mData.child("trang thai den").setValue("off");
                        imageBell.setImageResource(R.drawable.bell_ring_1);
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
                    textAlarm.setText("Cảnh báo có cháy");
                    textAlarm.setTextColor(Color.RED);
                    imageLight.setImageResource(R.drawable.alarm_2);
                    mData.child("trang thai den").setValue("on");
                    imageBell.setImageResource(R.drawable.bell_ring_2);
                    mData.child("trang thai chuong").setValue("on");

                    YoYo.with(Techniques.Swing)
                            .duration(400)
                            .repeat(10)
                            .playOn(imageBell);
                    YoYo.with(Techniques.FadeIn)
                            .duration(400)
                            .repeat(10)
                            .playOn(imageLight);
                }
                else {
                    status_3 = false;
                    if ((!status_1)&&(!status_2)) {
                        textAlarm.setText("Bình thường");
                        textAlarm.setTextColor(Color.rgb(1, 99, 90));
                        imageLight.setImageResource(R.drawable.alarm_1);
                        mData.child("trang thai den").setValue("off");
                        imageBell.setImageResource(R.drawable.bell_ring_1);
                        mData.child("trang thai chuong").setValue("off");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}