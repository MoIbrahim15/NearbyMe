package com.mohamedibrahim.nearbyme.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mohamedibrahim.nearbyme.listeners.AdapterListener;

/**
 * Created by Mohamed Ibrahim on 11/4/2016.
 **/

public abstract class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View view) {
        super(view);
    }
    public abstract void onBind(int position);
    protected AdapterListener mListener;
}