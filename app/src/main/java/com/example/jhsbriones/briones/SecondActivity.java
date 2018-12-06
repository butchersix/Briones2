package com.example.jhsbriones.briones;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    TextView txtTimer, txtCorrect, txtIncorrect;
    LinearLayout lytButtons;
    private int counter = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        txtTimer = findViewById(R.id.textTimer);;
        txtCorrect = findViewById(R.id.textCorrect);;
        txtIncorrect = findViewById(R.id.textIncorrect);
        lytButtons = findViewById(R.id.layoutButtons);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeButtons();
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                txtTimer.setText(String.valueOf(counter));
                counter--;
            }

            @Override
            public void onFinish() {
                txtTimer.setText(String.valueOf(counter));
                int count = lytButtons.getChildCount();
                int btnCount = 0;
                View rowLayout = null, currentBtn = null;
                for(int i=0; i < count; i++) {
                    rowLayout = (LinearLayout) lytButtons.getChildAt(i);
                    btnCount = ((LinearLayout) rowLayout).getChildCount();
                    for(int j = 0; j < btnCount; j++) {
                        currentBtn = (Button)((LinearLayout) rowLayout).getChildAt(j);
                        ((Button) currentBtn).setText(R.string.unknown);
                    }
                }
            }
        }.start();
    }

    public void initializeButtons() {
        int[] btnIDs = {R.string.A, R.string.B, R.string.C, R.string.D, R.string.E, R.string.F};
        int[] initCounts = {0, 0, 0, 0, 0, 0};
        Random rand = new Random();
        int randn = 0;
        int count = lytButtons.getChildCount();
        int btnCount = 0;
        View rowLayout = null, currentBtn = null;
        for(int i=0; i < count; i++) {
            rowLayout = (LinearLayout) lytButtons.getChildAt(i);
            btnCount = ((LinearLayout) rowLayout).getChildCount();
            for(int j = 0; j < btnCount; j++) {
                String a = "";
                for(int x = 0; x < initCounts.length; x++) {
                    a += String.valueOf(initCounts[x]);
                }
                Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

                currentBtn = (Button)((LinearLayout) rowLayout).getChildAt(j);
                randn = rand.nextInt(btnIDs.length);

                if(initCounts[randn] < 2) {
                    ((Button) currentBtn).setText(btnIDs[randn]);
                    initCounts[randn]++;
                }
                else {
                    for(int k=0; k < initCounts.length; k++) {
                        ((Button) currentBtn).setText(btnIDs[k]);
                        initCounts[k]++;
                        break;
                    }
                }
            }
        }
    }
}
