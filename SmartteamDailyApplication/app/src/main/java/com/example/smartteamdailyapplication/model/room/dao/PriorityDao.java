package com.example.smartteamdailyapplication.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.smartteamdailyapplication.model.room.entity.Priority;
import com.example.smartteamdailyapplication.model.room.entity.relations.TaskType;

import java.util.List;

@Dao
public interface PriorityDao {
    @Insert
    void insert(Priority... priority);

    @Query("SELECT id FROM priority_table WHERE priority = :priority")
    LiveData<Integer> selectPriority(String priority);

    @Transaction
    @Query("SELECT * FROM priority_table WHERE id = :id")
    LiveData<List<TaskType>> getPriorityOfTask(int id);
}
