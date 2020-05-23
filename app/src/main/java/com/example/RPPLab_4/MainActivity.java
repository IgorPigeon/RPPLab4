package com.example.RPPLab_4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    CalendarView calendar;
    TextView textDay;

    Calendar currentDate;
    Calendar changeDate;

    AppWidgetManager appWidgetManager;
    int appWidgetIds[];

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmIntent = new Intent(this, NotificationBuilder.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetIds = appWidgetManager.getAppWidgetIds(
                                        new ComponentName
                                            (this.getApplicationContext().getPackageName(),
                                                    NewAppWidgetProvider.class.getName()
                                            )
                                        );

        calendar = (CalendarView) findViewById(R.id.calendarView2);
        textDay = (TextView) findViewById(R.id.textView2);

        currentDate = Calendar.getInstance();
        changeDate = Calendar.getInstance();

        currentDate.setTimeInMillis(calendar.getDate());

        calendar.setMinDate(calendar.getDate());

        calendar.setOnDateChangeListener(new OnDateChangeListener(){
            // Описываем метод выбора даты в календаре:
            @Override
            public void onSelectedDayChange(CalendarView view, int year,int month, int dayOfMonth) {

                changeDate.set(year, month, dayOfMonth);
                long millisecond = changeDate.getTimeInMillis() - currentDate.getTimeInMillis();
                int days = (int)(millisecond/(24*60*60*1000));

                textDay.setText("Days remaining: "+days);

                NewAppWidgetProvider.info(days);
                NewAppWidgetProvider.update(MainActivity.this, appWidgetManager, appWidgetIds);

                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

            }});
    }
}
