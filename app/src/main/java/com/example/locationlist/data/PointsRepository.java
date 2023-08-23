package com.example.locationlist.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.locationlist.data.room.Point;
import com.example.locationlist.data.room.PointDAO;
import com.example.locationlist.data.room.PointRoomDatabase;

import java.util.List;

public class PointsRepository {
    private PointDAO pointDAO;
    private LiveData<List<Point>> allPoints;

    public PointsRepository(Application application){
        PointRoomDatabase db = PointRoomDatabase.getDatabase(application);
        pointDAO = db.pointDAO();
        allPoints = pointDAO.getPointsSortedDefault();
    }

    public LiveData<List<Point>> getAllPoints(){
        return allPoints;
    }
}
