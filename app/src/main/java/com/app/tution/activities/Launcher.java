package com.app.tution.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.tution.R;
import com.app.tution.fragments.ChatFragment;
import com.app.tution.items.InstructorClass;
import com.app.tution.items.StudentClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Launcher extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String[] type = {"Instructor", "Student"};
    MaterialCardView card;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    GoogleSignInOptions gso;
    TabLayout tl;

    DatabaseReference firebaseDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        card = findViewById(R.id.card);
        firebaseAuth = FirebaseAuth.getInstance();
        tl = findViewById(R.id.tabs);

        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            retriveUserType();
        }


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void startnewActivity(int type, String userid) {

        if (type == 0) {
            Intent i = new Intent(Launcher.this, Instructor.class);
            i.putExtra("USERID", userid);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(Launcher.this, Student.class);
            i.putExtra("USERID", userid);
            startActivity(i);
            finish();

        }
    }

    public void signIn() {
        GoogleSignInClient googleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(Launcher.this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(Launcher.this, "result " + e.getMessage() + e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Launcher.this, task -> {
                    if (task.isSuccessful()) {
                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                        startnewActivity(isNew);
                    } else {
                        Toast.makeText(Launcher.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Launcher.this, "failed" + e.getMessage() + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void startnewActivity(boolean isNew) {
        if (isNew) {
            int type = tl.getSelectedTabPosition();
            String uid = firebaseAuth.getCurrentUser().getUid();

            //Addds USERS to database
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            firebaseDatabaseRef = FirebaseDatabase.getInstance()
                    .getReference().child("USERS").child(uid);
            firebaseDatabaseRef.child("username").setValue(firebaseAuth.getCurrentUser().getDisplayName());

            Map<String, Object> mp = new HashMap<>();
            mp.put("USERID", uid);
            mp.put("USERTYPE", type);

            db.collection("USERS").document(uid).set(mp)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            registerInRespectiveClass(type, uid);
                            //
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Launcher.this, "Error occurred while creating user", Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            //TODO if user has registered previously then retrive user type and start respective activity

            retriveUserType();
        }
    }

    private void registerInRespectiveClass(int type, String uid) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (type == 0) {
            InstructorClass instructor = new InstructorClass(uid, firebaseAuth.getCurrentUser().getDisplayName(), new ArrayList<String>());

            db.collection("INSTRUCTOR").document(uid).set(instructor)
                    /*.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            startnewActivity(type, uid);
                        }
                    })*/
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startnewActivity(type, uid);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Launcher.this, "error while adding in respective class", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {

            StudentClass studentClass = new StudentClass(uid, firebaseAuth.getCurrentUser().getDisplayName(),
                    new ArrayList<String>(), new ArrayList<String>());

            db.collection("STUDENTS").document(uid).set(studentClass)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startnewActivity(type, uid);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Launcher.this, "error while adding in respective class", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    private void retriveUserType() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int type = ((Long) documentSnapshot.get("USERTYPE")).intValue();
                        startnewActivity(type, firebaseAuth.getCurrentUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}