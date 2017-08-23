package com.example.trungdinh.appmusic.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trungdinh.appmusic.services.MusicService;
import com.example.trungdinh.appmusic.view.DefaultLrcBuilder;
import com.example.trungdinh.appmusic.view.ILrcBuilder;
import com.example.trungdinh.appmusic.view.ILrcView;
import com.example.trungdinh.appmusic.view.LrcRow;
import com.example.trungdinh.appmusic.view.LrcView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LyricFragment extends Fragment {

    public final static String TAG = "MainActivity";
    ILrcView mLrcView;
    private int mPalyTimerDuration = 1000;
    private Timer mTimer;
    private TimerTask mTask;

    public String getLyricFromUrl(String link){
        String result = "";
        try {
            // Create a URL for the desired page
            URL url = new URL(link); //My text file location
            //First open the connection
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60000); // timing out in a minute

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            //t=(TextView)findViewById(R.id.TextView1); // ideally do this in onCreate()
            String str;
            while ((str = in.readLine()) != null) {
                Log.d("voday",str);
                result += str + "\r\n";
            }
            in.close();
        } catch (Exception e) {
            Log.d("MyTag",e.toString());
        }
        return result;
    }
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                Result += line + "\r\n";
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLrcView = new LrcView(getContext(), null);
        View view = (View) mLrcView;

        new Thread(new Runnable() {
            @Override
            public void run() {
               // String lrc = getLyricFromUrl("https://firebasestorage.googleapis.com/v0/b/chatapp-26120.appspot.com/o/letmeloveyou.txt?alt=media&token=48cf9828-a4a1-4f7a-9840-7e23e806bc9e");

                String lrc = getFromAssets("letmeloveyou.lrc");
                ILrcBuilder builder = new DefaultLrcBuilder();
                final List<LrcRow> rows = builder.getLrcRows(lrc);
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        mLrcView.setLrc(rows);
                    }
                });
            }
        }).start();

        beginPlay();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void beginPlay(){
        if (mTimer == null) {
            mTimer = new Timer();
            mTask = new LrcTask();
            mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
        }
        MusicService.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                stopLrcPlay();
            }
        });
    }

    public void stopLrcPlay() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    class LrcTask extends TimerTask {

        long beginTime = -1;

        @Override
        public void run() {
            if (beginTime == -1) {
                beginTime = System.currentTimeMillis();
            }

            try{
                final long timePassed = MusicService.mp.getCurrentPosition();
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mLrcView.seekLrcToTime(timePassed);
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }


        }
    }
}
