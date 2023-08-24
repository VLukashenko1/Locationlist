package com.example.locationlist.util;

import android.location.Location;

import com.example.locationlist.data.room.Point;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DistanceCalculator {
    private CompletableFuture<List<String>> supplier;

    public CompletableFuture<List<String>> getSupplier(List<Point> points, LatLng currentLocation) {
        supplier = CompletableFuture.supplyAsync(() -> {
            Location location = new Location("currentLocation");
            location.setLongitude(currentLocation.longitude);
            location.setLatitude(currentLocation.latitude);

            List<String> distanceList = new ArrayList<>();

            for (Point point:points){
                Location location2 = new Location("point");
                location2.setLatitude(point.getLat());
                location2.setLongitude(point.getLng());

                float distance = location2.distanceTo(location);
                if (distance > 1000){
                    distance = distance/1000;
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

    public String calculateDistanceBetweenTwoLatLngString(Point point, LatLng currentLocation){
        if (currentLocation.longitude == 0 && currentLocation.longitude == 0){
            return "- 0 -";
        }
        Location location = new Location("currentLocation");
        location.setLongitude(currentLocation.longitude);
        location.setLatitude(currentLocation.latitude);

        Location location2 = new Location("point");
        location2.setLatitude(point.getLat());
        location2.setLongitude(point.getLng());

        float distance = location.distanceTo(location2);
        if (distance > 1000){
            distance = distance /1000;
            return Math.round(distance) + "km";
        }
        else return Math.round(distance) + "m";
    }

    public float calculateDistanceBetweenTwoLatLngFloat(Point point, LatLng currentLocation) {
        if (currentLocation.longitude == 0 && currentLocation.longitude == 0){
            return 0;
        }

        Location location1 = new Location("point1");
        location1.setLatitude(currentLocation.latitude);
        location1.setLongitude(currentLocation.longitude);

        Location location2 = new Location("point2");
        location2.setLatitude(point.getLat());
        location2.setLongitude(point.getLng());

        return location1.distanceTo(location2);
    }
}
