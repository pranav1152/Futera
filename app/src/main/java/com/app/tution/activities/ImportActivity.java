package com.app.tution.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.tution.Connect;
import com.app.tution.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImportActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int FILE_SELECT_CODE = 1;
    MaterialButton imp, submit;
    TextView code;
    InputStream is = null;
    String COURSE_NAME = "DeepLearning";
    String ASSIGNMENT_NO = "1";
    String STUDENT_ID = "student_id.py";
    Uri URI = null;
    Connect connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        imp = findViewById(R.id.imp);
        submit = findViewById(R.id.submit);
        code = findViewById(R.id.code);
        connect = new Connect(this);
        imp.setOnClickListener(this);
        submit.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    URI = uri;
                    readFile(uri);
                }
                break;
        }
    }

    public void readFile(Uri uri) {

        try {
            InputStream inputStream = this.getContentResolver().openInputStream(uri);
            is = inputStream;
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }

            code.setText(total.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void showFileChooser() {
        is = null;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No app found to complete action :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imp:
                showFileChooser();
                break;
            case R.id.submit:
                _submitToServer();
                break;

        }
    }

    private void _submitToServer() {
        String data = code.getText().toString();
        connect.judge(COURSE_NAME + "/" + ASSIGNMENT_NO + "/" + STUDENT_ID, data);

    }

    private void _submitToFirebase() {
        // TODO upload files to the firebase


        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference ref = firebaseStorage.getReference("Files")
                .child(COURSE_NAME)
                .child(ASSIGNMENT_NO)
                .child(STUDENT_ID);
        InputStream inputStream = null;
        try {

            inputStream = this.getContentResolver().openInputStream(URI);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ref.putStream(inputStream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ImportActivity.this, "File Uploaded :)", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImportActivity.this, "Unable to upload file :(", Toast.LENGTH_SHORT).show();
            }
        });


    }
}