package com.example.locationlist.vm;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.locationlist.data.room.Point;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PointsListLocationViewModel extends AndroidViewModel implements ActivityCompat.OnRequestPermissionsResultCallback {
    public MutableLiveData<LatLng> currentLocation = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLocationPermissionAllow = new MutableLiveData<>(true);
    private FusedLocationProviderClient fusedLocationClient;

    public PointsListLocationViewModel(@NonNull Application application) {
        super(application);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application.getApplicationContext());
    }

    public void checkLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            subscribeToCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LocationRequest.PRIORITY_HIGH_ACCURACY
            );
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LocationRequest.PRIORITY_HIGH_ACCURACY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                subscribeToCurrentLocation();
                isLocationPermissionAllow.postValue(true);
            } else {
                isLocationPermissionAllow.postValue(false);
            }
        }
    }

    private void subscribeToCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000) // Update interval in milliseconds (e.g., 10 seconds)
                .setFastestInterval(5000) // Fastest update interval in milliseconds
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Location accuracy preference
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException e) {
            //error
        }

    }

    public LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(),
                    locationResult.getLastLocation().getLongitude());
            if (currentLocation.getValue() == null){
                currentLocation.postValue(latLng);
            }else if(!currentLocation.getValue().equals(latLng)){
                currentLocation.postValue(latLng);
            }
        }
    };
}