package com.example.smartteamdailyapplication.model.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smartteamdailyapplication.model.room.entity.Tasks;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Tasks tasks);

    @Update
    void update(Tasks tasks);

    @Delete
    void delete(Tasks tasks);

    @Query("SELECT * FROM task_table WHERE id = :ID")
    LiveData<Tasks> selectTask(int ID);

    @Query("SELECT * FROM task_table WHERE dateTime >= :date AND dateTime < :date+86400000")
    LiveData<List<Tasks>> selectOnDay(long date);

    @Query("SELECT * FROM task_table WHERE dateTime >= :date AND dateTime < :date+86400000 AND typeID = :typeID")
    LiveData<List<Tasks>> selectTasksByType(long date, int typeID);

    @Query("SELECT * FROM task_table WHERE dateTime >= :firstDate AND dateTime < :lastDay+86400000 AND typeID = :typeID")
    LiveData<List<Tasks>> selectWeekTasksByType(long firstDate, long lastDay, int typeID);

    @Query("SELECT * FROM task_table WHERE dateTime >= :firstDate AND dateTime < :lastDay+86400000 AND statusID != 3")
    LiveData<List<Tasks>> selectWeekTasks(long firstDate, long lastDay);

    @Query("SELECT * FROM TASK_TABLE WHERE strftime('%m', (datetime(dateTime/1000, 'unixepoch', 'localtime'))) = :month AND strftime('%Y', (datetime(dateTime/1000, 'unixepoch', 'localtime'))) = :year AND typeID = 1 ORDER BY dateTime ASC;")
    LiveData<List<Tasks>> selectMonthTasks(String month,String year);

    @Query("SELECT * FROM TASK_TABLE WHERE strftime('%m', (datetime(dateTime/1000, 'unixepoch', 'localtime'))) = :month AND strftime('%Y', (datetime(dateTime/1000, 'unixepoch', 'localtime'))) = :year AND typeID = 2 AND statusID != 3 ORDER BY dateTime ASC;")
    LiveData<List<Tasks>> selectMonthEvents(String month,String year);

    @Query("SELECT * FROM task_table WHERE strftime('%Y', " +"(datetime(dateTime/1000, 'unixepoch', 'localtime'))) = :year")
    LiveData<List<Tasks>> selectYearTasks(String year);

    @Query("UPDATE task_table SET statusID = :status WHERE id = :ID")
    void updateStatus(int ID, int status);

    @Query("UPDATE task_table SET alarmDisabled = :activated WHERE id = :ID ")
    void updateAlarm(int ID, int activated);


}
