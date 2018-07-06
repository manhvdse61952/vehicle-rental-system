package com.example.manhvdse61952.vrc_android.layout.vehicle;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.timessquare.CalendarPickerView;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalendarCustom extends AppCompatActivity {
    int startHours = 0, endHours = 0, startMinutes = 0, endMinute = 0;
    Button btn_save_time;
    String startDatePicker = "", endDatePicker = "";
    public static List<Date> listRentDay = new ArrayList<>();
    public static List<String> listRentHour = new ArrayList<>(); //Format: HH:dd:MM:yyyy
    CalendarPickerView calendarPickerView;
    Boolean isHourOK = true;
    TextView txt_hour_view_0, txt_hour_view_1, txt_hour_view_2, txt_hour_view_3, txt_hour_view_4, txt_hour_view_5,
            txt_hour_view_6, txt_hour_view_7, txt_hour_view_8, txt_hour_view_9, txt_hour_view_10, txt_hour_view_11,
            txt_hour_view_12, txt_hour_view_13, txt_hour_view_14, txt_hour_view_15, txt_hour_view_16, txt_hour_view_17,
            txt_hour_view_18, txt_hour_view_19, txt_hour_view_20, txt_hour_view_21, txt_hour_view_22, txt_hour_view_23;

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

        //Init calendar
        calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

        //Temp 1
        String startDate = "22:00:00:08:07:2018";
        String endDate = "15:00:00:12:07:2018";
        getListBusyDay(startDate, endDate);
        //Temp 2
        String startDate2 = "03:00:00:14:07:2018";
        String endDate2 = "22:00:00:14:07:2018";
        getListBusyDay(startDate2, endDate2);
        //Temp 3
        String startDate3 = "02:00:00:20:07:2018";
        String endDate3 = "15:00:00:25:07:2018";
        getListBusyDay(startDate3, endDate3);


        //add year to calendar from today
        calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
        if (listRentDay.size() > 0) {
            calendarPickerView.highlightDates(listRentDay);
        }
        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //Change color of hour in selected day
                isHourOK = true;
                changeColorGreenForAllHour();
                String startDateParse = convertDateToNormalFormatVersionTwo(calendarPickerView.getSelectedDate().toString());
                ////////////////////////////
                for (int k = 0;k<listRentDay.size();k++){
                    String parseListRentDay = convertDateToNormalFormatVersionTwo(listRentDay.get(k).toString());
                    if (parseListRentDay.equals(startDateParse)){
                        changeColorRedForAllHour();
                        isHourOK = false;
                        break;
                    }
                }
                ////////////////////////////
                for (int i = 0; i < listRentHour.size(); i++) {
                    String[] temp = listRentHour.get(i).split(":");
                    String startDateInBusy = temp[1] + ":" + temp[2] + ":" + temp[3];
                    String hourInBusy = temp[0];
                    if (startDateParse.equals(startDateInBusy)){
                        int pos = Integer.parseInt(hourInBusy);
                        changeColorHour(pos);
                    }
                }

                //Set date value to execute
                if (calendarPickerView.getSelectedDates().size() == 0) {
                    Date today = new Date();
                    startDatePicker = today.toString();
                    endDatePicker = today.toString();
                } else if (calendarPickerView.getSelectedDates().size() == 1) {
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
        NumberPicker time_picker_hours_start = (NumberPicker) findViewById(R.id.time_picker_hours_start);
        NumberPicker time_picker_minutes_start = (NumberPicker) findViewById(R.id.time_picker_minutes_start);
        NumberPicker time_picker_hours_end = (NumberPicker) findViewById(R.id.time_picker_hours_end);
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
        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
        editor.putLong("startDayLong", d1.getTime());
        editor.putLong("endDayLong", d2.getTime());
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

    public static void getListBusyDay(String startTime, String endTime) {
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
        try {
            dateStartSimple = sdfSimpleTime.parse(dateStart);
            dateEndSimple = sdfSimpleTime.parse(dateEnd);
            //TH1: ngay bat dau va ngay ket thuc giong nhau
            if (dateStartSimple.compareTo(dateEndSimple) == 0) {
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
                    listRentDay.add(dateStartSimple);
                    for (int i = 1; i < totalDay; i++) {
                        currentStartTime = currentStartTime + 86400000;
                        String getStringDate = convertLongToDate(currentStartTime);
                        Date getDate = sdfSimpleTime.parse(getStringDate);
                        listRentDay.add(getDate);
                    }
                    listRentDay.add(dateEndSimple);
                    //Ngay dau tien su dung duoc
                } else if ((24 - hourStart < 20) && (24 - hourEnd <= 4)) {
                    for (int i = 1; i < totalDay; i++) {
                        currentStartTime = currentStartTime + 86400000;
                        String getStringDate = convertLongToDate(currentStartTime);
                        Date getDate = sdfSimpleTime.parse(getStringDate);
                        listRentDay.add(getDate);
                    }
                    listRentDay.add(dateEndSimple);
                    String currentStartTimeString = convertLongToDate(dateStartSimple.getTime());
                    listRentHour.add((hourStart - 1) + ":" + currentStartTimeString);
                    for (int i = hourStart; i <= 23; i++) {
                        listRentHour.add(i + ":" + currentStartTimeString);
                    }
                    //Ngay cuoi cung su dung duoc
                } else if ((24 - hourStart >= 20) && (24 - hourEnd > 4)) {
                    listRentDay.add(dateStartSimple);
                    for (int i = 1; i < totalDay; i++) {
                        currentStartTime = currentStartTime + 86400000;
                        String getStringDate = convertLongToDate(currentStartTime);
                        Date getDate = sdfSimpleTime.parse(getStringDate);
                        listRentDay.add(getDate);
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
                        listRentDay.add(getDate);
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

    private void saveTime() {
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
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < listRentHour.size(); i++) {
                for (int j = 0; j < listHourTemp.size(); j++) {
                    if (listHourTemp.get(j).equals(listRentHour.get(i))) {
                        isHourOK = false;
                        break;
                    }
                }
            }

            if (isHourOK == false) {
                Toast.makeText(this, "Thời gian này đã có người thuê! Vui lòng chọn lại", Toast.LENGTH_SHORT).show();
            } else {
                //Save value to shared preference
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("startHour", startHours + "");
                editor.putString("endHour", endHours + "");
                editor.putString("startMinute", startMinutes + "");
                editor.putString("endMinute", endMinute + "");
                editor.putString("startDate", finalStartDate);
                editor.putString("endDate", finalEndDate);
                editor.putInt("totalDay", totalDay);
                editor.putInt("totalHour", totalHour);
                editor.putInt("totalMinute", totalMinute);
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
        }
        if (pos == 1) {
            txt_hour_view_1.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 2) {
            txt_hour_view_2.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 3) {
            txt_hour_view_3.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 4) {
            txt_hour_view_4.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 5) {
            txt_hour_view_5.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 6) {
            txt_hour_view_6.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 7) {
            txt_hour_view_7.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 8) {
            txt_hour_view_8.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 9) {
            txt_hour_view_9.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 10) {
            txt_hour_view_10.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 11) {
            txt_hour_view_11.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 12) {
            txt_hour_view_12.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 13) {
            txt_hour_view_13.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 14) {
            txt_hour_view_14.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 15) {
            txt_hour_view_15.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 16) {
            txt_hour_view_16.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 17) {
            txt_hour_view_17.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 18) {
            txt_hour_view_18.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 19) {
            txt_hour_view_19.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 20) {
            txt_hour_view_20.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 21) {
            txt_hour_view_21.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 22) {
            txt_hour_view_22.setBackgroundResource(R.drawable.border_red);
        }
        if (pos == 23) {
            txt_hour_view_23.setBackgroundResource(R.drawable.border_red);
        }
    }

    private void changeColorGreenForAllHour(){
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
    }

    private void changeColorRedForAllHour(){
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
    }

}
