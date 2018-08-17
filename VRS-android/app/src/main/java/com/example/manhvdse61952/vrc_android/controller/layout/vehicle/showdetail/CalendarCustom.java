package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.TimeAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.BusyDay;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.model.api_model.RentTime;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.shawnlin.numberpicker.NumberPicker;
import com.squareup.timessquare.CalendarPickerView;

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
    public List<RentTime> listRentHoursGetByAPI = new ArrayList<>();
    public List<String> listRentHourString = new ArrayList<>(); //Format: HH:dd:MM:yyyy
    public List<Date> listRentedDay = new ArrayList<>();
    public String currentConvertDay = "";
    public long currentLongDay = 0;

    CalendarPickerView calendarPickerView;
    Date selectDayTemp = null;
    ProgressDialog dialog;
    RadioButton rd_oneDay, rd_multipleDay;
    LinearLayout ln_parent_layout;
    Snackbar snackbar = null;
    TextView txt_hour_view_0, txt_hour_view_1, txt_hour_view_2, txt_hour_view_3, txt_hour_view_4, txt_hour_view_5,
            txt_hour_view_6, txt_hour_view_7, txt_hour_view_8, txt_hour_view_9, txt_hour_view_10, txt_hour_view_11,
            txt_hour_view_12, txt_hour_view_13, txt_hour_view_14, txt_hour_view_15, txt_hour_view_16, txt_hour_view_17,
            txt_hour_view_18, txt_hour_view_19, txt_hour_view_20, txt_hour_view_21, txt_hour_view_22, txt_hour_view_23, txt_hide_start, txt_hide_end, txt_title;
    NumberPicker time_picker_hours_start, time_picker_minutes_start, time_picker_hours_end, time_picker_minutes_end;

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
        declareID();

        //Get current day and init calendar
        getCurrentDateAndInitCalendar();

        //Change select calendar mode
        changeSelectCalendarMode();

        //Execute selected day
        executeSelectedDay();


        btn_save_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAction();
            }
        });
    }

    private void declareID() {
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
        txt_hide_start = (TextView) findViewById(R.id.txt_hide_start);
        txt_hide_end = (TextView) findViewById(R.id.txt_hide_end);
        txt_title = (TextView) findViewById(R.id.txt_title);
        calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        rd_oneDay = (RadioButton) findViewById(R.id.rd_oneDay);
        rd_multipleDay = (RadioButton) findViewById(R.id.rd_multipleDay);
        ln_parent_layout = (LinearLayout) findViewById(R.id.ln_parent_layout);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.SINGLE);


        time_picker_hours_start = (NumberPicker) findViewById(R.id.time_picker_hours_start);
        time_picker_minutes_start = (NumberPicker) findViewById(R.id.time_picker_minutes_start);
        time_picker_hours_end = (NumberPicker) findViewById(R.id.time_picker_hours_end);
        time_picker_minutes_end = (NumberPicker) findViewById(R.id.time_picker_minutes_end);

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
    }

    private void backToPreviousScreen() {
        Intent it = new Intent(CalendarCustom.this, VehicleDetail.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    //////////// SHOW IN CALENDAR //////////
    private void getCurrentDateAndInitCalendar() {
        dialog = ProgressDialog.show(CalendarCustom.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        Retrofit retrofit = RetrofitConfig.getClient();
        TimeAPI timeAPI = retrofit.create(TimeAPI.class);
        Call<Long> responseBodyCall = timeAPI.getServerTime();
        responseBodyCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.code() == 200) {
                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, 1);
                    Date today = new Date(response.body());
                    currentLongDay = response.body();
                    calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.SINGLE);

                    //Select current day
                    selectDayTemp = today;
                    startDatePicker = today.toString();
                    endDatePicker = today.toString();

                    //Set value for currentConvert day
                    SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
                    currentConvertDay = sdf.format(today);

                    //Call API
                    getListRentHour(response.body(), 0);
                    getRentHoursByVehicle();
                } else {
                    getRentHoursByVehicle();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CalendarCustom.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                backToPreviousScreen();
            }
        });
    }

    private void getRentHoursByVehicle() {
        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        final String frameNumber = editor.getString(ImmutableValue.MAIN_vehicleID, "Empty");
        if (!frameNumber.equals("Empty")) {
            Retrofit retrofit = RetrofitConfig.getClient();
            TimeAPI timeAPI = retrofit.create(TimeAPI.class);
            Call<List<RentTime>> responseBodyCall = timeAPI.getBusyTimeByVehicleID(frameNumber);
            responseBodyCall.enqueue(new Callback<List<RentTime>>() {
                @Override
                public void onResponse(Call<List<RentTime>> call, Response<List<RentTime>> response) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            listRentHoursGetByAPI = new ArrayList<>();
                            listRentHoursGetByAPI = response.body();
                            getRentHourByFreeTime(frameNumber);
                        }
                    } else {
                        getRentHourByFreeTime(frameNumber);
                    }
                }

                @Override
                public void onFailure(Call<List<RentTime>> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(CalendarCustom.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                    backToPreviousScreen();
                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            backToPreviousScreen();
        }
    }

    private void getRentHourByFreeTime(String frameNumber) {
        Retrofit retrofit = RetrofitConfig.getClient();
        VehicleAPI vehicleAPI = retrofit.create(VehicleAPI.class);
        Call<BusyDay> responseBodyCall = vehicleAPI.getBusyDay(frameNumber);
        responseBodyCall.enqueue(new Callback<BusyDay>() {
            @Override
            public void onResponse(Call<BusyDay> call, Response<BusyDay> response) {
                if (response.code() == 200) {
                    BusyDay obj = response.body();
                    Boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
                    monday = obj.getBusyMon();
                    tuesday = obj.getBusyTue();
                    wednesday = obj.getBusyWed();
                    thursday = obj.getBusyThu();
                    friday = obj.getBusyFri();
                    saturday = obj.getBusySat();
                    sunday = obj.getBusySun();

                    List<Integer> listDOW = new ArrayList<>();
                    if (monday == true) {
                        listDOW.add(2);
                    }
                    if (tuesday == true) {
                        listDOW.add(3);
                    }
                    if (wednesday == true) {
                        listDOW.add(4);
                    }
                    if (thursday == true) {
                        listDOW.add(5);
                    }
                    if (friday == true) {
                        listDOW.add(6);
                    }
                    if (saturday == true) {
                        listDOW.add(7);
                    }
                    if (sunday == true) {
                        listDOW.add(1);
                    }

                    if (listDOW.size() > 0) {
                        //Execute
                        Date dateTemp = new Date(currentLongDay);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String dateTemp2 = sdf.format(dateTemp);
                        try {
                            Date dateTempWithoutHour = sdf.parse(dateTemp2);
                            Calendar c = Calendar.getInstance();
                            Long dateLongTemp = dateTempWithoutHour.getTime() + 1000*60*60;
                            for (int i = 0; i < 364; i++) {
                                c.setTimeInMillis(dateLongTemp);
                                for (int j = 0; j < listDOW.size(); j++) {
                                    if (c.get(Calendar.DAY_OF_WEEK) == listDOW.get(j)) {
                                        RentTime rObj = new RentTime();
                                        rObj.setStartTime(dateLongTemp);
                                        rObj.setEndTime(dateLongTemp + 1000*60*60*23);
                                        listRentHoursGetByAPI.add(rObj);
                                    }
                                }
                                dateLongTemp = dateLongTemp + 1000*60*60*24;
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    getRentHourByUser();
                } else {
                    getRentHourByUser();
                }
            }

            @Override
            public void onFailure(Call<BusyDay> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CalendarCustom.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRentHourByUser() {
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        final int userID = editor.getInt(ImmutableValue.HOME_userID, 0);
        final String roleName = editor.getString(ImmutableValue.HOME_role, ImmutableValue.ROLE_USER);
        final int ownerIdOfVehicle = editor2.getInt(ImmutableValue.MAIN_ownerID, 0);
        if (userID == ownerIdOfVehicle) {
            ownerViewCalendarMode();
        }
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
                        List<ContractItem> deletedDuplicated = new ArrayList<>();
                        contractItemList = response.body();

                        if (roleName.equals(ImmutableValue.ROLE_OWNER)
                                && userID != ownerIdOfVehicle) {
                            deletedDuplicated.addAll(contractItemList);
                        } else {
                            for (int i = 0; i < contractItemList.size(); i++) {
                                if (contractItemList.get(i).getContractStatus().equals(ImmutableValue.CONTRACT_REFUNDED)) {
                                    deletedDuplicated.add(contractItemList.get(i));
                                }
                            }
                        }

                        if (deletedDuplicated.size() > 0) {
                            for (int i = 0; i < deletedDuplicated.size(); i++) {
                                contractItemList.remove(deletedDuplicated.get(i));
                            }
                        }

                        if (contractItemList.size() > 0) {
                            for (int i = 0; i < contractItemList.size(); i++) {
                                RentTime timeObj = new RentTime();
                                timeObj.setStartTime(contractItemList.get(i).getStartTime());
                                timeObj.setEndTime(contractItemList.get(i).getEndTime());
                                listRentHoursGetByAPI.add(timeObj);
                            }
                        }
                    }
                }
                dialog.dismiss();
                highLightRentDay();
            }

            @Override
            public void onFailure(Call<List<ContractItem>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CalendarCustom.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getListRentHour(long startTime, long endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:dd:MM:yyyy");
        Date executeDate = null;
        long executeTime = endTime;
        if (endTime != 0) {
            //Add start hour ensurance
            long startTimeEnsurance = startTime - 3600 * 1000;
            executeDate = new Date(startTimeEnsurance);
            String firstHourEnsuranceConvert = sdf.format(executeDate);
            listRentHourString.add(firstHourEnsuranceConvert);

            //Get first hour in list
            executeDate = new Date(startTime);
            String firstHourConvert = sdf.format(executeDate);
            listRentHourString.add(firstHourConvert);

            //Get next hours
            while (executeTime > startTime) {
                executeTime = executeTime - 3600 * 1000;
                executeDate = new Date(executeTime);
                String hourConvert = sdf.format(executeDate);
                listRentHourString.add(hourConvert);
            }

            //Add end hour ensurance
            long endHourEnsurance = endHours + 3600 * 1000;
            executeDate = new Date(endHourEnsurance);
            String endHourEnsuranceConvert = sdf.format(executeDate);
            listRentHourString.add(endHourEnsuranceConvert);

        } else if (endTime == 0) {
            //Get hour in current date
            executeDate = new Date(startTime);
            String currentHourConvert = sdf.format(executeDate);
            String[] temp = currentHourConvert.split(":");
            int hours = Integer.parseInt(temp[0]);
            for (int i = 0; i <= hours; i++) {
                String hourConvert = i + ":" + temp[1] + ":" + temp[2] + ":" + temp[3];
                listRentHourString.add(hourConvert);
            }

            //Add ensurance hour
            String hourConvert = (hours + 1) + ":" + temp[1] + ":" + temp[2] + ":" + temp[3];
            listRentHourString.add(hourConvert);
        }
    }

    private void highLightRentDay() {
        for (int i = 0; i < listRentHoursGetByAPI.size(); i++) {
            getListRentHour(listRentHoursGetByAPI.get(i).getStartTime(),
                    listRentHoursGetByAPI.get(i).getEndTime());
        }

        //Remove duplicated value
        Set<String> listStringRemoveDuplicatedValue = new HashSet<>();
        listStringRemoveDuplicatedValue.addAll(listRentHourString);
        listRentHourString.clear();
        listRentHourString.addAll(listStringRemoveDuplicatedValue);
        for (int i = 0; i < listRentHourString.size(); i++) {
            String[] temp = listRentHourString.get(i).split(":");
            int yearError = Integer.parseInt(temp[3]);
            if (yearError == 1970) {
                listRentHourString.remove(i);
            }
        }

        //Get rent day
        if (listRentHourString.size() > 0) {
            List<String> listRentDay = new ArrayList<>();
            List<String> deletedOverTime = new ArrayList<>();
            for (int i = 0; i < listRentHourString.size(); i++) {
                String[] temp = listRentHourString.get(i).split(":");
                String day = temp[1] + ":" + temp[2] + ":" + temp[3];
                listRentDay.add(day);
            }

            //Remove duplicated rent day
            Set<String> listStringRemoveDuplicatedDay = new HashSet<>();
            listStringRemoveDuplicatedDay.addAll(listRentDay);
            listRentDay.clear();
            listRentDay.addAll(listStringRemoveDuplicatedDay);

            //Catch exception when current day over than rent day
            SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
            Date currentDay = null, itemInListDay = null;
            try {
                currentDay = sdf.parse(currentConvertDay);
                for (int i = 0; i < listRentDay.size(); i++) {
                    itemInListDay = sdf.parse(listRentDay.get(i));
                    if (currentDay.compareTo(itemInListDay) > 0) {
                        deletedOverTime.add(listRentDay.get(i));
                    }
                }
                for (int i = 0; i < deletedOverTime.size(); i++) {
                    listRentDay.remove(deletedOverTime.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //show rent day in calendar
            listRentedDay = new ArrayList<>();
            for (int i = 0; i < listRentDay.size(); i++) {
                try {
                    itemInListDay = sdf.parse(listRentDay.get(i));
                    listRentedDay.add(itemInListDay);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            calendarPickerView.highlightDates(listRentedDay);
        }
    }

    /////////// CATCH WHEN USER SELECT DAY ////////////

    private void changeSelectCalendarMode() {
        rd_oneDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Init calendar
                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, 1);
                    Date today = new Date(currentLongDay);
                    calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.SINGLE);
                    calendarPickerView.selectDate(selectDayTemp);
                    rd_multipleDay.setChecked(false);
                    if (listRentedDay.size() > 0) {
                        calendarPickerView.highlightDates(listRentedDay);
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
                    Date today = new Date(currentLongDay);
                    calendarPickerView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
                    calendarPickerView.selectDate(selectDayTemp);
                    rd_oneDay.setChecked(false);
                    if (listRentedDay.size() > 0) {
                        calendarPickerView.highlightDates(listRentedDay);
                    }

                }
            }
        });
    }

    private void executeSelectedDay() {
        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                if (calendarPickerView.getSelectedDates().size() == 0) {
                    Date today = new Date(currentLongDay);
                    startDatePicker = today.toString();
                    endDatePicker = today.toString();
                    selectDayTemp = today;
                } else if (calendarPickerView.getSelectedDates().size() == 1) {
                    startDatePicker = calendarPickerView.getSelectedDate().toString();
                    endDatePicker = calendarPickerView.getSelectedDate().toString();
                    selectDayTemp = calendarPickerView.getSelectedDate();
                    if (rd_multipleDay.isChecked()) {
                        Toast.makeText(CalendarCustom.this, "Chọn ngày đầu tiên và ngày cuối cùng để đặt lịch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startDatePicker = calendarPickerView.getSelectedDates().get(0).toString();
                    endDatePicker = calendarPickerView.getSelectedDates().get(calendarPickerView.getSelectedDates().size() - 1).toString();
                    selectDayTemp = calendarPickerView.getSelectedDates().get(0);
                }

                String selectDayFormat = formatSelectedDay(endDatePicker);
                resetColorHour();

                List<Integer> listRent = new ArrayList<>();
                String dayShowInMessage = "";
                int maxHour = 0;
                int minHour = 0;

                for (int i = 0; i < listRentHourString.size(); i++) {
                    String[] temp = listRentHourString.get(i).split(":");
                    String itemInList = temp[1] + ":" + temp[2] + ":" + temp[3];
                    String itemTemp = listRentHourString.get(i);
                    if (itemInList.equals(selectDayFormat)) {
                        dayShowInMessage = itemInList;
                        dayShowInMessage = dayShowInMessage.replaceAll(":", "/");
                        changeColorHour(Integer.parseInt(temp[0]));
                        listRent.add(Integer.parseInt(temp[0]));
                    }
                }


                String strMessageDay = "";
                if (listRent.size() > 0) {
                    maxHour = listRent.get(0);
                    minHour = listRent.get(0);
                    for (int i = 0; i < listRent.size(); i++) {
                        if (minHour > listRent.get(i)) {
                            minHour = listRent.get(i);
                        }
                        if (maxHour < listRent.get(i)) {
                            maxHour = listRent.get(i);
                        }
                    }

                    strMessageDay = "Lịch bận từ: " + minHour + "h đến " + maxHour + "h";
                    if (minHour == 0 && maxHour == 23){
                        strMessageDay = "Xe bận cả ngày";
                    }

                    snackbar = Snackbar
                            .make(ln_parent_layout, strMessageDay, Snackbar.LENGTH_INDEFINITE)
                            .setAction("đồng ý", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });

                    snackbar.show();
                } else {
                    snackbar = Snackbar
                            .make(ln_parent_layout, "Xe không bận", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }

    private String formatSelectedDay(String str) {
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

    private void resetColorHour() {
        txt_hour_view_0.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_1.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_2.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_3.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_4.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_5.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_6.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_7.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_8.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_9.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_10.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_11.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_12.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_13.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_14.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_15.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_16.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_17.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_18.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_19.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_20.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_21.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_22.setBackgroundResource(R.drawable.border_green_primarygreen);
        txt_hour_view_23.setBackgroundResource(R.drawable.border_green_primarygreen);
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

    private Boolean catchErrorRentTime(String startTime, String endTime) {
        //format: HH:dd:MM:yyyy
        Boolean isFree = true;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:dd:MM:yyyy");
        Date startDay = null, endDay = null, executeDay = null;
        List<String> listHourSelected = new ArrayList<>();
        try {
            startDay = sdf.parse(startTime);
            endDay = sdf.parse(endTime);
            long executeTime = endDay.getTime();
            while (executeTime > startDay.getTime()) {
                executeTime = executeTime - 3600 * 1000;
                executeDay = new Date(executeTime);
                String hourConvert = sdf.format(executeDay);
                listHourSelected.add(hourConvert);
            }

            for (int i = 0; i < listHourSelected.size(); i++) {
                for (int j = 0; j < listRentHourString.size(); j++) {
                    if (listHourSelected.get(i).equals(listRentHourString.get(j))) {
                        isFree = false;
                        break;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isFree;
    }

    private void ownerViewCalendarMode() {
        rd_oneDay.setVisibility(View.INVISIBLE);
        rd_multipleDay.setVisibility(View.INVISIBLE);
        time_picker_hours_end.setVisibility(View.INVISIBLE);
        time_picker_hours_start.setVisibility(View.INVISIBLE);
        time_picker_minutes_end.setVisibility(View.INVISIBLE);
        time_picker_minutes_start.setVisibility(View.INVISIBLE);
        txt_hide_start.setVisibility(View.INVISIBLE);
        txt_hide_end.setVisibility(View.INVISIBLE);
        btn_save_time.setVisibility(View.INVISIBLE);
        txt_title.setText("Xem lịch thuê của xe");
    }

    /////////// SAVE BUTTON ///////////////
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
        return listTime;
    }

    private void saveAction() {
        if (startDatePicker.equals("") && endDatePicker.equals("")) {
            Date today = new Date(currentLongDay);
            startDatePicker = today.toString();
            endDatePicker = today.toString();
        }

        String finalStartDate = convertDateToNormalFormat(startDatePicker);
        String finalEndDate = convertDateToNormalFormat(endDatePicker);
        List<Integer> listTime = calculateTime(finalStartDate, finalEndDate, startHours + ":" + startMinutes, endHours + ":" + endMinute);
        int totalDay = listTime.get(0);
        int totalHour = listTime.get(1);
        int totalMinute = listTime.get(2);

        //Check rent time
        String startRentTime = startHours + ":" + formatSelectedDay(startDatePicker);
        String endRentTime = endHours + ":" + formatSelectedDay(endDatePicker);
        Boolean isFree = catchErrorRentTime(startRentTime, endRentTime);
        if (totalDay < 0 || totalHour < 0) {
            Toast.makeText(CalendarCustom.this, "Vui lòng chọn thời gian hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (totalDay == 0 && totalHour < 2) {
            Toast.makeText(CalendarCustom.this, "Thời gian thuê tối thiểu là 2 tiếng", Toast.LENGTH_SHORT).show();
        } else if (isFree == false) {
            Toast.makeText(this, "Thời gian này đã có người thuê! Vui lòng chọn lại", Toast.LENGTH_SHORT).show();
        } else {
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
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    CalendarCustom.this.finish();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }

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

    @Override
    public void onBackPressed() {
        CalendarCustom.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}