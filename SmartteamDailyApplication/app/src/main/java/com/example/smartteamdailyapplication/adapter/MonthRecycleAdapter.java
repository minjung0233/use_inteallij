package com.example.smartteamdailyapplication.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartteamdailyapplication.R;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;
import com.example.smartteamdailyapplication.singleton.FindIDSingletonClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MonthRecycleAdapter extends RecyclerView.Adapter<MonthRecycleAdapter.ViewHolder>{

    ArrayList<Tasks> mTasks;
    private final OnItemClickedListener listener;

    public MonthRecycleAdapter(ArrayList<Tasks> mTasks, OnItemClickedListener listener) {
        this.mTasks = mTasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_month_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pom = (position + 1) + ".";

        holder.itemView.setId(mTasks.get(position).getId());
        holder.txtNumber.setText(pom);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");

        String event = mTasks.get(position).getTaskEventName()+" "+holder.itemView.getContext().getString(R.string.on)+" ";
        event+=" "+dateFormat.format(mTasks.get(position).getDateTime())+".";

        holder.txtEvent.setText(event);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNumber,txtEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNumber = itemView.findViewById(R.id.txtNumber);
            txtEvent = itemView.findViewById(R.id.txtMonthEvent);

            itemView.setOnClickListener(view -> {
                FindIDSingletonClass findID = FindIDSingletonClass.getInstance();
                findID.setData(view.getId());

                listener.OnItemClicked();
            });
        }
    }

    public interface OnItemClickedListener{
        void OnItemClicked();
    }
}
