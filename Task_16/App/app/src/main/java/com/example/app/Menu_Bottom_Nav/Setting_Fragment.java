package com.example.app.Menu_Bottom_Nav;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.SettingData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Setting_Fragment extends Fragment {

    private View fm_setting;
    private Button bt_update;
    private FloatingActionButton fab;
    private EditText temp_min, temp_max, temp, time;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm_setting = inflater.inflate(R.layout.fragment_setting, container, false);

        anhXa();

        updateEvent();

        FloatingButton();

        realtimeData();

        return fm_setting;
    }

    private void anhXa() {
        bt_update = fm_setting.findViewById(R.id.bt_update);
        fab = fm_setting.findViewById(R.id.fab);
        temp_min = fm_setting.findViewById(R.id.temp_min);
        temp_max = fm_setting.findViewById(R.id.temp_max);
        temp = fm_setting.findViewById(R.id.temp);
        time = fm_setting.findViewById(R.id.time);
    }

    private void updateEvent() {
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingData settingData = new SettingData(Integer.parseInt(temp_max.getText().toString()),
                        Integer.parseInt(temp_min.getText().toString()), Integer.parseInt(temp.getText().toString()),
                        Integer.parseInt(time.getText().toString()));
                mData.child("Setting").setValue(settingData);
                Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FloatingButton() {
        fab.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), History_Activity.class);
            startActivity(intent);
        });
    }

    private void realtimeData() {
        mData.child("Setting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SettingData settingData = snapshot.getValue(SettingData.class);
                assert settingData != null;
                temp_min.setText(String.valueOf(settingData.tempMin));
                temp_max.setText(String.valueOf(settingData.tempMax));
                temp.setText(String.valueOf(settingData.temp));
                time.setText(String.valueOf(settingData.time));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}