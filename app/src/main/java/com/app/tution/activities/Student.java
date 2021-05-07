package com.app.tution.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.tution.R;
import com.app.tution.fragments.ChatFragment;
import com.app.tution.fragments.ExploreFragment;
import com.app.tution.fragments.HomeFragment;
import com.app.tution.fragments.SettingsFragment;
import com.app.tution.items.AssignmentClass;
import com.app.tution.items.CourseClass;
import com.app.tution.items.StudentClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Student extends AppCompatActivity {

    StudentClass student;
    String UID;
    FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;

    private ExploreFragment expFragment;
    private SettingsFragment settingsFragment;
    private HomeFragment hFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        UID = getIntent().getExtras().getString("USERID");
        db = FirebaseFirestore.getInstance();


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        loadFragment(hFragment);
                        break;
                    case R.id.Explore:
                        loadFragment(new ExploreFragment(student));
                        break;
                    case R.id.Settings:
                        loadFragment(settingsFragment);
                        break;
                }
                return true;
            }
        });
        retrieveStudent();
    }

    private void retrieveStudent() {
        db.collection("STUDENTS").document(UID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        student = documentSnapshot.toObject(StudentClass.class);
                        retrieveCourses();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Student.this, "Error while fetching details :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveCourses() {

        ArrayList<CourseClass> courses = new ArrayList<>();
        if (student.getStrOptedCourses().size() != 0) {
            db.collection("COURSES").whereIn("courseUID", student.getStrOptedCourses())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (DocumentSnapshot d : queryDocumentSnapshots) {
                                CourseClass course = d.toObject(CourseClass.class);
                                courses.add(course);
                            }
                            student.setOptedCourses(courses);
                            retrieveAssignments();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Student.this, "Error while retrieving courses :(", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            student.setOptedCourses(courses);
            retrieveAssignments();
        }
    }

    private void retrieveAssignments() {
        ArrayList<AssignmentClass> assignments = new ArrayList<>();
        if (student.getStrSubmittedAssignments().size() != 0) {
            db.collection("ASSIGNMENTS").whereIn("assignmentUID", student.getStrSubmittedAssignments())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (DocumentSnapshot d : queryDocumentSnapshots) {
                                AssignmentClass assignment = d.toObject(AssignmentClass.class);
                                assignments.add(assignment);
                            }
                            student.setSubmittedAssignments(assignments);
                            updateUI();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Student.this, "Error while loading assignments :(", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            student.setSubmittedAssignments(assignments);
            updateUI();
        }
    }


    private void updateUI() {
        hFragment = new HomeFragment(student);
        settingsFragment = new SettingsFragment();
        loadFragment(hFragment);
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentView, fragment)
                    .commit();
        }
    }

    public void selectHomeFragment() {

        bottomNavigationView.setSelectedItemId(R.id.Home);
        loadFragment(hFragment);

    }
}