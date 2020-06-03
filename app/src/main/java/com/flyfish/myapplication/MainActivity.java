package com.flyfish.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flyfish.xposed.R;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Intent intent = new Intent();
        //        intent.setClass(this,MyAccessibility.class);
        //        startService(intent);
        try {
            log("asdasdsad");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
    }

    private void log(final String msg) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new OkHttpClient().newCall(new Request.Builder().url("http://172.17.92.97:8080/logs")
                            .post(new FormBody.Builder().add("log", msg).build()).build()).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public void click(View v) {
//        String json = new Gson().toJson(new SearchInfo("iphone"));
//        HashMap<String, String> p = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {
//        }.getType());
//        Log.e("sd", new Gson().toJson(p));

        //

    }

}
