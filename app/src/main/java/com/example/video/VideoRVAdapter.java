package com.example.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoRVAdapter extends RecyclerView.Adapter<VideoRVAdapter.ViewHolder> {
    private ArrayList<VideoRVModel> videoRVModelsArrayList;
    private Context context;
    private VideoClickInterface videoClickInterface;

    public VideoRVAdapter(ArrayList<VideoRVModel> videoRVModelsArrayList, Context context, VideoClickInterface videoClickInterface) {
        this.videoRVModelsArrayList = videoRVModelsArrayList;
        this.context = context;
        this.videoClickInterface = videoClickInterface;
    }

    @NonNull
    @Override
    public VideoRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public  void onBindViewHolder(@NonNull VideoRVAdapter.ViewHolder holder, int position) {
       VideoRVModel videoRVModel=videoRVModelsArrayList.get(position);
       holder.thumbnailIV.setImageBitmap(videoRVModel.getThumbNail());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               videoClickInterface.onVideoClick(position);
           }
       });
    }

    @Override
    public int getItemCount() {
        return videoRVModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView thumbnailIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailIV=itemView.findViewById(R.id.idIVThumbNail);
        }
    }
    public interface VideoClickInterface{
        void onVideoClick(int position);
    }
}
