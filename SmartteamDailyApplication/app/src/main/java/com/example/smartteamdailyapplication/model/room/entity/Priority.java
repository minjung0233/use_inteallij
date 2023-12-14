package com.example.smartteamdailyapplication.model.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "priority_table")
public class Priority {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String priority;

    public Priority(String priority) {
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPriority() {
        return priority;
    }

    public static Priority[] populatePriority(){
        return new Priority[]{
                new Priority("High"),
                new Priority("Medium"),
                new Priority("Low")
        };
    }

}
