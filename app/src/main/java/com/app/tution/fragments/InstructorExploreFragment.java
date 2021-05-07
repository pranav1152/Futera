package com.app.tution.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tution.R;
import com.app.tution.adapters.CurrentlyTeachingCoursesAdapter;
import com.app.tution.items.InstructorClass;


public class InstructorExploreFragment extends Fragment {

    InstructorClass instructor;
    RecyclerView recyclerView;
    CurrentlyTeachingCoursesAdapter currentlyTeachingCoursesAdapter;

    public InstructorExploreFragment() {
        // Required empty public constructor
    }

    public InstructorExploreFragment(InstructorClass instructor) {
        this.instructor = instructor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_instructor_explore, container, false);

        recyclerView = v.findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        currentlyTeachingCoursesAdapter = new CurrentlyTeachingCoursesAdapter(getContext(), instructor.getCourses());
        recyclerView.setAdapter(currentlyTeachingCoursesAdapter);

        return v;
    }
}