package com.app.tution.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tution.R;
import com.app.tution.items.PostClass;
import com.app.tution.utils.PostCalls;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<PostClass> posts;
    Context context;
    PostCalls calls;
    String currentUser;
    String currentname;

    public PostsAdapter(ArrayList<PostClass> posts, Context context, PostCalls postcalls) {
        this.posts = posts;
        this.context = context;
        calls = postcalls;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser().getUid();
        currentname = auth.getCurrentUser().getDisplayName();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        PostViewHolder postViewHolder = new PostViewHolder(v);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        PostViewHolder holder = (PostViewHolder) h;
        holder.title.setText(posts.get(position).getTitle());
        holder.des.setText(posts.get(position).getDescription());
        holder.username.setText("- " + posts.get(position).getOwner());
        holder.cnt.setText("" + posts.get(position).getLikes());
        if (posts.get(position).getOwnerUID().equals(currentUser)) {
            holder.delete.setVisibility(View.VISIBLE);
        } else holder.delete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title, des, cnt, username;
        ImageView like, delete;
        boolean liked = false;
        Animation animation;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            des = itemView.findViewById(R.id.des);
            cnt = itemView.findViewById(R.id.cnt);
            username = itemView.findViewById(R.id.owner);
            like = itemView.findViewById(R.id.like);
            delete = itemView.findViewById(R.id.del);
            animation = AnimationUtils.loadAnimation(context, R.anim.bounce);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (liked) {
                        liked = false;
                        like.setColorFilter(Color.parseColor("#989898"));
                        calls.dislikePost(posts.get(position).getUID());
                        int current = Integer.parseInt(cnt.getText().toString());
                        current--;
                        cnt.setText("" + current);
                    } else {
                        liked = true;
                        like.setColorFilter(Color.parseColor("#FB5959"));
                        like.startAnimation(animation);
                        calls.likePost(posts.get(position).getUID());
                        int current = Integer.parseInt(cnt.getText().toString());
                        current++;
                        cnt.setText("" + current);
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    calls.deletePost(posts.get(position).getUID());
                    posts.remove(position);
                    notifyDataSetChanged();
                }
            });

        }
    }
}
