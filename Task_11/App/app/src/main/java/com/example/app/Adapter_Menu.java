package com.example.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Adapter_Menu extends FragmentStateAdapter {

    public Adapter_Menu(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new Fragment_Setting();
            case 2:
                return new Fragment_Account();
            default:
                return new Fragment_Home();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
