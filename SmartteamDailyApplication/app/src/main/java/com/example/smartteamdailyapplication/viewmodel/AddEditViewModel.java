package com.example.smartteamdailyapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartteamdailyapplication.model.PlannerRepository;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddEditViewModel extends AndroidViewModel {
    @Inject
    PlannerRepository plannerRepository;

    MutableLiveData<Boolean> timePickerDialogData = new MutableLiveData<>();
    MutableLiveData<Boolean> datePickerDialogData = new MutableLiveData<>();

    @Inject
    public AddEditViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getTimePickerDialogData() {
        return timePickerDialogData;
    }

    public LiveData<Boolean> getDatePickerDialogData() {
        return datePickerDialogData;
    }

    public void insert(Tasks tasks) {
        plannerRepository.insert(tasks);
    }

    public void update(Tasks tasks) {
        plannerRepository.update(tasks);
    }

    public LiveData<List<Tasks>> getDailyTasksByType(long date, int type){
        return plannerRepository.getDailyTasksByType(date,type);
    }

    public LiveData<Integer> getType(String type){
        return plannerRepository.getType(type);
    }

    public LiveData<Integer> getPriority(String priority){
        return plannerRepository.getPriority(priority);
    }

    public LiveData<Integer> getStatus(String status){
        return plannerRepository.getStatus(status);
    }

    public LiveData<Tasks> getTask(int ID){
        return plannerRepository.getTask(ID);
    }

    public void updateAlarm(int ID, int activated){
        plannerRepository.updateAlarm(ID,activated);
    }

    public void onDisplayTimePickerDialogClick() {
        timePickerDialogData.setValue(true);
    }

    public void onDisplayDatePickerDialogClick() {
        datePickerDialogData.setValue(true);
    }
}
