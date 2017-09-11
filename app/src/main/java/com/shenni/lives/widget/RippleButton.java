package com.shenni.lives.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.shenni.lives.R;

/**
 * Created by moon.zhong on 2015/4/27. <modify> 修改人：Ming 修改时间：2016-10-21
 * </modify>
 */
public class RippleButton extends Button {

	/* 起始点 */
	private int mInitX;
	private int mInitY;

	private float mCurrentX;
	private float mCurrentY;

	/* 高度和宽度 */
	private int mWidth;
	private int mHeight;

	/* 绘制的半径 */
	private float mRadius;
	private float mStepRadius;
	private float mStepOriginX;
	private float mStepOriginY;
	private float mDrawRadius;
	/* 绘制背景色 */
	private int backgroundColor = 0xff3493c8;
	private int enableColor = 0xff7f8c8d;

	private boolean mDrawFinish;
	private boolean enable = true;

	private int DURATION = 150;
	private final int FREQUENCY = 10;
	private float mCycle;
	private final Rect mRect = new Rect();

	private boolean mPressUp = false;

	private Paint mRevealPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private Paint paint = new Paint();
	private RectF viewRetF = new RectF();

	protected int drawableWidth;
	protected DrawablePositions drawablePosition;
	protected int iconPadding;
	Rect bounds;

	private enum DrawablePositions {
		NONE, LEFT_AND_RIGHT, LEFT, RIGHT
	}

	public RippleButton(Context context) {
		super(context);
		initData(context, null);
	}

	public RippleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context, attrs);
	}

	public RippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData(context, attrs);
	}

	// 设置图片和文字的间距
	public void setIconPadding(int padding) {
		iconPadding = padding;
		requestLayout();
	}

	//初始化数据
	private void initData(Context context, AttributeSet attrs) {
		if (attrs != null) { //设置图文间距
			if (null == bounds) {
				bounds = new Rect();
			}

			TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
					R.styleable.RippleButton);
			int paddingId = typedArray.getDimensionPixelSize(
					R.styleable.RippleButton_iconPadding, 0);
			if(paddingId>0)
				setIconPadding(paddingId);
			typedArray.recycle();
		}
		mRevealPaint.setColor(0x25000000);
		mCycle = DURATION / FREQUENCY;
		final float density = getResources().getDisplayMetrics().density;
		mCycle = (density * mCycle);
		mDrawFinish = true;
		paint.setAntiAlias(true);
		this.setTextColor(Color.WHITE);

	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enable = enabled;
		invalidate();
		super.setEnabled(enabled);
	}

	public void setBackgroundColor(int arg0) {
		this.backgroundColor = arg0;
		invalidate();
	}

	//设置按钮不可点击颜色
	public void setEnableColor(int arg0) {
		this.enableColor = arg0;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (enable) {
			paint.setColor(backgroundColor);
			canvas.drawRoundRect(viewRetF, 10, 10, paint); //
			paint.setColor(0x15000000);
			if (mDrawFinish) {
				super.onDraw(canvas);
				return;
			}
			canvas.drawRoundRect(viewRetF, 10, 10, paint);
			super.onDraw(canvas);
			if (mStepRadius == 0) {
				return;
			}
			mDrawRadius = mDrawRadius + mStepRadius;
			mCurrentX = mCurrentX + mStepOriginX;
			mCurrentY = mCurrentY + mStepOriginY;
			if (mDrawRadius > mRadius) {
				mDrawRadius = 0;
				canvas.drawRoundRect(viewRetF, 10, 10, mRevealPaint);
				mDrawFinish = true;
				if (mPressUp)
					invalidate();
				return;
			}

			canvas.drawCircle(mCurrentX, mCurrentY, mDrawRadius, mRevealPaint);
			ViewCompat.postInvalidateOnAnimation(this);
		} else {
			paint.setColor(enableColor);
			canvas.drawRoundRect(viewRetF, 10, 10, paint);
			paint.setColor(0x15000000);
			super.onDraw(canvas);
			return;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		viewRetF.set(0, 0, mWidth, mHeight);
		mRect.set(0, 0, mWidth, mHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		Paint textPaint = getPaint();
		String text = getText().toString();
		textPaint.getTextBounds(text, 0, text.length(), bounds);

		int textWidth = bounds.width();
		int factor = (drawablePosition == DrawablePositions.LEFT_AND_RIGHT) ? 2
				: 1;
		int contentWidth = drawableWidth + iconPadding * factor + textWidth;
		int horizontalPadding = (int) ((getWidth() / 2.0) - (contentWidth / 2.0));

		setCompoundDrawablePadding(-horizontalPadding + iconPadding);

		switch (drawablePosition) {
		case LEFT:
			setPadding(horizontalPadding, getPaddingTop(), 0,
					getPaddingBottom());
			break;

		case RIGHT:
			setPadding(0, getPaddingTop(), horizontalPadding,
					getPaddingBottom());
			break;

		case LEFT_AND_RIGHT:
			setPadding(horizontalPadding, getPaddingTop(), horizontalPadding,
					getPaddingBottom());
			break;

		default:
			setPadding(0, getPaddingTop(), 0, getPaddingBottom());
		}
	}

	private void updateDrawData() {
		/* 最大半径 */
		mRadius = (float) Math.sqrt(mRect.width() / 2 * mRect.width() / 2
				+ mRect.height() / 2 * mRect.height() / 2);
		;
		/* 半径的偏移量 */
		mStepRadius = mRadius / mCycle;
		/* 圆心X的偏移量 */
		mStepOriginX = (mRect.width() / 2 - mInitX) / mCycle;
		/* 圆心Y的偏移量 */
		mStepOriginY = (mRect.height() / 2 - mInitY) / mCycle;

		mCurrentX = mInitX;
		mCurrentY = mInitY;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (enable)
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				mPressUp = false;
				mDrawFinish = false;
				int index = MotionEventCompat.getActionIndex(event);
				int eventId = MotionEventCompat.getPointerId(event, index);
				if (eventId != -1) {
					mInitX = (int) MotionEventCompat.getX(event, index);
					mInitY = (int) MotionEventCompat.getY(event, index);
					updateDrawData();
					invalidate();
				}
				break;
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mStepRadius = (int) (5 * mStepRadius);
				mStepOriginX = (int) (5 * mStepOriginX);
				mStepOriginY = (int) (5 * mStepOriginY);
				mPressUp = true;
				invalidate();
				break;
			}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean performClick() {

		postDelayed(new Runnable() {
			@Override
			public void run() {
				RippleButton.super.performClick();
			}
		}, 150);
		return true;

	}

	@Override
	public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
			Drawable top, Drawable right, Drawable bottom) {
		super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);

		if (left != null && right != null) {
			drawableWidth = left.getIntrinsicWidth()
					+ right.getIntrinsicWidth();
			drawablePosition = DrawablePositions.LEFT_AND_RIGHT;
		} else if (left != null) {
			drawableWidth = left.getIntrinsicWidth();
			drawablePosition = DrawablePositions.LEFT;
		} else if (right != null) {
			drawableWidth = right.getIntrinsicWidth();
			drawablePosition = DrawablePositions.RIGHT;
		} else {
			drawablePosition = DrawablePositions.NONE;
		}

		requestLayout();
	}
}
