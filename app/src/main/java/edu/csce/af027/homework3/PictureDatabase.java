package edu.csce.af027.homework3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Picture.class}, version = 1, exportSchema = false)
public abstract class PictureDatabase extends RoomDatabase {
    public abstract PictureDAO pictureDAO();


//    private static volatile PictureDatabase INSTANCE;
//    private static final int NUMBER_OF_THREADS = 4;
//    static final ExecutorService databaseWriteExecutor =
//            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
//
//
//    static PictureDatabase getDatabase(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (PictureDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            PictureDatabase.class, "pciture_database")
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
}

