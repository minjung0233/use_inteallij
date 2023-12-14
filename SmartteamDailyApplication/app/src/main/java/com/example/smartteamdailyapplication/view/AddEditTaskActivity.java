package com.example.smartteamdailyapplication.view;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartteamdailyapplication.R;
import com.example.smartteamdailyapplication.broadcast.AlarmReceiver;
import com.example.smartteamdailyapplication.databinding.ActivityAddEditTaskBinding;
import com.example.smartteamdailyapplication.model.room.entity.Tasks;
import com.example.smartteamdailyapplication.singleton.FindIDSingletonClass;
import com.example.smartteamdailyapplication.viewmodel.AddEditViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AddEditTaskActivity extends AppCompatActivity {
    private static final String PREFS_THEME = "THEME";
    private String ADD_EDIT;

    private ActivityAddEditTaskBinding binding;

    private AddEditViewModel addEditViewModel;
    private Tasks mTasks;
    private AlarmManager mAlarmManager;
    private int ID;

    String date = "", time ="00:00:00";
    private String typeText = "", priorityText = "";
    int  type, done = 0;
    Integer priority,status;
    boolean doneEditing = false;
    short  isAlarmActivated;
    long milliSec, dateLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditTaskBinding.inflate(getLayoutInflater());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_task);
        addEditViewModel = new ViewModelProvider(this).get(AddEditViewModel.class);
        binding.setAddEditViewModel(addEditViewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int add_edit = extras.getInt("ADD_EDIT");
            if(add_edit == 1){
                ADD_EDIT = "ADD";
            } else if(add_edit == 2){
                ADD_EDIT = "EDIT";
            }
        }

        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        observeTimePickerDialogData(addEditViewModel);
        observeDatePickerDialogData(addEditViewModel);

        if(ADD_EDIT.equals("ADD")){
            setTitle(R.string.title_activity_new_task_event);

            binding.txtViewDate.append(returnDateFormat(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
            binding.txtViewTime.setText(getString(R.string.alarm));

            addEditViewModel.getStatus("notDone").observe(this, status -> this.status = status);
        } else if(ADD_EDIT.equals("EDIT")){
            FindIDSingletonClass findID = FindIDSingletonClass.getInstance();
            ID = findID.getData();
            addEditViewModel.getTask(findID.getData()).observe(this, tasks -> {
                mTasks = tasks;
                changeTitle(tasks.getTypeID());

                fillAllFields(tasks);

                binding.layoutType.setVisibility(View.GONE);
                binding.txtStatus.setVisibility(View.VISIBLE);
                binding.layoutPriority.setVisibility(View.VISIBLE);

                editTask(false);
                enableTextLayout(binding.txtTitleText,true);
                enableTextLayout(binding.txtEditDescription,true);
            });
        }

        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
        @ColorInt int color = typedValue.data;
        getWindow().setNavigationBarColor(color);


        binding.btnCancelAlarm.setOnClickListener(view -> {
            cancelAlarm(ID);
            addEditViewModel.updateAlarm(ID,  (short)1);
        });

        //OnChange za postavljanje prioriteta
        binding.radioGroupPriority.setOnCheckedChangeListener((group, checkedId) -> {
            if(binding.radioBtnHigh.getId()==checkedId){
                //priority = "High";
                addEditViewModel.getPriority("High").observe(this, priority -> this.priority = priority);
            }
            else if(binding.radioBtnMedium.getId()==checkedId){
                //priority = "Medium";
                addEditViewModel.getPriority("Medium").observe(this, priority -> this.priority = priority);
            }
            else if(binding.radioBtnLow.getId()==checkedId) {
                //priority = "Low";
                addEditViewModel.getPriority("Low").observe(this, priority -> this.priority = priority);
            }
        });

        //OnCheckChangeg postavljanje tipa
        binding.radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {
            if( binding.radioBtnTask.getId()==checkedId){
                binding.layoutPriority.setVisibility(View.VISIBLE);
                //type = "Task";
                addEditViewModel.getType("Task").observe(this, type -> this.type = type);
            }
            else if( binding.radioBtnEvent.getId()==checkedId){
                binding.layoutPriority.setVisibility(View.GONE);
                //type = "Event";
                addEditViewModel.getType("Event").observe(this, type -> this.type = type);
            }
        });


    }

    private void observeTimePickerDialogData(AddEditViewModel viewModel) {
        viewModel.getTimePickerDialogData().observe(this, display -> {
            if(display) setTimePickerDialog();
        });
    }

    private void observeDatePickerDialogData(AddEditViewModel viewModel) {
        viewModel.getDatePickerDialogData().observe(this, display -> {
            if(display) setDatePickerDialog();
        });
    }

    public void setTimePickerDialog(){
        int mHour, mMinute;
        final Calendar c = Calendar.getInstance();

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String temp;

            time = String.format("%02d:%02d", hourOfDay, minute);

            temp = getString(R.string.set_alarm)+" "+time;
            binding.txtViewTime.setText(temp);

            time+= ":00";
            isAlarmActivated = 1;

        },mHour,mMinute,true);

        timePickerDialog.show();
    }

    public void setDatePickerDialog(){
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth= c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            binding.txtViewDate.setText(returnDateFormat(year,month,dayOfMonth));
            //date = String.format("%04d-%02d-%02d",year, (month+1), dayOfMonth);
            date = String.format("%02d.%02d.%04d.",dayOfMonth, (month+1),year);
        },mYear,mMonth,mDay);

        datePickerDialog.show();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        SharedPreferences prefs = getSharedPreferences(PREFS_THEME, MODE_PRIVATE);
        String themeName = prefs.getString("theme", "");

        switch (themeName){
            case "Red":
                theme.applyStyle(R.style.Theme_smartteamdailyapplication,true);
                break;
            case "Purple":
                theme.applyStyle(R.style.Theme2,true);
                break;
            case "Yellow":
                theme.applyStyle(R.style.Theme3,true);
                break;
        }
        return super.getTheme();
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        if(ADD_EDIT.equals("ADD")){
            getMenuInflater().inflate(R.menu.add_new_menu, menu);
            Drawable doneIcon = menu.findItem(R.id.action_done).getIcon();

            doneIcon = DrawableCompat.wrap(doneIcon);
            DrawableCompat.setTint(doneIcon, getResources().getColor(R.color.white));
            menu.findItem(R.id.action_done).setIcon(doneIcon);
        } else if(ADD_EDIT.equals("EDIT")){
            getMenuInflater().inflate(R.menu.details_menu, menu);
            Drawable editPen = menu.findItem(R.id.action_edit).getIcon();

            editPen = DrawableCompat.wrap(editPen);
            DrawableCompat.setTint(editPen, getResources().getColor(R.color.white));
            menu.findItem(R.id.action_edit).setIcon(editPen);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (doneEditing) {
                if (updateDatabase()) {
                    doneEditing = false;
                    Drawable editPen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_edit, this.getTheme());
                    if(editPen!=null){
                        editPen = DrawableCompat.wrap(editPen);
                        DrawableCompat.setTint(editPen, getResources().getColor(R.color.white));
                        item.setIcon(editPen);
                    }
                    editTask(false);
                    enableTextLayout( binding.txtTitleText, true);
                    enableTextLayout( binding.txtEditDescription, true);
                }

            } else {
                doneEditing = true;
                Drawable checkDone = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, this.getTheme());
                if(checkDone!=null){
                    checkDone = DrawableCompat.wrap(checkDone);

                    DrawableCompat.setTint(checkDone, getResources().getColor(R.color.white));
                    item.setIcon(checkDone);
                }
                editTask(true);
                enableTextLayout( binding.txtTitleText, false);
                enableTextLayout( binding.txtEditDescription, false);
            }
            return true;
        } else if(item.getItemId() == R.id.action_done){
            if (insertTaskInDB()) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Metoda za kreiranje kanala za notifikacije za alarm
     */
    private  void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Alarm";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("plannerAlarm",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Metoda za postavljanje alarma
     * @param code      parametar tipa int koji predstavlja kod na osnovu koga ce se pratiti notifikacija
     * @param name      parametar tipa String, Naslov notifikacije
     * @param desc      parametar tipa String, tekst notifikacije
     */
    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(int code, String name, String desc) {
        String dateTime = date + " "+ time;
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        Bundle extras = new Bundle();
        extras.putString("TITLE",name);
        extras.putString("DESC",desc);
        extras.putInt("CODE",code);
        intent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, code, intent, 0);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat iso8601Format = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
        Date dates;

        try {
            dates = iso8601Format.parse(dateTime);
        } catch (ParseException e) {
            dates  = null;
        }

        if (dates != null) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,dates.getTime(), pendingIntent);
        }
    }


    /**
     * Metoda za otkazivanje alarma
     * @param code      parametar tipa int, kod koji trazi notifikaciju koja treba da se otkaze
     */
    private void cancelAlarm(int code){
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,code,intent,0);

        if(mAlarmManager== null){
            mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        }
        mAlarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        Toast.makeText(this, "Notification canceled", Toast.LENGTH_SHORT).show();
    }


    /**
     * Metoda za promjenu Activity naslova
     */
    private void changeTitle(int typeDB){
        switch (typeDB){
            case 1:
                binding.toolbar.setTitle(getString(R.string.radio_btn_task)+" "+getString(R.string.title_activity_task_details));
                typeText = "Task";
                break;
            case 2:
                binding.toolbar.setTitle(getString(R.string.radio_btn_event)+" "+getString(R.string.title_activity_task_details));
                typeText = "Event";
                break;
        }
    }


    /**
     *
     * Metoda za postavljanje stringa datum u odredjeni format
     * @param year      parametar tipa int, godina za formatiranje
     * @param month     parametar tipa int, mjesec za formatiranje
     * @param day       parametar tipa int, dan za formatiranje
     * @return          Datum formatiran u odredjenom obliku
     */
    public String returnDateFormat(int year, int month, int day){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        Calendar c = Calendar.getInstance();
        c.set(year,(month),day);

        Date myDate = new Date();
        myDate.setTime(c.getTimeInMillis());

        //date = String.format("%04d-%02d-%02d",year, (month+1), day);
        date = String.format("%02d.%02d.%04d.",day, (month+1),year);

        return (simpleDateFormat.format(myDate));
    }


    /**
     * Metoda za popunjavanje polja sa izabranim podacima
     * @param tasks parametar tipa Tasks, Objekat tipa Tasks,sa elementima koji se izmjenjuju
     */
    public void fillAllFields(Tasks tasks){
        Date pom = new Date();
        pom.setTime(tasks.getDateTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        //Tasks
        if(tasks.getTypeID() == 1){
            String temp = getString(R.string.radio_btn_priority)+": ";

            binding.txtPriority.setVisibility(View.VISIBLE);
            switch (tasks.getPriorityID()) {
                case 1:
                    binding.radioBtnHigh.setChecked(true);
                    temp+=getString(R.string.radio_btn_high);
                    break;
                case 2:
                    binding.radioBtnMedium.setChecked(true);
                    temp+=getString(R.string.radio_btn_medium);
                    break;
                case 3:
                    binding.radioBtnLow.setChecked(true);
                    temp+=getString(R.string.radio_btn_low);
                    break;
            }
            binding.txtPriority.setText(temp);
        }//EVENTs
        else if(tasks.getTypeID() == 2){
            binding.txtPriority.setVisibility(View.GONE);
        }

        if(tasks.getAlarmSet() == 1){
            binding.btnCancelAlarm.setVisibility(View.VISIBLE);
        } else{
            binding.btnCancelAlarm.setVisibility(View.GONE);
        }

        binding.txtViewDate.setText(dateFormat.format(pom));
        dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        date = dateFormat.format(pom);

        if(binding.txtTitle.getEditText()!=null ){
            binding.txtTitle.getEditText().setText(tasks.getTaskEventName());
        }
        binding.txtEditDescription.setText(tasks.getDescription());

        status = tasks.getStatusID();

        if(tasks.getAlarmSet() == 0){
            binding.txtViewTime.setText(getString(R.string.no_alarm));
        }
        else{
            String temp = getString(R.string.set_alarm)+" "+timeFormat.format(pom);
            binding.txtViewTime.setText(temp);
        }

        if(tasks.getAlarmDisabled() == 1){
            String temp = getString(R.string.alarm_canceled);
            binding.txtViewTime.setText(temp);
        }


        /*switch (status){
            case 1:
                binding.txtStatus.setText(getString(R.string.status_completed));
                break;
            case 2:
                binding.txtStatus.setText(getString(R.string.status_active));
                break;
            case 3:
                binding.txtStatus.setText(getString(R.string.status_canceled));
                break;
        }*/

        milliSec=tasks.getDateTime();
    }


    /**
     * Metoda za omogucavanje uredjivanja polja
     * @param edit parametar tipa boolean, da li je moguce uredjivanje ili ne
     */
    public void editTask(boolean edit){
        if(edit){
            if(status == 1){
                if (typeText.equals("Task")){
                    binding.radioGroupPriority.setVisibility(View.VISIBLE);
                }
                binding.btnCancelAlarm.setVisibility(View.GONE);
                binding.imgBtnCalendar.setVisibility(View.VISIBLE);
                binding.imgBtnTime.setVisibility(View.VISIBLE);
            } else{

                binding.radioGroupPriority.setVisibility(View.GONE);
                binding.imgBtnCalendar.setVisibility(View.GONE);
                binding.imgBtnTime.setVisibility(View.GONE);
                if(isAlarmActivated==1){
                    binding.btnCancelAlarm.setVisibility(View.VISIBLE);
                }
            }

            enableTextLayout( binding.txtTitleText, false);
            enableTextLayout( binding.txtEditDescription,false);
        }
        else{
            binding.radioGroupPriority.setVisibility(View.INVISIBLE);
            binding.imgBtnCalendar.setVisibility(View.GONE);
            binding.imgBtnTime.setVisibility(View.GONE);
            if(isAlarmActivated==1){
                binding.btnCancelAlarm.setVisibility(View.VISIBLE);
            }

            enableTextLayout( binding.txtTitleText,true);
            enableTextLayout( binding.txtEditDescription,true);
        }
    }

    /**
     * Metoda za upisivanje u bazu podataka
     * @return  tipa boolean, da li je upisivanje proslo uspjesno ili ne
     */
    private boolean insertTaskInDB(){
        if(!validateTitleName() | !validateType() | !validateDescriptionLength()){
            return false;
        }else{

            String taskName = "",description = "";
            String dateTime = date + " "+ time, tempString = date+" 00:00:00";

            if( binding.txtTitle.getEditText() != null && !TextUtils.isEmpty( binding.txtTitle.getEditText().getText())){
                taskName =  binding.txtTitle.getEditText().getText().toString();
            }
            if( binding.txtDescription.getEditText() != null && !TextUtils.isEmpty( binding.txtDescription.getEditText().getText())){
                description =  binding.txtDescription.getEditText().getText().toString();
            }


            SimpleDateFormat iso8601Format = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
            Date dates, tempDate;

            try {
                dates = iso8601Format.parse(dateTime);
                tempDate = iso8601Format.parse(tempString);
            } catch (ParseException e) {
                dates  = null;
                tempDate = null;
            }

            if (dates != null) {
                dateLong = dates.getTime();
            }


            addEditViewModel = new ViewModelProvider(this).get(AddEditViewModel.class);
            String finalTaskName = taskName;
            String finalDescription = description;
            addEditViewModel.getDailyTasksByType(tempDate.getTime(),type).observe(this, tasks -> {
                int pom = tasks.size();

                if(done == 0){
                    done = 1;
                    Tasks taskInsert = new Tasks((pom+1), finalTaskName,type,priority,status, finalDescription,dateLong,isAlarmActivated, (short)0);
                    addEditViewModel.insert(taskInsert);

                    if(isAlarmActivated==1){
                        createNotificationChannel();
                        setAlarm((pom+1),finalTaskName, finalDescription);
                    }
                }
            });
            return  true;
        }
    }

    /**
     * Metoda za azuriranje dB
     * @return  tipa boolean, da li je azuriranje proslo uspjesno ili ne
     */
    public boolean updateDatabase(){
        if(!validateTitleName() | !validatePriority() | !validateDescriptionLength()){
            return false;
        } else{
            Tasks pomTasks;
            String name = "", description = "", dateTime = date + " "+ time;

            if( binding.txtTitle.getEditText() != null && !TextUtils.isEmpty( binding.txtTitle.getEditText().getText())){
                name =  binding.txtTitle.getEditText().getText().toString();
            }

            if( binding.txtDescription.getEditText() != null && !TextUtils.isEmpty( binding.txtDescription.getEditText().getText())){
                description =  binding.txtDescription.getEditText().getText().toString();
            }

            SimpleDateFormat iso8601Format = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
            Date dates;

            try {
                dates = iso8601Format.parse(dateTime);
            } catch (ParseException e) {
                dates  = null;
            }

            if (dates != null) {
                milliSec= dates.getTime();
            }
            pomTasks = new Tasks(mTasks.getPosition(),name, mTasks.getTypeID(), priority,mTasks.getStatusID(),description,milliSec,isAlarmActivated,mTasks.getAlarmDisabled());
            pomTasks.setId(mTasks.getId());

            addEditViewModel.update(pomTasks);

            if(isAlarmActivated == 1){
                setAlarm(mTasks.getId(),pomTasks.getTaskEventName(),pomTasks.getDescription());
            }
            return true;
        }
    }

    /**
     *
     * Metoda za omggucavanje uredjivanja teksta
     * @param editText     parametar tipa TextInputEditText
     * @param isDisabled   parametar tipa boolean, da li je moguce uredjivanje ili ne
     */
    public void enableTextLayout(TextInputEditText editText, boolean isDisabled){
        if(isDisabled){
            editText.setFocusable(false);
        }
        else{
            editText.setFocusableInTouchMode(true);
        }
    }

    /**
     * Metoda za validaciju naslova (binding.txtTitle)
     * @return      tipa booleean, da li je naslov postavljen ili ne
     */
    private boolean validateTitleName(){
        String val = "";

        if( binding.txtTitle.getEditText() != null && !TextUtils.isEmpty( binding.txtTitle.getEditText().getText())){
            val =  binding.txtTitle.getEditText().getText().toString().trim();
        }

        if(val.isEmpty()){
            binding.txtTitle.setError(getString(R.string.title_validation));
            return false;
        }else{
            binding.txtTitle.setError(null);
            binding.txtTitle.setErrorEnabled(false);
            return true;
        }
    }

    /**
     * Fukcija za validaciju polja tipa (binding.radioGroupType)
     * @return      tipa boolean, da li je izabran tip ili ne
     */
    private boolean validateType(){
        if(binding.radioGroupType.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, getString(R.string.type_validation), Toast.LENGTH_SHORT).show();
            return false;
        } else{
            if(type == 1){
                typeText = "Task";
                return validatePriority();
            }else{
                return true;
            }
        }
    }

    /**
     * Fukcija za validaciju polja prioritet (binding.radioGroupPriority)
     * @return      tipa boolean, da li je izabran prioritet ili ne
     */
    private boolean validatePriority(){
        if(typeText.equals("Task")){
            if(binding.radioGroupPriority.getCheckedRadioButtonId()==-1){
                Toast.makeText(this, getString(R.string.priority_validation), Toast.LENGTH_SHORT).show();
                return false;
            } else{
                return true;
            }
        } else{
            return true;
        }
    }



    /**
     * Metoda za validaciju polja opis (binding.txtDescription)
     * @return     tipa  boolean, da li je duzina opisa veca il ne od moguce
     */
    private boolean validateDescriptionLength(){
        String val = "";

        if(binding.txtDescription.getEditText() != null && !TextUtils.isEmpty(binding.txtDescription.getEditText().getText())){
            val = binding.txtDescription.getEditText().getText().toString().trim();
        }

        if(val.length()>150){
            binding.txtDescription.setError(getString(R.string.description_validation));
            return false;
        }else{
            binding.txtDescription.setError(null);
            binding.txtDescription.setErrorEnabled(false);
            return true;
        }
    }
}