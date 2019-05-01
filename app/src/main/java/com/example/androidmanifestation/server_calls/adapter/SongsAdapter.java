package com.example.androidmanifestation.server_calls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.server_calls.entity.SongsEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private List<SongsEntity> songsEntityList;
    private Context context;

    public SongsAdapter(Context context) {
        this.context = context;
    }

    public void setSongs(List<SongsEntity> songsEntities) {
        this.songsEntityList = songsEntities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_row, viewGroup, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int i) {

        SongsEntity songsEntity = songsEntityList.get(i);

        if (songsEntity.getTrackName() != null) {
            songViewHolder.tvTrackName.setText(songsEntity.getTrackName());
        }

        if (songsEntity.getArtistName() != null) {
            songViewHolder.tvArtist.setText(songsEntity.getArtistName());
        }

        if (songsEntity.getTrackTimeMillis() != null) {
            String time = millisecondsToTime(Long.parseLong(songsEntity.getTrackTimeMillis()));
            songViewHolder.tvDuration.setText(time);
        }

        Picasso.get()
                .load(songsEntity.getArtworkUrl100())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(songViewHolder.ivListImage);

    }

    @Override
    public int getItemCount() {
        if (songsEntityList == null)
            return 0;
        else
            return songsEntityList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        TextView tvTrackName;
        TextView tvArtist;
        TextView tvDuration;
        ImageView ivListImage;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrackName = itemView.findViewById(R.id.tvTrackName); // title
            tvArtist = itemView.findViewById(R.id.tvArtist); // artist name
            tvDuration = itemView.findViewById(R.id.tvDuration); // duration
            ivListImage = itemView.findViewById(R.id.ivListImage); // thumb image
        }
    }

    private String millisecondsToTime(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        String secondsStr = Long.toString(seconds);
        String secs;
        if (secondsStr.length() >= 2) {
            secs = secondsStr.substring(0, 2);
        } else {
            secs = "0" + secondsStr;
        }
        return minutes + ":" + secs;
    }
}