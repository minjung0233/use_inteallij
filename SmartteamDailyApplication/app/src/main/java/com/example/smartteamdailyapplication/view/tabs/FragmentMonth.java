package com.example.smartteamdailyapplication.view.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.smartteamdailyapplication.R;
import com.example.smartteamdailyapplication.databinding.FragmentMonthBinding;
import com.example.smartteamdailyapplication.adapter.MonthRecycleAdapter;
import com.example.smartteamdailyapplication.view.AddEditTaskActivity;
import com.example.smartteamdailyapplication.viewmodel.DateViewModel;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;
import com.example.smartteamdailyapplication.viewmodel.MonthViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FragmentMonth extends Fragment implements MonthRecycleAdapter.OnItemClickedListener{
    public static final int EDIT_NOTE_REQUEST = 2;

    private FragmentMonthBinding binding;
    private DateViewModel viewModel;
    private MonthViewModel monthViewModel;
    private MonthRecycleAdapter monthRecycleAdapter;


    public FragmentMonth() {
        // Required empty public constructor
    }

    public static FragmentMonth newInstance() {
        Bundle args = new Bundle();

        FragmentMonth fragment = new FragmentMonth();
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
        dateViewModel.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            Date pom = dateViewModel.getSelectedItem().getValue();
            binding.calendarView.setDate (pom.getTime(), true, true);

            addEventsInTable(pom.getTime());
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_month, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        monthViewModel = new ViewModelProvider(this).get(MonthViewModel.class);

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Date pom = monthViewModel.dayInMonthSelected(year,month,dayOfMonth);

            viewModel.setSelectItem(pom);

            ViewPager2 viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(0);
        });

        addEventsInTable(binding.calendarView.getDate());

        return binding.getRoot();
    }


    /**
     * Metoda za dodavanje mjesecnik Dogadjaja
     * @param milliSec      parametar tipa long, datum u milisekundama
     */
    private void addEventsInTable(long milliSec){
        binding.recViewMonthEvents.removeAllViews();

        Date date = new Date();
        date.setTime(milliSec);
        DateFormat formatMonth = new SimpleDateFormat("MM"), formatYear = new SimpleDateFormat("yyyy");

        monthViewModel.getMonthlyEvents(formatMonth.format(date),formatYear.format(date)).observe(getViewLifecycleOwner(), monthEvents -> {
            monthRecycleAdapter = new MonthRecycleAdapter((ArrayList<Tasks>) monthEvents,this);
            binding.recViewMonthEvents.setLayoutManager(new LinearLayoutManager(this.getContext()));
            binding.recViewMonthEvents.setAdapter(monthRecycleAdapter);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //addEventsInTable(milliSec);
    }

    @Override
    public void OnItemClicked() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra("ADD_EDIT",EDIT_NOTE_REQUEST);
        startActivity(intent);
    }
}