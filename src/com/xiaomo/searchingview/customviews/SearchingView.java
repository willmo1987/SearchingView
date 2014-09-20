package com.xiaomo.searchingview.customviews;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.xiaomo.searchingview.R;

public class SearchingView extends View {

	private static final int REDRAW_VIEW = 197;
	private static final long TIME_GAP = 5L;
	private static final int ALPHA_VALUE = 180;
	private static final long ADD_NEW_CIRCLE_PERIOD = 300L;
	
	private float defaultStrokeWidth;
	private float defaultStrokeWidthGap;
	private float defaultRadius;
	private float defaultRadiusGap;
	private int defaultAlphaGap;
	private boolean isRunning = false;
	private long currentTimeOffset;
	private long previousTimeOffset;
	private LinkedList<CircleConfig> configList;
	private Bitmap bgBitmap;
	private Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			invalidate();
		}
		
	};
	
	public SearchingView(Context context) {
		super(context);
		
	}
	
	public SearchingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void initialize() {
		bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		defaultStrokeWidth = getResources().getDimension(R.dimen.circle_width);
		defaultRadius = getResources().getDimension(R.dimen.circle_radius);
		defaultRadiusGap = getResources().getDimension(R.dimen.radius_gap);
		defaultStrokeWidthGap = getResources().getDimension(R.dimen.stroke_gap);
		defaultAlphaGap = 1;
		configList = new LinkedList<CircleConfig>();
	}
	
	public void startRunning() {
		if (bgBitmap == null) {
			initialize();
		}
		CircleConfig config = new CircleConfig(defaultRadius, defaultStrokeWidth, ALPHA_VALUE);
		configList.addLast(config);
		isRunning = true;
		invalidate();
	}
	
	public void stopRunning() {
		isRunning = false;
		if (configList != null) {
			configList.clear();
		}
		currentTimeOffset = 0;
		previousTimeOffset = 0;
	}
	
	private void drawMiddlePicture(Canvas canvas, int width, int height) {
		if (bgBitmap != null) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			float left = (width - bgBitmap.getWidth()) / 2;
			float top = (height - bgBitmap.getHeight()) / 2;
			canvas.drawBitmap(bgBitmap, left, top, paint);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		
		if (isRunning) {
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
					config.refreshConfig(defaultRadiusGap, defaultStrokeWidthGap, defaultAlphaGap, height / 2);
				}
				
				CircleConfig firstConfig = configList.getFirst();
				if (!firstConfig.isValid()) {
					configList.removeFirst();
				}
				
				currentTimeOffset += TIME_GAP;
				if (currentTimeOffset - previousTimeOffset >= ADD_NEW_CIRCLE_PERIOD) {
					previousTimeOffset = currentTimeOffset;
					CircleConfig config = new CircleConfig(defaultRadius, defaultStrokeWidth, ALPHA_VALUE);
					configList.addLast(config);
				}
				
				handler.sendEmptyMessageDelayed(REDRAW_VIEW, TIME_GAP);
			}
			
			drawMiddlePicture(canvas, width, height);
		}
		
	}

}
