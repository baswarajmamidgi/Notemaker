package com.baswarajmamidgi.notemaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Viewdetails extends AppCompatActivity {
    EditText text;
    Mydatabase mydatabase;
    String[] datareceived;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdetails);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        text= (EditText) findViewById(R.id.editText3);
        mydatabase=new Mydatabase(this);
        Intent i=this.getIntent();
        String data=i.getStringExtra("data");
        datareceived=data.split("@!@");
        getSupportActionBar().setTitle(datareceived[1]);
        text.setText(datareceived[0]);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value=preferences.getString("list_preference_size","18");
        String color=preferences.getString("list_preference_color","#000000");
        text.setTextColor(Color.parseColor(color));
        text.setTextSize(Float.valueOf(value));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       getMenuInflater().inflate(R.menu.viewmenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        int id=menuItem.getItemId();
        if(id==R.id.delete)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Do you want to delete ?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "yes",
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int id) {
                            mydatabase.rowdelete(datareceived[0]);
                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                            finishAffinity();
                            startActivity(i);
                            finish();
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        if(id==R.id.update)
        {
            String newdata=text.getText().toString();
            mydatabase.rowupdate(datareceived[0],newdata);
            Intent i=new Intent(this,MainActivity.class);
            finishAffinity();
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);

    }


}
