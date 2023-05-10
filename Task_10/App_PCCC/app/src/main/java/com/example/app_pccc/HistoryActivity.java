package com.example.app_pccc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private Toolbar toolbar_1;
    private RecyclerView historyView;
    private HistoryAdapter mHAdapter;
    private List<History_Data> mList;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar_1 = findViewById(R.id.toolbar_1);

        setSupportActionBar(toolbar_1);
        toolbar_1.setNavigationOnClickListener(v -> onBackPressed());

        historyView = findViewById(R.id.historyView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryActivity.this);
        historyView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(HistoryActivity.this, DividerItemDecoration.VERTICAL);
        historyView.addItemDecoration(dividerItemDecoration);

        mList = new ArrayList<>();
        mHAdapter = new HistoryAdapter(mList);
        historyView.setAdapter(mHAdapter);

        mData.child("Alarm_History").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    History_Data history_data = dataSnapshot.getValue(History_Data.class);
                    mList.add(history_data);
                }
                mHAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}