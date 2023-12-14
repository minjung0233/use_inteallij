package com.example.smartteamdailyapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class DateViewModel extends ViewModel {
    private final MutableLiveData<Date> selectedItem = new MutableLiveData<>();

    public void setSelectItem(Date item) {
        selectedItem.setValue(item);
    }
    public LiveData<Date> getSelectedItem() {
        return selectedItem;
    }
}
