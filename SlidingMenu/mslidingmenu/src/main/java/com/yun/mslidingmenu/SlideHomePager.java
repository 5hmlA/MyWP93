package com.yun.mslidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class SlideHomePager extends RelativeLayout {
	private SlideMenu slidemenu;
	/**
	 * 无自定义属性调用
	 * @param context
	 * @param attrs
	 */
	public SlideHomePager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 有自定义属性调用
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public SlideHomePager(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!slidemenu.isClose) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction()==MotionEvent.ACTION_UP) {
			slidemenu.closeMenu();
		}
		return true;
//		return super.onTouchEvent(event);
	}
	public void attachToSlideMenu(SlideMenu slidemenu){
		this.slidemenu = slidemenu;
	}
}
