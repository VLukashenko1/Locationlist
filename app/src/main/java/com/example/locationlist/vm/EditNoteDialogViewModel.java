package com.example.locationlist.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.locationlist.data.PointsRepository;
import com.example.locationlist.data.room.Point;
import com.example.locationlist.data.room.PointRoomDatabase;

public class EditNoteDialogViewModel extends AndroidViewModel {

    private PointsRepository mPointsRepo;

    public EditNoteDialogViewModel(@NonNull Application application) {
        super(application);
        mPointsRepo = new PointsRepository(application);

    }
    public void updatePointNote(Point point){
        PointRoomDatabase.databaseExecutor.execute(()->{
            mPointsRepo.getPointDAO().updatePoint(point);
        });
    }
}
