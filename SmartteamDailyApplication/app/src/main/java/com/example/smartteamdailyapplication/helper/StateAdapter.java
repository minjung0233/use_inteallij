package com.example.smartteamdailyapplication.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.smartteamdailyapplication.R;
import com.example.smartteamdailyapplication.view.tabs.FragmentMonth;
import com.example.smartteamdailyapplication.view.tabs.FragmentToday;

public class StateAdapter extends FragmentStateAdapter {
    private static final int[] TAB_TITLES = new int[]{R.string.tab_day, R.string.tab_month};

    public StateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0){
            return FragmentToday.newInstance();
        } else if (position == 1) {
            return FragmentMonth.newInstance();
        }
        return FragmentToday.newInstance();
        /*Fragment fragment = this.mFragmentList.get(position);
        return fragment;*/
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }

    public void refreshFragment(){
        notifyItemChanged(0);
    }
}
