package com.example.app.Menu_Bottom_Nav;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main_Activity extends AppCompatActivity {

    private BottomNavigationView bottomNav_main;
    private ViewPager2 viewPager_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav_main = findViewById(R.id.bottomNav_main);
        viewPager_main = findViewById(R.id.viewPager_main);

        bottomNavi();
    }

    private void bottomNavi() {
        Menu_Adapter menu_adapter = new Menu_Adapter(this);
        viewPager_main.setAdapter(menu_adapter);

        selected_BottomNav();

        swipe_BottomNav();

        viewPager_main.setUserInputEnabled(false);
    }
    // Chon icon
    private void selected_BottomNav() {
        bottomNav_main.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.action_home) {
                viewPager_main.setCurrentItem(0);
            }
            else if (item.getItemId() == R.id.action_setting) {
                viewPager_main.setCurrentItem(1);
            }
            else if(item.getItemId() == R.id.action_account) {
                viewPager_main.setCurrentItem(2);
            }
            return true;
        });
    }

    // Xu ly vuot
    private void swipe_BottomNav() {
        viewPager_main.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 1:
                        bottomNav_main.getMenu().findItem(R.id.action_setting).setChecked(true);
                        break;
                    case 2:
                        bottomNav_main.getMenu().findItem(R.id.action_account).setChecked(true);
                        break;
                    default:
                        bottomNav_main.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                }
            }
        });
    }
}