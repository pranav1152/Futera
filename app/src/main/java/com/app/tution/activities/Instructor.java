package com.app.tution.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.tution.R;
import com.app.tution.fragments.InstructorExploreFragment;
import com.app.tution.fragments.InstructorSettingFragment;
import com.app.tution.fragments.PostsFragment;
import com.app.tution.items.CourseClass;
import com.app.tution.items.InstructorClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
public class Instructor extends AppCompatActivity {
    InstructorClass instructor;
    Gson gson;
    String UID;
    FirebaseFirestore db;


    InstructorExploreFragment instructorExploreFragment;
    InstructorSettingFragment instructorSettingFragment;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        bottomNavigationView = findViewById(R.id.navbar);
        gson = new Gson();

        db = FirebaseFirestore.getInstance();

        UID = getIntent().getExtras().getString("USERID");

        retriveInstructor();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.explore:
                        openFragment(instructorExploreFragment);
                        break;
                    case R.id.setting:
                        openFragment(instructorSettingFragment);
                        break;
                    case R.id.posts:
                        openFragment(new PostsFragment());
                }

                return false;
            }
        });


    }

    private void retriveInstructor() {


        DocumentReference ref = db.collection("INSTRUCTOR").document(UID);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                instructor = documentSnapshot.toObject(InstructorClass.class);
                retriveCourses();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void retriveCourses() {

        ArrayList<CourseClass> courses = new ArrayList<>();
        if (instructor.getCourseUids() != null && instructor.getCourseUids().size() != 0) {

            db.collection("COURSES").whereIn(FieldPath.documentId(), instructor.getCourseUids())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (DocumentSnapshot d : queryDocumentSnapshots) {

                        CourseClass courseClass = d.toObject(CourseClass.class);
                        courses.add(courseClass);
                    }
                    instructor.setCourses(courses);

                    updateUI();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Instructor.this, "Error while loading course details :(", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            instructor.setCourses(courses);
            updateUI();
        }

    }

    private void updateUI() {
        // TODO Update UI

        instructorExploreFragment = new InstructorExploreFragment(instructor);
        instructorSettingFragment = new InstructorSettingFragment();

        openFragment(instructorExploreFragment);


    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

}