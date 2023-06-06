package com.example.app;

public class CurrentData {
    public String time;
    public int temp;
    public boolean status;
    public String unit;

    public CurrentData() {
    }

    public CurrentData(String time, int temp, boolean status, String unit) {
        this.time = time;
        this.temp = temp;
        this.status = status;
        this.unit = unit;
    }
}
