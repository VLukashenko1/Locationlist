package com.example.locationlist.vm;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class PointsListLocationViewModel extends AndroidViewModel implements ActivityCompat.OnRequestPermissionsResultCallback {
    private MutableLiveData<LatLng> currentLocation = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLocationPermissionAllow = new MutableLiveData<>(true);
    private FusedLocationProviderClient fusedLocationClient;

    public MutableLiveData<LatLng> getCurrentLocation() {
        return currentLocation;
    }

    public MutableLiveData<Boolean> getIsLocationPermissionAllow() {
        return isLocationPermissionAllow;
    }

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
                .setInterval(100000) // Update interval in milliseconds (e.g., 100 seconds)
                .setFastestInterval(50000) // Fastest update interval in milliseconds
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Location accuracy preference
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, null);
            Log.d("Location service", "Location tracking started");
        } catch (SecurityException e) {
            //error
            Log.d("Location service", "Location tracking NOT started" + e.getLocalizedMessage());
        }

    }

    public LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(),
                    locationResult.getLastLocation().getLongitude());

            if (currentLocation == null || currentLocation.getValue() == null){
                currentLocation.postValue(latLng);
            }else if(!currentLocation.getValue().equals(latLng)){
                currentLocation.postValue(latLng);
            }
        }
    };
}