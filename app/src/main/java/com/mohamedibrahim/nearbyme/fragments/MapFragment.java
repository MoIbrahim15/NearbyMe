package com.mohamedibrahim.nearbyme.fragments;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.activities.HomeActivity;
import com.mohamedibrahim.nearbyme.activities.ParentActivity;
import com.mohamedibrahim.nearbyme.listeners.FragmentToActivityListener;
import com.mohamedibrahim.nearbyme.listeners.LocationSettingListener;
import com.mohamedibrahim.nearbyme.listeners.OperationListener;
import com.mohamedibrahim.nearbyme.managers.LocationManager;
import com.mohamedibrahim.nearbyme.models.places.Item;
import com.mohamedibrahim.nearbyme.models.places.Places;
import com.mohamedibrahim.nearbyme.models.places.Venue;
import com.mohamedibrahim.nearbyme.views.CustomTextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohamed Ibrahim on 10/31/2016.
 **/

public class MapFragment extends ParentFragment implements OperationListener {

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.find_places)
    ImageButton btnFindPlaces;
    LocationSettingListener mLocationSettingRequestInterface;
    private Location mComingLocation;
    GoogleMap googleMapBase;
    View mView;
    HashMap<String, Item> allPlaces;

    public static MapFragment newInstance(FragmentToActivityListener fragmentToActivityListener) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setFragmentToActivityListener(fragmentToActivityListener);
        return mapFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_map, null);
        ButterKnife.bind(this, mView);
        mMapView.onCreate(savedInstanceState);
        mLocationSettingRequestInterface = LocationManager.getInstance(getActivity()).setMapView(mMapView).buildGoogleMapApiClient(this);
        LocationManager.getInstance(getActivity()).setMapView(mMapView).onMovingMapLocation(mMapView, btnFindPlaces);
        allPlaces = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).setLocationSettingListener(mLocationSettingRequestInterface);
        return mView;
    }


    @OnClick(R.id.find_places)
    void onClickFindPlaces() {
        if (mComingLocation != null && mComingLocation.getLatitude() != 0.0) {
            String ll = "ll=" + mComingLocation.getLatitude() + "," + mComingLocation.getLongitude() /*+ "&query=asasdsdgfsfs"*/;
            progress.show();
            manager.createRequest("explore?", ll, Places.class);
        } else {
            ((ParentActivity) getActivity()).showSnackbar(R.string.sry_msg);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        LocationManager.getInstance(getActivity()).onDestroy();

    }


    @Override
    public void onOperationCompleted(int resultCode, Object mComingValue) {
        if (mComingValue instanceof Location) {
            mComingLocation = (Location) mComingValue;
//            Log.v("LOCATIOOOOOOOON------", mComingLocation.getLatitude() + "");

            if (resultCode == LocationManager.FIRST_LOCATION_CALL) {
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        googleMapBase = googleMap;
                        googleMap.setMyLocationEnabled(true);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mComingLocation.getLatitude(), mComingLocation.getLongitude()), 17.0f));
                        googleMapBase.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                Dialog detailsDialog = new Dialog(getContext(), R.style.DialogStyle);
                                detailsDialog.setContentView(R.layout.item_info_content);

                                fillData(marker, detailsDialog);

                                detailsDialog.show();
                                return true;
                            }
                        });

                    }
                });
            }
        }
    }

    private void fillData(Marker marker, Dialog detailsDialog) {
        Venue venue = allPlaces.get(marker.getSnippet()).getVenue();
        CustomTextView tvName = (CustomTextView) detailsDialog.findViewById(R.id.tv_title);
        CustomTextView tvAddress = (CustomTextView) detailsDialog.findViewById(R.id.tv_address);
        CustomTextView tvDistance = (CustomTextView) detailsDialog.findViewById(R.id.tv_distance);
        RatingBar ratingBar = (RatingBar) detailsDialog.findViewById(R.id.rate_place);
        CheckBox chkLike = (CheckBox) detailsDialog.findViewById(R.id.chk_like);

        tvName.setText(venue.getName());
        tvAddress.setText(venue.getLocation().getAddress());
        tvDistance.setText(venue.getLocation().getDistance().concat(getString(R.string.meter)));
        ratingBar.setRating(Float.parseFloat(venue.getRating()) / 2);

    }

    @Override
    public void onSuccess(String methodName, Object object) {
        progress.hide();
        if (object instanceof Places) {
            for (int i = 0; i < ((Places) object).getGroups().get(0).getItems().size(); i++) {
                Item item = ((Places) object).getGroups().get(0).getItems().get(i);
                String itemLatLng = item.getVenue().getLocation().getLat() +
                        "," + item.getVenue().getLocation().getLng();

//                Log.v("result", item.getVenue().getRating());

                MarkerOptions newMarker = new MarkerOptions();
                newMarker.position(new LatLng(item.getVenue().getLocation().getLat(),
                        item.getVenue().getLocation().getLng()));
                newMarker.snippet(itemLatLng);
                allPlaces.put(itemLatLng, item);
                googleMapBase.addMarker(newMarker);

            }
        }
    }
}

