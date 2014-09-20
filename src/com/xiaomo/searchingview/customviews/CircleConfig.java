package com.xiaomo.searchingview.customviews;

public class CircleConfig {

	private float radius;
	private float strokeWidth;
	private int alphaValue;
	private boolean isValid;
	
	public CircleConfig(float radius, float strokeWidth, int alphaValue) {
		super();
		this.radius = radius;
		this.strokeWidth = strokeWidth;
		this.alphaValue = alphaValue;
		this.isValid = true;
	}
	
	public boolean isValid() {
		return isValid;
	}

	public float getRadius() {
		return radius;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public float getStrokeWidth() {
		return strokeWidth;
	}
	
	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	
	public int getAlphaValue() {
		return alphaValue;
	}
	
	public void setAlphaValue(int alphaValue) {
		this.alphaValue = alphaValue;
	}
	
	public void refreshConfig(float radiusGap, float strokeWidthGap, int alphaValueGap, float maxSize) {
		radius += radiusGap;
		if (radius > maxSize) {
			isValid = false;
			return;
		}
		strokeWidth += strokeWidthGap;
		if (alphaValue > 0) {
			alphaValue --;
		}
	}
	
}
