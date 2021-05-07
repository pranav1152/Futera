package com.app.tution.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tution.EnrollCalls;
import com.app.tution.R;
import com.app.tution.items.CourseClass;

import java.util.ArrayList;

public class EnrolledCoursesAdapter extends RecyclerView.Adapter<EnrolledCoursesAdapter.ViewHolder> {

    Context context;
    EnrollCalls calls;
    private ArrayList<CourseClass> mCourses;
    private TextView courseName;

    public EnrolledCoursesAdapter(Context context, ArrayList<CourseClass> mCourses, EnrollCalls calls) {
        this.context = context;
        this.mCourses = mCourses;
        this.calls = calls;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View courseView = inflater.inflate(R.layout.enrolled_course_layout, parent, false);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    calls.expandCourse(mCourses.get(position));
                }
            });
        }
    }
}
