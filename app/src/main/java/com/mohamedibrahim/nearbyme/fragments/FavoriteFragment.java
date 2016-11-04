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
import com.mohamedibrahim.nearbyme.models.places.Item;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Ibrahim on 11/1/2016.
 **/
public class FavoriteFragment extends ParentFragment implements AdapterListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    FavoriteAdapter mAdapter;
    ArrayList<Item> items;

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
        items = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Location location = new Location();
//            location.setDistance("116");
//            location.setAddress("Mohandesen");
//            Venue venue = new Venue();
//            venue.setLocation(location);
//            venue.setName("Cafe");
//            venue.setRating("5");
//            Item item = new Item();
//            item.setVenue(venue);
//            items.add(item);
//        }
//        initRecycler();

        return view;
    }

    @Override
    public void onAdapterListener(Object mComingObject) {

    }


    @Override
    public void onRefresh() {

    }


    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FavoriteAdapter(getContext(), R.layout.item_info_content, items, this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
