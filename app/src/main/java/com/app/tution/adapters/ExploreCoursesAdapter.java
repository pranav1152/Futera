package com.app.tution.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.tution.EnrollCalls;
import com.app.tution.R;
import com.app.tution.fragments.ChatFragment;
import com.app.tution.items.CourseClass;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ExploreCoursesAdapter extends RecyclerView.Adapter<ExploreCoursesAdapter.ViewHolder> {

    private EnrollCalls calls;
    private ArrayList<CourseClass> eCourses;
    private TextView expCourseName;
    private Context context;

    public ExploreCoursesAdapter(ArrayList<CourseClass> eCourses, Context context, EnrollCalls calls) {
        this.eCourses = eCourses;
        this.context = context;
        this.calls = calls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View courseView = inflater.inflate(R.layout.course_layout, parent, false);
        return new ExploreCoursesAdapter.ViewHolder(courseView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseClass courses = eCourses.get(position);
        holder.expCourseName.setText(eCourses.get(position).getName());
        holder.instructorName.setText(eCourses.get(position).getInstructorName());
        holder.instructorName.setText(String.format(Locale.ENGLISH,"%s %d"," ", eCourses.get(position).getOptedStudentsUids().size()));
        holder.enrolledStudents.setText(String.format(Locale.ENGLISH,"%d", eCourses.get(position).getOptedStudentsUids().size()));
        holder.ratingBar.setRating(eCourses.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return eCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView expCourseName, instructorName, enrolledStudents;
        MaterialButton enroll;
        RatingBar ratingBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expCourseName = itemView.findViewById(R.id.expCourseName);
            instructorName = itemView.findViewById(R.id.instructorName);
            enrolledStudents = itemView.findViewById(R.id.enrolledStudents);
            enroll = itemView.findViewById(R.id.materialButton);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            enroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    calls.enroll(eCourses.get(position));
                }
            });

        }
    }
}
