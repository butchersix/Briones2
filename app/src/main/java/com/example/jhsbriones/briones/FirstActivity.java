package com.example.jhsbriones.briones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void startGame(View w) {
        startActivity(new Intent(FirstActivity.this, ThirdActivity.class));
    }

    public void quit(View v) {
        finish();
    }
}
