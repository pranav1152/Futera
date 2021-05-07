package com.app.tution.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.tution.R;
import com.app.tution.items.CourseClass;
import com.app.tution.utils.Helper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AddNewCourseActivity extends AppCompatActivity {


    TextInputEditText name, des, maxcount;
    MaterialButton add;
    String USERID, courseID;
    String stringCourseUids;
    ArrayList<String> courseUids;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);


        USERID = getIntent().getExtras().getString("USERID");
        stringCourseUids = getIntent().getExtras().getString("COURSEIDSLIST");
        courseID = Helper.getUID(10);

        gson = new Gson();

        courseUids = gson.fromJson(stringCourseUids, new TypeToken<ArrayList<String>>() {
        }.getType());


        name = findViewById(R.id.name);
        des = findViewById(R.id.des);
        maxcount = findViewById(R.id.maxcount);
        add = findViewById(R.id.add);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _name = name.getText().toString();
                int maxCount = Integer.parseInt(maxcount.getText().toString());
                String instructorName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                CourseClass course = new CourseClass(courseID, _name, new ArrayList<String>(), new ArrayList<String>(), maxCount, instructorName);


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("COURSES").document(courseID).set(course)
                        /*.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                // TODO add this course to instrictor's courses list
                                if (courseUids == null)
                                    courseUids = new ArrayList<>();
                                courseUids.add(courseID);
                                addCourseToInstructor();

                            }
                        })*/
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (courseUids == null)
                                    courseUids = new ArrayList<>();
                                courseUids.add(courseID);
                                addCourseToInstructor();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewCourseActivity.this, "Failed to add course :(", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });


    }

    private void addCourseToInstructor() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("INSTRUCTOR").document(USERID)
                .update("courseUids", courseUids)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNewCourseActivity.this, "Course added successfully :)", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewCourseActivity.this, "Can't add course at this time :(", Toast.LENGTH_SHORT).show();
            }
        });


    }
}