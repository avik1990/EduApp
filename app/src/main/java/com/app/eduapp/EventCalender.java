package com.app.eduapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.app.eduapp.adapter.EventsByDateAdapter;
import com.app.eduapp.common.ClickEventLisener;
import com.app.eduapp.customcalender.CaldroidFragment;
import com.app.eduapp.customcalender.CaldroidListener;
import com.app.eduapp.databinding.ActivityCalenderIncBinding;
import com.app.eduapp.helper.ConnectionDetector;
import com.app.eduapp.helper.EdUtils;
import com.app.eduapp.pojo.EventsByDate;
import com.app.eduapp.retrofit.api.ApiServices;
import com.app.eduapp.sqlitedb.LCDatabaseHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventCalender extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, ClickEventLisener {

    ActivityCalenderIncBinding binding;
    Context mContext;
    ProgressDialog pDialog;
    ConnectionDetector cd;
    EventsByDate getSchoolDetails;
    EventsByDateAdapter adapter;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    String selected_date = "", selected_date_clicked = "";
    String splitted[];
    String selected_year = "";
    String selected_month = "";
    String year_month = "";
    Date date;
    LCDatabaseHandler db;

    private CaldroidFragment caldroidFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        cd = new ConnectionDetector(mContext);
        db = new LCDatabaseHandler(mContext);
        initview();

        selected_date = EdUtils.getCurrentFormattedDate();
        Log.d("asdasdasd", selected_date);

        splitted = selected_date.split("-");
        selected_year = splitted[0];
        selected_month = splitted[1];

        initCalender(savedInstanceState);


    }

    private void initCalender(Bundle savedInstanceState) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        //    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        //  }2018-08-19  1970-01-01

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }


        //  setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                //  Toast.makeText(getApplicationContext(), formatter.format(date), Toast.LENGTH_SHORT).show();

                selected_date_clicked = formatter.format(date);
                //EdUtils.showToastLong(mContext,selected_date_clicked);
                //  }2018-08-19  1970-01-01
                getEventsByDate();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                //   Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                year_month = year + "-" + month;
                Log.d("sfdsfdsdfsdf", year_month);
                binding.activityCalender.vData.setVisibility(View.GONE);
                getEventsByMonth();
            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    //  Toast.makeText(getApplicationContext(),                            "Caldroid view is created", Toast.LENGTH_SHORT)F                            .show();

                    // getEventsByMonth();

                }
            }

        };

        caldroidFragment.setCaldroidListener(listener);


        if (cd.isConnected()) {
            year_month = selected_year + "-" + selected_month;
            getEventsByMonth();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        getnoticount();
    }

    private void getnoticount() {
        long count = db.getNotificationCount();
        binding.activityCalender.toolbar.tvCartcount.setSolidColor("#3de051");
        binding.activityCalender.toolbar.tvCartcount.setText(String.valueOf(count));
    }


    private void initview() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calender_inc);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        binding.activityCalender.toolbar.btnMenu.setOnClickListener(this);
        binding.activityCalender.toolbar.tvChild.setText("Events");
        binding.activityCalender.toolbar.btnMenu.setOnClickListener(this);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        final NumberPicker pickerWidth = new NumberPicker(this);
        pickerWidth.setMinValue(24);
        pickerWidth.setMaxValue(64);
        pickerWidth.setWrapSelectorWheel(false);
        pickerWidth.setValue(50);
        final NumberPicker pickerHeight = new NumberPicker(this);
        pickerHeight.setMinValue(24);
        pickerHeight.setMaxValue(64);
        pickerHeight.setWrapSelectorWheel(false);
        pickerHeight.setValue(25);
        layout.addView(pickerWidth);
        layout.addView(pickerHeight);


     /*   binding.activityCalender.calendarView.setTileSize(-1);
        binding.activityCalender.calendarView.setTileWidthDp(50);
        binding.activityCalender.calendarView.setTileHeightDp(25);
        binding.activityCalender.calendarView.setOnDateChangedListener(this);
        binding.activityCalender.calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        binding.activityCalender.toolbar.cartView.setOnClickListener(this);
        binding.activityCalender.calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {

            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
              *//*  year_month = calendarDay.getYear() + "-" + calendarDay.getMonth();
                Log.d("sfdsfdsdfsdf", year_month);
                binding.activityCalender.vData.setVisibility(View.GONE);
                getEventsByMonth();*//*
                *//***************************************//*//////////////////////
            }
        });*/
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

   /* @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Log.d("Dateeee", "" + date.getDate());

        selected_date_clicked = EdUtils.getFormattedDate_event(FORMATTER.format(date.getDate()));
        //EdUtils.showToastLong(mContext,selected_date_clicked);
        //  }
        getEventsByDate();
    }*/


    private void setDateSelected() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < getSchoolDetails.getData().getEventsList().size(); i++) {
          //  binding.activityCalender.calendarView.setDateSelected(toCalendar(format.parse(getSchoolDetails.getData().getEventsList().get(i).getEventsStartDate())), true);

            Log.d("dadadadad", getSchoolDetails.getData().getEventsList().get(i).getEventsStartDate());


            Date date = format.parse(getSchoolDetails.getData().getEventsList().get(i).getEventsStartDate());

            cal.setTime(date);
            Date blueDate = cal.getTime();
            if (caldroidFragment != null) {
                ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
                caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
                caldroidFragment.setTextColorForDate(R.color.white, blueDate);
                caldroidFragment.refreshView();
            }
        }
    }

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
            ColorDrawable green = new ColorDrawable(Color.GREEN);
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        EdUtils.openNavDrawer(id, mContext);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.activityCalender.toolbar.btnMenu) {
            binding.drawerLayout.openDrawer(Gravity.START);
        } else if (v == binding.activityCalender.toolbar.cartView) {
            Intent i = new Intent(mContext, NotificationListActivity.class);
            startActivity(i);
        }
    }


    private void getEventsByDate() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d("sdfsfdfdssdf", selected_date_clicked);
        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<EventsByDate> call = redditAPI.getEventsByDate(selected_date_clicked);
        call.enqueue(new Callback<EventsByDate>() {

            @Override
            public void onResponse(Call<EventsByDate> call, retrofit2.Response<EventsByDate> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getSchoolDetails = response.body();
                    if (getSchoolDetails.getStatus().equalsIgnoreCase("1")) {
                        if (getSchoolDetails.getData().getEventsList().size() > 0) {
                            binding.activityCalender.vData.setVisibility(View.VISIBLE);
                            showList();
                        } else {
                            binding.activityCalender.vData.setVisibility(View.GONE);
                        }
                    } else {
                        binding.activityCalender.vData.setVisibility(View.GONE);
                        EdUtils.showToastShort(mContext, getSchoolDetails.getMessage());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EventsByDate> call, Throwable t) {
                Log.d("Error", t.getMessage());
                pDialog.dismiss();
            }
        });
    }

    private void getEventsByMonth() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<EventsByDate> call = redditAPI.getEventsByMonth(year_month);
        call.enqueue(new Callback<EventsByDate>() {

            @Override
            public void onResponse(Call<EventsByDate> call, retrofit2.Response<EventsByDate> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getSchoolDetails = response.body();
                    if (getSchoolDetails.getStatus().equalsIgnoreCase("1")) {
                        if (getSchoolDetails.getData().getEventsList().size() > 0) {
                            try {
                                setDateSelected();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EventsByDate> call, Throwable t) {
                Log.d("Error", t.getMessage());
                pDialog.dismiss();
            }
        });
    }


    private void showList() {
        binding.activityCalender.tvDate.setText(selected_date_clicked);
        adapter = new EventsByDateAdapter(mContext, getSchoolDetails.getData().getEventsList(), this);
        binding.activityCalender.recylerview.setLayoutManager(new LinearLayoutManager(this));
        /*RecyclerViewMargin decoration = new RecyclerViewMargin(15, getSchoolDetails.getData().getEventsList().size());
        binding.activityCalender.recylerview.addItemDecoration(decoration);*/
        binding.activityCalender.recylerview.setAdapter(adapter);
    }

    @Override
    public void clickTrigger(View v, int position) {

    }
}