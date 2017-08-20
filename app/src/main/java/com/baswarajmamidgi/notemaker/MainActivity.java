package com.baswarajmamidgi.notemaker;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnSuggestionListener {
    ListView list;
    Mydatabase mydatabase;
    ArrayList<String> contents;
    boolean doubleBackToExitPressedOnce = false;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        mydatabase = new Mydatabase(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newnote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Dataloader.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);

            }
        });

        final Cursor cursor = mydatabase.getdiaries();
        String[] from = {Constants.CONTENT_NAME, Constants.DATETIME};
        int[] to = {R.id.textview, R.id.date};
        list = (ListView) findViewById(R.id.listView);
        CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.adapterview, cursor, from, to, 0);
        list.setAdapter(adapter);
        contents = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            contents.add(cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME)) + "@!@" + cursor.getString(cursor.getColumnIndex(Constants.DATETIME)));
            cursor.moveToNext();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = contents.get(position);
                Intent intent = new Intent(getApplicationContext(), Viewdetails.class);
                intent.putExtra("data", data);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        registerForContextMenu(list);
        adapter.notifyDataSetChanged();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position ;
        String data = contents.get(pos);
        String[] newdata=data.split("@!@");


        if (item.getItemId() == R.id.share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, newdata[0]);
            i.putExtra(Intent.EXTRA_SUBJECT, newdata[1]);
            i.setType("text/plain");
            startActivity(Intent.createChooser(i, "Send using"));

        }

        return true;

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.About) {
            startActivity(new Intent(this, About.class));
        }

        if (id == R.id.alarm) {
            startActivity(new Intent(this, Setalarm.class));
        }
        if (id == R.id.settings) {
            startActivity(new Intent(this,preference.class));
        }
        if (id == R.id.Feedback) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "baswarajmamidgi10@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Notes maker lite Feedback");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        boolean isChecked1 = settings.getBoolean("checkbox1", false);
        boolean isChecked2 = settings.getBoolean("checkbox2", false);
        MenuItem item = menu.findItem(R.id.newest_first);
        item.setChecked(isChecked1);
        MenuItem item2 = menu.findItem(R.id.oldest_first);
        item2.setChecked(isChecked2);
        MenuItem searchitem=menu.findItem(R.id.search);
        searchView= (SearchView) MenuItemCompat.getActionView(searchitem);
        SearchManager manager= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(new ComponentName(this,MainActivity.class)));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnSuggestionListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();


        if (id == R.id.newest_first) {
                editor.putString("sort", " DESC");
                editor.apply();
                Intent i = new Intent(this, MainActivity.class);
                finishAffinity();
                startActivity(i);
                return true;

        }
        if (id == R.id.oldest_first) {
                editor.putString("sort"," ASC" );
                editor.apply();
                Intent i = new Intent(this, MainActivity.class);
                finishAffinity();
                startActivity(i);
                return true;

        }
            return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        int id= (int) searchView.getSuggestionsAdapter().getItemId(position);
        Intent i=new Intent(this,Viewdetails.class);
        String data=contents.get(id);
        i.putExtra("data",data);
        startActivity(i);


        return true;
    }
}
