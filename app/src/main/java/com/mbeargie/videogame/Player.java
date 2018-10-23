package com.mbeargie.videogame;

/**
 * Created by mbeargie on 10/11/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Belal on 6/24/2016.
 */
public class Player {
    //Bitmap to get character from image
    public Bitmap bitmap;

    //coordinates
    private int x;
    private int y;

    //motion speed of the character
    private int speed = 0;

    //boolean variable to track the ship is boosting or not
    private boolean boosting;

    //Gravity Value to add gravity effect on the ship
    private final int GRAVITY = -10;

    //Controlling Y coordinate so that ship won't go outside the screen
    private int maxY;
    private int minY;

    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    // These next two values can be anything you like
    // As long as the ratio doesn't distort the sprite too much
    private int frameWidth = 336;
    private int frameHeight = 200;

    boolean isMoving = false;
    boolean isDescending = true;

    // How many frames are there on the sprite sheet?
    private int frameCount = 10;

    // Start at the first frame - where else?
    private int currentFrame = 0;

    // What time was it when we last changed frames
    private long lastFrameChangeTime = 0;

    // How long should each frame last
    private int frameLengthInMilliseconds = 50;

    private int frameLengthInMillisecondsSlow = 100;


    float bobXPosition = 75;

    private Rect detectCollision;

    // A rectangle to define an area of the
    // sprite sheet that represents 1 frame
    Rect frameToDraw = new Rect(
            0,
            0,
            frameWidth,
            frameHeight);

    RectF whereToDraw = new RectF(
            x,                0,
            x + frameWidth,
            frameHeight);

    long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;


    //constructor
    public Player(Context context, int screenX, int screenY) {
        x = 75;
        y = 0;
        speed = 1;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dog);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                frameWidth,
                frameHeight  * frameCount,
                false);

        //calculating maxY
        maxY = screenY - frameHeight;

        //top edge's y point is 0 so min y will always be zero
        minY = 0;

        //setting the boosting value to false initially
        boosting = false;

        //initializing rect object
        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void getCurrentFrame(){

        long time  = System.currentTimeMillis();
        if(isMoving) {// Only animate if bob is moving
            if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {

                    currentFrame = 0;
                }
            }
        }
        if(isDescending) {// Only animate if bob is moving
            if ( time > lastFrameChangeTime + frameLengthInMillisecondsSlow) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {

                    currentFrame = 0;
                }
            }
        }
        //update the left and right values of the source of
        //the next frame on the spritesheet
        frameToDraw.top = currentFrame * frameHeight;
        frameToDraw.bottom = frameToDraw.top + frameHeight;

    }


    //setting boosting true
    public void setBoosting() {
        boosting = true;
    }

    //setting boosting false
    public void stopBoosting() {
        boosting = false;
    }

    public void update() {

        long startFrameTime = System.currentTimeMillis();

        timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame;
        }


        if (boosting) {
            //speeding up the ship
            speed += 2;
            isMoving = true;
            isDescending = false;


        } else {
            //slowing down if not boosting
            speed -= 5;
            isMoving = false;
            isDescending = true;


        }
        //controlling the top speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        //if the speed is less than min speed
        //controlling it so that it won't stop completely
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        //moving the ship down
        y -= speed + GRAVITY;

        //but controlling it also so that it won't go off the screen
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }



        whereToDraw.left = x;
        whereToDraw.top = y;
        whereToDraw.right = x + frameWidth;
        whereToDraw.bottom = y + frameHeight;


        //adding top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + frameWidth;
        detectCollision.bottom = y + frameHeight;
    }


    /*
    * These are getters you can generate it autmaticallyl
    * right click on editor -> generate -> getters
    * */

    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {return bitmap;}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
