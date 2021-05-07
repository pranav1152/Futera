package com.app.tution.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.tution.fragments.AnnouncementFragment;
import com.app.tution.fragments.AssignmentFragment;
import com.app.tution.fragments.ChatFragment;
import com.app.tution.items.CourseClass;

public class PagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    int tabs;
    CourseClass course;

    public PagerAdapter(Context context, @NonNull FragmentManager fm, int totaltabs, CourseClass course) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.tabs = totaltabs;
        this.course = course;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Announcements fragment
                AnnouncementFragment fragment = new AnnouncementFragment(course);
                return fragment;
            case 1:
                ChatFragment fragment1 = new ChatFragment();
                return fragment1;
            case 2:
                AssignmentFragment fragment2 = new AssignmentFragment();
                return fragment2;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs;
    }
}
