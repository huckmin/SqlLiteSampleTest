package com.example.thefuuser.sqllitesampletest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.thefuuser.sqllitesampletest.dontfocus.ListData;
import com.example.thefuuser.sqllitesampletest.dontfocus.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button saveDataBt, searchTextBt, updateBt, deleteBt;
    private EditText titleText, subTitleText, searchText;
    private ListView dataListView;


    private FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveDataBt = findViewById(R.id.button);
        searchTextBt = findViewById(R.id.searchBt);
        titleText = findViewById(R.id.editText);
        subTitleText = findViewById(R.id.editText2);
        searchText = findViewById(R.id.searchEditText);
        dataListView = findViewById(R.id.listView);
        updateBt = findViewById(R.id.updateBt);
        deleteBt = findViewById(R.id.deleteBt);

        saveDataBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataFunc();
            }
        });

        searchTextBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                readDataFunc();
            }
        });

        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFunc();
            }
        });

        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataFunc();
            }
        });

    }

    //save db data
    public void saveDataFunc(){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String title = titleText.getText().toString();
        String subtitle = subTitleText.getText().toString();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        readDataFunc();
    }

    //read info from db
    public void readDataFunc(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
//        String[] selectionArgs = { "My Title" };
        String[] selectionArgs = { "%"+ searchText.getText().toString()+ "%" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        ArrayList itemIds = new ArrayList<ListData>();
        while(cursor.moveToNext()) {
            ListData itemId = new ListData(
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE))
            );
            itemIds.add(itemId);
        }
        cursor.close();

        ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext(),itemIds);

        dataListView.setAdapter(listViewAdapter);

    }

    private void deleteDataFunc(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {  titleText.getText().toString() };
        // Issue SQL statement.
        int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void updateFunc(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // New value for one column
        String title = titleText.getText().toString();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);

        // Which row to update, based on the title
        String selectionString = searchText.getText().toString();
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { selectionString };

        int count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

}
