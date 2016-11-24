package com.mohamedibrahim.nearbyme.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.activities.HomeActivity;
import com.mohamedibrahim.nearbyme.listeners.FragmentToActivityListener;
import com.mohamedibrahim.nearbyme.listeners.LocationSettingListener;
import com.mohamedibrahim.nearbyme.listeners.OperationListener;
import com.mohamedibrahim.nearbyme.managers.LocationManager;
import com.mohamedibrahim.nearbyme.models.places.Item;
import com.mohamedibrahim.nearbyme.models.places.Places;
import com.mohamedibrahim.nearbyme.models.places.Venue;
import com.mohamedibrahim.nearbyme.views.CustomButton;
import com.mohamedibrahim.nearbyme.views.CustomTextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohamed Ibrahim on 10/31/2016.
 **/

public class MapFragment extends ParentFragment implements OperationListener {

    @BindView(R.id.find_places)
    protected ImageButton btnFindPlaces;
    private static MapView mMapView;
    private static CustomButton btnFindOnMap;

    LocationSettingListener mLocationSettingRequestInterface;
    private Location mComingLocation;
    private HashMap<String, Item> allPlaces;
    private GoogleMap googleMapBase;
    private View mView;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 20005;

    public static MapFragment newInstance(FragmentToActivityListener fragmentToActivityListener) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setFragmentToActivityListener(fragmentToActivityListener);
        return mapFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_map, null);
        mMapView = (MapView) mView.findViewById(R.id.map);
        btnFindOnMap = (CustomButton) mView.findViewById(R.id.btn_find_on_map);
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
            fragmentToActivityListener.showSnackbar(R.string.sry_msg);
        }
    }

    @OnClick(R.id.btn_find_on_map)
    void onClickFindOnMap() {
        try {
            Intent autoCompleteIntent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(new AutocompleteFilter.Builder().setTypeFilter(com.google.android.gms.location.places.Place.TYPE_COUNTRY).build())
                    .build(getActivity());
            getActivity().startActivityForResult(autoCompleteIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();

        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e("Exception", message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void getChosenPlaceOnMap(Intent data, Context context) {
        final com.google.android.gms.location.places.Place place = PlaceAutocomplete.
                getPlace(context, data);
        if (place != null) {
            if (place.getAddress() != null) {
                btnFindOnMap.setText(place.getAddress().toString());
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(place.getLatLng())      // Sets the center of the map to Mountain View
                                .zoom(17.0f)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
            }
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
                        changeBtnMyLocationPosition();
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

    private void changeBtnMyLocationPosition() {
        if (mMapView != null &&
                mMapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }
    }

    private void fillData(final Marker marker, Dialog detailsDialog) {
        Venue venue = allPlaces.get(marker.getSnippet()).getVenue();
        CustomTextView tvName = (CustomTextView) detailsDialog.findViewById(R.id.tv_title);
        CustomTextView tvAddress = (CustomTextView) detailsDialog.findViewById(R.id.tv_address);
        CustomTextView tvDistance = (CustomTextView) detailsDialog.findViewById(R.id.tv_distance);
        RatingBar ratingBar = (RatingBar) detailsDialog.findViewById(R.id.rate_place);
        final CheckBox chkLike = (CheckBox) detailsDialog.findViewById(R.id.chk_like);


        if (venue.getName() != null) {
            tvName.setText(venue.getName());
        }

        if (venue.getLocation().getAddress() != null) {
            tvAddress.setText(venue.getLocation().getAddress());
        }
        if (venue.getLocation().getDistance() != null) {
            tvDistance.setText(venue.getLocation().getDistance().concat(getString(R.string.meter)));
        }
        if (venue.getRating() != null) {
            ratingBar.setRating(Float.parseFloat(venue.getRating()) / 2);
        }

        if (placesDBHelper.ifPlaceFavorite(venue.getId())) {
            chkLike.setChecked(true);
        }

        chkLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chkLike.isChecked()) {
                    placesDBHelper.deletePlace(allPlaces.get(marker.getSnippet()));
                } else {
                    placesDBHelper.addPlace(allPlaces.get(marker.getSnippet()));
                }
            }
        });

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
            if (((Places) object).getWarning() != null) {
                fragmentToActivityListener.showSnackbar(((Places) object).getWarning().getText());
            }
        }
    }
}

