package com.mbeargie.videogame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mbeargie on 11/1/2018.
 */

public class HowToPlay extends AppCompatActivity implements View.OnClickListener{

    TextView textView,textView2,textView3,textView4, textView5, textView6;
    Typeface tf;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        AssetManager mngr = getAssets();
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
        textView.setText("Tap the screen to move Lucky upwards.");
        textView2.setText("Help Lucky get to her food bowls...");
        textView3.setText("...But watch out for baby Bennett!");
        textView4.setText("3 missed food bowls and it's Game Over!");
        textView6.setText("<- Back");

        textView6.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        if (v == textView6) {
            //the transition from MainActivity to GameActivity
            startActivity(new Intent(HowToPlay.this, MainActivity.class));
        }
    }
}
