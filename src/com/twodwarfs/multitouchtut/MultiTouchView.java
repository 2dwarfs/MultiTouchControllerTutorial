package com.twodwarfs.multitouchtut;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.twodwarfs.multitouchcontroller.MultiTouchController;
import com.twodwarfs.multitouchcontroller.MultiTouchController.MultiTouchObjectCanvas;
import com.twodwarfs.multitouchcontroller.MultiTouchController.PointInfo;
import com.twodwarfs.multitouchcontroller.MultiTouchController.PositionAndScale;

/**
 * Canvas View for the MultiTouch controller
 * @author 2dwarfs.com
 *
 */

public class MultiTouchView extends View implements MultiTouchObjectCanvas<PinchWidget> {

	private static final int UI_MODE_ROTATE = 1;
	private static final int UI_MODE_ANISOTROPIC_SCALE = 2;
	private int mUIMode = UI_MODE_ROTATE;

	private MultiTouchController<PinchWidget> mMultiTouchController = new MultiTouchController<PinchWidget>(this);

	private int mWidth, mHeight;

	private PinchWidget mPinchWidget;
	private Context mContext;

	public MultiTouchView(Context context) {
		super(context);
	}

	public MultiTouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
	}

	public MultiTouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setPinchWidget(Bitmap bitmap) {
		mPinchWidget = new PinchWidget(bitmap);
		mPinchWidget.init(mContext.getResources());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mWidth = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		mHeight = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(Color.WHITE);
		mPinchWidget.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return mMultiTouchController.onTouchEvent(ev);
	}

	@Override
	public PinchWidget getDraggableObjectAtPoint(PointInfo pt) {
		float x = pt.getX(), y = pt.getY();

		if (mPinchWidget.containsPoint(x, y)) {
			return mPinchWidget;
		}

		return null;
	}

	@Override
	public void getPositionAndScale(PinchWidget pinchWidget, PositionAndScale objPosAndScaleOut) {
		objPosAndScaleOut.set(pinchWidget.getCenterX(), pinchWidget.getCenterY(), 
				(mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
				(pinchWidget.getScaleFactor() + pinchWidget.getScaleFactor()) / 2, 
				(mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, 
				pinchWidget.getScaleFactor(), 
				pinchWidget.getScaleFactor(),
				(mUIMode & UI_MODE_ROTATE) != 0, 
				pinchWidget.getAngle());
	}

	@Override
	public boolean setPositionAndScale(PinchWidget pinchWidget, PositionAndScale newImgPosAndScale, PointInfo touchPoint) {
		boolean ok = pinchWidget.setPos(newImgPosAndScale, mUIMode, UI_MODE_ANISOTROPIC_SCALE, touchPoint.isMultiTouch());
		if(ok) {
			invalidate();
		}

		return ok;
	}
	
	@Override
	public void selectObject(PinchWidget pinchWidget, PointInfo touchPoint) {
		if(touchPoint.isDown()) {
			mPinchWidget = pinchWidget;
		}

		invalidate();
	}
}
