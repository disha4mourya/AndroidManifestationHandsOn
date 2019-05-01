package com.example.androidmanifestation.server_calls.other_ways;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidmanifestation.R;

public class ServerCallOptions extends AppCompatActivity implements View.OnClickListener {


    Button btnNoLibrary;
    Button btnLoopj;
    Button btnVolley;
    Button btnOkhttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_call_options);

        btnNoLibrary = findViewById(R.id.btnNoLibrary);
        btnLoopj = findViewById(R.id.btnLoopj);
        btnVolley = findViewById(R.id.btnVolley);
        btnOkhttp = findViewById(R.id.btnOkhttp);


        btnNoLibrary.setOnClickListener(this);
        btnLoopj.setOnClickListener(this);
        btnVolley.setOnClickListener(this);
        btnOkhttp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNoLibrary: {
                Intent intent = new Intent(this, NoLibraryCallActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnLoopj: {
                Intent intent = new Intent(this, LoopjCallActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnVolley: {
                Intent intent = new Intent(this, VolleyServerCall.class);
                startActivity(intent);
                break;
            }
            case R.id.btnOkhttp: {
                Intent intent = new Intent(this, OkhttpCallActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
