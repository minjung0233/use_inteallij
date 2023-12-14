package com.example.smartteamdailyapplication.model;

import androidx.lifecycle.LiveData;

import com.example.smartteamdailyapplication.model.room.dao.PriorityDao;
import com.example.smartteamdailyapplication.model.room.dao.StatusDao;
import com.example.smartteamdailyapplication.model.room.dao.TaskDao;
import com.example.smartteamdailyapplication.model.room.dao.TypeDao;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;
import com.example.smartteamdailyapplication.model.room.entity.relations.TaskType;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class PlannerRepositoryImpl implements PlannerRepository{

    @Inject
    TaskDao taskDao;
    @Inject
    TypeDao typeDao;
    @Inject
    PriorityDao priorityDao;
    @Inject
    StatusDao statusDao;

    @Inject
    public PlannerRepositoryImpl(){
    }

    @Override
    public void insert(Tasks tasks) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.insert(tasks));
    }

    @Override
    public void update(Tasks tasks) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.update(tasks));
    }

    @Override
    public void updateStatus(int ID, int status) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.updateStatus(ID, status));
    }

    @Override
    public void updateAlarm(int ID, int activated) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.updateAlarm(ID, activated));
    }

    @Override
    public void delete(Tasks tasks) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.delete(tasks));
    }

    @Override
    public LiveData<Tasks> getTask(int ID) {
        return taskDao.selectTask(ID);
    }

    @Override
    public LiveData<List<Tasks>> getAllDaily(long date) {
        return taskDao.selectOnDay(date);
    }

    @Override
    public LiveData<List<Tasks>> getDailyTasksByType(long date, int type) {
        return taskDao.selectTasksByType(date,type);
    }

    @Override
    public LiveData<List<Tasks>> getWeeklyTasks(long firstDate, long lastDate) {
        return taskDao.selectWeekTasks(firstDate,lastDate);
    }

    @Override
    public LiveData<List<Tasks>> getWeeklyTasksByType(long firstDate, long lastDate, int type) {
        return taskDao.selectWeekTasksByType(firstDate,lastDate, type);
    }

    @Override
    public LiveData<List<Tasks>> getMonthlyTasks(String month, String year) {
        return taskDao.selectMonthTasks(month,year);
    }

    @Override
    public LiveData<List<Tasks>> getMonthlyEvents(String month, String year) {
        return taskDao.selectMonthEvents(month,year);
    }

    @Override
    public LiveData<List<Tasks>> getYearTasks(String year) {
        return taskDao.selectYearTasks(year);
    }

    @Override
    public LiveData<Integer> getType(String type) {
        return typeDao.selectType(type);
    }

    @Override
    public LiveData<Integer> getPriority(String priority) {
        return priorityDao.selectPriority(priority);
    }

    @Override
    public LiveData<Integer> getStatus(String status) {
        return statusDao.selectStatus(status);
    }


    @Override
    public LiveData<List<TaskType>> getTypeOfTask(int id) {
        return typeDao.getTypeOfTask(id);

    }

    @Override
    public LiveData<List<TaskType>> getStatusOfTask(int id) {
        return statusDao.getStatusOfTask(id);
    }

    @Override
    public LiveData<List<TaskType>> getPriorityOfTask(int id) {
        return priorityDao.getPriorityOfTask(id);
    }
}
