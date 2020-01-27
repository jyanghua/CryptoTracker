package com.example.ilovezappos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Navigation Bar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Sets Home Fragment (First tab) as the default navigation fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    /**
     * Navigation bar listener that will construct a new fragment if the user changes the
     * navigation view.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_order_book:
                        selectedFragment = new OrderBookFragment();
                        break;
                    case R.id.nav_alerts:
                        selectedFragment = new AlertsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };
}
