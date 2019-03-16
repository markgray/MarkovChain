package com.example.android.markovchain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.example.android.common.ClockDataItem;

/**
 * Draws a clock face of a {@code ClockDataItem}
 */
@SuppressWarnings("WeakerAccess")
public class ClockFaceView {


    ClockDataItem mTime;
    Bitmap mBitmap;
    Context mContext;
    BitmapDrawable testMe;
    Canvas mCanvas;
    RectF mOval;
    Paint mPaint = new Paint();
    /**
     *
     * @param view {@code View} to use for resources.
     * @param clockTime {@code ClockDataItem} for the time of day we display
     */
    public ClockFaceView(View view, ClockDataItem clockTime) {

        mTime = clockTime;
        mContext = view.getContext();
        int sizeOfBitmap = 200;
        mBitmap = Bitmap.createBitmap(sizeOfBitmap, sizeOfBitmap, Bitmap.Config.ARGB_8888);
        mOval = new RectF(0, 0, sizeOfBitmap, sizeOfBitmap);
        mCanvas = new Canvas(mBitmap);

        float startAngle = (float) Math.min(clockTime.angleHour, Math.min(clockTime.angleMinute, clockTime.angleSecond));
        startAngle -= 90;
        mPaint.setColor(Color.RED);
        mCanvas.drawArc(mOval, startAngle, (float) clockTime.pieSlices[0], true, mPaint);
        startAngle += clockTime.pieSlices[0];
        mPaint.setColor(Color.GREEN);
        mCanvas.drawArc(mOval, startAngle, (float) clockTime.pieSlices[1], true, mPaint);
        startAngle += clockTime.pieSlices[1];
        mPaint.setColor(Color.BLUE);
        mCanvas.drawArc(mOval, startAngle, (float) clockTime.pieSlices[2], true, mPaint);

        testMe = new BitmapDrawable(view.getResources(), mBitmap);
        testMe.setBounds(0, 0, sizeOfBitmap, sizeOfBitmap);
    }

    public BitmapDrawable getBitmapDrawable() {
        return testMe;

    }
}
