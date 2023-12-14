package com.example.smartteamdailyapplication.adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartteamdailyapplication.R;
import com.example.smartteamdailyapplication.R.color;
import com.example.smartteamdailyapplication.broadcast.AlarmReceiver;
import com.example.smartteamdailyapplication.helper.ItemTouchHelperAdapter;
//import com.example.smartteamdailyapplication.model.room.entity.Tasks;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;
import com.example.smartteamdailyapplication.singleton.FindIDSingletonClass;
import com.example.smartteamdailyapplication.viewmodel.TodayViewModel;

import java.util.List;


public class DayRecyclerAdapter extends RecyclerView.Adapter<DayRecyclerAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private String mType;
    private OnItemClickedListener mListener;
    private TodayViewModel mViewModel;
    private List<Tasks> mTasks;

    private AlarmManager mAlarmManager;

    private boolean mMoved = false;

    private ItemTouchHelper mTouchHelper;

    public DayRecyclerAdapter(String type, List<Tasks> tasks, TodayViewModel viewModel, OnItemClickedListener listener) {
        this.mType = type;
        this.mTasks = tasks;
        this.mViewModel = viewModel;
        this.mListener =  listener;
    }

    public DayRecyclerAdapter() {
        this.mTasks = null;
    }


    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.mTouchHelper = touchHelper;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_day_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mTasks.size()>0){
            String pom;

            holder.itemView.setId(mTasks.get(position).getId());
            holder.taskTitle.setText(mTasks.get(position).getTaskEventName());
            holder.taskTitle.setId(mTasks.get(position).getId());


            TypedValue typedValue = new TypedValue();
            Resources.Theme theme;

            if(mTasks.size()>0){
                switch (mTasks.get(position).getStatusID()) {
                    case 1:
                        theme = holder.itemView.getContext().getTheme();
                        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true);
                        @ColorInt int color = typedValue.data;

                        holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags());
                        holder.recycleContainer.setBackgroundColor(color);
                        break;
                    case 2:
                        theme = holder.itemView.getContext().getTheme();
                        theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true);
                        @ColorInt int colorDone = typedValue.data;

                        holder.checkBox.setChecked(true);
                        holder.recycleContainer.setBackgroundColor(colorDone);
                        break;
                    case 3:
                        holder.imageViewAlarm.setVisibility(View.GONE);
                        holder.imageViewEventDot.setVisibility(View.GONE);
                        holder.imageViewCancel.setVisibility(View.VISIBLE);
                        holder.checkBox.setVisibility(View.INVISIBLE);

                        holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags());
                        holder.taskTitle.setTextColor(Color.rgb(0, 0, 0));
                        holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.recycleContainer.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.grey));

                        cancelAlarm(holder.itemView.getContext(), holder.taskTitle.getId());
                        break;

                }

                if(mType.equals("Task")){
                    switch (mTasks.get(position).getPriorityID()){
                        case 1:
                            ImageViewCompat.setImageTintList(holder.dotPriority,ColorStateList.valueOf(holder.itemView.getContext().getResources().getColor(color.active)));
                            pom = holder.itemView.getContext().getString(R.string.radio_btn_high)+" "+holder.itemView.getContext().getString(R.string.radio_btn_priority);
                            holder.txtPriority.setText(pom);
                            break;
                        case 2:
                            ImageViewCompat.setImageTintList(holder.dotPriority,ColorStateList.valueOf(holder.itemView.getContext().getResources().getColor(color.canceled)));
                            pom = holder.itemView.getContext().getString(R.string.radio_btn_medium)+" "+holder.itemView.getContext().getString(R.string.radio_btn_priority);
                            holder.txtPriority.setText(pom);
                            break;
                        case 3:
                            ImageViewCompat.setImageTintList(holder.dotPriority,ColorStateList.valueOf(holder.itemView.getContext().getResources().getColor(color.done)));
                            pom = holder.itemView.getContext().getString(R.string.radio_btn_low)+" "+holder.itemView.getContext().getString(R.string.radio_btn_priority);
                            holder.txtPriority.setText(pom);
                            break;

                    }

                } else{
                    holder.dotPriority.setVisibility(View.GONE);
                }

                if(mTasks.get(position).getAlarmSet()==1){
                    holder.imageViewAlarm.setVisibility(View.VISIBLE);
                    holder.switchAlarm.setVisibility(View.VISIBLE);
                    if(mTasks.get(position).getAlarmDisabled()==1){
                        ImageViewCompat.setImageTintList(holder.imageViewAlarm, ColorStateList.valueOf(ContextCompat.getColor(holder.imageViewAlarm.getContext(), color.red)));
                        holder.switchAlarm.setChecked(false);
                    }else {
                        ImageViewCompat.setImageTintList(holder.imageViewAlarm, ColorStateList.valueOf(ContextCompat.getColor(holder.imageViewAlarm.getContext(), color.green)));
                        holder.switchAlarm.setChecked(true);
                    }
                }else{
                    holder.imageViewAlarm.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        mMoved = true;

        Tasks fromTasks = mTasks.get(fromPosition);
        mTasks.remove(fromTasks);
        mTasks.add(toPosition,fromTasks);
        notifyItemMoved(fromPosition,toPosition);
        updateArrayList(mTasks);
    }

    @Override
    public void onItemDelete(int position) {
        Tasks pom = mTasks.get(position);
        mTasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mTasks.size());

        mViewModel.delete(pom);
    }

    @Override
    public void onItemCancel(int position) {
        if(mTasks.get(position).getStatusID() == 3){
            mViewModel.updateStatus(mTasks.get(position).getId(), 1);
            mTasks.get(position).setStatusID(1);
        } else{
            mViewModel.updateStatus(mTasks.get(position).getId(), 3);
            mTasks.get(position).setStatusID(3);
        }
        notifyItemChanged(position);
    }

    @Override
    public void onUpdate() {
        if(mMoved){
            mMoved = false;
            for (Tasks tasks : mTasks) {
                mViewModel.update(tasks);
            }
        }
    }


    /**
     * Metoda za azuriranje pozicija u listi elemenata, za prikazivanje ispravnog reda nakon onMove
     * @param list      parametar tipa List<Tasks>, lista Tasks objekata
     */
    private void updateArrayList(List<Tasks> list){
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPosition(i);
        }

        mTasks = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnTouchListener, GestureDetector.OnGestureListener {
        SwitchCompat switchAlarm;
        CheckBox checkBox;
        TextView taskTitle,txtPriority;
        ImageView imageViewEventDot,imageViewAlarm,imageViewCancel, dotPriority;
        ConstraintLayout recycleContainer;

        GestureDetector mGestureDetector;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewEventDot = itemView.findViewById(R.id.imageViewEventDot);
            checkBox = itemView.findViewById(R.id.checkBox);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            recycleContainer = itemView.findViewById(R.id.recycleContainer);
            imageViewAlarm = itemView.findViewById(R.id.imageViewAlarm);
            imageViewCancel=itemView.findViewById(R.id.imageViewCanceled);
            txtPriority = itemView.findViewById(R.id.txtPriority);
            switchAlarm = itemView.findViewById(R.id.switchSetAlarm);
            dotPriority = itemView.findViewById(R.id.imageViewPriorityDot);


            if(mType.equals("Task")){
                imageViewEventDot.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);

            }else if(mType.equals("Event")){
                txtPriority.setVisibility(View.GONE);
                imageViewEventDot.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.GONE);
            }

            mGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);


            checkBox.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(position !=RecyclerView.NO_POSITION){
                    int status, ID = mTasks.get(position).getId();
                    if(checkBox.isChecked()){
                        status = 2;
                        if(mTasks.get(position).getAlarmSet() == 1){
                            cancelAlarm(itemView.getContext(),taskTitle.getId());
                        }
                    }else {
                        status = 1;
                        if(mTasks.get(position).getAlarmSet() == 1){
                            setAlarm(itemView.getContext(), mTasks.get(position));
                        }
                    }
                    mListener.OnCheckboxClicked(ID,status);
                }
            });

            switchAlarm.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(position !=RecyclerView.NO_POSITION){
                    int ID = mTasks.get(position).getId(), activated;

                    //OTKAZI ILI POSTAVI ALARM
                    if(switchAlarm.isChecked()){
                        ImageViewCompat.setImageTintList(imageViewAlarm, ColorStateList.valueOf(ContextCompat.getColor(imageViewAlarm.getContext(), color.green)));

                        activated = 0;

                        setAlarm(itemView.getContext(), mTasks.get(position));
                        Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.alarm_enabled), Toast.LENGTH_SHORT).show();
                    } else{
                        ImageViewCompat.setImageTintList(imageViewAlarm, ColorStateList.valueOf(ContextCompat.getColor(imageViewAlarm.getContext(), color.red)));

                        activated = 1;
                        cancelAlarm(itemView.getContext(),taskTitle.getId());
                        Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.alarm_disabled), Toast.LENGTH_SHORT).show();
                    }
                    mListener.OnSwitchClicked(ID, activated);
                }
            });
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            FindIDSingletonClass findID = FindIDSingletonClass.getInstance();
            findID.setData(itemView.getId());
            mListener.OnItemClicked();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override

        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }

    public interface OnItemClickedListener{
        void OnItemClicked();
        void OnCheckboxClicked(int ID, int status);
        void OnSwitchClicked(int ID, int activated);
    }

    /**
     * Metoda za otkazivanje alarma
     * @param context   parametar tipa Context
     * @param code      Parametar tipa int, potreban kod za otkazivanje alarma
     */
    private void cancelAlarm(Context context, int code){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_IMMUTABLE);

        if(mAlarmManager == null){
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        mAlarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }


    /**
     * Metoda za postavljanje alarma
     * @param context   parametar tipa Context
     */
    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(Context context, Tasks tasks){

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle extras = new Bundle();
        extras.putString("TITLE",tasks.getTaskEventName());
        extras.putString("DESC",tasks.getDescription());
        extras.putInt("CODE",tasks.getId());
        intent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,tasks.getId(),intent, PendingIntent.FLAG_IMMUTABLE);

        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,tasks.getDateTime(),pendingIntent );

    }
}