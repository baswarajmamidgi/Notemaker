package com.baswarajmamidgi.notemaker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by baswarajmamidgi on 20/07/16.
 */
public class Databasehelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE="create table "+ Constants.TABLE_NAME+" ("+
            Constants.KEY_ID+" integer primary key autoincrement, "+ Constants.CONTENT_NAME+" text not null, "+ Constants.DATETIME+" text not null);";
    public Databasehelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
