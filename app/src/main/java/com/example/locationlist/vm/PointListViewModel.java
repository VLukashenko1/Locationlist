package com.example.locationlist.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.locationlist.data.PointsRepository;
import com.example.locationlist.data.room.Point;

import java.util.List;

public class PointListViewModel extends AndroidViewModel {
    private LiveData<List<Point>> mAllPoints;
    private PointsRepository mPointsRepo;
    public LiveData<List<Point>> getAllPoints() {
        return mAllPoints;
    }

    public PointListViewModel(@NonNull Application application) {
        super(application);

        mPointsRepo = new PointsRepository(application);
        mAllPoints = mPointsRepo.getAllPoints();
    }
}
