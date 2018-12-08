package com.example.jhsbriones.briones;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SecondActivity extends AppCompatActivity {

    TextView txtTimer, txtCorrect, txtIncorrect;
    LinearLayout lytButtons;
    private int counter = 3;
    private int correct = 0, wrong = 0;
    private int numButtons = 16;

    private ArrayList<Integer> btnID = new ArrayList<Integer>();
    private int[] btnIDs = {R.string.A, R.string.B, R.string.C, R.string.D, R.string.E, R.string.F, R.string.G, R.string.H};
    private int[] initCounts = {0, 0, 0, 0, 0, 0, 0, 0};
    private String[] btnValues = new String[numButtons]; // string array to store original btn values
    private Btns[] btns = new Btns[numButtons];
    private boolean pending = false, reset_flag = false;
    private int reset_counter = 0, btnIndex;
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
//        Toast.makeText(this, TextUtils.join("", btnValues), Toast.LENGTH_SHORT).show();
        Log.d("debugger", TextUtils.join("", btnValues));
//        String a = "";
//        for(int x = 0; x < initCounts.length; x++) {
//            a += String.valueOf(initCounts[x]);
//        }
//        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    // reset after 2 seconds have passed
    CountDownTimer resetTimer = null;

    public void btnChoice(View v) { // function to be executed when clicked
        btnIndex = Integer.parseInt(v.getResources().getResourceEntryName(v.getId()).split("_")[1]) - 1;
//        Toast.makeText(this, String.valueOf(btnIndex), Toast.LENGTH_SHORT).show()

        if(resetTimer != null)
            resetTimer.cancel();

        if(!pending) {
            btns[btnIndex].updateStatus(Status.PENDING);
            currentPair.add(btnIndex);
            pending = true;
            ((Button) v).setText(btnValues[btnIndex]);
            reset_flag = true;
            refresh();
            reset_flag = false;
//            Toast.makeText(this, btns[btnIndex].getStatus(), Toast.LENGTH_SHORT).show();
        }
        else {
            currentPair.add(btnIndex);
//            Toast.makeText(this, String.format("%d %d", currentPair.get(0), currentPair.get(1)), Toast.LENGTH_SHORT).show();
            ((Button) v).setText(btnValues[btnIndex]);
//            if(checkMatchSelf()) {
//                updateDefault();
//                refresh();
//                resetPair();
//                reset_flag = true;
//            }
            if(checkMatchPair()) {
                updateBtnPairColor(Status.MATCHED);
                refresh();
                correct++;
                txtCorrect.setText(String.format("%02d", correct));
                resetPair();
            }
            else {
                updateBtnPairColor(Status.WRONG);
                refresh();
                wrong++;
                txtIncorrect.setText(String.format("%02d", wrong));
                resetPair();
                resetTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        reset_flag = true;
                        refresh();
                        reset_flag = false;
                    }
                }.start();
            }
//            Toast.makeText(SecondActivity.this, btns[btnIndex].getStatus(), Toast.LENGTH_SHORT).show();
            pending = false;
        }
    }
    
    public void refresh() {
        View currentBtn = null;
        for (Btns i : btns) {
            currentBtn = findViewById(i.getButtonID());
            if(i.isCheckStatus(Status.NOT_PICKED)) {
                currentBtn.setBackgroundColor(getResources().getColor(R.color.colorGray2));
            }
            else if(i.isCheckStatus(Status.PENDING)) {
                currentBtn.setBackgroundColor(getResources().getColor(R.color.colorHover));
            }
            else if(i.isCheckStatus(Status.WRONG)) {
                currentBtn.setBackgroundColor(getResources().getColor(R.color.colorRed));
                if(reset_flag) {
                    ((Button) currentBtn).setText(R.string.unknown);
                    currentBtn.setBackgroundColor(getResources().getColor(R.color.colorGray2));
                    i.updateStatus(Status.NOT_PICKED);
                }
            }
            else if(i.isCheckStatus(Status.MATCHED)) {
                currentBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            }
        }
    }

    public void updateBtnPairColor(Status status) {
        View currentBtn = null;
        for (int i : currentPair) {
            currentBtn = findViewById(btnID.get(i));
            if(status == Status.MATCHED) {
                currentBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                currentBtn.setEnabled(false);
            }
            else {
                currentBtn.setBackgroundColor(getResources().getColor(R.color.colorRed));
            }
        }
    }

    public void updateDefault() {
        View currentBtn = null;
        currentBtn = findViewById(btnID.get(currentPair.get(0)));
        currentBtn.setBackgroundColor(getResources().getColor(R.color.colorGray2));
    }

    public boolean checkMatchPair() {
        if(btnValues[currentPair.get(0)] == btnValues[currentPair.get(1)]) {
            for ( int i : currentPair) {
                btns[i].updateStatus(Status.MATCHED);
            }
            return true;
        }
        else {
            for ( int i : currentPair) {
                btns[i].updateStatus(Status.WRONG);
            }
            return false;
        }
    }

    public boolean checkMatchSelf() {
        if(currentPair.get(0) == currentPair.get(1)) {
            btns[currentPair.get(0)].updateStatus(Status.NOT_PICKED);
            return true;
        }
        else {
            btns[currentPair.get(0)].updateStatus(Status.NOT_PICKED);
            return false;
        }
    }

    public void resetPair() {
        currentPair.clear();
    }
}
