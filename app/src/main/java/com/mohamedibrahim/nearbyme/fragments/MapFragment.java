package com.mohamedibrahim.nearbyme.fragments;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
            Log.v("LOCATIOOOOOOOON------", mComingLocation.getLatitude() + "");

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
                                final Dialog detailsDialog = new Dialog(getContext(), R.style.DialogStyle);
                                detailsDialog.setContentView(R.layout.info_content);

                                TextView tvName = (TextView) detailsDialog.findViewById(R.id.tv_title);
                                TextView tvAddress = (TextView) detailsDialog.findViewById(R.id.tv_address);
                                TextView tvDistance = (TextView) detailsDialog.findViewById(R.id.tv_distance);

                                tvName.setText(allPlaces.get(marker.getSnippet()).getVenue().getName());
                                tvAddress.setText(allPlaces.get(marker.getSnippet()).getVenue().getLocation().getAddress());
                                tvDistance.setText(allPlaces.get(marker.getSnippet()).getVenue().getLocation().getDistance().concat(getString(R.string.meter)));
                                detailsDialog.show();
                                return true;
                            }
                        });

                    }
                });
            }
        }
    }

    @Override
    public void onSuccess(String methodName, Object object) {
        progress.hide();
        if (object instanceof Places) {
            for (int i = 0; i < ((Places) object).getGroups().get(0).getItems().size(); i++) {
                Item item = ((Places) object).getGroups().get(0).getItems().get(i);
                String itemLatLng = item.getVenue().getLocation().getLat() +
                        "," + item.getVenue().getLocation().getLng();
                Log.v("result", item.getVenue().getName());
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

