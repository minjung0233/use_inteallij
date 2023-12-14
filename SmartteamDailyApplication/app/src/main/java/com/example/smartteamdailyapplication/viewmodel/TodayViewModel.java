package com.example.smartteamdailyapplication.viewmodel;


//import android.app.Application;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.smartteamdailyapplication.Tasks;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//public class TodayViewModel extends AndroidViewModel {
//
//    public TodayViewModel(@NonNull Application application) {
//        super(application);
//    }
//
//
//    public void delete(Tasks pom) {
//    }
//}
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartteamdailyapplication.model.PlannerRepository;
import com.example.smartteamdailyapplication.model.PlannerRepositoryImpl;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;

//import com.example.smartteamdailyapplication.Tasks;
import com.example.smartteamdailyapplication.model.PlannerRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TodayViewModel extends AndroidViewModel {

    @Inject
    PlannerRepository plannerRepository;

    MutableLiveData<Date> daysInLong = new MutableLiveData<>();
    MutableLiveData<Date> todayDate = new MutableLiveData<>();

    @Inject
    public TodayViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Date> getDaysInLong() {
        return daysInLong;
    }

    public void setDaysInLong(Date daysInLong) {
        this.daysInLong.setValue(daysInLong);
    }

    public MutableLiveData<Date> getTodayDate() {
        return todayDate;
    }

    public void insert(Tasks tasks) {
        plannerRepository.insert(tasks);
    }

    public void update(Tasks tasks) {
        plannerRepository.update(tasks);
    }

    public void delete(Tasks tasks) {
        plannerRepository.delete(tasks);
    }


    public LiveData<List<Tasks>> getDailyTasksByType(long date, String type){
        int typeID;
        if(type.equals("Task")){
            typeID = 1;
        } else{
            typeID = 2;
        }
        return plannerRepository.getDailyTasksByType(date,typeID);
    }

    public void updateStatus(int ID, int status){
        plannerRepository.updateStatus(ID,status);
    }

    public void updateAlarm(int ID, int activated){
        plannerRepository.updateAlarm(ID,activated);
    }


    public void todayDate(){
        Date pom = new Date();
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE), month = c.get(Calendar.MONTH), year = c.get(Calendar.YEAR);
        c.clear();
        c.set(year,month,day);

        pom.setTime(c.getTimeInMillis());

        todayDate.setValue(pom);
    }

    public void setDate(){
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
        Calendar calendar = Calendar.getInstance();
        String days = format.format(calendar.getTime());
        days += " 00:00:00";


        SimpleDateFormat iso8601Format = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
        Date dates;
        try {
            dates = iso8601Format.parse(days);
        } catch (ParseException e) {
            dates  = null;
        }

        if (dates != null) {
            daysInLong.setValue(dates);
        }
    }

}

