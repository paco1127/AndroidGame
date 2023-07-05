package com.example.assigment;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class g_rank extends AppCompatActivity implements OnDownloadFinishListener{
    @Override
    public void onBackPressed() {
    }
    private ListView lvRank;
    private ArrayAdapter<String> adapter;
    MyAsyncTask task=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank);
        Button btnback = findViewById(R.id.back);


        lvRank = findViewById(R.id.record);
        task=new MyAsyncTask();
        new MyAsyncTask(this).execute();
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void updateDownloadResult(String result) {
        // Parse the JSON array
        try {
            JSONArray jsonArray = new JSONArray(result);

            // Extract the required information and add it to the adapter
            List<JSONObject> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getJSONObject(i));
            }

            // Sort the list of objects by the "Moves" field in ascending order
            Collections.sort(list, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject jsonObject1, JSONObject jsonObject2) {
                    int moves1 = jsonObject1.optInt("Moves", 0);
                    int moves2 = jsonObject2.optInt("Moves", 0);
                    return Integer.compare(moves1, moves2);
                }
            });

            List<String> stringList = new ArrayList<>();
            int place=1;
            for (JSONObject jsonObject : list) {
                String name = jsonObject.optString("Name", "");
                int moves = jsonObject.optInt("Moves", 0);
                stringList.add("Rank " + place + ", " +name + ", " + moves + " moves");
                place++;
            }


            // Set the adapter to the ListView
            adapter = new ArrayAdapter<>(g_rank.this, android.R.layout.simple_list_item_1, stringList);
            lvRank.setAdapter(adapter);
        } catch (JSONException e) {
            e.getMessage();
        }
    }
}


