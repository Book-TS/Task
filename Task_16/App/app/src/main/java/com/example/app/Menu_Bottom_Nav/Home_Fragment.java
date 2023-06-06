package com.example.app.Menu_Bottom_Nav;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.ChartData;
import com.example.app.R;
import com.example.app.CurrentData;
import com.example.app.SettingData;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Home_Fragment extends Fragment {

    private View fm_home;
    private LineChart lineChart_temp;
    private final List<Entry> chartData = new ArrayList<>();
    private final List<String> indexX = new ArrayList<>();
    private TextView text_time;
    private ArcProgress arc1;
    private SwitchCompat switch1, switch2;
    private int valMax, valMin;
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fm_home = inflater.inflate(R.layout.fragment_home, container, false);

        anhXa();

        realTimeData();

        realTimeLineChart();

        setData();

        limitLine();

        return fm_home;
    }

    private void anhXa() {
        lineChart_temp = fm_home.findViewById(R.id.lineChart_temp);
        text_time = fm_home.findViewById(R.id.text_time);
        arc1 = fm_home.findViewById(R.id.arc1);
        switch1 = fm_home.findViewById(R.id.switch1);
        switch2 = fm_home.findViewById(R.id.switch2);
    }

    private void realTimeData() {
        mData.child("Current").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CurrentData currentData = snapshot.getValue(CurrentData.class);

                text_time.setText("Thời gian cập nhật: " + currentData.time);

                switch1.setChecked(currentData.status);

                if (Objects.equals(currentData.unit, "°K")) {
                    switch2.setChecked(true);
                    arc1.setMax(473);
                    arc1.setProgress(currentData.temp + 273);
                }
                else {
                    switch2.setChecked(false);
                    arc1.setMax(200);
                    arc1.setProgress(currentData.temp);
                }

                arc1.setSuffixText(currentData.unit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setData() {
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mData.child("Current/status").setValue(isChecked);
        });
        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                mData.child("Current/unit").setValue("°K");
            }
            else mData.child("Current/unit").setValue("°C");
        });
    }

    private void realTimeLineChart() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateandTime = sdf.format(new Date());
        mData.child("Chart/" + currentDateandTime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chartData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChartData chartData1 = dataSnapshot.getValue(ChartData.class);

                    assert chartData1 != null;

                    int index = chartData1.time.indexOf(":");
                    String substring_1 = chartData1.time.substring(0, index);
                    String substring_2 = chartData1.time.substring(index+1);

                    float x = Integer.parseInt(substring_1) + (Float.parseFloat(substring_2)/60);

                    chartData.add(new Entry(x, chartData1.val));
                    indexX.add(chartData1.time);
                    lineChartShow();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lineChartShow() {
        configChart();
        onTouchSingle();
//        chartData.add(new Entry(7, 29));
//        chartData.add(new Entry(8, 29));
//        chartData.add(new Entry(9, 30));
//        chartData.add(new Entry(10, 30));
//        chartData.add(new Entry(11, 29));
//        chartData.add(new Entry(12, 30));

        LineDataSet dataSet = new LineDataSet(chartData, "Nhiệt độ");
        dataSet.setDrawValues(false);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        dataSet.setValueTextSize(10);
        dataSet.setColor(Color.RED);    // Đặt màu sắc đường là màu đỏ
        dataSet.setLineWidth(2f);       // Đặt độ dày của đường là 2 pixels

        LineData lineData = new LineData(dataSets);
        lineChart_temp.setData(lineData);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Đọc thời gian hệ thống
        String currentDate = sdf.format(new Date());                                                        // Định dạng thời gian

        Description description = new Description();
        description.setText(currentDate);
        description.setTextSize(12);
        lineChart_temp.setDescription(description);

        lineChart_temp.invalidate();            // refresh biểu đồ
    }

    private void limitLine() {
        mData.child("Setting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SettingData settingData = snapshot.getValue(SettingData.class);

                assert settingData != null;
                valMax = settingData.tempMax;
                valMin = settingData.tempMin;

                lineChartShow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configChart() {
        // Định dạng trục x
        XAxis xAxis = lineChart_temp.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);          // Vị trí của trục x
        xAxis.setGranularity(1f);                               // Độ dày giữa các điểm trục x
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(indexX)); // Định dạng nhãn trục x

        // Nhiệt độ cao nhất
        LimitLine tempMax = new LimitLine(valMax, "Nhiệt độ cao nhất"); // Giá trị nhiệt độ cao nhất
        tempMax.setLineColor(Color.RED);                                        // Màu sắc của đường giới hạn
        tempMax.setLineWidth(0.8f);                                             // Độ dày của đường giới hạn
        tempMax.setTextSize(12f);                                               // Kích thước văn bản đường giới hạn

        // Nhiệt độ thấp nhất
        LimitLine tempMin = new LimitLine(valMin, "Nhiệt độ thấp nhất"); // Giá trị nhiệt độ thap nhất
        tempMin.setLineColor(Color.RED);                                        // Màu sắc của đường giới hạn
        tempMin.setLineWidth(0.8f);                                             // Độ dày của đường giới hạn
        tempMin.setTextSize(12f);                                               // Kích thước văn bản đường giới hạn

        // Định dạng trục y
        YAxis yAxis = lineChart_temp.getAxisLeft();
        yAxis.setAxisMinimum(20f);                              // Giá trị tối thiểu của trục y
        yAxis.setAxisMaximum(35f);                              // Giá trị tối đa của trục y
        yAxis.setGranularity(1f);                               // Độ dày giữa các điểm trục y
        yAxis.removeAllLimitLines();
        yAxis.addLimitLine(tempMax);
        yAxis.addLimitLine(tempMin);
        YAxis rightYAxis = lineChart_temp.getAxisRight();
        rightYAxis.setEnabled(false);                           // Tắt trục y bên phải

        // Cấu hình chú thích
        Legend legend = lineChart_temp.getLegend();
        legend.setForm(Legend.LegendForm.LINE);                                 // Đặt hình dạng chú thích là hình tròn
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // Canh chính giữa
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);     // Canh dưới cùng
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);             // Hiển thị ngang
        legend.setFormSize(30);
        legend.setTextSize(13);

        lineChart_temp.setScaleEnabled(false);                 // Tắt chức năng thu/phóng
        lineChart_temp.setDragEnabled(true);                   // Bật chức năng di chuyển trục x
    }

    private void onTouchSingle() {
        lineChart_temp.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                Highlight highlight = lineChart_temp.getHighlightByTouchPoint(me.getX(), me.getY());
                if (highlight != null) {
                    Entry entry = lineChart_temp.getData().getEntryForHighlight(highlight);
                    float x = entry.getX();
                    float y = entry.getY();

                    int hourVal = (int) (x);
                    int minuteVal = (int) ((x - hourVal) * 60);
                    String timeVal;
                    if (hourVal < 10) {
                        timeVal = "0" + hourVal + ":";
                    } else {
                        timeVal = hourVal + ":";
                    }
                    if (minuteVal < 10) {
                        timeVal += "0" + minuteVal;
                    } else {
                        timeVal += minuteVal;
                    }
                    Toast.makeText(getActivity(), "Nhiệt độ " + String.valueOf(y) +" °C, vào lúc " + timeVal, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
    }
}