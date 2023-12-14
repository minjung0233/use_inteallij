package com.example.smartteamdailyapplication.model.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "type_table")
public class Type {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String  type;

    public Type(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public static  Type[] populateType(){
        return new Type[]{
                new Type("Task"),
                new Type("Event")
        };
    }
}
