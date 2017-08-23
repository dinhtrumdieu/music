package com.example.trungdinh.appmusic.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trungdinh.appmusic.activitys.MainActivity;
import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.listeners.INavigation;
import com.example.trungdinh.appmusic.adapters.NavigationAdapter;
import com.example.trungdinh.appmusic.models.NavigationData;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class NavigationFragment extends Fragment implements INavigation {

    private RecyclerView rvNavigation;
    private NavigationAdapter mAdapter;
    private int array_icons[] = {R.drawable.ic_home, R.drawable.ic_playlist, R.drawable.ic_video, R.drawable.ic_music_navigation, R.drawable.ic_setting};
    public static final String TAG = NavigationFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static NavigationFragment newInstance() {
        NavigationFragment navigationFragment = new NavigationFragment();
        Bundle args = new Bundle();
        navigationFragment.setArguments(args);
        return navigationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_drawer, container, false);
        rvNavigation = (RecyclerView) view.findViewById(R.id.rccNavigation);
        fillData();
        setAdapter();
        mAdapter.setSelected(0);
        return view;
    }

    private ArrayList<NavigationData> fillData() {
        ArrayList<NavigationData> navigationDataArrayList = new ArrayList<>();
        String array_navigation[] = getResources().getStringArray(R.array.nav_item_activity_titles);
        for (int i = 0; i < array_navigation.length; i++) {
            NavigationData navigationData = new NavigationData();
            navigationData.setName(array_navigation[i]);
            navigationData.setDrawableId(array_icons[i]);
            navigationDataArrayList.add(navigationData);
        }
        return navigationDataArrayList;
    }

    private void setAdapter() {
        mAdapter = new NavigationAdapter(this, getContext());
        rvNavigation.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Horizontal
        OverScrollDecoratorHelper.setUpOverScroll(rvNavigation, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        // Vertical
        OverScrollDecoratorHelper.setUpOverScroll(rvNavigation, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        rvNavigation.setAdapter(mAdapter);
        mAdapter.refreshAdapter(fillData());
    }

    @Override
    public void onViewClick(int position) {
        Log.e(TAG, "View" + position);
        replaceFragment(position);
    }

    private void replaceFragment(int position) {
        ((MainActivity) getActivity()).replaceFragment(position);
        mAdapter.setSelected(position);
    }
}
