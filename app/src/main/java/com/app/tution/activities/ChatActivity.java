package com.app.tution.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.tution.items.Messages;
import com.app.tution.R;
import com.app.tution.adapters.MessageAdapter;
import com.app.tution.utils.GetTimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String mChatUser;
    private Toolbar mChatToolbar;

    private DatabaseReference mRootRef;

    private TextView mTitleView;
    private TextView mLastSeenView;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;

    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;

    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;

    private int itemPos = 0;

    private String mLastKey = "";
    private String mPrevKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatToolbar = findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mChatUser = getIntent().getStringExtra("user_id");
        String userName = getIntent().getStringExtra("user_name");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);

        actionBar.setCustomView(action_bar_view);

        mTitleView = findViewById(R.id.custom_bar_title);
        mLastSeenView = findViewById(R.id.custom_bar_seen);

        mChatAddBtn = findViewById(R.id.chat_add_btn);
        mChatSendBtn = findViewById(R.id.chat_send_btn);
        mChatMessageView = findViewById(R.id.chat_message_view);

        mAdapter = new MessageAdapter(messagesList);

        mMessagesList = findViewById(R.id.messages_list);
        mRefreshLayout = findViewById(R.id.message_swipe_layout);
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

        mRootRef.child("Chats").child(mCurrentUserId).child(mChatUser).child("seen").setValue(true);

        loadMessages();
        
        mTitleView.setText(userName);

        mRootRef.child("USERS").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String online = snapshot.child("online").getValue().toString();

                if(online.equals("true")) {

                    mLastSeenView.setText("Online");

                } else {

                    GetTimeAgo getTimeAgo = new GetTimeAgo();

                    long lastTime = Long.parseLong(online);

                    String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, getApplicationContext());

                    mLastSeenView.setText(lastSeenTime);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRootRef.child("Chats").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(mChatUser)){
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chats/" + mCurrentUserId + "/" + mChatUser, chatAddMap);
                    chatUserMap.put("Chats/" + mChatUser + "/" + mCurrentUserId, chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if(error != null){

                                Log.d("CHAT_LOG", error.getMessage().toString());

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();

            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPage++;

                itemPos = 0;

                loadMoreMessages();


            }
        });
    }

    private void loadMessages() {
        DatabaseReference messageRef = mRootRef.child("Messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages message = snapshot.getValue(Messages.class);

                itemPos++;

                if(itemPos == 1){

                    String messageKey = snapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(messagesList.size() - 1);

                mRefreshLayout.setRefreshing(false);
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
    }

    private void loadMoreMessages() {
        DatabaseReference messageRef = mRootRef.child("Messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages message = snapshot.getValue(Messages.class);
                String messageKey = snapshot.getKey();

                if(!mPrevKey.equals(messageKey)){
                    messagesList.add(itemPos++, message);
                } else {
                    mPrevKey = mLastKey;
                }

                if(itemPos == 1){
                    mLastKey = messageKey;
                }
                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
                mLinearLayout.scrollToPositionWithOffset(10,0);
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

    }

    private void sendMessage(){
        String message = mChatMessageView.getText().toString();
        if(!TextUtils.isEmpty(message)){
            String current_user_ref = "Messages/" + mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = "Messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child("Messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            mChatMessageView.setText("");

            mRootRef.child("Chats").child(mCurrentUserId).child(mChatUser).child("seen").setValue(true);
            mRootRef.child("Chats").child(mCurrentUserId).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);

            mRootRef.child("Chats").child(mChatUser).child(mCurrentUserId).child("seen").setValue(false);
            mRootRef.child("Chats").child(mChatUser).child(mCurrentUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error != null){

                        Log.d("CHAT_LOG", error.getMessage());

                    }
                }
            });
        }
    }
}