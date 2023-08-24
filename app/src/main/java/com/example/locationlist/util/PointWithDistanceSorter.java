package com.example.locationlist.util;

import com.example.locationlist.Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PointWithDistanceSorter {
    public static List<PointWithDistance> sort(List<PointWithDistance> points, Constants.SortTypes sortTypes) {
        switch (sortTypes) {
            case BY_DISTANCE:
                points.sort(new PointsComparatorDestination());
                return points;
            case BY_NAME:
                points.sort(new PointsComparatorName());
                return points;
            case BY_NOTE:
                points.sort(new PointsComparatorNote());
                return points;
            case REVERSE:
                Collections.reverse(points);
                return points;
        }
        return null;
    }

    private static class PointsComparatorDestination implements Comparator<PointWithDistance> {
        @Override
        public int compare(PointWithDistance p1, PointWithDistance p2) {
            return Float.compare(p1.distanceFloat, p2.distanceFloat);
        }
    }

    private static class PointsComparatorName implements Comparator<PointWithDistance> {
        @Override
        public int compare(PointWithDistance p1, PointWithDistance p2) {
            return p1.point.getName().compareTo(p2.point.getName());
        }
    }

    private static class PointsComparatorNote implements Comparator<PointWithDistance> {
        @Override
        public int compare(PointWithDistance p1, PointWithDistance p2) {
            return p1.point.getNote().compareTo(p2.point.getNote());
        }
    }
}
