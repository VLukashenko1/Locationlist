package com.example.locationlist.util;

import android.location.Location;

import com.example.locationlist.data.room.Point;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DistanceCalculator {
    public CompletableFuture<List<String>> supplier;

    public CompletableFuture<List<String>> getSupplier(List<Point> points, LatLng currentLocation) {
        supplier = CompletableFuture.supplyAsync(() -> {
            Location location = new Location("currentLocation");
            location.setLongitude(currentLocation.longitude);
            location.setLatitude(currentLocation.latitude);

            List<String> distanceList = new ArrayList<>();

            for (Point point:points){
                Location location2 = new Location("point");
                location2.setLatitude(point.lat);
                location2.setLongitude(point.lng);

                float distance = location2.distanceTo(location);
                if (distance > 1000){
                    distance = distance/100;
                    distanceList.add(Math.round(distance) + " km");
                }
                else {
                    distanceList.add(Math.round(distance) + " m");
                }
            }
            return distanceList;
        });

        return supplier;
    }

    public String calculateDistance(Point point, LatLng currentLocation){
        Location location = new Location("currentLocation");
        location.setLongitude(currentLocation.longitude);
        location.setLatitude(currentLocation.latitude);

        Location location2 = new Location("point");
        location2.setLatitude(point.lat);
        location2.setLongitude(point.lng);

        float distance = location.distanceTo(location2);
        if (distance > 1000){
            distance = distance /1000;
            return Math.round(distance) + "km";
        }
        else return Math.round(distance) + "m";
    }
}
