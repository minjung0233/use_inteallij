package com.example.smartteamdailyapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartteamdailyapplication.model.PlannerRepository;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MonthViewModel extends AndroidViewModel {

    @Inject
    PlannerRepository plannerRepository;
    @Inject
    public MonthViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Tasks>> getMonthlyEvents(String month, String year){
        return plannerRepository.getMonthlyEvents(month,year);
    }

    public Date dayInMonthSelected(int year, int month, int day){
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year,month,day);
        Date pom = new Date();
        pom.setTime(c.getTimeInMillis());

        return pom;
    }

}
