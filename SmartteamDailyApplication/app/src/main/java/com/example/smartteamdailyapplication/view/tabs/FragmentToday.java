package com.example.smartteamdailyapplication.view.tabs;

//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.example.smartteamdailyapplication.R;
//
//public class FragmentToday extends Fragment {
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//
//        View v=inflater.inflate(R.layout.fragment_today,container,false);
//        return v;
//    }
//}
//package com.example.smartteamdailyapplication;
//
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smartteamdailyapplication.R;
import com.example.smartteamdailyapplication.adapter.DayRecyclerAdapter;
import com.example.smartteamdailyapplication.databinding.FragmentTodayBinding;
import com.example.smartteamdailyapplication.helper.DayTabItemTouchHelperCallback;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;
//import com.example.smartteamdailyapplication.view.AddEditTaskActivity;
import com.example.smartteamdailyapplication.view.AddEditTaskActivity;
import com.example.smartteamdailyapplication.viewmodel.DateViewModel;
import com.example.smartteamdailyapplication.viewmodel.TodayViewModel;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentToday extends Fragment  implements DayRecyclerAdapter.OnItemClickedListener{
    public static final int EDIT_NOTE_REQUEST = 2;

    private FragmentTodayBinding binding;
    private DayRecyclerAdapter recyclerAdapterTask, recyclerAdapterEvent;

    TodayViewModel todayViewModel;

    public FragmentToday() {
        // Required empty public constructor
    }

    public static FragmentToday newInstance() {
        Bundle args = new Bundle();

        FragmentToday fragment = new FragmentToday();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DateViewModel dateViewModel = new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        dateViewModel.getSelectedItem().observe(getViewLifecycleOwner(), date -> {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
            todayViewModel.todayDate();
            todayViewModel.getTodayDate().observe(getViewLifecycleOwner(), dateToday -> {
                if(date.getTime() == dateToday.getTime()){
                    String temp = getString(R.string.today)+", "+dateFormat.format(date);
                    binding.dateMain.setText(temp);
                } else{
                    binding.dateMain.setText(dateFormat.format(date));
                }
                addRecycleView(date.getTime());

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
                binding.recViewTasks.addItemDecoration(dividerItemDecoration);
                binding.recViewEvents.addItemDecoration(dividerItemDecoration);
            });
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_today,container,false);
        DateViewModel dateViewModel = new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        todayViewModel =  new ViewModelProvider(requireActivity()).get(TodayViewModel.class);
        todayViewModel.setDate();


        todayViewModel.getDaysInLong().observe(getViewLifecycleOwner(), date -> {
            binding.dateMain.setText(new  SimpleDateFormat("dd.MM.yyyy.").format(date));
            dateViewModel.setSelectItem(date);
        });

        return binding.getRoot();
    }


    /**
     * Metoda za dodavanje dogadjaja ili obaveza za odredjeni datum
     * @param date     parametar tipa log, datum u milisekundama
     */
    private void addRecycleView(long date){
        binding.recViewTasks.removeAllViews();
        binding.recViewEvents.removeAllViews();

        todayViewModel = new ViewModelProvider(this).get(TodayViewModel.class);
        todayViewModel.getDailyTasksByType(date,"Task").observe(getViewLifecycleOwner(), tasks -> {
            //binding.recViewTasks.removeAllViews();

            if(tasks.size()>0){
                Collections.sort(tasks, new Comparator<Tasks>() {
                    @Override
                    public int compare(Tasks obj1, Tasks obj2) {
                        return Integer.valueOf(obj1.getPosition()).compareTo(Integer.valueOf(obj2.getPosition())); // To compare integer values
                    }
                });

                recyclerAdapterTask = new DayRecyclerAdapter("Task", tasks, todayViewModel,this);

                ItemTouchHelper.Callback callbackTask = new DayTabItemTouchHelperCallback(recyclerAdapterTask);
                ItemTouchHelper itemTouchHelperTasks = new ItemTouchHelper(callbackTask);
                recyclerAdapterTask.setTouchHelper(itemTouchHelperTasks);
                itemTouchHelperTasks.attachToRecyclerView(binding.recViewTasks);

                binding.recViewTasks.setLayoutManager(new LinearLayoutManager(this.getContext()));
                binding.recViewTasks.setAdapter(recyclerAdapterTask);

                binding.noTasks.setVisibility(View.GONE);
            }   else{

                binding.recViewTasks.setAdapter(null);

                binding.noTasks.setVisibility(View.VISIBLE);
            }
        });

        todayViewModel.getDailyTasksByType(date,"Event").observe(getViewLifecycleOwner(), tasks -> {
            if(tasks.size()>0){
                Collections.sort(tasks, new Comparator<Tasks>(){
                    public int compare(Tasks obj1, Tasks obj2) {
                        return Integer.valueOf(obj1.getPosition()).compareTo(Integer.valueOf(obj2.getPosition())); // To compare integer values
                    }
                });

                recyclerAdapterEvent = new DayRecyclerAdapter("Event",  tasks,todayViewModel, this);

                ItemTouchHelper.Callback callbackEvents = new DayTabItemTouchHelperCallback(recyclerAdapterEvent);
                ItemTouchHelper itemTouchHelperEvents = new ItemTouchHelper(callbackEvents);
                recyclerAdapterEvent.setTouchHelper(itemTouchHelperEvents);
                itemTouchHelperEvents.attachToRecyclerView(binding.recViewEvents);

                binding.recViewEvents.setLayoutManager(new LinearLayoutManager(this.getContext()));
                binding.recViewEvents.setAdapter(recyclerAdapterEvent);

                binding.noEvents.setVisibility(View.GONE);
            } else{
                binding.recViewEvents.setAdapter(null);

                binding.noEvents.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextViewLanguage();
    }


    /**
     * Metoda za postavljanje textView u odredjeni jezik
     */
    private void setTextViewLanguage(){
        binding.listOfTasksTxtView.setText(getResources().getString(R.string.list_of_tasks));
        binding.listOfEventsTxtView.setText(getResources().getString(R.string.list_of_events));
    }

    @Override
    public void OnItemClicked() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra("ADD_EDIT",EDIT_NOTE_REQUEST);
        startActivity(intent);
    }

    @Override
    public void OnCheckboxClicked(int ID, int status) {
        todayViewModel.updateStatus(ID, status);
    }

    @Override
    public void OnSwitchClicked(int ID, int activated) {
        todayViewModel.updateAlarm(ID, activated);
    }
}