package com.baswarajmamidgi.notemaker;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by baswarajmamidgi on 16/12/16.
 */

public class contentprovider extends ContentProvider {
    Mydatabase mydatabase;
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        mydatabase=new Mydatabase(getContext());
        ArrayList<String> arrayList=mydatabase.getnotes();
        MatrixCursor matrixCursor=new MatrixCursor(new String[] {BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID

        });
        if(arrayList!=null)
        {
            String query=uri.getLastPathSegment().toString();
            int limit=Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));
            int length=arrayList.size();
            for(int i=0; i<length && matrixCursor.getCount()<limit;i++)
            {
                String notes=arrayList.get(i);
                if(notes.toLowerCase().contains(query.toLowerCase()))
                {
                    matrixCursor.addRow(new Object[] { i,notes,i});
                }
            }
        }
        return matrixCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
