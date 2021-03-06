package com.mbeargie.videogame;

/**
 * Created by mbeargie on 10/12/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HighScore extends AppCompatActivity implements View.OnClickListener{

    TextView textView,textView2,textView3,textView4,textView5,textView6;
    Typeface tf;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        AssetManager mngr = getAssets();
        tf = Typeface.createFromAsset(mngr,"fonts/witless.ttf");

        tf = Typeface.createFromAsset(mngr,"fonts/witless.ttf");

        //initializing the textViews
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);


        textView.setTypeface(tf);
        textView2.setTypeface(tf);
        textView3.setTypeface(tf);
        textView4.setTypeface(tf);
        textView5.setTypeface(tf);
        textView6.setTypeface(tf);


        textView.setTextColor(Color.parseColor("#0b66f2"));
        textView2.setTextColor(Color.parseColor("#0b66f2"));
        textView3.setTextColor(Color.parseColor("#0b66f2"));
        textView4.setTextColor(Color.parseColor("#0b66f2"));
        textView5.setTextColor(Color.parseColor("#5F686F"));
        textView6.setTextColor(Color.parseColor("#0b66f2"));

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //setting the values to the textViews
        textView.setText("1st. "+sharedPreferences.getInt("score1",0));
        textView2.setText("2nd. "+sharedPreferences.getInt("score2",0));
        textView3.setText("3rd. "+sharedPreferences.getInt("score3",0));
        textView4.setText("4th. "+sharedPreferences.getInt("score4",0));
        textView6.setText("<- Back");

        textView6.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v == textView6) {
            //the transition from MainActivity to GameActivity
            startActivity(new Intent(HighScore.this, MainActivity.class));
        }
    }
}
