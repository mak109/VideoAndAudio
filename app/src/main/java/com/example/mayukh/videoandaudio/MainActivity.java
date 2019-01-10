package com.example.mayukh.videoandaudio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,MediaPlayer.OnCompletionListener{
    //UI Components
    private VideoView myVideoView;
    //Button for several functions
    private Button btnPlayVideo;
    private Button btnPlayMusic;
    private Button btnPauseMusic;
    //for implementing media controls to our video
    private MediaController mediaController;
    //for operating several music and sounds options
    private MediaPlayer mediaPlayer;
    private SeekBar seekBarVolume,seekBarMove;//for controlling the volume and playback
    private AudioManager audioManager;//enables several musical and sound properties
    //for operating timing controls in our music
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialising several UI components
        myVideoView = findViewById(R.id.myVideoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnPlayMusic= findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarMove = findViewById(R.id.seekBarMove);
        //Setting On Click Listener for certain components for there response on clicking
        btnPlayVideo.setOnClickListener(MainActivity.this);
        btnPlayMusic.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);
        // Initialising media controller for video playback controls and media player controls for audio locating the
        // music on raw folder under res folder
        mediaController = new MediaController(MainActivity.this);
        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.music1);
        //Setting on completion listener so that the seekbar automatically comes to its initial position on completion of the music
        mediaPlayer.setOnCompletionListener(MainActivity.this);
        //initialisation of audiomanager for volume controls
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //fetching maximum and current volume of user device
        int maximumVolumeOfUserDevice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int getCurrentVolumeOfUserDevice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //Setting maximum limit and current progress to our ui components i.e. seekbar
        seekBarVolume.setMax(maximumVolumeOfUserDevice);
        seekBarVolume.setProgress(getCurrentVolumeOfUserDevice);
        //Setting on seekbar listener for our seekbar track movement
        seekBarMove.setOnSeekBarChangeListener(MainActivity.this);
        //Setting the maximum time for which the audio will play according to the duration of the audio
        seekBarMove.setMax(mediaPlayer.getDuration());
        //implementing the methods under on seekbar volume change listener
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    //Toast.makeText(MainActivity.this,progress+"",Toast.LENGTH_LONG).show();
                    //setting the volume of the audio on dragging the seekbar volume
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }
            }
            //these two methods have no use on volume seekbar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    //on click method for several buttons
    @Override
    public void onClick(View buttonView) {
        //fetching the button id of several button ui components
        switch (buttonView.getId()){
            case R.id.btnPlayVideo:
                //Uri is used to fetch the source of our video
                Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
                myVideoView.setVideoURI(videoUri);
                //setting the media controls for our video
                myVideoView.setMediaController(mediaController);
                mediaController.setAnchorView(myVideoView);
                //starting the video
                myVideoView.start();
                break;
            case R.id.btnPlayMusic:
                mediaPlayer.start();
                //setting the timer controls with 0 start delay and a period of 1 sec
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        seekBarMove.setProgress(mediaPlayer.getCurrentPosition());

                    }
                },0,1000);
                break;
            case R.id.btnPauseMusic:
                //pausing the music followed by canceling the timer
                mediaPlayer.pause();
                timer.cancel();
                break;
        }

    }

    @Override
    //implementing methods for seekbar track
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            //Toast.makeText(MainActivity.this,progress+"",Toast.LENGTH_SHORT).show();
            mediaPlayer.seekTo(progress);
        }

    }

    @Override
    //pausing the music on holding the cursor of seekbar
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();

    }

    @Override
    //resuming the music on releasing the cursor of seekbar
    public void onStopTrackingTouch(SeekBar seekBar) {

        mediaPlayer.start();
    }

    @Override
    //cancelling the timer on completion of the music track
    public void onCompletion(MediaPlayer mp)
    {
        timer.cancel();
        Toast.makeText(this,"Music has ended",Toast.LENGTH_SHORT).show();
    }
}
