package com.iflytek.demo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.iflytek.demo.R;

/**
 * Created by jianglei on 2017/9/21.
 */

public class MarkingView extends View {
    //底部的圆
    private float bottomWidth = 200;
    private Paint bottomPaint;
    private int bottomColor = Color.GRAY;
    //上面的扇形
    private float topWidth;
    private Paint topPaint;
    private int topColor = Color.DKGRAY;
    private int ringWidth = 20;
    private int angle = 300;
    private int currentAngle = 0;
    private int startAngle = 270;
    private int endAngle = 18;
    //中间的圆
    private int centerColor = Color.WHITE;
    private Paint centerPaint;
    boolean isFirst = true;
    boolean isDrawText = true;
    private int duation = 3000;//动画时间
    public MarkingView(Context context) {
        super(context);
    }

    public MarkingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        topPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarkingView);
        setBottomColor(a.getColor(R.styleable.MarkingView_bottomCorlor,bottomColor));
        setTopColor(a.getColor(R.styleable.MarkingView_topCorlor,topColor));
        setCenterColor(a.getColor(R.styleable.MarkingView_centerCorlor,centerColor));
        setEndAngle(a.getInteger(R.styleable.MarkingView_endAngle,angle));
        setStartAngle(a.getInteger(R.styleable.MarkingView_startAngle,startAngle));
        setDuation(a.getInteger(R.styleable.MarkingView_duation,duation));
        setRingWidth(a.getDimensionPixelOffset(R.styleable.MarkingView_ringWidth,ringWidth));
        a.recycle();

    }


    public MarkingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setStartAngle(int startAngle){
        this.startAngle = startAngle;
    }
    public void setDuation(int duation){
        this.duation = duation;
    }
    public void setRingWidth(int ringWidth){
        this.ringWidth = ringWidth;
    }
    public void setTopColor(int topColor){
        this.topColor = topColor;
    }
    public void setCenterColor(int centerColor){
        this.centerColor = centerColor;
    }
    public void setEndAngle(int angle){
        this.angle = angle;
    }
    public void setBottomColor(int angle){
        this.bottomColor = angle;
    }
    public void refresh(){
        startAnimation();
    }
    public void startAnimation(){
        ValueAnimator animator = ValueAnimator.ofInt(0,angle);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(duation);
        animator.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isFirst){
            isFirst = false;
            topWidth = getWidth();
            bottomWidth = getWidth();
            drawBottom(canvas);
            drawTop(canvas);
            drawCenter(canvas);
            startAnimation();
        }else {
            drawBottom(canvas);
            drawTop(canvas);
            drawCenter(canvas);
        }


    }
    protected void drawCenter(Canvas canvas){
        centerPaint.setColor(centerColor);
        canvas.drawCircle(getWidth()/2,getHeight()/2,(topWidth-ringWidth*2)/2,centerPaint);
    }
    protected void drawTop(Canvas canvas){
        topPaint.setColor(topColor);
        RectF rectF = new RectF(0,0,getWidth(),getWidth());
        canvas.drawArc(rectF,startAngle,currentAngle,true,topPaint);
    }
    protected void drawBottom(Canvas canvas){
        bottomPaint.setColor(bottomColor);
        canvas.drawCircle(getWidth()/2,getHeight()/2,bottomWidth/2,bottomPaint);
    }
}
