package com.example.smartteamdailyapplication.singleton;

public class FindIDSingletonClass {
    int taskID;

    private static FindIDSingletonClass INSTANCE;

    public static FindIDSingletonClass getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new FindIDSingletonClass();
        }
        return INSTANCE;
    }

    private FindIDSingletonClass(){
    }

    public void setData(int data) {
        taskID = data;
    }

    public int getData(){
        return taskID;
    }
}
