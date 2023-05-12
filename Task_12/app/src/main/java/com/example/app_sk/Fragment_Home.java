package com.example.app_sk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_Home extends Fragment {
    private View fm_home;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    private ArcProgress arc1, arc2;
    private TextView text_time;
    private String device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fm_home = inflater.inflate(R.layout.fragment_home, container, false);

        anhXa();

        read_Name();

        realTimeData();

        return fm_home;
    }

    private void anhXa() {
        arc1 = fm_home.findViewById(R.id.arc1);
        arc2 = fm_home.findViewById(R.id.arc2);
        text_time = fm_home.findViewById(R.id.text_time);
    }

    private void realTimeData() {
        mData.child(device + "/present_val").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                History_Data history_data = snapshot.getValue(History_Data.class);

                assert history_data != null;
                arc1.setProgress(history_data.hr);
                arc2.setProgress(history_data.spo2);
                text_time.setText("Thời gian cập nhật: " + history_data.time);
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
                        device = "devices_1";
                    }
                    else if (name_1.equals("an49966@donga.edu.vn")) {
                        device = "devices_2";
                    }
                }
            }
        }
    }
}
