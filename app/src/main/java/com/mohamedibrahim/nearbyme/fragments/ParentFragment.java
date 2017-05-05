package com.mohamedibrahim.nearbyme.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.activities.ParentActivity;
import com.mohamedibrahim.nearbyme.listeners.FragmentToActivityListener;
import com.mohamedibrahim.nearbyme.listeners.VolleyCallback;
import com.mohamedibrahim.nearbyme.managers.BaseManager;
import com.mohamedibrahim.nearbyme.models.places.Item;
import com.mohamedibrahim.nearbyme.utils.DBUtils;
import com.mohamedibrahim.nearbyme.utils.SoftKeyboardUtils;

import java.util.ArrayList;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class ParentFragment extends Fragment implements VolleyCallback {

    protected FragmentToActivityListener fragmentToActivityListener;
    protected ProgressDialog progress;
    protected BaseManager manager;
    protected int titleRes;
    protected ArrayList<Item> items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = BaseManager.newInstance(((ParentActivity) getActivity()), this);

        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        items = DBUtils.getAllPlaces(getContext());
    }

    public void setFragmentToActivityListener(FragmentToActivityListener fragmentToActivityListener) {
        this.fragmentToActivityListener = fragmentToActivityListener;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }

    @Override
    public void onSuccess(String methodName, Object object) {
    }

    @Override
    public void onFailure(String methodName, String message) {
        if (progress.isShowing()) {
            progress.hide();
        }
        if (fragmentToActivityListener != null) {
            fragmentToActivityListener.showSnackbar(message);
        }
    }

    @Override
    public void onFailure(String methodName, int messageRes) {
        if (progress.isShowing()) {
            progress.hide();
        }
        if (fragmentToActivityListener != null) {
            fragmentToActivityListener.showSnackbar(messageRes);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SoftKeyboardUtils.hideSoftKeyboard(getActivity().getWindow());
        if (fragmentToActivityListener != null) {
            fragmentToActivityListener.hideSnackbar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentToActivityListener != null &&
                !(getFragmentManager().findFragmentById(R.id.container) instanceof HomeFragment)) {
            fragmentToActivityListener.changeTitle(titleRes);
        }
    }

    /**
     * using Picasso will be in next phase
     */
//    public static void SetPicassoImage(Context context, String url, ImageView imageView) {
//        Picasso.with(context)
//                .load(url)
//                .placeholder(R.drawable.progress_animation)
//                .error(R.drawable.ic_error)
//                .fit()
//                .into(imageView);
//    }
    protected void share(String placeName, String placeAddress) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg)
                + " " + placeName
                + getResources().getString(R.string.at)
                + placeAddress
                + " " + getResources().getString(R.string.app_hashtag));
        startActivity(shareIntent);
    }

}
