package com.iflytek.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.iflytek.demo.view.Draglayout.Status;

public class MyLinearLayout extends LinearLayout {

	private Draglayout layout;

	public MyLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setDragLayout(Draglayout layout){
		this.layout = layout;
	}
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(layout.getStatus() == Status.Close){
			// 如果DragLayout是关闭状态， 按原本拦截判断处理
			return super.onInterceptTouchEvent(ev);
		}else {
			// 否则，直接拦截下来。
			return true;
		}
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(layout.getStatus() == Status.Close){
			// 如果DragLayout是关闭状态， 按原本拦截判断处理
			return super.onTouchEvent(event);
		}else {
			// 否则，按自己的方式处理, 关闭控件
			if(event.getAction() == MotionEvent.ACTION_UP){
				layout.close();
			}
			return true;
		}
		
	}

}
