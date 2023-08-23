package com.example.locationlist.data.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "point_table")
public class Point {
    @PrimaryKey(autoGenerate = true)
    public int uId;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "lat")
    public double lat;
    @ColumnInfo(name = "lng")
    public double lng;
    @ColumnInfo(name = "note")
    public String note;
    @ColumnInfo(name = "photoLink")
    public String photoLink;

    public Point(){}

    public Point(String name, double lat, double lng, String note, String photoLink) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.note = note;
        this.photoLink = photoLink;
    }
}
