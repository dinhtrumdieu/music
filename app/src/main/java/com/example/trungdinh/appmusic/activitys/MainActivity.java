package com.example.trungdinh.appmusic.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.controls.Controls;
import com.example.trungdinh.appmusic.fragments.HomeFragment;
import com.example.trungdinh.appmusic.fragments.MusicInDeviceFragment;
import com.example.trungdinh.appmusic.fragments.NavigationFragment;
import com.example.trungdinh.appmusic.listeners.IItemMainActivity;
import com.example.trungdinh.appmusic.models.Music;
import com.example.trungdinh.appmusic.services.MusicService;
import com.example.trungdinh.appmusic.utils.PlayerConstants;
import com.example.trungdinh.appmusic.utils.UtilFunctions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IItemMainActivity {

    private static ImageView mBtnPlay;
    private static ImageView mBtnPause;
    private ImageView mBtnForward;
    private ImageView mBtnBackward;
    private static TextView mTvNameSong;
    private static TextView mTvNameSinger;
    private DrawerLayout mDrawer;
    private Handler mHandler;
    public FrameLayout mFrameLayout;
    private LinearLayout llPlaySong;
    private LinearLayout llBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initView();

        //instantiate Handler
        mHandler = new Handler();

        // retrieve Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // retrieve DrawerLayout
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);

        // change color for drawer arrow
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.text_color));
        toggle.syncState();

        //set Opacity(Alpha) for view group
        llBackground.getBackground().setAlpha(242);
        mFrameLayout.getBackground().setAlpha(200);

        // create and replace navigation
        replaceNavigationFragment();
        replaceFragment(0);
    }

    public void initView() {
        // retrieve
        mBtnPlay = (ImageView) findViewById(R.id.btnPlay);
        mBtnPause = (ImageView) findViewById(R.id.btnPause);
        mBtnForward = (ImageView) findViewById(R.id.btnForward);
        mBtnBackward = (ImageView) findViewById(R.id.btnBackward);
        mTvNameSong = (TextView) findViewById(R.id.tvNameMusic);
        mTvNameSinger = (TextView) findViewById(R.id.tvNameSinger);
        llPlaySong = (LinearLayout) findViewById(R.id.llPlaySong);
        llBackground = (LinearLayout) findViewById(R.id.llBackground);
        mFrameLayout = (FrameLayout) findViewById(R.id.flContainerNavigationMenu);
        //listener
        mBtnPlay.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnForward.setOnClickListener(this);
        mBtnBackward.setOnClickListener(this);
        llPlaySong.setOnClickListener(this);

    }

    public void replaceNavigationFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainerNavigationMenu, NavigationFragment.newInstance(), "Navigation").commit();
    }

    public void replaceFragment(int position) {
        Fragment fragment;
        String tag = null;
        String array_navigation[] = getResources().getStringArray(R.array.nav_item_activity_titles);
        switch (position) {
            case 0:
                fragment = new HomeFragment(this);
                tag = array_navigation[position];
                break;
            case 1:
                fragment = new HomeFragment(this);
                tag = array_navigation[position];
                break;
            case 2:
                fragment = new HomeFragment(this);
                tag = array_navigation[position];
                break;
            case 3:
                fragment = new MusicInDeviceFragment(this);
                tag = array_navigation[position];
                break;

            default:
                fragment = new HomeFragment(this);
        }
        replaceFragment(fragment, tag);
    }

    /**
     * Set toolbar title
     * Update the main content by replacing fragments
     */
    public void replaceFragment(final Fragment fragment, final String tag) {
        // set toolbar title
        setToolbarTitle(tag.toUpperCase());
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("xxx","x2");
                // update the main content by replacing fragments
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, tag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            Log.d("xxx","x1");
            mHandler.post(mPendingRunnable);
        }
        closeNavigationDrawer();
    }

    /**
     * This method help close Navigation DrawerLayout
     */
    public void closeNavigationDrawer() {
        mDrawer.closeDrawer(GravityCompat.START);
    }

    /**
     * This method help change title of toolbar
     */
    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onItemClick(Music music, int position) {

        PlayerConstants.SONG_PAUSED = false;
        PlayerConstants.SONG_NUMBER = position;

        boolean isServiceRuning = UtilFunctions.isServiceRunning(MusicService.class.getName(), getApplicationContext());
        if (!isServiceRuning) {
            Intent intent = new Intent(getApplicationContext(), MusicService.class);
            startService(intent);
        } else {
            PlayerConstants.SONG_CHANGE_HANDLER.sendMessage(PlayerConstants.SONG_CHANGE_HANDLER.obtainMessage());
        }
        changeButton();
        updateUI();

        Log.d("xxx", music.getData() + "-" + position);
    }

    public static void updateUI() {
        Music music = PlayerConstants.SONGS_LIST.get(PlayerConstants.SONG_NUMBER);
        mTvNameSong.setText(music.getNameSong());
        mTvNameSinger.setText(music.getNameSinger());
    }

    public static void changeButton() {
        if (PlayerConstants.SONG_PAUSED) {
            mBtnPlay.setVisibility(View.VISIBLE);
            mBtnPause.setVisibility(View.GONE);
        } else {
            mBtnPlay.setVisibility(View.GONE);
            mBtnPause.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        try {
            boolean isServiceRunning = UtilFunctions.isServiceRunning(MusicService.class.getName(), getApplicationContext());
            if (isServiceRunning) {
                updateUI();
            }
            changeButton();
        } catch (Exception e) {
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPlaySong:
                boolean isServiceRunning = UtilFunctions.isServiceRunning(MusicService.class.getName(), getApplicationContext());
                if (isServiceRunning) {
                    Intent intent = new Intent(MainActivity.this, PlaySongActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btnPause:
                Controls.pauseControl(getApplicationContext());
                break;
            case R.id.btnPlay:
                Controls.playControl(getApplicationContext());
                break;
            case R.id.btnForward:
                Controls.nextControl(getApplicationContext());
                break;
            case R.id.btnBackward:
                Controls.previousControl(getApplicationContext());
                break;

        }
    }
}
