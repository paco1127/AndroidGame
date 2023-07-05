package com.example.assigment;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.DownloadListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyAsyncTask extends AsyncTask<String,Integer,String> {
    private OnDownloadFinishListener listener;
    private final String TAG="HI";

    public MyAsyncTask(){

    }

    public MyAsyncTask(OnDownloadFinishListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream=null;
        String result="";
        try{
            // get url
            URL url = new URL("https://ranking-mobileasignment-wlicpnigvf.cn-hongkong.fcapp.run");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            inputStream=conn.getInputStream();
            BufferedReader buffer=new BufferedReader(new InputStreamReader(inputStream));

            String line="";
            while ((line=buffer.readLine())!=null){
                result += line;
            }
            Log.d(TAG,"Debug");
            inputStream.close();


        }catch (Exception e){
            result=e.getMessage();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.updateDownloadResult(s);
    }
}

