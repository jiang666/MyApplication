package com.iflytek.demo.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * 侧滑面板
 * @author poplar
 *
 */
public class Draglayout extends FrameLayout {

    private ViewDragHelper mHelper;

    // 控件的三个状态
    public static enum Status {
        Close, Open, Draging
    }
    private Status status = Status.Close;

    public interface OnDragUpdateListener{

        void onClose();
        void onOpen();
        void onDraging(float percent);

    }
    private OnDragUpdateListener dragUpdateListener;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OnDragUpdateListener getDragUpdateListener() {
        return dragUpdateListener;
    }

    public void setDragUpdateListener(OnDragUpdateListener dragUpdateListener) {
        this.dragUpdateListener = dragUpdateListener;
    }

    public Draglayout(Context context) {
        this(context, null);
    }

    public Draglayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Draglayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // forParent 要拖拽控件所在的容器
        // sensitivity 敏感度 默认1.0f
        // Callback 触摸事件的回调， 用于提供信息并接受事件

        // 1. 创建ViewDragHelper辅助类
        mHelper = ViewDragHelper.create(this, 1.0f, callback);

    }

    // 3. 重写回调里的方法
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {


        // a. 返回结果决定了child是否可以被拖拽
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // child 被拖拽的子View
            // pointerId 多点触摸id
            return true;
        }

        // b. 返回值设定了横向的拖拽范围（不限制范围）
        @Override
        public int getViewHorizontalDragRange(View child) {
            // 只决定了动画执行时长， 水平方向是否可以拖拽 > 0
            return mRange;
        }

        // c. 返回值决定了View将要移动到的位置。 修正View水平方向的位置。(此方法执行时，还没发生移动)
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            // child 被拖拽的子View
            // left 辅助类建议移动到的位置。
            // dx 将要发生的位移
//			int oldLeft = mMainContent.getLeft();
//			System.out.println("clampViewPositionHorizontal: left: " + left + " dx: " + dx + " oldLeft: " + oldLeft);
            if(child == mMainContent){
                left = fixLeft(left);
            }

            return left;
        }

        // d. 决定了当子View位置发生变化时要做的事情， 伴随动画， 更新状态，执行回调
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            // changedView 被拖拽的View
            // left 水平方向移动到的位置
            // dx 水平位移

            System.out.println("onViewPositionChanged: left" + left + " dx: " + dx);
            if(changedView == mLeftContent){
                // 拖拽的是左面板，把左面板的拖拽位移转交给主面板
                mLeftContent.layout(0, 0, 0 + mWidth, 0 + mHeight);
                // 让新的左边值生效
                int newLeft = mMainContent.getLeft() + dx;
                newLeft = fixLeft(newLeft);
                mMainContent.layout(newLeft, 0, newLeft + mWidth, 0 + mHeight);
            }

            dispathDragEvent();

            // 手动引发界面的重绘
            invalidate();

        }


        // e.决定了子View被释放时做的事情， 结束动画
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // releasedChild 被释放的View
            // xvel ： 水平方向速度, 向右+， 向左-
            System.out.println("onViewReleased xvel: " + xvel);
            if(xvel == 0 && mMainContent.getLeft() > mRange * 0.5f){
                open();
            }else if (xvel > 0) {
                open();
            }else {
                close();
            }

        }

        // f. 拖拽状态改变
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
//			ViewDragHelper.STATE_IDLE 闲置
//			ViewDragHelper.STATE_DRAGGING 拖拽
//			ViewDragHelper.STATE_SETTLING 自动化
            System.out.println("onViewDragStateChanged： " + state);
        }

    };

    /**
     * 修正左边值
     * @param left
     * @return
     */
    private int fixLeft(int left) {
        if(left < 0){
            return 0;
        }else if (left > mRange) {
            return mRange;
        }
        return left;
    }

    /**
     *  拖拽过程中， 动态修改各个控件的大小， 透明度...
     */
    protected void dispathDragEvent() {
//		0.0 -> 1.0
        float percent = mMainContent.getLeft() * 1.0f / mRange;
        System.out.println("percent: " + percent);

        // 执行动画
        animViews(percent);

        if(dragUpdateListener != null){
            dragUpdateListener.onDraging(percent);
        }

        // 更新状态
        Status lastStatus = status;
        status = updateStatus(percent);
        // 状态更新的时候执行回调
        if(lastStatus != status && dragUpdateListener != null){
            if(status == Status.Open){
                dragUpdateListener.onOpen();
            }else if (status == Status.Close) {
                dragUpdateListener.onClose();
            }
        }
    }

    /**
     * 根据当前动画执行的百分比， 更新状态
     * @param percent
     * @return
     */
    private Status updateStatus(float percent) {
        if(percent == 0){
            return Status.Close;
        }else if (percent == 1) {
            return Status.Open;
        }

        return Status.Draging;
    }

    private void animViews(float percent) {
        //		* 左面板 ： 缩放动画，平移动画，透明度动画
        // 缩放 0.0 -> 1.0 >>> 0.5f -> 1.0f  >>> 0.5f + percent * 0.5f
        //		mLeftContent.setScaleX(0.5f + percent * 0.5f);
        //		mLeftContent.setScaleY(0.5f + percent * 0.5f);
        ViewHelper.setScaleX(mLeftContent, 0.5f + percent * 0.5f);
        ViewHelper.setScaleY(mLeftContent, evaluate(percent, 0.5f, 1.0f));
        // 平移动画 -mWidth / 2.0f  -> 0
        // 1 - percent >>> 1.0 -> 0.0
        ViewHelper.setTranslationX(mLeftContent, evaluate(percent, -mWidth / 2.0f, 0));
        // 透明度 0.5f, 1.0
        ViewHelper.setAlpha(mLeftContent, evaluate(percent, 0.5f, 1.0f));

        //		* 主面板 ： 缩放动画 1.0 -> 0.5f
        ViewHelper.setScaleX(mMainContent, evaluate(percent, 1.0f, 0.8f));
        ViewHelper.setScaleY(mMainContent, evaluate(percent, 1.0f, 0.8f));

        //		* 背景动画： 亮度变化
        getBackground().setColorFilter((Integer)evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), Mode.SRC_OVER);
    }

    /**
     * 估值器
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * 颜色估值器
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }

    /**
     * 关闭
     */
    public void close() {
        close(true);
    }
    public void close(boolean isSmooth){
        int finalLeft = 0;
        if(isSmooth){
            // 执行平滑动画
            // 1. 触发平滑动画（开始模拟数据）
            if(mHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)){
                // 返回true， 说明还没有移动到指定位置。需要重绘界面
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth,  0 + mHeight);
        }
    }
    /**
     * 打开
     */
    public void open() {
        open(true);
    }
    public void open(boolean isSmooth){
        int finalLeft = mRange;
        if(isSmooth){
            // 执行平滑动画
            // 1. 触发平滑动画（开始模拟数据）
            if(mHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)){
                // 返回true， 说明还没有移动到指定位置。需要重绘界面
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth,  0 + mHeight);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 2. 维持动画的继续
        if(mHelper.continueSettling(true)){
            // 返回true， 说明还没有移动到指定位置。需要重绘界面
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }



    private ViewGroup mLeftContent;
    private ViewGroup mMainContent;
    private int mWidth;
    private int mHeight;
    private int mRange;


    // 2. 转交触摸判断， 和触摸处理
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        // 由ViewDragHelper判断拦截
        return mHelper.shouldInterceptTouchEvent(ev);
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            mHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        // 计算拖拽范围
        mRange = (int) (mWidth * 0.6f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 查找里边的控件

        // 健壮性检查
        // 检查孩子的个数
        if(getChildCount() < 2){
            throw new IllegalStateException("当前布局内必须至少有俩孩子，Your viewgroup must contains 2 children at least!");
        }
        // 检查孩子的类型
        if(!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("子View必须是ViewGroup的子类. Child must be an instance of ViewGroup。");
        }

        mLeftContent = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);

    }


}

