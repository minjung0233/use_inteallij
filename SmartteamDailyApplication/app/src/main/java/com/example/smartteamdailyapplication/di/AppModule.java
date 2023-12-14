package com.example.smartteamdailyapplication.di;

import android.content.Context;

import com.example.smartteamdailyapplication.model.PlannerRepository;
import com.example.smartteamdailyapplication.model.PlannerRepositoryImpl;
import com.example.smartteamdailyapplication.model.room.PlannerDatabase;
import com.example.smartteamdailyapplication.model.room.dao.PriorityDao;
import com.example.smartteamdailyapplication.model.room.dao.StatusDao;
import com.example.smartteamdailyapplication.model.room.dao.TaskDao;
import com.example.smartteamdailyapplication.model.room.dao.TypeDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract public class AppModule {

    @Singleton
    @Provides
    public static PlannerDatabase getAppDB(@ApplicationContext Context context){
        return PlannerDatabase.getInstance(context);
    }

    @Singleton
    @Provides
    public static TaskDao getTaskDao(PlannerDatabase plannerDatabase){
        return plannerDatabase.taskDao();
    }

    @Singleton
    @Provides
    public static TypeDao getTypeDao(PlannerDatabase plannerDatabase){
        return plannerDatabase.typeDao();
    }

    @Singleton
    @Provides
    public static StatusDao getStatusDao(PlannerDatabase plannerDatabase){
        return plannerDatabase.statusDao();
    }

    @Singleton
    @Provides
    public static PriorityDao getPriorityDao(PlannerDatabase plannerDatabase){
        return plannerDatabase.priorityDao();
    }

    @Provides
    public static PlannerRepository getRepository(PlannerRepositoryImpl plannerRepository){
        return plannerRepository;
    }
}

