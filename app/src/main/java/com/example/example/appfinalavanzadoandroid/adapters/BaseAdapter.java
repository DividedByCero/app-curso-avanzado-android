package com.example.example.appfinalavanzadoandroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.CustomViewHolder> {
    private Context mContext;
    private int mViewId;
    private ArrayList<T> mItems;

    public T GetItem(int position){
        return mItems.get(position);
    }

    BaseAdapter(Context mContext, int mViewId, ArrayList<T> items) {
        this.mContext = mContext;
        this.mViewId = mViewId;
        this.mItems = items;
    }

     public static class CustomViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ViewGroup mViewGroup;
        CustomViewHolder(View itemView, ViewGroup viewGroup) {
            super(itemView);
            this.mView = itemView;
            this.mViewGroup = viewGroup;
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(mViewId, viewGroup, false);
        return new CustomViewHolder(v, viewGroup);
    }

    @Override
    public abstract void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i);

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}



