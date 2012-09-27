package com.twodwarfs.multitouchtut;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.twodwarfs.multitouchcontroller.MultiTouchController.PositionAndScale;

/**
 * PinchWidget for the MultiTouch controller
 * @author 2dwarfs.com
 *
 */

public class PinchWidget {

	private Bitmap mImage;

	private float mCenterX = 0.0f, mCenterY = 0.0f; 
	private int mDisplayWidth, mDisplayHeight;

	private float mScaleFactor = 1.0f;
	private float mAngle = 0.0f;
	private float mMinX, mMaxX, mMinY, mMaxY;

	public PinchWidget() {
	}

	public PinchWidget(Bitmap bm) {
		this();
		setImage(bm);
	}

	public void init(Resources res) {
		getMetrics(res);

		float cx = (mCenterX==0) ? mDisplayWidth * 0.5f : mCenterX;
		float cy = (mCenterY==0) ? mDisplayHeight * 0.5f : mCenterY;

		setPos(cx, cy, mScaleFactor, mScaleFactor, mAngle);

	}

	private void getMetrics(Resources res) {
		DisplayMetrics metrics = res.getDisplayMetrics();

		mDisplayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
				metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);

		mDisplayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
				metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);

	}

	public boolean setPos(PositionAndScale newImgPosAndScale, int uiMode, int uiModeAnisotropic, boolean isMultitouch) {
		boolean ret = false;
		float x = newImgPosAndScale.getXOff();
		float y = newImgPosAndScale.getYOff();
		if(isMultitouch) {
			x = mCenterX;
			y = mCenterY;
		}

		ret = setPos(x, y, 
				(uiMode & uiModeAnisotropic) != 0 ? newImgPosAndScale.getScaleX() : newImgPosAndScale.getScale(),
						(uiMode & uiModeAnisotropic) != 0 ? newImgPosAndScale.getScaleY() : newImgPosAndScale.getScale(), 
								newImgPosAndScale.getAngle());

		return ret;
	}

	private boolean setPos(float centerX, float centerY, float scaleX, float scaleY, float angle) {
		float ws = (mImage.getWidth() / 2) * scaleX, hs = (mImage.getHeight() / 2) * scaleY;
		float newMinX = centerX - ws, newMinY = centerY - hs, newMaxX = centerX + ws, newMaxY = centerY + hs;
		mCenterX = centerX;
		mCenterY = centerY;
		mScaleFactor = scaleX;
		mAngle = angle;

		mMinX = newMinX;
		mMinY = newMinY;
		mMaxX = newMaxX;
		mMaxY = newMaxY;

		return true;
	}

	public boolean containsPoint(float scrnX, float scrnY) {
		return (scrnX >= mMinX && scrnX <= mMaxX && scrnY >= mMinY && scrnY <= mMaxY);
	}

	public void draw(Canvas canvas) {

		Paint itemPaint = new Paint();
		itemPaint.setAntiAlias(true);
		itemPaint.setFilterBitmap(true);

		float dx = (mMaxX + mMinX) / 2;
		float dy = (mMaxY + mMinY) / 2;

		canvas.save();

		canvas.translate(dx, dy);
		canvas.rotate(mAngle * 180.0f / (float) Math.PI);
		canvas.translate(-dx, -dy);

		if(mImage == null) return;
		Rect srcRect = new Rect(0, 0, mImage.getWidth(), mImage.getHeight());
		Rect dstRect = new Rect((int) mMinX, (int) mMinY, (int) mMaxX, (int) mMaxY);

		canvas.drawBitmap(mImage, srcRect, dstRect, null);

		canvas.restore();
	}

	public void setCenterX(float centerX) {
		mCenterX = centerX;
	}

	public float getCenterX() {
		return mCenterX;
	}

	public float getCenterY() {
		return mCenterY;
	}

	public void setCenterY(float centerY) {
		mCenterY = centerY;
	}

	public float getScaleFactor() {
		return mScaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		mScaleFactor = scaleFactor;
	}

	public float getAngle() {
		return mAngle;
	}

	public void setAngle(float angle) {
		mAngle = angle;
	}

	public float getMinX() {
		return mMinX;
	}

	public float getMaxX() {
		return mMaxX;
	}

	public float getMinY() {
		return mMinY;
	}

	public float getMaxY() {
		return mMaxY;
	}

	public Bitmap getImage() {
		return mImage;
	}

	public void setImage(Bitmap image) {
		mImage = image;
	}
}
