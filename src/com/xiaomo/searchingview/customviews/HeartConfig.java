package com.xiaomo.searchingview.customviews;



public class HeartConfig {

	private int alpha;
	private float scale;
	private float left;
	private float top;
	private boolean isValid;
	private float scaleInterval;
	
	public HeartConfig(int alpha, float scale, float scaleInterval, float left, float top) {
		super();
		this.alpha = alpha;
		this.scale = scale;
		this.scaleInterval = scaleInterval;
		this.left = left;
		this.top = top;
		isValid = true;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public int getAlpha() {
		return alpha;
	}
	
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getLeft() {
		return left;
	}
	
	public void setLeft(float left) {
		this.left = left;
	}
	
	public float getTop() {
		return top;
	}
	
	public void setTop(float top) {
		this.top = top;
	}
	
	public void refreshConfig() {
		if (alpha == 255 && scale >= 1.0f) {
			isValid = false;
		}
		if (alpha < 255) {
			alpha ++;
		}
		if (scale < 1.0f) {
			scale += scaleInterval;
		}
	}
	
}
