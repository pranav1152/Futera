package com.app.tution.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.app.tution.R;
import com.app.tution.adapters.PagerAdapter;
import com.app.tution.items.CourseClass;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class ExpandCourseActivity extends AppCompatActivity {

    CourseClass course;
    Gson gson;
    TextView title, des;
    ViewPager pager;
    TabLayout tabLayout;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_course);
        title = findViewById(R.id.title);
        des = findViewById(R.id.des);
        pager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);

        gson = new Gson();
        String jCourse = getIntent().getExtras().getString("jCourse");
        course = gson.fromJson(jCourse, CourseClass.class);
        adapter = new PagerAdapter(this, getSupportFragmentManager(), 3, course);

        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("onTabSelected()",Integer.toString(tab.getPosition()));
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}