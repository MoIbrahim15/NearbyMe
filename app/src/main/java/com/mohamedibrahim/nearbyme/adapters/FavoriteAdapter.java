package com.mohamedibrahim.nearbyme.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.listeners.AdapterListener;
import com.mohamedibrahim.nearbyme.models.places.Item;
import com.mohamedibrahim.nearbyme.views.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohamed Ibrahim on 11/4/2016.
 **/


public class FavoriteAdapter extends AnimationBaseAdapter<Item> {
    public static final int ITEM_CLICK_UNLIKE = 1;
    public static final int ITEM_CLICK_SHARE = 2;
    private AdapterListener listener;
    ArrayList<Item> items;


    public FavoriteAdapter(Context context, ArrayList<Item> items, AdapterListener listener) {
        super(context, R.layout.item_info_content, items);
        this.listener = listener;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(initView(parent));
    }


    class ViewHolder extends AnimationBaseAdapter<Item>.BaseViewHolder {

        @BindView(R.id.tv_title)
        CustomTextView tvTitle;
        @BindView(R.id.tv_address)
        CustomTextView tvAddress;
        @BindView(R.id.tv_distance)
        CustomTextView tvDistance;
        @BindView(R.id.rate_place)
        RatingBar ratingBar;
        @BindView(R.id.chk_like)
        CheckBox chkLike;
        @BindView(R.id.img_share)
        ImageView imgShare;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void fillData(int position) {
            Item item = (Item) getItem(position);
            if (item.getVenue().getName() != null) {
                tvTitle.setText(item.getVenue().getName());
            }
            if (item.getVenue().getLocation().getAddress() != null) {
                tvAddress.setText(item.getVenue().getLocation().getAddress());
            }
            if (item.getVenue().getLocation().getDistance() != null) {
                tvDistance.setText(item.getVenue().getLocation().getDistance().concat(getContext().getResources().getString(R.string.meter)));
            }
            if (item.getVenue().getRating() != null) {
                ratingBar.setRating(Float.parseFloat(item.getVenue().getRating()) / 2);
            }
            chkLike.setChecked(true);
            chkLike.setTag(position);
            imgShare.setTag(position);
        }

        @OnClick(R.id.chk_like)
        void itemUnLike(View v) {
            int mPosition = (int) v.getTag();
            listener.onAdapterListener(mPosition, ITEM_CLICK_UNLIKE);
        }

        @OnClick(R.id.img_share)
        void itemShare(View v) {
            int mPosition = (int) v.getTag();
            listener.onAdapterListener(mPosition, ITEM_CLICK_SHARE);
        }

    }

    @Override
    protected Animator getAnimators(View view, int position) {

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", DEFAULT_FROM, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", DEFAULT_FROM, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        return set;
    }
}
