package com.maps.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.maps.route.extensions.MapExtensionKt;
import com.maps.route.model.TravelMode;

public class MapsFragment extends Fragment {
    private final OnMapReadyCallback callback = googleMap -> {

        LatLng source = new LatLng(31.490127, 74.316971); //starting point (LatLng)
        LatLng destination = new LatLng(31.474316, 74.316112); // ending point (LatLng)

        //zoom/move cam on map ready
        MapExtensionKt.moveCameraOnMap(googleMap, 16, true, source);

        //only draw route on map
        MapExtensionKt.drawRouteOnMap(googleMap,
                getString(R.string.google_map_api_key),
                getContext(),
                source,
                destination,
                getActivity().getColor(R.color.pathColor),
                true, true, 13, TravelMode.DRIVING, null, null);


        //If want to draw route on maps and also required the Estimates
        MapExtensionKt.drawRouteOnMap(googleMap,
                getString(R.string.google_map_api_key),
                getContext(),
                source,
                destination,
                getActivity().getColor(R.color.pathColor),
                true, true, 13, TravelMode.DRIVING, null /*deprecated*/,
                //call the lambda if you need the estimates
                (estimates -> {
                    //Estimated time of arrival
                    Log.d("estimatedTimeOfArrival", "withUnit " + estimates.getDuration().getText());
                    Log.d("estimatedTimeOfArrival", "InMilliSec " + estimates.getDuration().getValue());

                    //Google suggested path distance
                    Log.d("GoogleSuggestedDistance", "withUnit " + estimates.getDistance().getText());
                    Log.d("GoogleSuggestedDistance", "InMilliSec " + estimates.getDistance().getValue());
                    return null;
                }));


        //if you only want the Estimates (Distance & Time of arrival)
        MapExtensionKt.getTravelEstimations(
                getString(R.string.google_map_api_key),
                source,
                destination,
                TravelMode.DRIVING,
                null /*deprecated*/,
                //call the lambda if you need the estimates
                (estimates -> {
                    //Estimated time of arrival
                    Log.d("estimatedTimeOfArrival", "withUnit " + estimates.getDuration().getText());
                    Log.d("estimatedTimeOfArrival", "InMilliSec " + estimates.getDuration().getValue());

                    //Google suggested path distance
                    Log.d("GoogleSuggestedDistance", "withUnit " + estimates.getDistance().getText());
                    Log.d("GoogleSuggestedDistance", "InMilliSec " + estimates.getDistance().getValue());
                    return null;
                }));
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}