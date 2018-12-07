package com.example.jhsbriones.briones;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    TextView txtTimer, txtCorrect, txtIncorrect;
    LinearLayout lytButtons;
    private int counter = 3;
    private int numButtons = 16;

    private ArrayList<Integer> btnID = new ArrayList<Integer>();
    private int[] btnIDs = {R.string.A, R.string.B, R.string.C, R.string.D, R.string.E, R.string.F, R.string.G, R.string.H};
    private int[] initCounts = {0, 0, 0, 0, 0, 0, 0, 0};
    private String[] btnValues = new String[numButtons]; // string array to store original btn values
    private Btns[] btns = new Btns[numButtons];
    private ArrayList<Integer> currentPair = new ArrayList<Integer>();
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
                txtTimer.setText(String.format("%02d", counter));
                counter--;
            }

            @Override
            public void onFinish() {
                txtTimer.setText(String.format("%02d", counter));
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
        Random rand = new Random();
        int randn = 0;
        int count = lytButtons.getChildCount();
        int btnCount = 0;
        int currentIndex = 0;
        View rowLayout = null, currentBtn = null;
        for(int i=0; i < count; i++) { // iterate every button in each linear layout(horizontal)
            rowLayout = (LinearLayout) lytButtons.getChildAt(i);
            btnCount = ((LinearLayout) rowLayout).getChildCount();
            for(int j = 0; j < btnCount; j++) {
                currentBtn = (Button)((LinearLayout) rowLayout).getChildAt(j);

                // store every button to a list
                btnID.add(currentBtn.getId());

                while(true) { // randomizer part
                    randn = rand.nextInt(btnIDs.length);
                    if(initCounts[randn] < 2) {

                        // create new btns instances for each button in recording their statuses
                        currentIndex = 4*i + j;
                        btns[currentIndex] = new Btns(currentBtn.getId());

                        // set value for btn after getting randomized value
                        ((Button) currentBtn).setText(btnIDs[randn]);
                        btnValues[currentIndex] = getApplication().getApplicationContext().getResources().getString(btnIDs[randn]);
                        initCounts[randn]++;
                        break;
                    }
                }
            }
        }

        // debug btn value count
        Toast.makeText(this, TextUtils.join("", btnValues), Toast.LENGTH_SHORT).show();
        Log.d("debugger", TextUtils.join("", btnValues));
//        String a = "";
//        for(int x = 0; x < initCounts.length; x++) {
//            a += String.valueOf(initCounts[x]);
//        }
//        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    public void btnChoice(View v) { // function to be executed when clicked
        int btnIndex= Integer.parseInt(v.getResources().getResourceName(v.getId()).split("_")[1]) - 1;
        if(btns[btnIndex].isNotPicked()) {
            btns[btnIndex].updateStatus(Status.FIRST);
            currentPair.add(btnIndex);

        }
        else if(btns[btnIndex].isFirst()) {
            btns[btnIndex].updateStatus(Status.SECOND);
            currentPair.add(btnIndex);
        }
        else if(btns[btnIndex].isSecond()) {
            btns[btnIndex].updateStatus(Status.MATCHED);
        }
    }

    public boolean checkMatchPair() {

        return false;
    }

    public void resetPair() {
        currentPair.clear();
    }
}
