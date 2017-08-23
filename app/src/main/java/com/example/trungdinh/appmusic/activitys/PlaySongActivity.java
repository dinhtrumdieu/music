package com.example.trungdinh.appmusic.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.trungdinh.appmusic.MyPageIndicator;
import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.adapters.ViewPagerAdapter;
import com.example.trungdinh.appmusic.controls.Controls;
import com.example.trungdinh.appmusic.fragments.LyricFragment;
import com.example.trungdinh.appmusic.fragments.PlaySongFragment;
import com.example.trungdinh.appmusic.services.MusicService;
import com.example.trungdinh.appmusic.utils.PlayerConstants;
import com.example.trungdinh.appmusic.utils.UtilFunctions;
import com.example.trungdinh.appmusic.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class PlaySongActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ViewPagerAdapter mAdapter;
    private MyPageIndicator mIndicator;
    private LinearLayout mLinearLayout;
    private ViewPager mPager;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private static ImageView sBtnPlay;
    private static ImageView sBtnPause;
    private static ImageView sBtnForward;
    private static ImageView sBtnBackward;
    private static TextView mTvNameSong;
    private static TextView mTvNameSinger;
    private ImageButton mImgBtnBack;

    public static SeekBar mSeekBar;
    private Handler mHandler = new Handler();

    private Utilities utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        init();

        //instantiate list
        List<Fragment> list = new ArrayList<>();
        list.add(new PlaySongFragment());
        list.add(new LyricFragment());
        list.add(new LyricFragment());
        //create and set adapter
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), list);
        mPager.setAdapter(mAdapter);
        //create and set MyPageIndicator
        mIndicator = new MyPageIndicator(this, mLinearLayout, mPager, R.drawable.indicator_circle);
        mIndicator.setPageCount(list.size());
        mIndicator.show();
    }

    public void init() {
        initView();
        updateUI();
        changeButton();
        PlayerConstants.PROGRESSBAR_HANDLER = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Integer i[] = (Integer[]) msg.obj;
                songCurrentDurationLabel.setText(UtilFunctions.getDuration(i[0]));
                songTotalDurationLabel.setText(UtilFunctions.getDuration(i[1]));
                Log.d("vodayha", UtilFunctions.getDuration(i[0]) + ":" + UtilFunctions.getDuration(i[1]));
            }
        };

        // set Progress bar values
        mSeekBar.setProgress(0);
        mSeekBar.setMax(100);
        updateProgressBar();

        utils = new Utilities();
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                long totalDuration = MusicService.mp.getDuration();
                long currentDuration = MusicService.mp.getCurrentPosition();
                // Updating progress bar
                int progress = utils.getProgressPercentage(currentDuration, totalDuration);
                //Log.d("Progress", ""+progress);
                mSeekBar.setProgress(progress);
                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void initView() {
        //retrieve ViewPager and LinearLayout
        sBtnPlay = (ImageView) findViewById(R.id.imgBtnPlay);
        sBtnPause = (ImageView) findViewById(R.id.imgBtnPause);
        sBtnForward = (ImageView) findViewById(R.id.imgBtnNext);
        sBtnBackward = (ImageView) findViewById(R.id.imgBtnPrevious);
        mTvNameSong = (TextView) findViewById(R.id.tvNameMusic);
        mTvNameSinger = (TextView) findViewById(R.id.tvNameSinger);
        mImgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);
        mPager = (ViewPager) findViewById(R.id.viewPagerPlayActivity);
        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);
        songCurrentDurationLabel = (TextView) findViewById(R.id.tvTimeCurrent);
        songTotalDurationLabel = (TextView) findViewById(R.id.tvTimeTotalDuration);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        // listeners
        mSeekBar.setOnSeekBarChangeListener(this);
        mImgBtnBack.setOnClickListener(this);
        sBtnPlay.setOnClickListener(this);
        sBtnPause.setOnClickListener(this);
        sBtnBackward.setOnClickListener(this);
        sBtnForward.setOnClickListener(this);
    }

    public static void changeButton() {
        if (PlayerConstants.SONG_PAUSED) {
            sBtnPause.setVisibility(View.GONE);
            sBtnPlay.setVisibility(View.VISIBLE);
        } else {
            sBtnPause.setVisibility(View.VISIBLE);
            sBtnPlay.setVisibility(View.GONE);
        }
    }

    public static void updateUI() {
        mTvNameSong.setText(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getNameSong());
        mTvNameSinger.setText(PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER).getNameSinger());
    }

    public static void changeUI() {
        updateUI();
        changeButton();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnBack:
                finish();
                break;
            case R.id.imgBtnPlay:
                Controls.playControl(getApplicationContext());
                break;
            case R.id.imgBtnPause:
                Controls.pauseControl(getApplicationContext());
                break;
            case R.id.imgBtnPrevious:
                Controls.previousControl(getApplicationContext());
                break;
            case R.id.imgBtnNext:
                Controls.nextControl(getApplicationContext());
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = MusicService.mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        MusicService.mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }
}
