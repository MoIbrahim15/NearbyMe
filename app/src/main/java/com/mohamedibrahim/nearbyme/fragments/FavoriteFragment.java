package com.mohamedibrahim.nearbyme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.adapters.FavoriteAdapter;
import com.mohamedibrahim.nearbyme.listeners.AdapterListener;
import com.mohamedibrahim.nearbyme.listeners.FragmentToActivityListener;
import com.mohamedibrahim.nearbyme.listeners.LifecycleTabsListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Ibrahim on 11/1/2016.
 **/
public class FavoriteFragment extends ParentFragment implements SwipeRefreshLayout.OnRefreshListener, AdapterListener, LifecycleTabsListener {

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private FavoriteAdapter mAdapter;


    public static FavoriteFragment newInstance(FragmentToActivityListener fragmentToActivityListener) {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        favoriteFragment.setFragmentToActivityListener(fragmentToActivityListener);
        return favoriteFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        initRecycler();

        return view;
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FavoriteAdapter(getContext(), R.layout.item_info_content, items, this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        items = placesDBHelper.getAllPlaces();
        mAdapter.notifyDataSetChanged(items);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onAdapterListener(Object mComingObject) {
        placesDBHelper.deletePlace(items.get((int) mComingObject));
        items.remove(items.get((int) mComingObject));
        mAdapter.notifyDataSetChanged(items);
    }

    @Override
    public void onResumeFragment() {
        onRefresh();
    }
}
