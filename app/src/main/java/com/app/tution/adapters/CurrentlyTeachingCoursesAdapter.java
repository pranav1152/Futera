package com.app.tution.adapters;;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.tution.R;
import com.app.tution.items.CourseClass;

import java.util.ArrayList;


public class CurrentlyTeachingCoursesAdapter extends RecyclerView.Adapter<CurrentlyTeachingCoursesAdapter.ViewHolder> {

    private ArrayList<CourseClass> mCourses;
    private TextView courseName;
    private Context context;


    public CurrentlyTeachingCoursesAdapter(Context context, ArrayList<CourseClass> mCourses) {
        this.context = context;
        this.mCourses = mCourses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View courseView = inflater.inflate(R.layout.currently_teaching_courses, parent, false);
        return new ViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseClass courses = mCourses.get(position);
        courseName = holder.courseName;
        courseName.setText(courses.getName());
    }

    @Override
    public int getItemCount() {

        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
        }
    }

}