package com.example.manhvdse61952.vrc_android.layout.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarCustom extends AppCompatActivity {
    int startHours = 0, endHours = 0, startMinutes = 0, endMinute = 0;
    Button btn_save_time;
    String startDatePicker = "", endDatePicker = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        btn_save_time = (Button)findViewById(R.id.btn_save_time);

        //Init calendar
        final CalendarPickerView calendarPickerView = (CalendarPickerView)findViewById(R.id.calendar_view);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        String a = "15-07-2018";
//        java.util.Date date = null;
//        try {
//            date = sdf.parse(a);
//            //calendarPickerView.init(today, date).inMode(CalendarPickerView.SelectionMode.RANGE);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        //add year to calendar from today date
        calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
//                Toast.makeText(CalendarCustom.this, date.toString(), Toast.LENGTH_SHORT).show();
                //Log.d("DateTemp", date.toString());
                if (calendarPickerView.getSelectedDates().size() == 0){
                    Date today = new Date();
                    startDatePicker = today.toString();
                    endDatePicker = today.toString();
                } else if (calendarPickerView.getSelectedDates().size() == 1){
                    startDatePicker = calendarPickerView.getSelectedDate().toString();
                    endDatePicker = calendarPickerView.getSelectedDate().toString();
                } else {
                    startDatePicker = calendarPickerView.getSelectedDates().get(0).toString();
                    endDatePicker = calendarPickerView.getSelectedDates().get(calendarPickerView.getSelectedDates().size() - 1).toString();
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        //Init time picker
        NumberPicker time_picker_hours_start = (NumberPicker)findViewById(R.id.time_picker_hours_start);
        NumberPicker time_picker_minutes_start = (NumberPicker)findViewById(R.id.time_picker_minutes_start);
        NumberPicker time_picker_hours_end = (NumberPicker)findViewById(R.id.time_picker_hours_end);
        NumberPicker time_picker_minutes_end = (NumberPicker)findViewById(R.id.time_picker_minutes_end);

        time_picker_hours_start.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                startHours = newVal;
            }
        });

        time_picker_minutes_start.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                startMinutes = newVal;
            }
        });

        time_picker_hours_end.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                endHours = newVal;
            }
        });

        time_picker_minutes_end.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                endMinute = newVal;
            }
        });

        btn_save_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalStartDate = convertDateToNormalFormat(startDatePicker);
                String finalEndDate = convertDateToNormalFormat(endDatePicker);

                //Save value to shared preference
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("startHour", startHours + "");
                editor.putString("endHour", endHours + "");
                editor.putString("startMinute", startMinutes + "");
                editor.putString("endMinute", endMinute + "");
                editor.putString("startDate", finalStartDate);
                editor.putString("endDate", finalEndDate);
                editor.apply();

                final ProgressDialog progress = new ProgressDialog(CalendarCustom.this);
                progress.setTitle("Hệ thống");
                progress.setMessage("Đang xử lý...");
                progress.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        progress.cancel();
                        Intent it = new Intent(CalendarCustom.this, MainItem.class);
                        startActivity(it);
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 1500);

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String convertDateToNormalFormat(String str){
        String result = "";
        String[] temp = str.split(" ");
        String day = temp[2];
        String monthTemp = temp[1];
        String year = temp[5];
        String month = "";
        if (monthTemp.equals("Jan")) {
            month = "01";
        } else if (monthTemp.equals("Feb")){
            month = "02";
        } else if (monthTemp.equals("Mar")){
            month = "03";
        } else if (monthTemp.equals("Apr")){
            month = "04";
        } else if (monthTemp.equals("May")){
            month = "05";
        } else if (monthTemp.equals("Jun")){
            month = "06";
        } else if (monthTemp.equals("Jul")){
            month = "07";
        } else if (monthTemp.equals("Aug")){
            month = "08";
        } else if (monthTemp.equals("Sep")){
            month = "09";
        } else if (monthTemp.equals("Oct")){
            month = "10";
        } else if (monthTemp.equals("Nov")){
            month = "11";
        } else {
            month = "12";
        }
        result = day + "/" + month + "/" + year;
        return result;
    }

}
