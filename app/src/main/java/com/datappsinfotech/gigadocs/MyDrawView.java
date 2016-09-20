package com.datappsinfotech.gigadocs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyDrawView extends View {
    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint mbitmappaint;
    private Paint   mPaint;
    public static int height;
    public static int width;
    public MyDrawView(Context c, AttributeSet attrs) {
        super(c, attrs);

        path = new Path();
        mbitmappaint = new Paint(Paint.DITHER_FLAG);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            if (getParent() != null && event.getAction() == MotionEvent.ACTION_DOWN) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(10, 20, oldw, oldh);
        try {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

            canvas = new Canvas(bitmap);
            clear();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the height required to render the view
        // Assume Width will always be MATCH_PARENT.
        try {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(1300); // Since 3000 is bottom of last Rect to be drawn added and 50 for padding.
            setMeasuredDimension(width, height);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        try {
            canvas.drawBitmap(bitmap, 0, 0, mbitmappaint);

            canvas.drawPath(path, mPaint);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        try {
            path.reset();
            path.moveTo(x, y);
            mX = x;
            mY = y;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private void touch_move(float x, float y) {
        try {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);

            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x+1;
                mY = y+1;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private void touch_up() {
        try {
            path.lineTo(mX, mY);
            // commit the path to our offscreen
            canvas.drawPath(path, mPaint);
            // kill this so we don't double draw
            path.reset();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }
    public void clear(){
        try {
            bitmap.eraseColor(Color.WHITE);
            invalidate();
            System.gc();
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

}