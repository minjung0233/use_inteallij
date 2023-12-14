package com.example.smartteamdailyapplication.model.room.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "status_table")
public class Status {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String status;

    public Status(String status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getStatus() {
        return status;
    }


    public static  Status[] populateStatus(){
        return new Status[]{
                new Status("notDone"),
                new Status("Done"),
                new Status("Canceled")
        };
    }

}
