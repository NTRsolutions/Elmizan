package com.sismatix.Elmizan.Fregment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.sismatix.Elmizan.Configgg;
import com.sismatix.Elmizan.R;

import java.util.Locale;

import static com.sismatix.Elmizan.Adapter.Premium_Lawyer_Video_adapter.MyViewHolder.YPlayerr;

public class YouTubeVideoPlayer extends Fragment {

    String vidId;

    public YouTubeVideoPlayer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_you_tube_video_player, container, false);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle bundle = this.getArguments();
        lang_arbi();
        if (bundle != null) {

            vidId = bundle.getString("videoId");
            Log.e("videoooooyt", "" + vidId);

        }

        Youtube(vidId);

        return v;
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
    private void Youtube(final String vidId) {
        Log.e("vidsYT", "" + vidId);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment_mp, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Configgg.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {

                    YPlayerr = youTubePlayer;
                    YPlayerr.setFullscreen(true);
                    YPlayerr.loadVideo(vidId);
                    YPlayerr.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                    YPlayerr.setShowFullscreenButton(true);
                    YPlayerr.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    YPlayerr.getCurrentTimeMillis();

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(activity, "Video Problemmmmm", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
