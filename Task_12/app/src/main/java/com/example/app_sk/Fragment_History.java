package com.example.app_sk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_History extends Fragment {

    private View fm_history;
    private TextView txtTime;
    private RecyclerView historyView;
    private HistoryAdapter mHAdapter;
    private List<History_Data> mList;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    private String device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fm_history = inflater.inflate(R.layout.fragment_history, container, false);

        anhXa();
        read_Name();
        listView_1_Show();

        return fm_history;
    }

    private void anhXa() {
        historyView = fm_history.findViewById(R.id.historyView);
        txtTime = fm_history.findViewById(R.id.txtTime);
    }

    // Cấu hình list view
    private void listView_1_Show() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        historyView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        historyView.addItemDecoration(dividerItemDecoration);

        mList = new ArrayList<>();
        mHAdapter = new HistoryAdapter(mList);
        historyView.setAdapter(mHAdapter);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf_1 = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());
        String currentDateandTime_1 = sdf_1.format(new Date());
        txtTime.setText("Ngày " + currentDateandTime_1);

//        History_Data history_data = new History_Data(140, 80, "11:30:20");
//        mData.child("devices_1/history/" + currentDateandTime).push().setValue(history_data);

        mData.child(device + "/history/" + currentDateandTime).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History_Data history = dataSnapshot.getValue(History_Data.class);
                    mList.add(history);
                }
                mHAdapter.notifyDataSetChanged();
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
