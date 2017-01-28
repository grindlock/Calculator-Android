package com.example.sergio.calculator_android;

import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class Calc_Screen extends AppCompatActivity {

    public static String DEFAULT_TXT = "0.0";
    public static String NUM_SOUND = "keyboard_key";
    public static String OPER_SOUND = "keyboard_tap";

    TextView screen;

    Button dot;

    public String txtOnScreen = "";
    public String leftNum = "";
    public String rightNum = "";


    public enum Operations{
        ADD, SUBTRACTION, DIVISION, MULTIPLICATION, EQUAL, EMPTY
    }

    Operations curOper;
    int result = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_screen);

        screen = (TextView) findViewById(R.id.screen);
        screen.setText(DEFAULT_TXT);

        dot = (Button)findViewById(R.id.pointBtn);
        //Here you attach the onClick function by code
        dot.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //if the numbers store doesnt have a dot in there continue
                if(!txtOnScreen.contains(".")) {
                    //if the txt in the screen equal 0.0 change the variable to 0. otherwise add a dot.
                    txtOnScreen += screen.getText() == DEFAULT_TXT ? "0." : ".";
                    screen.setText(txtOnScreen);
                    playSound(NUM_SOUND);
                }
            }
        });


    }

    //Here you attach the onClick function in the xml
    public void numbersBtnPressed(View view){
        if(screen.getText().length() < 9){
            int tag = Integer.parseInt(view.getTag().toString());

            if(tag >= 0 && tag < 10){
                txtOnScreen += tag;
            }
        }
        screen.setText(txtOnScreen);
        playSound(NUM_SOUND);
    }

    public void clearBtnPressed(View view){
        txtOnScreen = "";
        leftNum = "";
        rightNum = "";
        result = 0;
        curOper = null;
        screen.setText(DEFAULT_TXT);
        playSound(OPER_SOUND);
    }

    public void chnageSignBtnPressed(View view){
        txtOnScreen = screen.getText().toString();

        if(!txtOnScreen.equals("0")|| !txtOnScreen.equals("0.") || !txtOnScreen.equals(DEFAULT_TXT)){
            if(txtOnScreen.endsWith(".0")) {
                float n = Float.parseFloat(txtOnScreen) * -1;
                txtOnScreen = String.valueOf(n);
            }
            else{
                int n = Integer.parseInt(txtOnScreen) * -1;
                txtOnScreen = String.valueOf(n);
            }

            screen.setText(txtOnScreen);
            playSound(OPER_SOUND);
        }
    }

    public void percentBtnPressed(View view){
        if(txtOnScreen != null){
            float n = Float.parseFloat(txtOnScreen) / 100;
            txtOnScreen = String.valueOf(n);
            screen.setText(txtOnScreen);
            playSound(OPER_SOUND);
        }
    }

    public void division(View view){
        processOperation(Operations.DIVISION);

        playSound(OPER_SOUND);
    }
    public void multiplication(View view){
        processOperation(Operations.MULTIPLICATION);
        playSound(OPER_SOUND);
    }
    public void subtraction(View view){
        processOperation(Operations.SUBTRACTION);
        playSound(OPER_SOUND);
    }
    public void addition(View view){
        processOperation(Operations.ADD);
        playSound(OPER_SOUND);
    }
    public void equal(View view){
        processOperation(Operations.EQUAL);

        playSound(OPER_SOUND);
    }

    public void processOperation(Operations operation){
        if(curOper != null){
            if(txtOnScreen != null ){
                rightNum = txtOnScreen;
                txtOnScreen = "";

                switch (curOper){
                    case ADD:
                        result = Integer.parseInt(leftNum) + Integer.parseInt(rightNum);
                        break;
                    case SUBTRACTION:
                        result = Integer.parseInt(leftNum) - Integer.parseInt(rightNum);
                        break;
                    case DIVISION:
                        result = Integer.parseInt(leftNum) / Integer.parseInt(rightNum);
                        break;
                    case MULTIPLICATION:
                        result = Integer.parseInt(leftNum) * Integer.parseInt(rightNum);
                        break;
                }
                leftNum = String.valueOf(result);
                screen.setText(leftNum);
            }
        }
        else{
            leftNum = txtOnScreen;
            txtOnScreen = "";
        }
        curOper = operation;
    }


    //This function plays the sound send
    public void playSound(String audio){

        Uri path = Uri.parse("android.resource://"+getPackageName()+"/raw/"+audio);

        Log.v("SOUND", path.toString());

        final MediaPlayer audioPlayer = MediaPlayer.create(this, path);
        audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //This make sure to realease this resource before is use again.
        audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp){
                mp.release();
            }
        });

        audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp){
                mp.start();
            }
        });
    }

}
