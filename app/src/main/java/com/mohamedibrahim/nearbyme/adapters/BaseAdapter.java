package com.mohamedibrahim.nearbyme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedibrahim.nearbyme.views.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 11/4/2016.
 **/

abstract class BaseAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private int itemLayoutRes;
    private ArrayList<?> items;
    View mView;

    BaseAdapter(Context context, int itemLayoutRes, ArrayList<?> items) {
        this.items = items;
        this.context = context;
        this.itemLayoutRes = itemLayoutRes;
    }

    void initView(ViewGroup parent) {
        mView = LayoutInflater.from(context).inflate(itemLayoutRes, parent, false);
    }

    Object getItem(int position) {
        if (items != null && items.size() > 0) {
            return items.get(position);
        }
        return null;
    }

    public void notifyDataSetChanged(ArrayList<?> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);
    }
}