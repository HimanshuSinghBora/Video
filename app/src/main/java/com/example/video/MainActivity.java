package com.example.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements VideoRVAdapter.VideoClickInterface {
    private ArrayList<VideoRVModel> videoRVModelArrayList;
    private VideoRVAdapter videoRVAdapter;
    private RecyclerView videoRV;
    private static final int STORAGE_PERMISSION=101;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoRV = findViewById(R.id.idRVVideos);
        videoRVModelArrayList = new ArrayList<>();
        videoRVAdapter = new VideoRVAdapter(videoRVModelArrayList, this, this::onVideoClick);
        videoRV.setLayoutManager(new GridLayoutManager(this, 2));
        videoRV.setAdapter(videoRVAdapter);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Toast.makeText(MainActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();
                        getVideos();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getVideos(){
        ContentResolver contentResolver=getContentResolver();
        Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor=contentResolver.query(uri,null,null,null,null);
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                 String videoTitle=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                 String videoPath=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                 Bitmap videoThumbnail= ThumbnailUtils.createVideoThumbnail(videoPath,MediaStore.Images.Thumbnails.MINI_KIND);

                 videoRVModelArrayList.add(new VideoRVModel(videoTitle,videoPath,videoThumbnail));
            }
            while (cursor.moveToNext());
        }
    }
    public void onVideoClick(int position) {
        Intent intent=new Intent(MainActivity.this, VideoPlayerActivity.class);
        intent.putExtra("videoName",videoRVModelArrayList.get(position).getVideoName());
        intent.putExtra("videoPath",videoRVModelArrayList.get(position).getVideoPath());
        startActivity(intent);
    }


}