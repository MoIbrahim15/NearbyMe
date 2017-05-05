package com.mohamedibrahim.nearbyme.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.gson.Gson;
import com.mohamedibrahim.nearbyme.R;
import com.mohamedibrahim.nearbyme.activities.HomeActivity;
import com.mohamedibrahim.nearbyme.listeners.FragmentToActivityListener;
import com.mohamedibrahim.nearbyme.listeners.LocationSettingListener;
import com.mohamedibrahim.nearbyme.listeners.OperationListener;
import com.mohamedibrahim.nearbyme.managers.LocationManager;
import com.mohamedibrahim.nearbyme.models.general_response.ResponseObject;
import com.mohamedibrahim.nearbyme.models.places.Item;
import com.mohamedibrahim.nearbyme.models.places.Places;
import com.mohamedibrahim.nearbyme.models.places.Venue;
import com.mohamedibrahim.nearbyme.utils.DBUtils;
import com.mohamedibrahim.nearbyme.utils.NetworkUtils;
import com.mohamedibrahim.nearbyme.views.CustomButton;
import com.mohamedibrahim.nearbyme.views.CustomTextView;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mohamed Ibrahim on 10/31/2016.
 **/

public class MapFragment extends ParentFragment implements OperationListener, LoaderManager.LoaderCallbacks<String> {

    @BindView(R.id.find_places)
    ImageButton btnFindPlaces;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    private static MapView mMapView;
    private static CustomButton btnFindOnMap;

    LocationSettingListener mLocationSettingRequestInterface;
    private Location mComingLocation;
    private HashMap<String, Item> allPlaces;
    private GoogleMap googleMapBase;
    private View mView;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 20005;
    private static final String URL_EXTRA = "URL";
    private static final int LOADER_ID = 1;

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
            fetchPlacesFromAPI(ll);
        } else {
            fragmentToActivityListener.showSnackbar(R.string.sry_msg);
        }
    }


    private void fetchPlacesFromAPI(String location) {
        progressBar.setVisibility(View.VISIBLE);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(URL_EXTRA, String.valueOf(NetworkUtils.buildUrl(getContext(), location)));
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<String> moviesLoader = loaderManager.getLoader(LOADER_ID);
        if (moviesLoader == null) {
            loaderManager.initLoader(LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, queryBundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(getActivity()) {
            String mJsonResult;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                if (mJsonResult != null) {
                    deliverResult(mJsonResult);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                try {
                    String urlString = args.getString(URL_EXTRA);
                    if (urlString == null || TextUtils.isEmpty(urlString)) {
                        return null;
                    } else {
                        URL url = new URL(urlString);
                        return NetworkUtils.getResponseFromHttpUrl(url);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String jsonResult) {
                mJsonResult = jsonResult;
                super.deliverResult(mJsonResult);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (null != data) {
            Log.v("response", data);
            progressBar.setVisibility(View.GONE);
            ResponseObject responseObject = new Gson().fromJson(data, ResponseObject.class);
            if (responseObject.getMeta().getCode().equalsIgnoreCase("200")) {
                String json;
                json = new Gson().toJson(responseObject.getResponse());
                Places places = new Gson().fromJson(json, Places.class);
                for (int i = 0; i < places.getGroups().get(0).getItems().size(); i++) {
                    Item item = places.getGroups().get(0).getItems().get(i);
                    String itemLatLng = item.getVenue().getLocation().getLat() +
                            "," + item.getVenue().getLocation().getLng();

                    MarkerOptions newMarker = new MarkerOptions();
                    newMarker.position(new LatLng(item.getVenue().getLocation().getLat(),
                            item.getVenue().getLocation().getLng()));
                    newMarker.snippet(itemLatLng);
                    allPlaces.put(itemLatLng, item);
                    googleMapBase.addMarker(newMarker);
                }
                if (places.getWarning() != null) {
                    fragmentToActivityListener.showSnackbar(places.getWarning().getText());
                }
            } else {
                if (fragmentToActivityListener != null) {
                    fragmentToActivityListener.showSnackbar(R.string.sry_msg);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        /*
         * We aren't using this method in application, but i required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
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
                                detailsDialog.setContentView(R.layout.dialog_info_content);

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
        final Venue venue = allPlaces.get(marker.getSnippet()).getVenue();
        CustomTextView tvName = (CustomTextView) detailsDialog.findViewById(R.id.tv_title);
        CustomTextView tvAddress = (CustomTextView) detailsDialog.findViewById(R.id.tv_address);
        CustomTextView tvDistance = (CustomTextView) detailsDialog.findViewById(R.id.tv_distance);
        RatingBar ratingBar = (RatingBar) detailsDialog.findViewById(R.id.rate_place);
        final CheckBox chkLike = (CheckBox) detailsDialog.findViewById(R.id.chk_like);
        ImageView imgShare = (ImageView) detailsDialog.findViewById(R.id.img_share);


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

        if (DBUtils.ifPlaceFavorite(venue.getId(), getContext())) {
            chkLike.setChecked(true);
        }

        chkLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chkLike.isChecked()) {
                    DBUtils.deletePlace(allPlaces.get(marker.getSnippet()), getContext());
                } else {
                    DBUtils.addPlace(allPlaces.get(marker.getSnippet()), getContext());
                }
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(venue.getName(), venue.getLocation().getAddress());
            }
        });
    }
}

