package com.baswarajmamidgi.notemaker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Setalarm extends AppCompatActivity {
    TimePicker timePicker;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    int a,b;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setalarm);
        timePicker= (TimePicker) findViewById(R.id.timePicker);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Alarm");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        preferences=getSharedPreferences("settings",0);
        editor=preferences.edit();



    }
    public void onsetAlarm(View v)
    {
        boolean state=preferences.getBoolean("switch",true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            a = timePicker.getHour();
            b = timePicker.getMinute();
        }

        else {
            a = timePicker.getCurrentHour();
            b=timePicker.getCurrentMinute();

        }

        Calendar calendar=Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY,a);
        calendar.set(calendar.MINUTE,b);
        Intent intent=new Intent(getApplicationContext(),notificationreceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if(state==true)
        {
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            Toast.makeText(this,"Alarm setted",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_menu, menu);

        final MenuItem toggleservice = menu.findItem(R.id.switch1);
        Switch actionView = (Switch) toggleservice.getActionView();
        boolean state=preferences.getBoolean("switch",true);
        actionView.setChecked(state);
        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("log",String.valueOf(isChecked));

                if(isChecked==false) {
                    editor.putBoolean("switch",false);
                    editor.apply();
                    alarmManager.cancel(pendingIntent);
                    Log.i("log", "cancelled alarm");
                }
                else
                {
                    editor.putBoolean("switch",true);
                    editor.apply();

                }



            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    }