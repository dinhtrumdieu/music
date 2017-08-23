package com.example.trungdinh.appmusic.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.trungdinh.appmusic.MusicRetriever;
import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.TransparentProgressDialog;
import com.example.trungdinh.appmusic.adapters.AbumRecycleAdapter;
import com.example.trungdinh.appmusic.adapters.MusicOfflineRecycleAdapter;
import com.example.trungdinh.appmusic.adapters.VideoRecycleAdapter;
import com.example.trungdinh.appmusic.listeners.IItemMainActivity;
import com.example.trungdinh.appmusic.listeners.IItemRecycle;
import com.example.trungdinh.appmusic.models.Abum;
import com.example.trungdinh.appmusic.models.Music;
import com.example.trungdinh.appmusic.models.Video;
import com.example.trungdinh.appmusic.utils.PlayerConstants;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class HomeFragment extends Fragment implements IItemRecycle {

    private RecyclerView recyclerView;
    private RecyclerView rclVideo;
    private RecyclerView rclAbum;
    private AbumRecycleAdapter mAbumAdapter;
    private VideoRecycleAdapter mVideoAdapter;
    private MusicOfflineRecycleAdapter mAdapter;
    private IItemMainActivity mListener;
    private ScrollView scrollView;
    TransparentProgressDialog dialog;

    public HomeFragment(IItemMainActivity mListener) {
        this.mListener = mListener;
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IItemMainActivity) {
            mListener = (IItemMainActivity) context;
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleMusic);
        rclAbum = (RecyclerView) view.findViewById(R.id.rclAbum);
        rclVideo = (RecyclerView) view.findViewById(R.id.rclVideo);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

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

            // video
            List<Video> listVideo = new ArrayList<>();
            listVideo.add(new Video(R.drawable.img_video + "", "Video demo", "FAP TV"));
            listVideo.add(new Video(R.drawable.img_video1 + "", "Video demo", "FAP TV"));
            listVideo.add(new Video(R.drawable.img_video2 + "", "Video demo", "FAP TV"));
            mVideoAdapter = new VideoRecycleAdapter(getContext(), listVideo);
            rclVideo.setAdapter(mVideoAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rclVideo.setLayoutManager(layoutManager);
            OverScrollDecoratorHelper.setUpOverScroll(rclVideo, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);


            // abum
            final List<Abum> list = new ArrayList<>();
            list.add(new Abum("Chúng ta không thuộc về nhau", "Sơn Tùng MTP", R.drawable.img_abum + ""));
            list.add(new Abum("Âm thầm bên em", "Sơn Tùng MTP", R.drawable.abum1 + ""));
            list.add(new Abum("Cơn mưa ngang qua", "Sơn Tùng MTP", R.drawable.abum2 + ""));
            list.add(new Abum("Chiều lên bản thượng", "Sơn Tùng MTP", R.drawable.abum2 + ""));
            list.add(new Abum("Shape of you", "Sơn Tùng MTP", R.drawable.abum1 + ""));

            mAbumAdapter = new AbumRecycleAdapter(getContext(), list);
            rclAbum.setAdapter(mAbumAdapter);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rclAbum.setLayoutManager(layoutManager1);
            OverScrollDecoratorHelper.setUpOverScroll(rclAbum, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

            // music
            final ArrayList<Music> listMusic = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                if (i < PlayerConstants.SONGS_LIST.size()) {
                    listMusic.add(PlayerConstants.SONGS_LIST.get(i));
                }

            }
            mAdapter = new MusicOfflineRecycleAdapter(getContext(), listMusic, HomeFragment.this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        }
    }

}
