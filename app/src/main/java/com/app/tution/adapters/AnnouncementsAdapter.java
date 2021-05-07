package com.app.tution.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tution.R;
import com.app.tution.items.AnnouncementClass;

import java.util.ArrayList;


public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.AnnouncemntHolder> {
    Context context;
    ArrayList<AnnouncementClass> announcements;

    public AnnouncementsAdapter(Context context, ArrayList<AnnouncementClass> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public AnnouncemntHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.announce_item, parent, false);
        return new AnnouncemntHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncemntHolder holder, int position) {
        holder.date.setText(announcements.get(position).getDate().toString());
        holder.content.setText(announcements.get(position).getDes());
    }


    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public class AnnouncemntHolder extends RecyclerView.ViewHolder {

        TextView date, content;

        public AnnouncemntHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            content = itemView.findViewById(R.id.content);

        }
    }
}
