package com.sismatix.Elmizan.Activity;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.sismatix.Elmizan.Configgg;
import com.sismatix.Elmizan.R;

public class YPlayer extends AppCompatActivity {

    String vidId;
    YouTubePlayer YPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yplayer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        vidId= getIntent().getStringExtra("videoId");
        Youtube(vidId);
    }

    private void Youtube(final String vidId) {
        Log.e("vidsYT", "" + vidId);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        final AppCompatActivity activity = (AppCompatActivity) this;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment_mpp, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Configgg.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {

                    YPlay = youTubePlayer;
                    YPlay.loadVideo(vidId);
                    YPlay.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                    YPlay.setShowFullscreenButton(true);
                    YPlay.play();
                    //YPlay.getCurrentTimeMillis();

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(activity, "Video Problemmmmm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
