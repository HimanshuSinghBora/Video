package com.example.video;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {
    private ImageButton play,replay,forward;
    private TextView videoNameIV,videoTimeIV;
    private SeekBar seekBar;
    boolean isOpen=false;
    private String videoName,videoPath;
    private RelativeLayout controlsRL,videoRL;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private Thread updateSeek;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        String videoName = getIntent().getStringExtra("videoName");
        String videoPath = getIntent().getStringExtra("videoPath");
        videoNameIV = findViewById(R.id.idIVVideoTitle);
        videoTimeIV=findViewById(R.id.idIVTime);
        play=findViewById(R.id.idPlay);
        forward = findViewById(R.id.idForward);
        replay = findViewById(R.id.idReplay);
        seekBar=findViewById(R.id.idSeekBarProgress);
        videoView=findViewById(R.id.idVideoView);
        controlsRL=findViewById(R.id.idRLControls);
        videoRL=findViewById(R.id.idRLVideo);
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(videoView.getDuration());
                videoView.start();
            }
        });
        videoNameIV.setText(videoName);

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.seekTo(videoView.getCurrentPosition() - 10000);
            }
        });
        forward.setOnClickListener(view -> videoView.seekTo(videoView.getCurrentPosition()+10000));
        play.setOnClickListener(view -> {
            if(videoView.isPlaying()){
                play.setImageResource(R.drawable.play);
                videoView.pause();
            }
            else{
                play.setImageResource(R.drawable.pause);
                videoView.start();
            }
        });

        videoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    hideControls();
                    isOpen=false;
                }
                else{
                    showControls();
                    isOpen=true;
                }
            }
        });
        setHandler();
        initializeSeekBar();
    }
    private void setHandler(){
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(videoView.getDuration()>0){
                    int curPos=videoView.getCurrentPosition();
                    seekBar.setProgress(curPos);
                    videoTimeIV.setText(""+convertTime(videoView.getDuration()-curPos));
                }
                handler.postDelayed(this,0);
            }
        };
        handler.postDelayed(runnable,500);
    }
    private String convertTime(int ms){
        String time;
        int x,seconds,minutes,hours;
        x=ms/1000;
        seconds=x%60;
        x/=60;
        minutes=x%60;
        x/=60;
        hours=x%24;
        if(hours!=0){
            time=String.format("%02d",hours)+":"+String.format("%02d",minutes)+":"+String.format("%02d",seconds);
        }
        else{
            time=String.format("%02d",minutes)+":"+String.format("%02d",seconds);
        }
        return time;
    }
    private void initializeSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getId()==R.id.idSeekBarProgress){
                    if(fromUser){
                        videoView.seekTo(progress);
                        videoView.start();
                        int curPos=videoView.getCurrentPosition();
                        videoTimeIV.setText(""+convertTime(videoView.getDuration()-curPos));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void showControls() {
        controlsRL.setVisibility(View.VISIBLE);
        final Window window=this.getWindow();
        if(window==null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView =window.getDecorView();
        if(decorView!=null){
            int uiOption=decorView.getAccessibilityLiveRegion();
            if(Build.VERSION.SDK_INT>=14){
                uiOption&=View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT>=16){
                uiOption&=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT>=19){
                uiOption&=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void hideControls() {
        controlsRL.setVisibility(View.GONE);
        final Window window=this.getWindow();
        if(window==null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView =window.getDecorView();
        if(decorView!=null){
            int uiOption=decorView.getAccessibilityLiveRegion();
            if(Build.VERSION.SDK_INT>=14){
                uiOption|=View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT>=16){
                uiOption|=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT>=19){
                uiOption|=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }
}