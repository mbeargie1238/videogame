package com.mbeargie.videogame;

/**
 * Created by mbeargie on 10/11/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Manish on 10/24/2016.
 */

public class Friend {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 1;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    //creating a rect object
    private Rect detectCollision;
    private int frameWidth = 300;
    private int frameHeight = 200;

    // How many frames are there on the sprite sheet?
    private int frameCount = 30;

    // Start at the first frame - where else?
    private int currentFrame = 0;

    // What time was it when we last changed frames
    private long lastFrameChangeTime = 0;

    // How long should each frame last
    private int frameLengthInMilliseconds = 50;

    private int frameLengthInMillisecondsSlow = 100;

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

    public Friend(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.baby);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                frameWidth * frameCount,
                frameHeight,
                false);


        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;
        if (x < minX - bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }

        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

        whereToDraw.left = x;
        whereToDraw.top = y;
        whereToDraw.right = x + frameWidth;
        whereToDraw.bottom = y + frameHeight;

    }

    public void getCurrentFrame(){

        long time  = System.currentTimeMillis();

            if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {

                    currentFrame = 0;
                }
            }

        //update the left and right values of the source of
        //the next frame on the spritesheet
        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;

    }

    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
