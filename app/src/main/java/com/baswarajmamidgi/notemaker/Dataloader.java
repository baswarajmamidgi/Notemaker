package com.baswarajmamidgi.notemaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Dataloader extends AppCompatActivity {
    EditText editcontent;
    Mydatabase mydatabase=new Mydatabase(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataloader);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   New Note");
        editcontent = (EditText) findViewById(R.id.editText2);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value=preferences.getString("list_preference_size","18");
        String color=preferences.getString("list_preference_color","#000000");
        editcontent.setTextColor(Color.parseColor(color));
        editcontent.setTextSize(Float.valueOf(value));


        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
      public String getdata() {

          return (editcontent.getText().toString());
      }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.data_menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            String data=this.getdata();
            if(!data.isEmpty()) {
                mydatabase.insertdiary(data);
            }
                Intent i = new Intent(this, MainActivity.class);
            finishAffinity();
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
      String data=  editcontent.getText().toString();
        if(!data.isEmpty()) {
            mydatabase.insertdiary(data);
        }
        finishAffinity();
        startActivity(new Intent(this,MainActivity.class));
        super.onBackPressed();
    }
}
