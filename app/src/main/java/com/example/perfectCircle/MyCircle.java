package com.example.perfectCircle;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MyCircle extends View {

	Paint mPaint;
	int width;
	int height;
	Bitmap bm;
	int rotate;
	boolean first = true;
	ValueAnimator vanim;
	String mText = null;
	private float mTextSize;
	private int mColor;
	Handler han = new Handler() {
		public void handleMessage(android.os.Message msg) {
			invalidate();
			rotate += 10;
			sendEmptyMessageDelayed(123, 30);
		};
	};
	private int mCircleColor;
	private float mCircleWidth;
	private int mCircleColorM;
	private int mCircleColorE;
	private int imageId;

	public MyCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		ImageView a;
		TypedArray attr = context.obtainStyledAttributes(attrs,
				R.styleable.perfectCircle);

		mPaint = new Paint();
		mPaint.setColor(Color.RED);

		// ���Զ���ʵ��
		// vanim = ValueAnimator.ofInt(2,100);
		// vanim.addUpdateListener(new AnimatorUpdateListener() {
		//
		// @Override
		// public void onAnimationUpdate(ValueAnimator animation) {
		// // TODO Auto-generated method stub
		// int value = (Integer) animation.getAnimatedValue();
		// Log.d("wang","the value is " + value);
		// rotate += value;
		// invalidate();
		// }
		// });
		// vanim.setDuration(7000);

		mText = attr.getString(R.styleable.perfectCircle_circletext);
		mTextSize = attr
				.getDimension(R.styleable.perfectCircle_textSize, 10.0f);
		mColor = attr
				.getColor(R.styleable.perfectCircle_textColor, Color.BLACK);
		mCircleColor = attr.getColor(R.styleable.perfectCircle_circleColor,
				Color.BLUE);
		mCircleWidth = attr.getDimension(R.styleable.perfectCircle_circleWidth,
				10.0f);
		mCircleColorM = Color.GRAY;
		mCircleColorE = attr.getColor(R.styleable.perfectCircle_circleColorE,
				Color.TRANSPARENT);

		imageId = attr.getResourceId(R.styleable.perfectCircle_circleBm, 0);
		bm = BitmapFactory.decodeResource(getResources(), imageId);

		mPaint.setShader(new SweepGradient(0, 0, new int[] { mCircleColor,
				mCircleColorE }, new float[] { 0, 0.5f }));
		mPaint.setStyle(Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(mCircleWidth);
		mPaint.setStrokeCap(Cap.ROUND);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);

	}

	public void setDstBitmap(int b) {
		imageId = b;
		bm = BitmapFactory.decodeResource(getResources(), b);
		invalidate();
	}

	public void setText(String text) {
		mText = text;
	}

	public void setTextSize(int size) {
		mTextSize = size;
	}

	public void setTextColor(int color) {
		mColor = color;
	}

	public void setCircleColor(int color) {
		mCircleColor = color;
	}

	public void setCircleWidth(int width) {
		mCircleWidth = width;
	}

	public void setCircleColorM(int color) {
		mCircleColorM = color;
	}

	public void setCircleColorE(int color) {
		mCircleColorE = color;
	}

	@SuppressLint("DrawAllocation") @Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		ImageView i;
		if (rotate >= 360) {
			rotate = 0;
		}
		// canvas.rotate(10);
		// canvas.drawCircle(width/2, height/2, 30, mPaint);
		// 保存一下画布的状态，为了下次能正确的恢复到这个状态，如果不保存就没办法恢复到这个状态了。
		canvas.save();
		canvas.translate(70, 70);
		// 画布旋转指定度数，而且是以原点作为圆心旋转的，所以要想正确的旋转，必须先平移画布到圆心处，
		// 不然旋转的时候就不正确了
		canvas.rotate(-rotate);
		RectF oval = new RectF(-30, -30, 30, 30);

		Paint paint = new Paint();
		paint.setColor(mCircleColor);
		paint.setStyle(Style.STROKE);
		// 设置圆角画笔
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeWidth(mCircleWidth);

		// canvas.drawCircle(70, 70, 30, paint);
		canvas.drawArc(oval, 0, 300, false, mPaint);
		// 画一个圆形的小圆弧，不然头部是方的
		canvas.drawArc(oval, 0, 3, false, paint);

		if (first) {
			han.sendEmptyMessage(123);
			first = false;
			// vanim.start();
		}
	
		if (bm != null) {
			// 恢复画布到save之前的状态。
			canvas.restore();
			canvas.save();
			//对要显示的图片进行缩放，如果不缩放，图片裁剪后只能看到一部分
			Bitmap tempBm = Bitmap.createBitmap((int)(30 - mCircleWidth), (int)(30 - mCircleWidth), bm.getConfig());
			Canvas c = new Canvas(tempBm);
			Log.d("wang","tempBm width " + tempBm.getWidth() + " height " + tempBm.getHeight());
			Matrix matrix = new Matrix();
			//计算缩放比例
			float sx = (30 - mCircleWidth)/ bm.getWidth();
			float sy = (30 - mCircleWidth) / bm.getHeight();
			Log.d("wang","x " + sx + " y " + sy);
			matrix.setScale(sx, sy);
			Paint p = new Paint();
			p.setFilterBitmap(true);
			p.setAntiAlias(true);
			c.drawBitmap(bm, matrix, p);
			Log.d("wang","tempBm after width " + tempBm.getWidth() + " height " + tempBm.getHeight());
			
			// 平移画布
			canvas.translate(70 - tempBm.getWidth() / 2, 70 - tempBm.getHeight() / 2);
			Paint paint1 = new Paint();
			// 设置shader为clamp方式，之前必须平移画布，因为画圆的时候bm会从原点开始画起，如果不平移画布，
			// 图片就不会到达圆的正中心了。
			paint1.setShader(new BitmapShader(tempBm, TileMode.CLAMP,
					TileMode.CLAMP));
			
			canvas.drawCircle(tempBm.getWidth() / 2, tempBm.getHeight() / 2, 30 - mCircleWidth/2, paint1);
		}
		if (mText != null) {
			canvas.restore();
			
			canvas.translate(70, 70);
			Paint paint2 = new Paint();
			paint2.setColor(Color.BLACK);
			paint2.setTextSize(mTextSize);
			float width = paint2.measureText(mText);
			canvas.drawText(mText,  - width / 2,
					 - (paint2.ascent() + paint2.descent())/ 2, paint2);
		}
	}
}
