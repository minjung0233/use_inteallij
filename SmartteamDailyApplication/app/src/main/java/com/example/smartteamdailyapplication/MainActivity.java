package com.example.smartteamdailyapplication;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.example.smartteamdailyapplication.databinding.ActivityMainBinding;
import com.example.smartteamdailyapplication.helper.StateAdapter;
import com.example.smartteamdailyapplication.view.AddEditTaskActivity;
import com.example.smartteamdailyapplication.view.tabs.FragmentMonth;
import com.example.smartteamdailyapplication.view.tabs.FragmentToday;
import com.example.smartteamdailyapplication.viewmodel.MainViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;

    private static final int[] TAB_TITLES = new int[]{R.string.tab_day, R.string.tab_month};

    //    private Toolbar toolbar;
//    private DrawerLayout drawerLayout;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//
//    private StateAdapter stateAdapter;
//    private FragmentAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        toolbar = findViewById(R.id.tool_bar);
//        drawerLayout = findViewById(R.id.layout_drawer);
//
//        tabLayout=findViewById(R.id.layout_tabs);
//        viewPager=findViewById(R.id.view_pager);
//        adapter=new FragmentAdapter(getSupportFragmentManager(),1);
//
//        //툴바 변경
//        setSupportActionBar(toolbar);
//
//        //매니저에 프레그먼트 추가
//        adapter.addFragment(new FragmentToday());
//        adapter.addFragment(new FragmentMonth());
//
//        //뷰페이저와 어댑터 연결
//        viewPager.setAdapter(adapter);
//
//        //탭과 뷰페이저 연결
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.getTabAt(0).setText("Tab1");
//        tabLayout.getTabAt(1).setText("Tab2");
//    }
    private MainViewModel mainViewModel;
    private StateAdapter stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setMainViewModel(mainViewModel);

        stateAdapter = new StateAdapter(this);
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(stateAdapter);


        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
        @ColorInt int color = typedValue.data;
        getWindow().setNavigationBarColor(color);

        setSupportActionBar(binding.toolbar);
        TabLayout tabs = binding.tabs;
        new TabLayoutMediator(tabs, viewPager, (tab, position) -> tab.setText(TAB_TITLES[position])).attach();

        observeFabClicked(mainViewModel);
    }

    /**
     * Metoda za promjenu jezika
     */

    private void observeFabClicked(@NonNull MainViewModel viewModel) {
        viewModel.getFabClicked().observe(this, clicked -> {
            if(clicked) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra("ADD_EDIT",ADD_NOTE_REQUEST);
                startActivity(intent);
            }
        });
    }


    /**
     * Metoda za zamjenu teme ukoliko nije ista kao izabrana
     */
//    private void switchTheme (){
//        if(mainViewModel.getPrefsIsThemeSet().getBoolean("isThemeSet", false)){
//            mainViewModel.setPrefsIsThemeSet();
//            recreate();
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
//        setLanguage();
//        switchTheme();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.action_settings){
//            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(settings);
//            return true;
//        } else if(item.getItemId() == R.id.action_progress){
//            Intent progress = new Intent(MainActivity.this, ProgressChartActivity.class);
//            startActivity(progress);
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }
}
