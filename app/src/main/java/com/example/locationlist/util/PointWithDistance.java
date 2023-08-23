package com.example.locationlist.util;

import com.example.locationlist.data.room.Point;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PointWithDistance {
    public Point point;
    public String distanceString;
    public float distanceFloat;

    public PointWithDistance(Point point, LatLng currentLocation){
        this.point = point;
        distanceString = new DistanceCalculator().calculateDistanceBetweenTwoLatLngString(point, currentLocation);
        distanceFloat = new DistanceCalculator().calculateDistanceBetweenTwoLatLngFloat(point, currentLocation);
    }

    public static List<PointWithDistance> getPointListWithDistance(List<Point> points, LatLng currentLocation){
        List<PointWithDistance> pointWithDistanceList = new ArrayList<>();
        for (Point point : points){
            pointWithDistanceList.add(new PointWithDistance(point, currentLocation));
        }
        return pointWithDistanceList;
    }

}
