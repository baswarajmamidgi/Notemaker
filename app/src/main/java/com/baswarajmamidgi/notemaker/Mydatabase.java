package com.baswarajmamidgi.notemaker;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by baswarajmamidgi on 20/07/16.
 */
public class Mydatabase {
    private SQLiteDatabase db;
    private final Context context;
    private final Databasehelper dbhelper;
    public Mydatabase(Context c) {
        context = c;
        dbhelper = new Databasehelper(context);
    }

    public long insertdiary(String content) {
        db = dbhelper.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(Constants.CONTENT_NAME,content);
        contentValues.put(Constants.DATETIME,getDateTime());
        long id=db.insert(Constants.TABLE_NAME,null,contentValues);
        db.close();
        return id;
    }
    public Cursor getdiaries()
    {

        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        String order = settings.getString("sort", " ASC");

        db = dbhelper.getWritableDatabase();
        String[] columns={Constants.KEY_ID, Constants.CONTENT_NAME, Constants.DATETIME};
        Cursor cursor = db.query(Constants.TABLE_NAME,columns,null,null,null,null, Constants.KEY_ID+order);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }


    public  void rowdelete(String data) {
        db=dbhelper.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.CONTENT_NAME+"=?",new String[]{data});
        db.close();
    }
    public int rowupdate(String oldcontent, String newcontent)
    {
        db=dbhelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.CONTENT_NAME,newcontent );
        int id= db.update(Constants.TABLE_NAME,values, Constants.CONTENT_NAME+"=?",new String[]{oldcontent});
        db.close();
        return id;
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yy  HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    public ArrayList<String> getnotes()
    {
        db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME,null,null,null,null,null,null,null);
        cursor.moveToFirst();
        ArrayList<String> arrayList=new ArrayList<>();
        while (!cursor.isAfterLast())
        {
           String name=cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME));
        arrayList.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return arrayList;

    }
}
