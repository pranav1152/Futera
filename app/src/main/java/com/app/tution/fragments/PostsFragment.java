package com.app.tution.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tution.R;
import com.app.tution.adapters.PostsAdapter;
import com.app.tution.items.PostClass;
import com.app.tution.utils.Helper;
import com.app.tution.utils.PostCalls;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class PostsFragment extends Fragment implements View.OnClickListener {

    ArrayList<PostClass> posts;
    FirebaseFirestore db;
    ChipGroup chipGroup;
    Chip clike, cnewest;
    RecyclerView recyclerView;
    PostsAdapter adapter;
    ConstraintLayout main, newpost;
    TextInputLayout iptitle, ipdes;
    TextView cancel;
    MaterialButton post;
    FloatingActionButton add;


    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<>();
        db = FirebaseFirestore.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        chipGroup = v.findViewById(R.id.chipGroup);
        clike = v.findViewById(R.id.clike);
        cnewest = v.findViewById(R.id.cnewest);
        recyclerView = v.findViewById(R.id.recyclerview);
        main = v.findViewById(R.id.main);
        newpost = v.findViewById(R.id.newpost);
        iptitle = v.findViewById(R.id.iptitle);
        ipdes = v.findViewById(R.id.ipdes);
        cancel = v.findViewById(R.id.cancel);
        post = v.findViewById(R.id.submit);
        add = v.findViewById(R.id.add);

        add.setOnClickListener(this);
        post.setOnClickListener(this);
        cancel.setOnClickListener(this);

        PostCalls postCalls = new PostCalls() {
            @Override
            public void deletePost(String uid) {
                db.collection("POSTS").document(uid).delete();
            }

            @Override
            public void likePost(String uid) {

                db.collection("POSTS").document(uid).update("likes", FieldValue.increment(1));
            }

            @Override
            public void dislikePost(String uid) {
                db.collection("POSTS").document(uid).update("likes", FieldValue.increment(-1));
            }
        };

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.clike:
                        cnewest.setChecked(false);
                        retriveByLikes();
                        break;
                    case R.id.cnewest:
                        clike.setChecked(false);
                        retriveByDate();
                        break;
                }
            }
        });

        adapter = new PostsAdapter(posts, getContext(), postCalls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        retriveByDate();

        return v;
    }

    private void retriveByDate() {

        posts.clear();
        db.collection("POSTS").orderBy("date", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            PostClass post = d.toObject(PostClass.class);
                            posts.add(post);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void retriveByLikes() {
        posts.clear();
        db.collection("POSTS").orderBy("likes", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot d : queryDocumentSnapshots) {
                            PostClass post = d.toObject(PostClass.class);
                            posts.add(post);
                        }
                        adapter.notifyDataSetChanged();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                main.setClickable(false);
                newpost.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel:
                main.setClickable(true);
                newpost.setVisibility(View.GONE);
                break;
            case R.id.submit:
                postPost();
                break;

        }

    }

    private void postPost() {

        String title = iptitle.getEditText().getText().toString();
        String des = ipdes.getEditText().getText().toString();
        String uid = Helper.getUID(10);
        String owner = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String ownerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Date date = new Date();

        PostClass post = new PostClass(uid, title, des, owner, date, ownerUID);

        db.collection("POSTS").document(uid).set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        newpost.setVisibility(View.GONE);
                        main.setClickable(true);

                        posts.add(0, post);
                        adapter.notifyItemInserted(0);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error while creating post :(", Toast.LENGTH_SHORT).show();
            }
        });

    }
}