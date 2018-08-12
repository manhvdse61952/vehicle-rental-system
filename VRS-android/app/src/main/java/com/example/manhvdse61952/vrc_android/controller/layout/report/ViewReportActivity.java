package com.example.manhvdse61952.vrc_android.controller.layout.report;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ReportAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.Report;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewReportActivity extends AppCompatActivity {
    Spinner spn_report_first, spn_report_second;
    TextView txt_report_totalMoney, txt_report_totalContract, txt_report_totalRefunded, txt_report_total_fee;
    PieChart pie_chart;
    int month = 0, year = 2018, userID = 0;

    int finishedContract = 0, refundedContract = 0, inactiveContract = 0,
            activeContract = 0, issueContract = 0, pendingContract = 0, prefinishedContract = 0;
    long totalMoney = 0, totalRefunded = 0, totalOtherFee = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();

        initLayout();

    }

    private void declareID() {
        pie_chart = (PieChart) findViewById(R.id.pie_chart);
        spn_report_first = (Spinner) findViewById(R.id.spn_report_first);
        spn_report_second = (Spinner) findViewById(R.id.spn_report_second);
        txt_report_totalMoney = (TextView) findViewById(R.id.txt_report_totalMoney);
        txt_report_totalContract = (TextView) findViewById(R.id.txt_report_totalContract);
        txt_report_totalRefunded = (TextView) findViewById(R.id.txt_report_totalRefunded);
        txt_report_total_fee = (TextView) findViewById(R.id.txt_report_total_fee);
    }

    private void initLayout() {
        //Use for init spinner
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        userID = editor.getInt(ImmutableValue.HOME_userID, 0);
        final ArrayList<String> listMonth = new ArrayList<>();
        listMonth.add("Xem cả năm");
        listMonth.add("Tháng 1");
        listMonth.add("Tháng 2");
        listMonth.add("Tháng 3");
        listMonth.add("Tháng 4");
        listMonth.add("Tháng 5");
        listMonth.add("Tháng 6");
        listMonth.add("Tháng 7");
        listMonth.add("Tháng 8");
        listMonth.add("Tháng 9");
        listMonth.add("Tháng 10");
        listMonth.add("Tháng 11");
        listMonth.add("Tháng 12");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewReportActivity.this, R.layout.spinner_item, listMonth);
        spn_report_first.setAdapter(adapter);


        final ArrayList<String> listYear = new ArrayList<>();
        listYear.add("2018");
        listYear.add("2019");
        listYear.add("2020");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ViewReportActivity.this, R.layout.spinner_item, listYear);
        spn_report_second.setAdapter(adapter2);

        spn_report_first.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position;
                setupData(month, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_report_second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = Integer.parseInt(spn_report_second.getSelectedItem().toString());
                setupData(month, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Use for pie chart
        pie_chart.setUsePercentValues(false);
        pie_chart.getDescription().setEnabled(false);
        pie_chart.setExtraOffsets(5, 10, 5, 5);
        pie_chart.setDrawHoleEnabled(false);
        pie_chart.setEntryLabelColor(Color.WHITE);
        pie_chart.setEntryLabelTextSize(10f);
        pie_chart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setText("Biểu đồ về trạng thái hợp đồng");
        description.setTextSize(10);
        pie_chart.setDescription(description);
        setupData(month, year);

    }

    private void setupData(int getMonth, int getYear) {
        final ProgressDialog dialog = ProgressDialog.show(ViewReportActivity.this, "Hệ thống",
                "Đang xử lý", true);
        Retrofit retrofit = RetrofitConfig.getClient();
        ReportAPI reportAPI = retrofit.create(ReportAPI.class);
        Call<Report> reportCall = null;
        if (getMonth == 0) {
            reportCall = reportAPI.getReportPerYear(userID, getYear);
        } else {
            reportCall = reportAPI.getReportPerMonth(userID, getYear, getMonth);
        }
        reportCall.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (response.code() == 200) {
                    Report obj = response.body();
                    if (obj.getNumberOfContract() != 0) {
                        ArrayList<PieEntry> pieEntries = new ArrayList<>();
                        if (obj.getNumberOfActiveContract() != 0) {
                            pieEntries.add(new PieEntry(obj.getNumberOfActiveContract(), "Trong tiến trình"));
                        }
                        if (obj.getNumberOfInactiveContract() != 0) {
                            pieEntries.add(new PieEntry(obj.getNumberOfInactiveContract(), "Mới tạo"));
                        }
                        if (obj.getNumberOfFinishedContract() != 0) {
                            pieEntries.add(new PieEntry(obj.getNumberOfFinishedContract(), "Hoàn thành"));
                        }
                        if (obj.getNumberOfIssueContract() != 0) {
                            pieEntries.add(new PieEntry(obj.getNumberOfIssueContract(), "Khiếu nại"));
                        }
                        if (obj.getNumberOfPendingContract() != 0) {
                            pieEntries.add(new PieEntry(obj.getNumberOfPendingContract(), "Chờ xác nhận"));
                        }
                        if (obj.getNumberOfPreFinishContract() != 0) {
                            pieEntries.add(new PieEntry(obj.getNumberOfPreFinishContract(), "Sắp hoàn thành"));
                        }
                        if (obj.getNumberOfRefundedContract() != 0) {
                            pieEntries.add(new PieEntry(obj.getNumberOfRefundedContract(), "Hủy hợp đồng"));
                        }
                        PieDataSet dataSet = new PieDataSet(pieEntries, "");
                        dataSet.setValueFormatter(new DefaultValueFormatter(0));
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        dataSet.setValueLinePart1OffsetPercentage(90.f);
                        dataSet.setSliceSpace(1f);
                        dataSet.setValueTextColor(Color.WHITE);
                        PieData data = new PieData(dataSet);
                        data.setValueTextSize(14f);
                        pie_chart.setVisibility(View.VISIBLE);
                        pie_chart.setData(data);
                        pie_chart.animateY(1000, Easing.EasingOption.EaseInCubic);
                        pie_chart.invalidate();

                        NumberFormat nf = new DecimalFormat("#.####");
                        txt_report_totalMoney.setText(GeneralController.convertPrice(nf.format(obj.getTotalRentFee())));
                        txt_report_totalContract.setText(obj.getNumberOfContract() + "");
                        txt_report_total_fee.setText(GeneralController.convertPrice(nf.format(obj.getTotalOtherFee())));
                        txt_report_totalRefunded.setText(GeneralController.convertPrice(nf.format(obj.getTotalRefunded())));
                    } else {
                        txt_report_totalRefunded.setText("0");
                        txt_report_total_fee.setText("0");
                        txt_report_totalContract.setText("0");
                        txt_report_totalMoney.setText("0");
                        pie_chart.setVisibility(View.INVISIBLE);
                    }
                } else {
                    txt_report_totalRefunded.setText("0");
                    txt_report_total_fee.setText("0");
                    txt_report_totalContract.setText("0");
                    txt_report_totalMoney.setText("0");
                    pie_chart.setVisibility(View.INVISIBLE);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ViewReportActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
