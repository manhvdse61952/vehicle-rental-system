package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail;

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
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.TimeAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.model.api_model.RentTime;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.timessquare.CalendarPickerView;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CalendarCustom extends AppCompatActivity {
    int startHours = 0, endHours = 0, startMinutes = 0, endMinute = 0;
    Button btn_save_time;
    String startDatePicker = "", endDatePicker = "";
    public static List<RentTime> listRentTimeTotal = new ArrayList<>();
    public static List<Date> listRentDay = new ArrayList<>(); //Format: dd:MM:yyyy
    public static List<Date> listRentDayNotFull = new ArrayList<>();  //Format: dd:MM:yyyy
    public static List<String> listRentHour = new ArrayList<>(); //Format: HH:dd:MM:yyyy
    CalendarPickerView calendarPickerView;
    Date selectDayTemp = null;
    Boolean isHourOK = true;
    ProgressDialog dialog;
    RadioButton rd_oneDay, rd_multipleDay;
    TextView txt_hour_view_0, txt_hour_view_1, txt_hour_view_2, txt_hour_view_3, txt_hour_view_4, txt_hour_view_5,
            txt_hour_view_6, txt_hour_view_7, txt_hour_view_8, txt_hour_view_9, txt_hour_view_10, txt_hour_view_11,
            txt_hour_view_12, txt_hour_view_13, txt_hour_view_14, txt_hour_view_15, txt_hour_view_16, txt_hour_view_17,
            txt_hour_view_18, txt_hour_view_19, txt_hour_view_20, txt_hour_view_21, txt_hour_view_22, txt_hour_view_23;
    Boolean isEndDate = false;

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

        //Declare ID
        btn_save_time = (Button) findViewById(R.id.btn_save_time);
        txt_hour_view_0 = (TextView) findViewById(R.id.txt_hour_view_0);
        txt_hour_view_1 = (TextView) findViewById(R.id.txt_hour_view_1);
        txt_hour_view_2 = (TextView) findViewById(R.id.txt_hour_view_2);
        txt_hour_view_3 = (TextView) findViewById(R.id.txt_hour_view_3);
        txt_hour_view_4 = (TextView) findViewById(R.id.txt_hour_view_4);
        txt_hour_view_5 = (TextView) findViewById(R.id.txt_hour_view_5);
        txt_hour_view_6 = (TextView) findViewById(R.id.txt_hour_view_6);
        txt_hour_view_7 = (TextView) findViewById(R.id.txt_hour_view_7);
        txt_hour_view_8 = (TextView) findViewById(R.id.txt_hour_view_8);
        txt_hour_view_9 = (TextView) findViewById(R.id.txt_hour_view_9);
        txt_hour_view_10 = (TextView) findViewById(R.id.txt_hour_view_10);
        txt_hour_view_11 = (TextView) findViewById(R.id.txt_hour_view_11);
        txt_hour_view_12 = (TextView) findViewById(R.id.txt_hour_view_12);
        txt_hour_view_13 = (TextView) findViewById(R.id.txt_hour_view_13);
        txt_hour_view_14 = (TextView) findViewById(R.id.txt_hour_view_14);
        txt_hour_view_15 = (TextView) findViewById(R.id.txt_hour_view_15);
        txt_hour_view_16 = (TextView) findViewById(R.id.txt_hour_view_16);
        txt_hour_view_17 = (TextView) findViewById(R.id.txt_hour_view_17);
        txt_hour_view_18 = (TextView) findViewById(R.id.txt_hour_view_18);
        txt_hour_view_19 = (TextView) findViewById(R.id.txt_hour_view_19);
        txt_hour_view_20 = (TextView) findViewById(R.id.txt_hour_view_20);
        txt_hour_view_21 = (TextView) findViewById(R.id.txt_hour_view_21);
        txt_hour_view_22 = (TextView) findViewById(R.id.txt_hour_view_22);
        txt_hour_view_23 = (TextView) findViewById(R.id.txt_hour_view_23);
        calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        rd_oneDay = (RadioButton) findViewById(R.id.rd_oneDay);
        rd_multipleDay = (RadioButton) findViewById(R.id.rd_multipleDay);


        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.SINGLE);
        selectDayTemp = today;
        startDatePicker = today.toString();
        endDatePicker = today.toString();

        //Init time picker
        final NumberPicker time_picker_hours_start = (NumberPicker) findViewById(R.id.time_picker_hours_start);
        NumberPicker time_picker_minutes_start = (NumberPicker) findViewById(R.id.time_picker_minutes_start);
        final NumberPicker time_picker_hours_end = (NumberPicker) findViewById(R.id.time_picker_hours_end);
        NumberPicker time_picker_minutes_end = (NumberPicker) findViewById(R.id.time_picker_minutes_end);

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
        //Call first API
        getVehicleBusyTime();
        calendarPickerView.selectDate(today);

        rd_oneDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Init calendar
                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, 1);
                    Date today = new Date();
                    calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.SINGLE);
                    calendarPickerView.selectDate(selectDayTemp);
                    rd_multipleDay.setChecked(false);
                    if (listRentDay.size() > 0) {
                        calendarPickerView.highlightDates(listRentDay);
                    }
                    if (listRentDayNotFull.size() > 0) {
                        calendarPickerView.highlightDates(listRentDayNotFull);
                    }
                }
            }
        });

        rd_multipleDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Init calendar
                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, 1);
                    Date today = new Date();
                    calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
                    calendarPickerView.selectDate(selectDayTemp);
                    rd_oneDay.setChecked(false);
                    if (listRentDay.size() > 0) {
                        calendarPickerView.highlightDates(listRentDay);
                    }
                    if (listRentDayNotFull.size() > 0) {
                        calendarPickerView.highlightDates(listRentDayNotFull);
                    }
                }
            }
        });

        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //Set date value to execute
                if (calendarPickerView.getSelectedDates().size() == 0) {
                    Date today = new Date();
                    startDatePicker = today.toString();
                    endDatePicker = today.toString();
                    selectDayTemp = today;
                    isEndDate = false;
                } else if (calendarPickerView.getSelectedDates().size() == 1) {
                    startDatePicker = calendarPickerView.getSelectedDate().toString();
                    endDatePicker = calendarPickerView.getSelectedDate().toString();
                    selectDayTemp = calendarPickerView.getSelectedDate();
                    if (rd_multipleDay.isChecked()) {
                        Toast.makeText(CalendarCustom.this, "Chọn ngày đầu tiên và ngày cuối cùng để đặt lịch", Toast.LENGTH_SHORT).show();
                    }
                    isEndDate = false;
                } else {
                    startDatePicker = calendarPickerView.getSelectedDates().get(0).toString();
                    endDatePicker = calendarPickerView.getSelectedDates().get(calendarPickerView.getSelectedDates().size() - 1).toString();
                    selectDayTemp = calendarPickerView.getSelectedDates().get(0);
                    isEndDate = true;
                }

                //Change color of all hour in busy day and check if user selected busy day
                isHourOK = true;
                changeColorGreenForAllHour();
                String startDateParse = convertDateToNormalFormatVersionTwo(endDatePicker);
                for (int k = 0; k < listRentDay.size(); k++) {
                    String parseListRentDay = convertDateToNormalFormatVersionTwo(listRentDay.get(k).toString());
                    if (parseListRentDay.equals(startDateParse)) {
                        changeColorRedForAllHour();
                        isHourOK = false;
                        break;
                    }
                }
                //Change to red color of hour
                for (int i = 0; i < listRentHour.size(); i++) {
                    String[] temp = listRentHour.get(i).split(":");
                    String startDateInBusy = temp[1] + ":" + temp[2] + ":" + temp[3];
                    String hourInBusy = temp[0];
                    if (startDateParse.equals(startDateInBusy)) {
                        int pos = Integer.parseInt(hourInBusy);
                        changeColorHour(pos);
                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });



        txt_hour_view_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(0);
                } else {
                    time_picker_hours_start.setValue(0);
                }
            }
        });
        txt_hour_view_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(1);
                } else {
                    time_picker_hours_start.setValue(1);
                }
            }
        });
        txt_hour_view_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(2);
                } else {
                    time_picker_hours_start.setValue(2);
                }
            }
        });
        txt_hour_view_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(3);
                } else {
                    time_picker_hours_start.setValue(3);
                }
            }
        });
        txt_hour_view_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(4);
                } else {
                    time_picker_hours_start.setValue(4);
                }
            }
        });
        txt_hour_view_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(5);
                } else {
                    time_picker_hours_start.setValue(5);
                }
            }
        });
        txt_hour_view_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(6);
                } else {
                    time_picker_hours_start.setValue(6);
                }
            }
        });
        txt_hour_view_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(7);
                } else {
                    time_picker_hours_start.setValue(7);
                }
            }
        });
        txt_hour_view_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(8);
                } else {
                    time_picker_hours_start.setValue(8);
                }
            }
        });
        txt_hour_view_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(9);
                } else {
                    time_picker_hours_start.setValue(9);
                }
            }
        });
        txt_hour_view_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(10);
                } else {
                    time_picker_hours_start.setValue(10);
                }
            }
        });
        txt_hour_view_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(11);
                } else {
                    time_picker_hours_start.setValue(11);
                }
            }
        });
        txt_hour_view_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(12);
                } else {
                    time_picker_hours_start.setValue(12);
                }
            }
        });
        txt_hour_view_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(13);
                } else {
                    time_picker_hours_start.setValue(13);
                }
            }
        });
        txt_hour_view_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(14);
                } else {
                    time_picker_hours_start.setValue(14);
                }
            }
        });
        txt_hour_view_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(15);
                } else {
                    time_picker_hours_start.setValue(15);
                }
            }
        });
        txt_hour_view_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(16);
                } else {
                    time_picker_hours_start.setValue(16);
                }
            }
        });
        txt_hour_view_17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(17);
                } else {
                    time_picker_hours_start.setValue(17);
                }
            }
        });
        txt_hour_view_18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(18);
                } else {
                    time_picker_hours_start.setValue(18);
                }
            }
        });
        txt_hour_view_19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(19);
                } else {
                    time_picker_hours_start.setValue(19);
                }
            }
        });
        txt_hour_view_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(20);
                } else {
                    time_picker_hours_start.setValue(20);
                }
            }
        });
        txt_hour_view_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(21);
                } else {
                    time_picker_hours_start.setValue(21);
                }
            }
        });
        txt_hour_view_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(22);
                } else {
                    time_picker_hours_start.setValue(22);
                }
            }
        });
        txt_hour_view_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEndDate == true){
                    time_picker_hours_end.setValue(23);
                } else {
                    time_picker_hours_start.setValue(23);
                }
            }
        });

        btn_save_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTime();
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
        Intent it = new Intent(CalendarCustom.this, VehicleDetail.class);
        startActivity(it);
        super.onBackPressed();
    }

    private String convertDateToNormalFormat(String str) {
        String result = "";
        String[] temp = str.split(" ");
        String day = temp[2];
        String monthTemp = temp[1];
        String year = temp[5];
        String month = "";
        if (monthTemp.equals("Jan")) {
            month = "01";
        } else if (monthTemp.equals("Feb")) {
            month = "02";
        } else if (monthTemp.equals("Mar")) {
            month = "03";
        } else if (monthTemp.equals("Apr")) {
            month = "04";
        } else if (monthTemp.equals("May")) {
            month = "05";
        } else if (monthTemp.equals("Jun")) {
            month = "06";
        } else if (monthTemp.equals("Jul")) {
            month = "07";
        } else if (monthTemp.equals("Aug")) {
            month = "08";
        } else if (monthTemp.equals("Sep")) {
            month = "09";
        } else if (monthTemp.equals("Oct")) {
            month = "10";
        } else if (monthTemp.equals("Nov")) {
            month = "11";
        } else {
            month = "12";
        }
        result = day + " / " + month + " / " + year;
        return result;
    }

    private String convertDateToNormalFormatVersionTwo(String str) {
        String result = "";
        String[] temp = str.split(" ");
        String day = temp[2];
        String monthTemp = temp[1];
        String year = temp[5];
        String month = "";
        if (monthTemp.equals("Jan")) {
            month = "01";
        } else if (monthTemp.equals("Feb")) {
            month = "02";
        } else if (monthTemp.equals("Mar")) {
            month = "03";
        } else if (monthTemp.equals("Apr")) {
            month = "04";
        } else if (monthTemp.equals("May")) {
            month = "05";
        } else if (monthTemp.equals("Jun")) {
            month = "06";
        } else if (monthTemp.equals("Jul")) {
            month = "07";
        } else if (monthTemp.equals("Aug")) {
            month = "08";
        } else if (monthTemp.equals("Sep")) {
            month = "09";
        } else if (monthTemp.equals("Oct")) {
            month = "10";
        } else if (monthTemp.equals("Nov")) {
            month = "11";
        } else {
            month = "12";
        }
        result = day + ":" + month + ":" + year;
        return result;
    }

    private List<Integer> calculateTime(String startDay, String endDay, String startHour, String endHour) {
        List<Integer> listTime = new ArrayList<>();
        startDay = startDay.replaceAll("\\s", "");
        endDay = endDay.replaceAll("\\s", "");

        String dateStart = startDay + " " + startHour;
        String dateEnd = endDay + " " + endHour;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date d1 = null, d2 = null;
        try {
            d1 = sdf.parse(dateStart);
            d2 = sdf.parse(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Get time
        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
        editor.putLong(ImmutableValue.MAIN_startDayLong, d1.getTime());
        editor.putLong(ImmutableValue.MAIN_endDayLong, d2.getTime());
        editor.apply();
        long diff = d2.getTime() - d1.getTime();
        long diffDate = TimeUnit.MILLISECONDS.toDays(diff);
        long diffHour = diff / (60 * 60 * 1000) % 24;
        long diffMinute = diff / (60 * 1000) % 60;
        listTime.add((int) diffDate);
        listTime.add((int) diffHour);
        listTime.add((int) diffMinute);
        Log.d("total date", diffDate + "");
        Log.d("total hour", diffHour + "");
        Log.d("total minute", diffMinute + "");
        return listTime;
    }

    public static void getListBusyDay(String serverTime, String startTime, String endTime) {
        //Two type of convert date
        //SimpleDateFormat sdfFullTime = new SimpleDateFormat("HH:mm:ss:dd:MM:yyyy");
        SimpleDateFormat sdfSimpleTime = new SimpleDateFormat("dd:MM:yyyy");

        //Convert date to execute
        String[] dateStartTemp = startTime.split("[:]");
        String[] dateEndTemp = endTime.split("[:]");
        String dateStart = dateStartTemp[3] + ":" + dateStartTemp[4] + ":" + dateStartTemp[5];
        String dateEnd = dateEndTemp[3] + ":" + dateEndTemp[4] + ":" + dateEndTemp[5];
        int hourStart = Integer.parseInt(dateStartTemp[0]);
        int hourEnd = Integer.parseInt(dateEndTemp[0]);

        //Check if start date and end date are equal without calculate hour and minutes
        Date dateStartSimple = null;
        Date dateEndSimple = null;
        Date dateCurrentSimple = null;
        try {
            dateStartSimple = sdfSimpleTime.parse(dateStart);
            dateEndSimple = sdfSimpleTime.parse(dateEnd);
            dateCurrentSimple = sdfSimpleTime.parse(serverTime);
            //TH1: ngay bat dau va ngay ket thuc giong nhau
            if (dateStartSimple.compareTo(dateEndSimple) == 0 && dateStartSimple.after(dateCurrentSimple)) {
                if ((24 - hourStart >= 21) && (24 - hourEnd <= 3)) {
                    listRentDay.add(dateStartSimple);
                }
                if (hourStart == 0 && hourEnd != 23) {
                    long yesterdayStartTime = dateStartSimple.getTime() - 86400000;
                    String yesterdayStartTimeString = convertLongToDate(yesterdayStartTime);
                    String currentStartTimeString = convertLongToDate(dateStartSimple.getTime());
                    listRentHour.add("23:" + yesterdayStartTimeString);
                    for (int i = hourStart; i <= hourEnd; i++) {
                        listRentHour.add(i + ":" + currentStartTimeString);
                    }
                    listRentHour.add((hourEnd + 1) + ":" + currentStartTimeString);
                } else if (hourStart != 0 && hourEnd == 23) {
                    String currentStartTimeString = convertLongToDate(dateStartSimple.getTime());
                    listRentHour.add((hourStart - 1) + ":" + currentStartTimeString);
                    for (int i = hourStart; i <= hourEnd; i++) {
                        listRentHour.add(i + ":" + currentStartTimeString);
                    }
                    long tomorrowEndTime = dateEndSimple.getTime() + 86400000;
                    String tomorrowEndTimeString = convertLongToDate(tomorrowEndTime);
                    listRentHour.add("00:" + tomorrowEndTimeString);
                } else {
                    String currentStartTimeString = convertLongToDate(dateStartSimple.getTime());
                    listRentHour.add((hourStart - 1) + ":" + currentStartTimeString);
                    for (int i = hourStart; i <= hourEnd; i++) {
                        listRentHour.add(i + ":" + currentStartTimeString);
                    }
                    listRentHour.add((hourEnd + 1) + ":" + currentStartTimeString);
                }
                //TH2: ngay bat dau va ngay ket thuc khac nhau
            } else {
                long diff = dateEndSimple.getTime() - dateStartSimple.getTime();
                long currentStartTime = dateStartSimple.getTime();
                long diffDate = TimeUnit.MILLISECONDS.toDays(diff);
                int totalDay = (int) diffDate;
                //Ngay dau tien va ngay cuoi cung deu khong su dung duoc
                if ((24 - hourStart >= 21) && (24 - hourEnd <= 3)) {
                    if (dateStartSimple.after(dateCurrentSimple)){
                        listRentDay.add(dateStartSimple);
                    }
                    for (int i = 1; i < totalDay; i++) {
                        currentStartTime = currentStartTime + 86400000;
                        String getStringDate = convertLongToDate(currentStartTime);
                        Date getDate = sdfSimpleTime.parse(getStringDate);
                        if (getDate.after(dateCurrentSimple)){
                            listRentDay.add(getDate);
                        }
                    }
                    if (dateEndSimple.after(dateCurrentSimple)){
                        listRentDay.add(dateEndSimple);
                    }
                    //Ngay dau tien su dung duoc
                } else if ((24 - hourStart < 20) && (24 - hourEnd <= 4)) {
                    for (int i = 1; i < totalDay; i++) {
                        currentStartTime = currentStartTime + 86400000;
                        String getStringDate = convertLongToDate(currentStartTime);
                        Date getDate = sdfSimpleTime.parse(getStringDate);
                        if (getDate.after(dateCurrentSimple)){
                            listRentDay.add(getDate);
                        }
                    }
                    if (dateEndSimple.after(dateCurrentSimple)){
                        listRentDay.add(dateEndSimple);
                    }

                    String currentStartTimeString = convertLongToDate(dateStartSimple.getTime());
                    listRentHour.add((hourStart - 1) + ":" + currentStartTimeString);
                    for (int i = hourStart; i <= 23; i++) {
                        listRentHour.add(i + ":" + currentStartTimeString);
                    }
                    //Ngay cuoi cung su dung duoc
                } else if ((24 - hourStart >= 20) && (24 - hourEnd > 4)) {
                    if (dateStartSimple.after(dateCurrentSimple)){
                        listRentDay.add(dateStartSimple);
                    }
                    for (int i = 1; i < totalDay; i++) {
                        currentStartTime = currentStartTime + 86400000;
                        String getStringDate = convertLongToDate(currentStartTime);
                        Date getDate = sdfSimpleTime.parse(getStringDate);
                        if (getDate.after(dateCurrentSimple)){
                            listRentDay.add(getDate);
                        }
                    }
                    String currentEndTimeString = convertLongToDate(dateEndSimple.getTime());
                    for (int i = 0; i <= hourEnd; i++) {
                        listRentHour.add(i + ":" + currentEndTimeString);
                    }
                    listRentHour.add((hourEnd + 1) + ":" + currentEndTimeString);
                    //Ca 2 ngay deu su dung duoc
                } else {
                    for (int i = 1; i < totalDay; i++) {
                        currentStartTime = currentStartTime + 86400000;
                        String getStringDate = convertLongToDate(currentStartTime);
                        Date getDate = sdfSimpleTime.parse(getStringDate);
                        if (getDate.after(dateCurrentSimple)){
                            listRentDay.add(getDate);
                        }
                    }
                    String currentStartTimeString = convertLongToDate(dateStartSimple.getTime());
                    listRentHour.add((hourStart - 1) + ":" + currentStartTimeString);
                    for (int i = hourStart; i <= 23; i++) {
                        listRentHour.add(i + ":" + currentStartTimeString);
                    }
                    String currentEndTimeString = convertLongToDate(dateEndSimple.getTime());
                    for (int i = 0; i <= hourEnd; i++) {
                        listRentHour.add(i + ":" + currentEndTimeString);
                    }
                    listRentHour.add((hourEnd + 1) + ":" + currentEndTimeString);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String convertLongToDate(long value) {
        Date date = new Date(value);
        Format format = new SimpleDateFormat("dd:MM:yyyy");
        return format.format(date);
    }

    public static String convertLongToFullDate(long value) {
        Date date = new Date(value);
        Format format = new SimpleDateFormat("HH:mm:ss:dd:MM:yyyy");
        return format.format(date);
    }

    private void saveTime() {
        if (startDatePicker.equals("") && endDatePicker.equals("")) {
            Date today = new Date();
            startDatePicker = today.toString();
            endDatePicker = today.toString();
        }

        String finalStartDate = convertDateToNormalFormat(startDatePicker);
        String finalEndDate = convertDateToNormalFormat(endDatePicker);
        List<Integer> listTime = calculateTime(finalStartDate, finalEndDate, startHours + ":" + startMinutes, endHours + ":" + endMinute);
        int totalDay = listTime.get(0);
        int totalHour = listTime.get(1);
        int totalMinute = listTime.get(2);
        if (totalDay < 0 || totalHour < 0) {
            Toast.makeText(CalendarCustom.this, "Vui lòng chọn thời gian hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (totalDay == 0 && totalHour < 2) {
            Toast.makeText(CalendarCustom.this, "Thời gian thuê tối thiểu là 2 tiếng", Toast.LENGTH_SHORT).show();
        } else {
            List<String> listHourTemp = new ArrayList<>();
            Date date1 = null, date2 = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
            String startDateParse = convertDateToNormalFormatVersionTwo(startDatePicker);
            String endDateParse = convertDateToNormalFormatVersionTwo(endDatePicker);
            try {
                isHourOK = true;
                date1 = sdf.parse(startDateParse);
                date2 = sdf.parse(endDateParse);
                if (totalDay == 0) {
                    if (date1.compareTo(date2) == 0) {
                        for (int i = startHours; i <= endHours; i++) {
                            listHourTemp.add(i + ":" + startDateParse);
                        }
                    } else {
                        for (int i = startHours; i <= 23; i++) {
                            listHourTemp.add(i + ":" + startDateParse);
                        }
                        for (int j = 0; j <= endHours; j++) {
                            listHourTemp.add(j + ":" + endDateParse);
                        }

                    }
                    //Check busy hour
                    for (int i = 0; i < listRentHour.size(); i++) {
                        for (int j = 0; j < listHourTemp.size(); j++) {
                            if (listHourTemp.get(j).equals(listRentHour.get(i))) {
                                isHourOK = false;
                                break;
                            }
                        }
                    }
                    //Check busy day
                    for (int i = 0; i < listRentDay.size(); i++) {
                        if (date1.compareTo(listRentDay.get(i)) == 0) {
                            isHourOK = false;
                            break;
                        }
                    }
                } else {
                    long currentStartTime = date1.getTime();
                    for (int i = 0; i < totalDay; i++) {
                        currentStartTime = currentStartTime + 86400000;
                        String nextday = convertLongToDate(currentStartTime);
                        for (int j = 0; j < listRentDay.size(); j++) {
                            String busyDay = convertDateToNormalFormatVersionTwo(listRentDay.get(j).toString());
                            if (nextday.equals(busyDay)) {
                                isHourOK = false;
                                break;
                            }
                        }
                    }
                    for (int i = startHours; i <= 23; i++) {
                        listHourTemp.add(i + ":" + startDateParse);
                    }
                    for (int j = 0; j <= endHours; j++) {
                        listHourTemp.add(j + ":" + endDateParse);
                    }
                    for (int i = 0; i < listRentHour.size(); i++) {
                        for (int j = 0; j < listHourTemp.size(); j++) {
                            if (listHourTemp.get(j).equals(listRentHour.get(i))) {
                                isHourOK = false;
                                break;
                            }
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (isHourOK == false) {
                Toast.makeText(this, "Thời gian này đã có người thuê! Vui lòng chọn lại", Toast.LENGTH_SHORT).show();
            } else {
                //Save value to shared preference
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.MAIN_startHour, startHours + "");
                editor.putString(ImmutableValue.MAIN_endHour, endHours + "");
                editor.putString(ImmutableValue.MAIN_startMinute, startMinutes + "");
                editor.putString(ImmutableValue.MAIN_endMinute, endMinute + "");
                editor.putString(ImmutableValue.MAIN_startDate, finalStartDate);
                editor.putString(ImmutableValue.MAIN_endDate, finalEndDate);
                editor.putInt(ImmutableValue.MAIN_totalDay, totalDay);
                editor.putInt(ImmutableValue.MAIN_totalHour, totalHour);
                editor.putInt(ImmutableValue.MAIN_totalMinute, totalMinute);
                editor.apply();

                final ProgressDialog progress = new ProgressDialog(CalendarCustom.this);
                progress.setTitle("Hệ thống");
                progress.setMessage("Đang xử lý...");
                progress.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        progress.cancel();
                        Intent it = new Intent(CalendarCustom.this, VehicleDetail.class);
                        startActivity(it);
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 2000);
            }
        }

    }

    private void changeColorHour(int pos) {
        if (pos == 0) {
            txt_hour_view_0.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_0.setClickable(false);
        } else {
            txt_hour_view_0.setClickable(true);
        }
        if (pos == 1) {
            txt_hour_view_1.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_1.setClickable(false);
        } else {
            txt_hour_view_1.setClickable(true);
        }
        if (pos == 2) {
            txt_hour_view_2.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_2.setClickable(false);
        } else {
            txt_hour_view_2.setClickable(true);
        }
        if (pos == 3) {
            txt_hour_view_3.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_3.setClickable(false);
        } else {
            txt_hour_view_3.setClickable(true);
        }
        if (pos == 4) {
            txt_hour_view_4.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_4.setClickable(false);
        } else {
            txt_hour_view_4.setClickable(true);
        }
        if (pos == 5) {
            txt_hour_view_5.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_5.setClickable(false);
        } else {
            txt_hour_view_5.setClickable(true);
        }
        if (pos == 6) {
            txt_hour_view_6.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_6.setClickable(false);
        } else {
            txt_hour_view_6.setClickable(true);
        }
        if (pos == 7) {
            txt_hour_view_7.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_7.setClickable(false);
        } else {
            txt_hour_view_7.setClickable(true);
        }
        if (pos == 8) {
            txt_hour_view_8.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_8.setClickable(false);
        } else {
            txt_hour_view_8.setClickable(true);
        }
        if (pos == 9) {
            txt_hour_view_9.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_9.setClickable(false);
        } else {
            txt_hour_view_9.setClickable(true);
        }
        if (pos == 10) {
            txt_hour_view_10.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_10.setClickable(false);
        } else {
            txt_hour_view_10.setClickable(true);
        }
        if (pos == 11) {
            txt_hour_view_11.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_11.setClickable(false);
        } else {
            txt_hour_view_11.setClickable(true);
        }
        if (pos == 12) {
            txt_hour_view_12.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_12.setClickable(false);
        } else {
            txt_hour_view_12.setClickable(true);
        }
        if (pos == 13) {
            txt_hour_view_13.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_13.setClickable(false);
        } else {
            txt_hour_view_13.setClickable(true);
        }
        if (pos == 14) {
            txt_hour_view_14.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_14.setClickable(false);
        } else {
            txt_hour_view_14.setClickable(true);
        }
        if (pos == 15) {
            txt_hour_view_15.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_15.setClickable(false);
        } else {
            txt_hour_view_15.setClickable(true);
        }
        if (pos == 16) {
            txt_hour_view_16.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_16.setClickable(false);
        } else {
            txt_hour_view_16.setClickable(true);
        }
        if (pos == 17) {
            txt_hour_view_17.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_17.setClickable(false);
        } else {
            txt_hour_view_17.setClickable(true);
        }
        if (pos == 18) {
            txt_hour_view_18.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_18.setClickable(false);
        } else {
            txt_hour_view_18.setClickable(true);
        }
        if (pos == 19) {
            txt_hour_view_19.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_19.setClickable(false);
        } else {
            txt_hour_view_19.setClickable(true);
        }
        if (pos == 20) {
            txt_hour_view_20.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_20.setClickable(false);
        } else {
            txt_hour_view_20.setClickable(true);
        }
        if (pos == 21) {
            txt_hour_view_21.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_21.setClickable(false);
        } else {
            txt_hour_view_21.setClickable(true);
        }
        if (pos == 22) {
            txt_hour_view_22.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_22.setClickable(false);
        } else {
            txt_hour_view_22.setClickable(true);
        }
        if (pos == 23) {
            txt_hour_view_23.setBackgroundResource(R.drawable.border_red);
            txt_hour_view_23.setClickable(false);
        } else {
            txt_hour_view_23.setClickable(true);
        }
    }

    private void changeColorGreenForAllHour() {
        txt_hour_view_0.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_1.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_2.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_3.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_4.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_5.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_6.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_7.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_8.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_9.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_10.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_11.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_12.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_13.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_14.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_15.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_16.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_17.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_18.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_19.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_20.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_21.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_22.setBackgroundResource(R.drawable.border_green);
        txt_hour_view_23.setBackgroundResource(R.drawable.border_green);

        txt_hour_view_0.setClickable(true);
        txt_hour_view_1.setClickable(true);
        txt_hour_view_2.setClickable(true);
        txt_hour_view_3.setClickable(true);
        txt_hour_view_4.setClickable(true);
        txt_hour_view_5.setClickable(true);
        txt_hour_view_6.setClickable(true);
        txt_hour_view_7.setClickable(true);
        txt_hour_view_8.setClickable(true);
        txt_hour_view_9.setClickable(true);
        txt_hour_view_10.setClickable(true);
        txt_hour_view_11.setClickable(true);
        txt_hour_view_12.setClickable(true);
        txt_hour_view_13.setClickable(true);
        txt_hour_view_14.setClickable(true);
        txt_hour_view_15.setClickable(true);
        txt_hour_view_16.setClickable(true);
        txt_hour_view_17.setClickable(true);
        txt_hour_view_18.setClickable(true);
        txt_hour_view_19.setClickable(true);
        txt_hour_view_20.setClickable(true);
        txt_hour_view_21.setClickable(true);
        txt_hour_view_22.setClickable(true);
        txt_hour_view_23.setClickable(true);
    }

    private void changeColorRedForAllHour() {
        txt_hour_view_0.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_1.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_2.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_3.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_4.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_5.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_6.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_7.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_8.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_9.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_10.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_11.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_12.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_13.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_14.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_15.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_16.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_17.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_18.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_19.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_20.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_21.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_22.setBackgroundResource(R.drawable.border_red);
        txt_hour_view_23.setBackgroundResource(R.drawable.border_red);

        txt_hour_view_0.setClickable(false);
        txt_hour_view_1.setClickable(false);
        txt_hour_view_2.setClickable(false);
        txt_hour_view_3.setClickable(false);
        txt_hour_view_4.setClickable(false);
        txt_hour_view_5.setClickable(false);
        txt_hour_view_6.setClickable(false);
        txt_hour_view_7.setClickable(false);
        txt_hour_view_8.setClickable(false);
        txt_hour_view_9.setClickable(false);
        txt_hour_view_10.setClickable(false);
        txt_hour_view_11.setClickable(false);
        txt_hour_view_12.setClickable(false);
        txt_hour_view_13.setClickable(false);
        txt_hour_view_14.setClickable(false);
        txt_hour_view_15.setClickable(false);
        txt_hour_view_16.setClickable(false);
        txt_hour_view_17.setClickable(false);
        txt_hour_view_18.setClickable(false);
        txt_hour_view_19.setClickable(false);
        txt_hour_view_20.setClickable(false);
        txt_hour_view_21.setClickable(false);
        txt_hour_view_22.setClickable(false);
        txt_hour_view_23.setClickable(false);
    }

    private void getVehicleBusyTime() {
        dialog = ProgressDialog.show(CalendarCustom.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String frameNumber = editor.getString(ImmutableValue.MAIN_vehicleID, "aaaaaa");
        Retrofit retrofit = RetrofitConfig.getClient();
        TimeAPI timeAPI = retrofit.create(TimeAPI.class);
        Call<List<RentTime>> responseBodyCall = timeAPI.getBusyTimeByVehicleID(frameNumber);
        responseBodyCall.enqueue(new Callback<List<RentTime>>() {
            @Override
            public void onResponse(Call<List<RentTime>> call, Response<List<RentTime>> response) {
                if (response.code() == 200 && response.body() != null) {
                    listRentTimeTotal = response.body();
                    getUserBusyTime();
                } else if (response.code() == 204) {
                    getUserBusyTime();
                } else {
                    getUserBusyTime();
                }
            }

            @Override
            public void onFailure(Call<List<RentTime>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CalendarCustom.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserBusyTime() {
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int userID = editor.getInt(ImmutableValue.HOME_userID, 0);
        String roleName = editor.getString(ImmutableValue.HOME_role, ImmutableValue.ROLE_USER);
        Retrofit retrofit = RetrofitConfig.getClient();
        ContractAPI contractAPI = retrofit.create(ContractAPI.class);
        Call<List<ContractItem>> responseBodyCall = null;
        if (roleName.equals(ImmutableValue.ROLE_USER)) {
            responseBodyCall = contractAPI.findContractByCustomerID(userID);
        } else if (roleName.equals(ImmutableValue.ROLE_OWNER)) {
            responseBodyCall = contractAPI.findContractByOwnerID(userID);
        } else {
            responseBodyCall = contractAPI.findContractByCustomerID(userID);
        }
        responseBodyCall.enqueue(new Callback<List<ContractItem>>() {
            @Override
            public void onResponse(Call<List<ContractItem>> call, Response<List<ContractItem>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        List<ContractItem> contractItemList = new ArrayList<>();
                        contractItemList = response.body();
                        for (int i = 0; i < contractItemList.size(); i++) {
                            RentTime timeObj = new RentTime();
                            timeObj.setStartTime(contractItemList.get(i).getStartTime());
                            timeObj.setEndTime(contractItemList.get(i).getEndTime());
                            listRentTimeTotal.add(timeObj);
                        }
                    }
                    getServerTime();
                } else {
                    getServerTime();
                }
            }

            @Override
            public void onFailure(Call<List<ContractItem>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CalendarCustom.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getServerTime() {

        Retrofit retrofit = RetrofitConfig.getClient();
        TimeAPI timeAPI = retrofit.create(TimeAPI.class);
        Call<Long> responseBodyCall = timeAPI.getServerTime();
        responseBodyCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.code() == 200) {
                    String currentServerTime = convertLongToFullDate(response.body());
                    String currentServerDay = convertLongToDate(response.body());
                    String[] currentServerTimeTemp = currentServerTime.split(":");
                    int currentServerHour = Integer.parseInt(currentServerTimeTemp[0]);
                    if (currentServerHour > 0) {
                        for (int i = 0; i <= currentServerHour; i++) {
                            listRentHour.add(i + ":" + currentServerTimeTemp[3] + ":" + currentServerTimeTemp[4] + ":" + currentServerTimeTemp[5]);
                        }
                    } else {
                        listRentHour.add(currentServerHour + ":" + currentServerTimeTemp[3] + ":" + currentServerTimeTemp[4] + ":" + currentServerTimeTemp[5]);
                    }
                    if (listRentTimeTotal.size() > 0) {
                        for (int i = 0; i < listRentTimeTotal.size(); i++) {
                            RentTime rentObj = listRentTimeTotal.get(i);
                            String startTime = convertLongToFullDate(rentObj.getStartTime());
                            String endTime = convertLongToFullDate(rentObj.getEndTime());
                            getListBusyDay(currentServerDay, startTime, endTime);
                        }
                    }
                    if (listRentHour.size() > 0) {
                        List<String> listStringDateTemp = new ArrayList<>();
                        for (int i = 0; i < listRentHour.size(); i++) {
                            String[] temp = listRentHour.get(i).split(":");
                            String dateTemp = temp[1] + ":" + temp[2] + ":" + temp[3];
                            listStringDateTemp.add(dateTemp);
                        }
                        Set<String> listStringRemoveDuplicatedValue = new HashSet<>();
                        listStringRemoveDuplicatedValue.addAll(listStringDateTemp);
                        listStringDateTemp.clear();
                        listStringDateTemp.addAll(listStringRemoveDuplicatedValue);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
                        Date dateTemp = null, currentDate = null;
                        for (int i = 0; i < listStringDateTemp.size(); i++) {
                            try {
                                currentDate = sdf.parse(currentServerDay);
                                dateTemp = sdf.parse(listStringDateTemp.get(i));
                                if (currentDate.compareTo(dateTemp) > 0) {
                                    listStringDateTemp.remove(i);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }


                        for (int j = 0; j < listStringDateTemp.size(); j++) {
                            try {
                                dateTemp = sdf.parse(listStringDateTemp.get(j));
                                listRentDayNotFull.add(dateTemp);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (listRentDay.size() > 0) {
                        calendarPickerView.highlightDates(listRentDay);
                    }
                    if (listRentDayNotFull.size() > 0) {
                        calendarPickerView.highlightDates(listRentDayNotFull);
                    }

                    //Change to red color of hour in the first time (default is select current day)
                    for (int i = 0; i < listRentHour.size(); i++) {
                        String[] temp = listRentHour.get(i).split(":");
                        String startDateInBusy = temp[1] + ":" + temp[2] + ":" + temp[3];
                        String hourInBusy = temp[0];
                        if (currentServerDay.equals(startDateInBusy)) {
                            int pos = Integer.parseInt(hourInBusy);
                            changeColorHour(pos);
                        }
                    }
                } else {
                    Toast.makeText(CalendarCustom.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CalendarCustom.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
