package com.example.app_sk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<History_Data> mList;

    public HistoryAdapter(List<History_Data> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History_Data mHistory = mList.get(position);
        if (mHistory == null) {
            return;
        }
        holder.hrVal.setText(String.valueOf(mHistory.hr));
        holder.spo2Val.setText(String.valueOf(mHistory.hr));
        holder.timeVal.setText(mHistory.time);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView hrVal;
        private final TextView spo2Val;
        private final TextView timeVal;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            hrVal = itemView.findViewById(R.id.hr_val);
            spo2Val = itemView.findViewById(R.id.spo2_val);
            timeVal = itemView.findViewById(R.id.time_val);
        }
    }
}
