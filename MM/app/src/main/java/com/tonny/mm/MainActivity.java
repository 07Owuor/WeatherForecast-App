package com.tonny.mm;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        toolbar_title = toolbar.findViewById(R.id.toolbar_title);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadfragment(new TodoFragment());
        toolbar_title.setText("To Do List");
    }

    private void loadfragment(android.support.v4.app.Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framecontainer, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_todo:
                    fragment = new TodoFragment();
                    loadfragment(fragment);
                    toolbar_title.setText("To Do List");
                    return true;
                case R.id.navigation_weather:
                    fragment = new WeatherFragment();
                    loadfragment(fragment);
                    toolbar_title.setText("Weather");
                    return true;
                case R.id.navigation_news:
                    fragment = new NewsFragment();
                    loadfragment(fragment);
                    toolbar_title.setText("News");
                    return true;

            }
            return false;
        }
    };

}
