package com.example.smartteamdailyapplication.model.room.entity.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.smartteamdailyapplication.model.room.entity.Status;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;

import java.util.List;

public class TaskStatus {
    @Embedded
    private Status status;
    @Relation(
            parentColumn = "id",
            entityColumn = "statusID"
    )
    public List<Tasks> tasks;

    public TaskStatus(Status status, List<Tasks> tasks) {
        this.status = status;
        this.tasks = tasks;
    }
}
