package com.example.assigment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class p_record extends AppCompatActivity {
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        Button btnback = findViewById(R.id.back);

        ArrayList<String> dataList = new ArrayList<String>();
        Cursor cursor;
        ListView lvrecord = findViewById(R.id.record);
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.assigment/AssigmentDB", null, SQLiteDatabase.OPEN_READONLY);

        String[] columns ={"playDate", "playTime", "moves", "difficulty", "score"};
        cursor = db.query("record", columns, null,null,null,null,null);



        if (cursor.getCount() == 0){
            dataList.add("There is NO record");
        } else {
            while (cursor.moveToNext()) {
                // Extract data from the cursor
                String playDate = cursor.getString(0);
                String playTime = cursor.getString(1);
                String moves = cursor.getString(2);
                String difficulty = cursor.getString(3);
                String score = cursor.getString(4);

                // Create the main item string
                String mainItem = "Difficulty: " + difficulty + ", Play Time: " + playTime;

                // Create the sub-item string
                String subItem = "Date: " + playDate + ", Moves: " + moves + ", Score: " + score;

                String listItem = mainItem + "\n" + subItem;

                dataList.add(listItem);
            }
            cursor.close();
            db.close();
        }

// Convert the data list to an array
        String[] dataarray = dataList.toArray(new String[0]);

// Set up the adapter with a custom layout
        lvrecord.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, dataarray));

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}