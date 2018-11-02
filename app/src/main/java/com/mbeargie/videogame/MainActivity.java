package com.mbeargie.videogame;

        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.media.Image;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //image button
    private ImageButton buttonPlay;
    //high score button
    private ImageButton buttonScore;

    private ImageButton buttonHow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //getting the button
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);

        //initializing the highscore button
        buttonScore = (ImageButton) findViewById(R.id.buttonScore);

        buttonHow = (ImageButton) findViewById(R.id.buttonHow);


        //setting the on click listener to high score button
        buttonScore.setOnClickListener(this);
        //adding a click listener
        buttonPlay.setOnClickListener(this);

        buttonHow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == buttonPlay) {
            //the transition from MainActivity to GameActivity
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        }
        if (v == buttonScore) {

            //the transition from MainActivity to HighScore activity
            startActivity(new Intent(MainActivity.this, HighScore.class));
        }

        if (v == buttonHow) {

            //the transition from MainActivity to HighScore activity
            startActivity(new Intent(MainActivity.this, HowToPlay.class));
        }
    }
}
