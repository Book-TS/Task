package com.example.app.Menu_Bottom_Nav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.app.HistoryData;
import com.example.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History_Activity extends AppCompatActivity {

    private RecyclerView historyView;
    private HistoryAdapter mHAdapter;
    private List<HistoryData> mList;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolBar();

        anhXa();

        HistoryView();
    }


    private void toolBar() {
        Toolbar toolbar_History = findViewById(R.id.toolbar_History);

        setSupportActionBar(toolbar_History);
        toolbar_History.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void anhXa() {
        historyView = findViewById(R.id.historyView);
    }

    private void HistoryView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(History_Activity.this);
        historyView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(History_Activity.this, DividerItemDecoration.VERTICAL);
        historyView.addItemDecoration(dividerItemDecoration);

        mList = new ArrayList<>();
        mHAdapter = new HistoryAdapter(mList);
        historyView.setAdapter(mHAdapter);

        mData.child("History").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HistoryData history_data = dataSnapshot.getValue(HistoryData.class);
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