package com.nearsoft.reminder.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nearsoft.reminder.main.helpers.AlarmManagerHelper;
import com.nearsoft.reminder.main.helpers.DBHelper;
import com.nearsoft.reminder.main.models.Reminder;

/**
 * Created by Baniares on 4/16/14.
 */
public class ReminderFragmentActivity extends FragmentActivity {
    private Button startDate;
    private Button endDate;
    private EditText titleTextEdit;
    private EditText descriptionTextEdit;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button save;
    private Time startTime;
    private Time endTime;
    private Intent bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);
        bundle = getIntent();
        initializeViewVariables();
        switch(bundle.getExtras().getInt("mode")){
            case 0:
                Toast.makeText(this,"An error has ocurred",Toast.LENGTH_LONG).show();
                break;
            case 1:
                reminderState(0);
                break;
            case 2:
                viewReminderState(0);
                break;
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(bundle.getExtras().getInt("mode")==1){
            menu.clear();
        }
        super.onResume();
        return super.onPrepareOptionsMenu(menu);
    }

    private void viewReminderState(int state) {
        switch(state){
            case 0:
                setVisibilityState(1);
                DBHelper db = new DBHelper(this);
                bundle = getIntent();
                Reminder reminder = db.getReminder(bundle.getExtras().getInt("ReminderId",-6));
                db.close();
                titleTextView.setText(reminder.getTitle());
                descriptionTextView.setText(reminder.getDescription());
                startDate.setText(
                        reminder.getStartDate().monthDay+"/"+
                                (reminder.getStartDate().month+1)+"/"+
                                reminder.getStartDate().year+"   "+
                                reminder.getStartDate().hour+":"+
                                reminder.getStartDate().minute
                );
                startTime.set(reminder.getStartDate());
                endDate.setText(
                        reminder.getEndDate().monthDay+"/"+
                                (reminder.getEndDate().month+1)+"/"+
                                reminder.getEndDate().year+"   "+
                                reminder.getEndDate().hour+":"+
                                reminder.getEndDate().minute
                );
                endTime.set(reminder.getEndDate());
                startDate.setTextColor(Color.WHITE);
                endDate.setTextColor(Color.WHITE);
                break;
            case 1:
                reminderState(1);
                setVisibilityState(0);
                titleTextEdit.setText(titleTextView.getText());
                descriptionTextEdit.setText(descriptionTextView.getText());
                break;
            default:
                setVisibilityState(1);
                break;
        }
    }
    private void reminderState(int state){
        setVisibilityState(state);
        save.setText("Save");
        saveOnClickState(state);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog(0);
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog(1);
            }
        });
    }
    private void initializeViewVariables(){
        startDate=(Button)findViewById(R.id.reminderStartDateButton);
        endDate=(Button)findViewById(R.id.reminderEndDateButton);
        titleTextEdit=(EditText)findViewById(R.id.titleReminder);
        descriptionTextEdit=(EditText)findViewById(R.id.descriptionReminder);
        save = (Button)findViewById(R.id.saveReminderButton);

        titleTextView = (TextView)findViewById(R.id.reminderTitleTextView);
        descriptionTextView = (TextView)findViewById(R.id.reminderDescriptionTextView);

        startTime = new Time();
        endTime = new Time();
    }
    private void saveOnClickState(int state){
        switch (state){
            case 0:
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(startTime.before(endTime)){
                            SharedPreferences prefs = getSharedPreferences("user", 0);
                            Reminder newReminder = new Reminder(
                                    prefs.getInt("id",-200),
                                    String.valueOf(titleTextEdit.getText()),
                                    String.valueOf(descriptionTextEdit.getText()),
                                    startTime,endTime);
                            AlarmManagerHelper.cancelAlarms(getApplicationContext());
                            DBHelper db = new DBHelper(getApplicationContext());
                            db.addReminder(newReminder);
                            db.close();
                            AlarmManagerHelper.setAlarms(getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Saved",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "End Time is before start time",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 1:
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(startTime.before(endTime)){
                            SharedPreferences prefs = getSharedPreferences("user", 0);
                            bundle = getIntent();
                            Reminder newReminder = new Reminder(
                                    prefs.getInt("id",-200),
                                    String.valueOf(titleTextEdit.getText()),
                                    String.valueOf(descriptionTextEdit.getText()),
                                    startTime,endTime);
                            newReminder.setId(bundle.getExtras().getInt("ReminderId",-6));
                            AlarmManagerHelper.cancelAlarms(getApplicationContext());
                            DBHelper db = new DBHelper(getApplicationContext());
                            db.updateReminder(newReminder);
                            db.close();
                            AlarmManagerHelper.setAlarms(getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Saved",
                                    Toast.LENGTH_SHORT).show();
                            viewReminderState(0);
                        }else{
                            Toast.makeText(getApplicationContext(), "End Time is before start time",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }
    private void setVisibilityState(int state){
        switch (state){
            case 0:
                titleTextEdit.setVisibility(View.VISIBLE);
                titleTextView.setVisibility(View.GONE);
                descriptionTextEdit.setVisibility(View.VISIBLE);
                descriptionTextView.setVisibility(View.GONE);
                startDate.setClickable(true);
                endDate.setClickable(true);
                save.setVisibility(View.VISIBLE);
                break;
            case 1:
                titleTextEdit.setVisibility(View.GONE);
                titleTextView.setVisibility(View.VISIBLE);
                descriptionTextEdit.setVisibility(View.GONE);
                descriptionTextView.setVisibility(View.VISIBLE);
                startDate.setClickable(false);
                endDate.setClickable(false);
                save.setVisibility(View.GONE);
                break;
        }
    }
    private void calendarDialog(int when){
        Time yoloTime = new Time();
        yoloTime.setToNow();
        DatePickerDialog dpd;
        switch(when){
            case 0:
                timeDialog(0);
                dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Time minTime = new Time();
                                minTime.set(System.currentTimeMillis());
                                minTime.hour = 0;
                                minTime.minute = 0;
                                minTime.second = 0;
                                view.setCalendarViewShown(false);
                                view.setMinDate(minTime.toMillis(false));
                                view.setCalendarViewShown(false);
                                startTime.set(startTime.second,startTime.minute,startTime.hour,
                                        dayOfMonth,monthOfYear,year);
                            }
                        }, yoloTime.year, yoloTime.month, yoloTime.monthDay);
                dpd.show();
                break;
            case 1:
                timeDialog(1);
                dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Time minTime = new Time();
                                minTime.set(0,0,0,startTime.monthDay,startTime.month,startTime.year);
                                view.setCalendarViewShown(false);
                                view.setMinDate(minTime.toMillis(false));
                                endTime.set(endTime.second,endTime.minute,endTime.hour,
                                        dayOfMonth,monthOfYear,year);
                            }
                        }, yoloTime.year, yoloTime.month, yoloTime.monthDay);
                dpd.show();
                break;
        }
    }
    private void timeDialog(int when){
        TimePickerDialog tpd;
        Time yoloTime = new Time();
        yoloTime.setToNow();
        switch (when){
            case 0:
                tpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                startTime.set(startTime.second,minute,hourOfDay,
                                        startTime.monthDay,startTime.month,startTime.year);
                                startDate.setText(
                                        startTime.monthDay + "/" +
                                                (startTime.month + 1) + "/" +
                                                startTime.year + "   " +
                                                startTime.hour + ":" +
                                                startTime.minute
                                );
                            }
                        }, yoloTime.hour, yoloTime.minute, false);
                tpd.show();
                startDate.setTextColor(Color.WHITE);
                break;
            case 1:
                tpd = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                endTime.hour=hourOfDay;
                                endTime.minute=minute;
                                endTime.set(endTime.second,minute,hourOfDay,
                                        endTime.monthDay,endTime.month,endTime.year);
                                endDate.setText(
                                        endTime.monthDay+"/"+
                                        (endTime.month+1)+"/"+
                                        endTime.year+"   "+
                                        endTime.hour + ":" +
                                        endTime.minute
                                );
                            }
                        }, yoloTime.hour, yoloTime.minute, false);
                tpd.show();
                endDate.setTextColor(Color.WHITE);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewreminder, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.editReminderActionBar:
                reminderState(0);
                viewReminderState(1);
                return true;
            case R.id.deleteReminderActionBar:
                deleteDialog();
                return true;
            default:
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
    private void deleteDialog(){
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);
        deleteBuilder.setMessage("You'll lose reminder data forever!")
                .setTitle("Delete reminder?").setIcon(R.drawable.ic_action_warning);
        deleteBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DBHelper db = new DBHelper(getApplicationContext());
                AlarmManagerHelper.cancelAlarms(getApplicationContext());
                db.deleteReminder(bundle.getExtras().getInt("ReminderId",-6));
                db.close();
                AlarmManagerHelper.setAlarms(getApplicationContext());
                Toast.makeText(getApplicationContext(),"Reminder deleted",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        deleteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog deleteDialog = deleteBuilder.create();
        deleteDialog.show();
    }
}