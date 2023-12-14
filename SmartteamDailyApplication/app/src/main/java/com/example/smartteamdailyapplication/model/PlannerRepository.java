package com.example.smartteamdailyapplication.model;

import androidx.lifecycle.LiveData;

import com.example.smartteamdailyapplication.model.room.entity.Tasks;
import com.example.smartteamdailyapplication.model.room.entity.relations.TaskType;

import java.util.List;

public interface PlannerRepository {

    void insert(Tasks tasks);
    void update(Tasks tasks) ;
    void updateStatus(int ID, int status);
    void updateAlarm(int ID, int activated);
    void delete(Tasks tasks);

    LiveData<Tasks> getTask(int ID);
    LiveData<List<Tasks>> getAllDaily(long date);
    LiveData<List<Tasks>> getDailyTasksByType(long date, int type);
    LiveData<List<Tasks>> getWeeklyTasks(long firstDate, long lastDate);
    LiveData<List<Tasks>> getWeeklyTasksByType(long firstDate,long lastDate, int type);
    LiveData<List<Tasks>> getMonthlyTasks(String month, String year);
    LiveData<List<Tasks>> getMonthlyEvents(String month, String year);
    LiveData<List<Tasks>> getYearTasks(String year);
    LiveData<Integer> getType(String type);
    LiveData<Integer> getPriority(String priority);
    LiveData<Integer> getStatus(String status);


    //VEZE
    LiveData<List<TaskType>> getTypeOfTask(int id);
    LiveData<List<TaskType>> getStatusOfTask(int id);
    LiveData<List<TaskType>> getPriorityOfTask(int id);
}
