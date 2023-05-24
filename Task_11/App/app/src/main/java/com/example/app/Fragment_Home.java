package com.example.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Fragment_Home extends Fragment {
    private View fm_home;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    private SwitchCompat switch1, switch2;
    private TextView timeUpdate;
    private ArcProgress arc1, arc2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fm_home = inflater.inflate(R.layout.fragment_home, container, false);

        anhXa();

        realTime();

        control();
        return fm_home;
    }

    private void anhXa() {
        switch1 = fm_home.findViewById(R.id.switch1);
        switch2 = fm_home.findViewById(R.id.switch2);
        timeUpdate = fm_home.findViewById(R.id.timeUpdate);

        arc1 = fm_home.findViewById(R.id.arc1);
        arc2 = fm_home.findViewById(R.id.arc2);
    }

    private void realTime() {
        mData.child("app/relay_1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch1.setChecked(Objects.requireNonNull(snapshot.getValue()).hashCode() == 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("app/relay_2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch2.setChecked(Objects.requireNonNull(snapshot.getValue()).hashCode() == 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("giatri/thoigian").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeUpdate.setText("Thời gian cập nhật " + snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("giatri/nhietdo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arc1.setProgress(Objects.requireNonNull(snapshot.getValue()).hashCode());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mData.child("giatri/mucnuoc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arc2.setProgress(Objects.requireNonNull(snapshot.getValue()).hashCode());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void control() {
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(switch1.isChecked()) mData.child("app/relay_1").setValue(1);
            else mData.child("app/relay_1").setValue(0);
        });

        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(switch2.isChecked()) mData.child("app/relay_2").setValue(1);
            else mData.child("app/relay_2").setValue(0);
        });

        arc1.setOnClickListener(v -> mData.child("app/doc_1").setValue(1));

        arc2.setOnClickListener(v -> mData.child("app/doc_2").setValue(1));
    }
}
