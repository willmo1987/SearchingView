package com.xiaomo.searchingview.customviews;

import java.util.LinkedList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.xiaomo.searchingview.R;

public class SearchingView extends View {

	private static final long TIME_INTERVAL = 5L;
	private static final int CIRCLE_ALPHA_VALUE = 180;
	private static final int HEART_ALPHA_VALUE = 5;
	private static final float HEART_SCALE_VALUE = 0.2f;
	private static final long ADD_NEW_CIRCLE_PERIOD = 300L;
	private static final long ADD_NEW_HEART_PERIOD = 600L;
	private static final long BLINK_PERIOD = 1000L;
	private static final int MAX_HEART_NUM = 3;
	
	private float defaultStrokeWidth;
	private float defaultStrokeWidthInterval;
	private float defaultRadius;
	private float defaultRadiusInterval;
	private boolean isRunning = false;
	private long currentTimeOffset;
	private long previousCircleTimeOffset;
	private long previousHeartTimeOffset;
	private LinkedList<CircleConfig> configList;
	private LinkedList<HeartConfig> heartList;
	private Bitmap logoBitmap;
	private Bitmap heartBitmap;
	
	public SearchingView(Context context) {
		super(context);
	}
	
	public SearchingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void initialize() {
		logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		heartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
		defaultStrokeWidth = getResources().getDimension(R.dimen.circle_width);
		defaultRadius = getResources().getDimension(R.dimen.circle_radius);
		defaultRadiusInterval = getResources().getDimension(R.dimen.radius_gap);
		defaultStrokeWidthInterval = getResources().getDimension(R.dimen.stroke_gap);
		configList = new LinkedList<CircleConfig>();
		heartList = new LinkedList<HeartConfig>();
	}
	
	public void startRunning() {
		if (logoBitmap == null) {
			initialize();
		}
		//Initialize circle config list
		configList.addLast(new CircleConfig(defaultRadius, defaultStrokeWidth, CIRCLE_ALPHA_VALUE));
		//Initialize heart config list
		refillHeartList();
		
		isRunning = true;
		invalidate();
	}
	
	public void stopRunning() {
		isRunning = false;
		if (configList != null) {
			configList.clear();
		}
		if (heartList != null) {
			heartList.clear();
		}
		currentTimeOffset = 0;
		previousCircleTimeOffset = 0;
		previousHeartTimeOffset = 0;
	}
	
	private void refillHeartList() {
		int number = randomHeartNumber();
		for (int i = 0; i < number; i++) {
			float[] size = randomPosition(getWidth(), getHeight());
			heartList.addLast(new HeartConfig(HEART_ALPHA_VALUE, HEART_SCALE_VALUE,  
					TIME_INTERVAL * 1.0f / BLINK_PERIOD, size[0], size[1]));
		}
	}
	
	private float[] randomPosition(int width, int height) {
		Random random = new Random();
		int left = random.nextInt(width);
		int top = random.nextInt(height);
		return new float[]{left, top};
	}
	
	private int randomHeartNumber() {
		Random random = new Random();
		return random.nextInt(MAX_HEART_NUM);
	}
	
	private void drawMiddlePicture(Canvas canvas, int width, int height) {
		if (logoBitmap != null) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			float left = (width - logoBitmap.getWidth()) / 2;
			float top = (height - logoBitmap.getHeight()) / 2;
			canvas.drawBitmap(logoBitmap, left, top, paint);
		}
	}
	
	private void drawRunningCircle(Canvas canvas, int width, int height) {
		int listSize = configList.size();
		if (listSize > 0) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(getResources().getColor(R.color.white));
			paint.setStyle(Style.STROKE);
			
			for (int i = 0; i < listSize; i++) {
				CircleConfig config = configList.get(i);
				paint.setStrokeWidth(config.getStrokeWidth());
				paint.setAlpha(config.getAlphaValue());
				canvas.drawCircle(width / 2, height / 2, config.getRadius(), paint);
				config.refreshConfig(defaultRadiusInterval, defaultStrokeWidthInterval, height / 2);
			}
			
			CircleConfig firstConfig = configList.getFirst();
			if (!firstConfig.isValid()) {
				configList.removeFirst();
			}
			
		}
		if (currentTimeOffset - previousCircleTimeOffset >= ADD_NEW_CIRCLE_PERIOD) {
			previousCircleTimeOffset = currentTimeOffset;
			configList.addLast(new CircleConfig(defaultRadius, defaultStrokeWidth, CIRCLE_ALPHA_VALUE));
		}
	}
	
	private void drawRandomHeart(Canvas canvas) {
		int heartSize = heartList.size();
		if (heartSize > 0) {
			Paint heartPaint = new Paint();
			heartPaint.setAntiAlias(true);
			for (int i = 0; i < heartSize; i++) {
				HeartConfig heartConfig = heartList.get(i);
				heartPaint.setAlpha(heartConfig.getAlpha());
				Matrix matrix = new Matrix();
				matrix.setTranslate(heartConfig.getLeft(), heartConfig.getTop());
				matrix.postScale(heartConfig.getScale(), heartConfig.getScale(), 
											heartConfig.getLeft() + heartBitmap.getWidth() / 2,
											heartConfig.getTop() + heartBitmap.getHeight() / 2);
				canvas.drawBitmap(heartBitmap, matrix, heartPaint);
				heartConfig.refreshConfig();
			}
			
			HeartConfig firstConfig = heartList.getFirst();
			if (!firstConfig.isValid()) {
				heartList.removeFirst();
			}
		}
		if (currentTimeOffset - previousHeartTimeOffset > ADD_NEW_HEART_PERIOD) {
			previousHeartTimeOffset = currentTimeOffset;
			refillHeartList();
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		if (isRunning) {
			currentTimeOffset += TIME_INTERVAL;
			drawRunningCircle(canvas, width, height);
			drawMiddlePicture(canvas, width, height);
			drawRandomHeart(canvas);
			postInvalidateDelayed(TIME_INTERVAL);
		}
	}
	
	public void destroy() {
		isRunning = false;
		if (configList != null) {
			configList.clear();
		}
		if (logoBitmap != null) {
			logoBitmap.recycle();
		}
		if (heartList != null) {
			heartList.clear();
		}
		if (heartBitmap != null) {
			heartBitmap.recycle();
		}
	}
	
}
