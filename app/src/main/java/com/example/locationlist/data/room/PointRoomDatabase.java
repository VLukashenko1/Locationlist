package com.example.locationlist.data.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.locationlist.data.InitialPoints;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Point.class}, version = 1, exportSchema = false)
public abstract class PointRoomDatabase extends RoomDatabase {
    public abstract PointDAO pointDAO();

    private static volatile PointRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PointRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PointRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PointRoomDatabase.class, "points_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            Log.d("Database", "Database opened;");
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseExecutor.execute(() -> {

                PointDAO dao = INSTANCE.pointDAO();

                if (dao.getRowCount() > 0) return;

                dao.insertPoints(InitialPoints.getInitialPoints());

                Log.d("Database", "Database created (first time)");
            });
        }
    };
}
