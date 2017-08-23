package com.example.trungdinh.appmusic.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trungdinh.appmusic.MusicRetriever;
import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.TransparentProgressDialog;
import com.example.trungdinh.appmusic.adapters.MusicOnlineRecycleAdapter;
import com.example.trungdinh.appmusic.listeners.IItemMainActivity;
import com.example.trungdinh.appmusic.listeners.IItemRecycle;
import com.example.trungdinh.appmusic.listeners.OnLoadMoreListener;
import com.example.trungdinh.appmusic.models.Music;
import com.example.trungdinh.appmusic.utils.PlayerConstants;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by TrungDinh on 7/31/2017.
 * a
 */

public class MusicInDeviceFragment extends Fragment implements IItemRecycle {

    private RecyclerView recyclerView;
    private MusicOnlineRecycleAdapter mAdapter;
    private IItemMainActivity mListener;
    private TransparentProgressDialog dialog;

    public MusicInDeviceFragment(IItemMainActivity mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_in_device, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rclMusicInDevice);

        dialog = new TransparentProgressDialog(getContext());

        new GetDataMusic().execute();
        return view;
    }

    @Override
    public void onItemClick(Music music, int position) {
        mListener.onItemClick(music, position);
    }

    private class GetDataMusic extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (PlayerConstants.SONGS_LIST.size() <= 0) {
                PlayerConstants.SONGS_LIST = MusicRetriever.getPlaylist(getContext());
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.dismiss();
            // music
            final ArrayList<Music> listMusic = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                if (i < PlayerConstants.SONGS_LIST.size()) {
                    listMusic.add(PlayerConstants.SONGS_LIST.get(i));
                }
            }
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager2);
            mAdapter = new MusicOnlineRecycleAdapter(getContext(), listMusic, MusicInDeviceFragment.this, recyclerView);
            recyclerView.setAdapter(mAdapter);
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (listMusic.size() != PlayerConstants.SONGS_LIST.size()) {
                        listMusic.add(null);
                        mAdapter.notifyItemInserted(listMusic.size() - 1);

                        //Load more data for reyclerview
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("haint", "Load More 2");

                                //Remove loading item
                                listMusic.remove(listMusic.size() - 1);
                                mAdapter.notifyItemRemoved(listMusic.size());

                                //Load data
                                int index = listMusic.size();
                                int end = index + 40;
                                for (int i = index; i < end; i++) {
                                    if (i < PlayerConstants.SONGS_LIST.size()) {
                                        listMusic.add(PlayerConstants.SONGS_LIST.get(i));
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setLoaded();
                            }
                        }, 2000);
                    }
                }
            });

        }
    }
}
