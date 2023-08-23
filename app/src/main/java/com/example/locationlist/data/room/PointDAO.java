package com.example.locationlist.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PointDAO {
    @Query("SELECT COUNT(*) FROM point_table")
    int getRowCount();

    @Query("SELECT * FROM point_table ORDER BY name ASC")
    LiveData<List<Point>> getPointsSortedByName();

    @Query("SELECT * FROM point_table ORDER BY note ASC")
    LiveData<List<Point>> getPointsSortedByNote();

    @Query("SELECT * FROM point_table")
    LiveData<List<Point>> getPointsSortedDefault();

    @Insert
    void insertPoints(List<Point> pointList);

    @Update
    void updatePoint(Point point);

}
