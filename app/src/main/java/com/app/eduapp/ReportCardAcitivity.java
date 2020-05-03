package com.app.eduapp;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.app.eduapp.adapter.ReportCardAdapter;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.common.RecyclerViewMargin;
import com.app.eduapp.databinding.ActivityReportCardAcitivityBinding;
import com.app.eduapp.pojo.ReportCardBean;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import java.util.ArrayList;
import java.util.List;


public class ReportCardAcitivity extends AppCompatActivity implements ClickEventLisener {
    LCDatabaseHandler db;
    ActivityReportCardAcitivityBinding binding;
    List<ReportCardBean> reportCardBeanList;
    ReportCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil
                .setContentView(this, R.layout.activity_report_card_acitivity);
        initView();
        showList();
    }

    private void initView() {
        binding.examName.setText("Annual Exam");
    }

    private void showList() {
        adapter = new ReportCardAdapter(this, dataEntry(), this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMargin decoration = new RecyclerViewMargin(15, dataEntry().size());
        binding.recyclerView.addItemDecoration(decoration);
        binding.recyclerView.setAdapter(adapter);
    }

    private List<ReportCardBean> dataEntry() {
        reportCardBeanList = new ArrayList<>();
        reportCardBeanList.add(new ReportCardBean("BioLogy", "25/08/2018", "30", "80", "55", "55"));
        reportCardBeanList.add(new ReportCardBean("History", "25/08/2018", "30", "80", "55", "55"));
        reportCardBeanList.add(new ReportCardBean("Science", "25/08/2018", "30", "80", "55", "55"));
        reportCardBeanList.add(new ReportCardBean("Bengali", "25/08/2018", "30", "80", "55", "55"));
        reportCardBeanList.add(new ReportCardBean("Computer", "25/08/2018", "30", "80", "55", "55"));
        reportCardBeanList.add(new ReportCardBean("Computer Science", "25/08/2018", "30", "80", "55", "55"));


        return reportCardBeanList;
    }

    @Override
    public void clickTrigger(View v, int position) {

    }
}