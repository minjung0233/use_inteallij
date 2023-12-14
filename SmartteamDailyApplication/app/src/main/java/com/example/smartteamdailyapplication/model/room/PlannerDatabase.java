package com.example.smartteamdailyapplication.model.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.smartteamdailyapplication.model.room.dao.PriorityDao;
import com.example.smartteamdailyapplication.model.room.dao.StatusDao;
import com.example.smartteamdailyapplication.model.room.dao.TaskDao;
import com.example.smartteamdailyapplication.model.room.dao.TypeDao;
import com.example.smartteamdailyapplication.model.room.entity.Priority;
import com.example.smartteamdailyapplication.model.room.entity.Status;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;
import com.example.smartteamdailyapplication.model.room.entity.Type;

import java.util.concurrent.Executors;

@Database(entities = {Tasks.class, Priority.class, Type.class, Status.class}, version = 1)
public abstract class PlannerDatabase extends RoomDatabase {

    private static PlannerDatabase instance;

    public abstract TaskDao taskDao();
    public abstract PriorityDao priorityDao();
    public abstract TypeDao typeDao();
    public abstract StatusDao statusDao();

    public static synchronized PlannerDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlannerDatabase.class, "planner_database")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                                getInstance(context).priorityDao().insert(Priority.populatePriority());
                                getInstance(context).typeDao().insert(Type.populateType());
                                getInstance(context).statusDao().insert(Status.populateStatus());
                            });
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
