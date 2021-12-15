package com.iflytek.demo.jni;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyView extends View {

	//通过构造方法把压力 传过来
	private int presure;
	private Paint paint;
	public MyView(Context context,int presure) {
		super(context);
		this.presure = presure;
		paint = new Paint(); //创建画笔

		if (presure < 30) {
			//画一个绿色的矩形
			paint.setColor(Color.GREEN);
		}else if (presure<60 ) {
			paint.setColor(Color.YELLOW);
		}else if (presure<90) {
			paint.setColor(Color.RED);
		}else {
			System.out.println("爆炸了...");

		}

	}


	//我想把画的内容 画到当前控件上
	@Override
	protected void onDraw(Canvas canvas) {

		//画一个矩形

		canvas.drawRect(40, 40, 400, (100+presure)*4, paint);

		super.onDraw(canvas);
	}

}
