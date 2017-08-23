package com.example.trungdinh.appmusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.listeners.INavigation;
import com.example.trungdinh.appmusic.models.NavigationData;

import java.util.ArrayList;
import java.util.List;


public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private List<NavigationData> navigationDatas;
    private INavigation listener;
    private Context mContext;

    public NavigationAdapter(INavigation listener, Context context) {
        this.mContext = context;
        navigationDatas = new ArrayList<>();
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNavigationName;
        ImageView ivNavigation;


        private ViewHolder(View itemView) {
            super(itemView);

            tvNavigationName = (TextView) itemView.findViewById(R.id.tvNavigationName);
            ivNavigation = (ImageView) itemView.findViewById(R.id.imgIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           listener.onViewClick(Integer.parseInt(view.getTag().toString()));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_navigation, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NavigationData navigationData = navigationDatas.get(position);
        if (holder.tvNavigationName != null) {
            holder.tvNavigationName.setText(navigationData.getName());
        }
        if (holder.ivNavigation != null) {
            holder.ivNavigation.setImageResource(navigationData.getDrawableId());
            holder.ivNavigation.setTag(position);
        }

        holder.itemView.setTag(position);
        holder.itemView.setBackgroundResource(navigationData.isSelected() ? R.color.colorPrimary : android.R.color.transparent);
    }

    @Override
    public int getItemCount() {
        return navigationDatas.size();
    }

    public void refreshAdapter(ArrayList<NavigationData> data) {
        navigationDatas.clear();
        navigationDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void setSelected(int position) {
        for (int i = 0; i < navigationDatas.size(); i++) {
            navigationDatas.get(i).setSelected(i == position);
        }

        notifyDataSetChanged();
    }
}
