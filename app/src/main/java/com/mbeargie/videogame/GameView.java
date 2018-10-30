package com.mbeargie.videogame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by mbeargie on 10/11/2018.
 */

public class GameView extends SurfaceView implements Runnable{

    //boolean variable to track if the game is playing or not
    volatile boolean playing;
    //the game thread
    private Thread gameThread = null;
    //adding the player to this class
    private Player player;
    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    //Adding enemies object array
    private Enemy enemies;
    private Background background;

    //Adding an stars list
    private ArrayList<Star> stars = new
            ArrayList<Star>();
    //created a reference of the class Friend
    private Friend friend;
    //defining a boom object to display blast
    private Boom boom;
    //Class constructor
    //a screenX holder
    int screenX;

    int ROTATE_TIME_MILLIS = 7000;
    //to count the number of Misses
    int countMisses;
    //indicator that the enemy has just entered the game screen
    boolean flag ;
    //an indicator if the game is Over
    private boolean isGameOver ;
    //the score holder
    int score;
    //the high Scores Holder
    int highScore[] = new int[4];
    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;

    Background backgrounds;

    // Holds a reference to the Activity
    Context context;

    // Control the fps
    long fps =60;
    Matrix matrix = new Matrix();

    Bitmap game_over;
    Bitmap play_again;

    int bitmapXPosition;
    int bitmapYPosition;


    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.context = context;

        backgrounds = new Background(
                this.context,
                screenX,
                screenY,
                "kitchen",  0, 104, 50);


        //setting the score to 0 initially
        score = 0;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);

        //initializing the array high scores with the previous values
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);

        player = new Player(context, screenX, screenY);

        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s  = new Star(screenX, screenY);
            stars.add(s);
        }

        enemies = new Enemy(context, screenX, screenY);

        boom = new Boom(context);

        friend = new Friend(context, screenX, screenY);

        this.screenX = screenX;

        countMisses = 0;

        isGameOver = false;

        game_over = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover);
        play_again = BitmapFactory.decodeResource(context.getResources(), R.drawable.playagain);


        game_over = Bitmap.createScaledBitmap(game_over,
                game_over.getWidth() / 2,
                game_over.getHeight() / 2,
                false);

        play_again = Bitmap.createScaledBitmap(play_again,
                play_again.getWidth() / 3,
                play_again.getHeight() / 3,
                false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if(isGameOver == false) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    //stopping the boosting when screen is released
                    player.stopBoosting();
                    break;
                case MotionEvent.ACTION_DOWN:
                    //boosting the space jet when screen is pressed
                    player.setBoosting();
                    break;
            }
        } else {

            float x = motionEvent.getX();
            float y = motionEvent.getY();


            switch(motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    //Check if the x and y position of the touch is inside the bitmap
                    if( x > bitmapXPosition && x < bitmapXPosition + play_again.getWidth() && y > bitmapYPosition && y < bitmapYPosition + play_again.getHeight() )
                    {
                        ((GameActivity)context).finish();
                        Intent intent = new Intent().setClass(getContext(), GameActivity.class);
                        (this.context).startActivity(intent);
                    }
                    return true;
            }

        }
            return true;
    }

    @Override
    public void run() {
        while (playing) {

            long startFrameTime = System.currentTimeMillis();

            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();

            // Calculate the fps this frame
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

        }
    }

    private void drawBackground() {

        // Make a copy of the relevant background
        Background bg = backgrounds;

        // define what portion of images to capture and
        // what coordinates of screen to draw them at

        // For the regular bitmap
        Rect fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
        Rect toRect1 = new Rect(bg.xClip, bg.startY, bg.width, bg.endY);

        // For the reversed background
        Rect fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
        Rect toRect2 = new Rect(0, bg.startY, bg.xClip, bg.endY);

        //draw the two background bitmaps
        if (!bg.reversedFirst) {
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
        } else {
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
        }

    }


    private void update() {

            backgrounds.update(fps);


        //incrementing score as time passes
        score++;

        //updating player position
        player.update();

        //setting boom outside the screen
        boom.setX(-500);
        boom.setY(-500);

        for (Star s : stars) {
            s.update(player.getSpeed());
        }// Update all the background positions


        //setting the flag true when the enemy just enters the screen
        if(enemies.getX()==screenX){
            flag = true;
        }

        enemies.update(player.getSpeed());

        //if collision occurs with player
        if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())) {
            //displaying boom at that location
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());
            //will play a sound at the collision between player and the enemy
            enemies.setX(-500);
        } else {
            //if the enemy has just entered
            if (flag) {
                //if player's x coordinate is more than the enemies's x coordinate.i.e. enemy has just passed across the player
                if (player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX()) {
                    //increment countMisses
                    countMisses++;

                    //setting the flag false so that the else part is executed only when new enemy enters the screen
                    flag = false;
                    //if no of Misses is equal to 3, then game is over.
                    if (countMisses == 3) {
                        //setting playing false to stop the game.
                        playing = false;
                        isGameOver = true;

                        //Assigning the scores to the highscore integer array
                        for(int i=0;i<4;i++){
                            if(highScore[i]<score){

                                final int finalI = i;
                                highScore[i] = score;
                                break;
                            }
                        }

                        //storing the scores through shared Preferences
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        for(int i=0;i<4;i++){
                            int j = i+1;
                            e.putInt("score"+j,highScore[i]);
                        }
                        e.apply();

                    }
                }
            }
        }

        //updating the friend ships coordinates
        friend.update(player.getSpeed());

        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())){

            //displaying the boom at the collision
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            //setting playing false to stop the game
            playing = false;
            //setting the isGameOver true as the game is over
            isGameOver = true;

            //Assigning the scores to the highscore integer array
            for(int i=0;i<4;i++){
                if(highScore[i]<score){

                    final int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }

            //storing the scores through shared Preferences
            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int i=0;i<4;i++){
                int j = i+1;
                e.putInt("score"+j,highScore[i]);
            }
            e.apply();

        }
    }

    private void draw() {

        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas

            canvas.drawColor(Color.argb(255, 0, 0, 0));


            //setting the paint color to white to draw the stars
            //paint.setColor(Color.WHITE);
            //paint.setTextSize(20);


            drawBackground();


            //drawing all stars
            /*
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }
            */


            player.getCurrentFrame();

            //Drawing the player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.frameToDraw,
                    player.whereToDraw,
                    paint);

            float angle = (float) (System.currentTimeMillis() % ROTATE_TIME_MILLIS)
                    / ROTATE_TIME_MILLIS * 360;
            matrix.reset();
            matrix.postTranslate(-enemies.getBitmap().getWidth() / 2, -enemies.getBitmap().getHeight() / 2);
            matrix.postRotate(angle);
            matrix.postTranslate(enemies.getX(), enemies.getY());
            canvas.drawBitmap(enemies.getBitmap(), matrix, paint);

            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            friend.getCurrentFrame();

            //drawing friends image
            canvas.drawBitmap(
                    friend.getBitmap(),
                    friend.frameToDraw,
                    friend.whereToDraw,
                    paint
            );

            //drawing the score on the game screen
            paint.setColor(Color.BLACK);
            paint.setTextSize(50);
            canvas.drawText("Score:"+score,100,50,paint);

            //draw game Over when the game is over
            if(isGameOver){

                bitmapXPosition = (canvas.getWidth()/2) - (play_again.getWidth() / 2);
                bitmapYPosition = (canvas.getHeight()/2) - (play_again.getHeight() / 2) + (game_over.getHeight() / 2) + 50;


                //drawing boom image
                canvas.drawBitmap(
                        game_over,
                        (canvas.getWidth()/2) - (game_over.getWidth() / 2),
                        (canvas.getHeight()/2) - (game_over.getHeight() / 2),
                        paint
                );
                canvas.drawBitmap(
                        play_again,
                        (canvas.getWidth()/2) - (play_again.getWidth() / 2),
                        (canvas.getHeight()/2) - (play_again.getHeight() / 2) + (game_over.getHeight() / 2) + 50,
                        paint
                );

            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}