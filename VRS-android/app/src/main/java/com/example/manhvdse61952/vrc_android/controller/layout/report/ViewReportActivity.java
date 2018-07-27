package com.example.manhvdse61952.vrc_android.controller.layout.report;

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

import com.example.manhvdse61952.vrc_android.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity {
    Spinner spn_report_first, spn_report_second;
    TextView txt_report_totalMoney;
    PieChart pie_chart;

    int finishedContract = 0, refundedContract = 0, inactiveContract = 0,
    activeContract = 0,issueContract = 0, pendingContract = 0, prefinishedContract = 0;
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

    private void declareID(){
        pie_chart = (PieChart)findViewById(R.id.pie_chart);
        spn_report_first = (Spinner)findViewById(R.id.spn_report_first);
        spn_report_second = (Spinner)findViewById(R.id.spn_report_second);
        txt_report_totalMoney = (TextView)findViewById(R.id.txt_report_totalMoney);
    }

    private void initLayout(){
        //Use for init spinner
        ArrayList<String> listSpn1 = new ArrayList<>();
        listSpn1.add("Tháng");
        listSpn1.add("Quý");
        listSpn1.add("Năm");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewReportActivity.this, R.layout.spinner_item, listSpn1);
        spn_report_first.setAdapter(adapter);

        final ArrayList<String> listMonth = new ArrayList<>();
        listMonth.add("1");
        listMonth.add("2");
        listMonth.add("3");
        listMonth.add("4");
        listMonth.add("5");
        listMonth.add("6");
        listMonth.add("7");
        listMonth.add("8");
        listMonth.add("9");
        listMonth.add("10");
        listMonth.add("11");
        listMonth.add("12");

        final ArrayList<String> listQuater = new ArrayList<>();
        listQuater.add("I");
        listQuater.add("II");
        listQuater.add("III");
        listQuater.add("IV");

        final ArrayList<String> listYear = new ArrayList<>();
        listYear.add("2018");
        listYear.add("2019");
        listYear.add("2020");

        spn_report_first.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewReportActivity.this, R.layout.spinner_item, listMonth);
                    spn_report_second.setAdapter(adapter);
                } else if (position == 1){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewReportActivity.this, R.layout.spinner_item, listQuater);
                    spn_report_second.setAdapter(adapter);
                } else if (position == 2){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewReportActivity.this, R.layout.spinner_item, listYear);
                    spn_report_second.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_report_second.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupData();
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
        setupData();

    }

    private void setupData(){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(10, "Hoàn thành"));
        pieEntries.add(new PieEntry(5, "Sắp hoàn thành"));
        pieEntries.add(new PieEntry(3, "Mới tạo"));
        pieEntries.add(new PieEntry(4, "Trong tiến trình"));
        pieEntries.add(new PieEntry(2, "Hủy hợp đồng"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setValueFormatter(new DefaultValueFormatter(0));
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueLinePart1OffsetPercentage(90.f);
        dataSet.setSliceSpace(1f);
        dataSet.setValueTextColor(Color.WHITE);
//        dataSet.setValueLinePart1Length(.7f);
//        dataSet.setValueLineWidth(1f);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        pie_chart.setData(data);
        pie_chart.animateY(1000, Easing.EasingOption.EaseInCubic);
        pie_chart.invalidate();
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
