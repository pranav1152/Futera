package com.app.tution.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tution.EnrollCalls;
import com.app.tution.R;
import com.app.tution.activities.Student;
import com.app.tution.adapters.ExploreCoursesAdapter;
import com.app.tution.items.CourseClass;
import com.app.tution.items.StudentClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    ArrayList<CourseClass> exploreCourses;
    FirebaseFirestore db;
    ExploreCoursesAdapter adapter;
    EnrollCalls calls;
    StudentClass student;
    private RecyclerView expRecyclerView;


    public ExploreFragment() {
        // Required empty public constructor
    }

    public ExploreFragment(StudentClass student) {
        this.student = student;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        exploreCourses = new ArrayList<>();
        calls = new EnrollCalls() {
            @Override
            public void enroll(CourseClass course) {
                student.getStrOptedCourses().add(course.getCourseUID());
                student.getOptedCourses().add(course);


                db.collection("STUDENTS").document(student.getUID())
                        .update("strOptedCourses", student.getStrOptedCourses())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                addIntoCourse(course);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error while enrolling into course :(", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void expandCourse(CourseClass course) {

            }
        };
    }

    private void addIntoCourse(CourseClass course) {
        course.getOptedStudentsUids().add(student.getUID());
        db.collection("COURSES").document(course.getCourseUID())
                .update("optedStudentsUids", course.getOptedStudentsUids())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Enrolled", Toast.LENGTH_SHORT).show();
                        ((Student) getActivity()).selectHomeFragment();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void retriveExploreCourses() {
        exploreCourses.clear();
        db.collection("COURSES").limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            CourseClass c = d.toObject(CourseClass.class);
                            if (!student.getStrOptedCourses().contains(c.getCourseUID()))
                                exploreCourses.add(c);
                        }
                        updateUI();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void updateUI() {

        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        expRecyclerView = v.findViewById(R.id.expRecyclerView);

        expRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ExploreCoursesAdapter(exploreCourses, getContext(), calls);
        expRecyclerView.setAdapter(adapter);

        retriveExploreCourses();

        return v;
    }


}