package com.app.tution.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tution.R;
import com.app.tution.adapters.AnnouncementsAdapter;
import com.app.tution.items.AnnouncementClass;
import com.app.tution.items.CourseClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;


public class AnnouncementFragment extends Fragment implements View.OnClickListener {
    MaterialCardView drawer, pad;
    TextView cancel;
    MaterialButton done;
    RecyclerView recyclerView;
    CourseClass course;
    TextInputLayout des;
    AnnouncementsAdapter adapter;

    public AnnouncementFragment() {
        // Required empty public constructor
    }

    public AnnouncementFragment(CourseClass courseClass) {
        this.course = courseClass;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_announcement, container, false);
        drawer = v.findViewById(R.id.drawer);
        pad = v.findViewById(R.id.pad);
        cancel = v.findViewById(R.id.cancel);
        done = v.findViewById(R.id.done);
        recyclerView = v.findViewById(R.id.recyclerview);
        des = v.findViewById(R.id.des);
        drawer.setOnClickListener(this);
        cancel.setOnClickListener(this);
        done.setOnClickListener(this);


        if (course.getAnnouncements() != null)
            adapter = new AnnouncementsAdapter(getContext(), course.getAnnouncements());
        else adapter = new AnnouncementsAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawer:
                if (pad.getVisibility() == View.VISIBLE)
                    pad.setVisibility(View.GONE);
                else pad.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel:
                pad.setVisibility(View.GONE);
                break;
            case R.id.done:
                postAnnouncement();
                // upload announcement
                pad.setVisibility(View.GONE);
                break;

        }
    }

    private void postAnnouncement() {
        AnnouncementClass announcement = new AnnouncementClass(new Date(), des.getEditText().getText().toString());
        course.getAnnouncements().add(announcement);
        adapter.notifyDataSetChanged();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("COURSES").document(course.getCourseUID())
                .update("announcements", course.getAnnouncements())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error while making announcement :(", Toast.LENGTH_SHORT).show();
            }
        });


    }
}