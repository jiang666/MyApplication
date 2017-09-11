package com.iflytek.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.iflytek.demo.R;

/**
 * Created by jianglei on 2017/9/5.
 */

public class DrawCircle extends View {
    private int x;
    private int y;
    private int image_hight;
    private int image_width;
    Context context;
    public DrawCircle(Context context) {
        super(context);
        this.context = context;
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

   @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
         canvas.drawColor(Color.WHITE);
        Paint paint=new Paint();
        Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(), R.drawable.setting);
       image_hight = bitmap.getHeight();
        image_width = bitmap.getWidth();

        canvas.drawBitmap(bitmap, x, y,paint);
        if(bitmap.isRecycled())
        {
            bitmap.recycle();
        }
         /*//画笔
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //绘制圆
        canvas.drawCircle(x,y,30,paint);*/
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 获取当前触摸点的x,y坐标
                x = (int) event.getX();
                y = (int) event.getY();
            case MotionEvent.ACTION_UP:
                break;
        }
        //获取屏幕宽高
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int heigh = manager.getDefaultDisplay().getHeight();

        //重新绘制圆 ,控制小球不会被移出屏幕
        if(x>=image_width && y>= image_hight && x<=width-image_width && y<=heigh-image_hight){
            invalidate();
        }
        return true;
    }
}
