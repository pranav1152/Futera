package com.app.tution.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tution.items.Conv;
import com.app.tution.R;
import com.app.tution.activities.ChatActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ChatFragment extends Fragment {

    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_chat,container,false);
        mConvList = mMainView.findViewById(R.id.conv_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chats").child(mCurrent_user_id);

        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("USERS");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(mCurrent_user_id);
        mUsersDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query conversationQuery = mConvDatabase.orderByChild("timestamp");
        Log.e("ChatFrag/onStart",conversationQuery.getRef().toString());
        FirebaseRecyclerOptions<Conv> options = new FirebaseRecyclerOptions.Builder<Conv>()
                .setQuery(conversationQuery, new SnapshotParser<Conv>() {
                    @NonNull
                    @Override
                    public Conv parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Conv(snapshot.child("seen").getValue().toString(),
                                snapshot.child("timestamp").getValue().toString());
                    }
                }).build();
        Log.e("ChatFrag","Created options");
        FirebaseRecyclerAdapter<Conv,ConvViewHolder> firebaseAdapter =
                new FirebaseRecyclerAdapter<Conv, ConvViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConvViewHolder holder, int position, @NonNull Conv model) {
                Log.e("ChatFrag","Inside onBind");
                final String list_user_id = getRef(position).getKey();
                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);
                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String data = snapshot.child("message").getValue().toString();
                        holder.setMessage(data,model.isSeen());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String userName = snapshot.child("username").getValue().toString();

                        holder.setName(userName);
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("user_id", list_user_id);
                                chatIntent.putExtra("user_name", userName);
                                startActivity(chatIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public ConvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_layout,parent,false);
                return new ConvViewHolder(view);
            }
        };
        mConvList.setAdapter(firebaseAdapter);
    }
    public static class ConvViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ConvViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setMessage(String message, String isSeen){

            TextView userStatusView = mView.findViewById(R.id.user_single_status);
            userStatusView.setText(message);

            if(!isSeen.equals("false")){
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            } else {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
            }
        }
        public void setName(String name){

            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }
    }
}