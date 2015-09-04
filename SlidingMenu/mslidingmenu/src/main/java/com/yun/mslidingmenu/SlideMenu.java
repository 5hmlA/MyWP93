package com.yun.mslidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import com.nineoldandroids.view.ViewHelper;

/**
 * 
 * 左右拖拉菜单 布局规定 slideMenu包裹一个整体布局，该布局内必须有且只有三个布局
 */
public class SlideMenu extends HorizontalScrollView {
	private int PADDING = 100;
	/** 距离的边距 */
	private int slideMenuPadding;
	/** dragmenu的宽度 */
	private int slideMenuWidth;
	/** screen width */
	private int screenWidth;
	/** 各个界面 */
	private ViewGroup dm_left_menu;
	private ViewGroup dm_right_menu;
	private SlideHomePager dm_content;
	/**
	 * 设置 侧滑模式 左边 右边 两边 无
	 */
	public SlideMode mode = SlideMode.BOUTH;
	public boolean isRightOpen, isLeftOpen, isClose = true;
	/**
	 * 使用哪边的菜单
	 */
	private String beside = "both";
	/**
	 * 菜单的数量
	 */
	private int menu_num =2;
	private float dy;
	private float dx;

	public enum SlideMode {
		ONLYLEFT, ONLYRIGHT, NONE, BOUTH
	}

	public SlideMenu(Context context) {
		this(context, null, 0);
	}

	/**
	 * 布局使用了 自定义属性时 调用
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	/**
	 * 布局没使用自定义 属性时 调用
	 * @param context
	 * @param attrs
	 */
	public SlideMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		Logger.LOWEST_LOG_LEVEL=8;
		screenWidth = getScreenWidth(getContext());
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SlideMenu);
		beside = a.getString(R.styleable.SlideMenu_slidemode);
		slideMenuPadding = (int) a.getDimension(
				R.styleable.SlideMenu_drag_menu_padding, 0f);
		if (TextUtils.isEmpty(beside)) {
			beside = "both";
		}
		menu_num = a.getInteger(R.styleable.SlideMenu_menu_num, 2);
		a.recycle();
	}

	/**
	 * 设置边距 使用默认的就好
	 * 
	 * @param dragMenuPadding
	 */
	public void setDragMenuPadding(int dragMenuPadding) {
		this.slideMenuPadding = dragMenuPadding;
	}

	/**
	 * 
	 */
	@Override
	protected void onFinishInflate() {

		Logger.e("SldeView", "onFinishInflate");

		// setScrollBarSize(0);

		slideMenuPadding = slideMenuPadding == 0
				|| slideMenuPadding < screenWidth / 5 * 2 ? screenWidth / 5 * 2
				: slideMenuPadding;
		slideMenuWidth = screenWidth - slideMenuPadding;

		ViewGroup vg = (ViewGroup) getChildAt(0);
		if (vg.getChildCount() == 3 && menu_num != 2) {
			throw new RuntimeException(
					"布局错误，必须有三个子布局,menu_num错误，应该为2，或者去掉一个菜单布局");
		}
		if (menu_num == 2) {
			dm_left_menu = (ViewGroup) vg.getChildAt(0);
			dm_content = (SlideHomePager) vg.getChildAt(1);
			dm_right_menu = (ViewGroup) vg.getChildAt(2);
			if (dm_left_menu == null || dm_right_menu == null
					|| dm_content == null) {
				throw new RuntimeException("布局错误，必须有三个子布局");
			}

			// 在有两个测边菜单的情况下 默认是两边可侧滑的，同时可设置 单一侧滑 左或者右
			mode = "left".equals(beside) ? SlideMode.ONLYLEFT : "right"
					.equals(beside) ? SlideMode.ONLYRIGHT : SlideMode.BOUTH;
			dm_right_menu.getLayoutParams().width = "left".equals(beside) ? 0
					: slideMenuWidth;
			dm_left_menu.getLayoutParams().width = "right".equals(beside) ? 0
					: slideMenuWidth;
		}
		if (vg.getChildCount() == 2 && "left".equals(beside)) {
			mode = SlideMode.ONLYLEFT;
			dm_left_menu = (ViewGroup) vg.getChildAt(0);
			dm_content = (SlideHomePager) vg.getChildAt(1);
			dm_left_menu.getLayoutParams().width = slideMenuWidth;
		}
		if (vg.getChildCount() == 2 && "right".equals(beside)) {
			mode = SlideMode.ONLYRIGHT;
			dm_content = (SlideHomePager) vg.getChildAt(0);
			dm_right_menu = (ViewGroup) vg.getChildAt(1);
			dm_right_menu.getLayoutParams().width = slideMenuWidth;
		}

		dm_content.getLayoutParams().width = screenWidth;
		super.onFinishInflate();
		dm_content.attachToSlideMenu(this);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		Logger.e("SldeView", "onSizeChanged");
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		Logger.e("SldeView", "onLayout");
		if (mode == SlideMode.ONLYLEFT || mode == SlideMode.BOUTH) {
			this.scrollTo(slideMenuWidth, 0);
		}
		if (mode == SlideMode.NONE
				&& ("left".equals(beside) || "both".equals(beside))) {
			this.scrollTo(slideMenuWidth, 0);
		}
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mode == SlideMode.NONE) {
			return false;
		}
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			dx = ev.getX();
			dy = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float mx = ev.getX();
			float my = ev.getY();
			if (Math.abs(my - dy) >= Math.abs(mx - dx)) {
				return false;// 斜着划 不拦截
			}
			dx = mx;
			dy = my;
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	//在onInterceptTouchEvent中拦截 (x>y)横向move(自己消费) 将横向move传递到 此处onTouchEvent 执行横向滑动 move事件在滑动之后返回false
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mode == SlideMode.NONE) {
			return false;
		}		
		super.onTouchEvent(ev);//横向滑动的代码在父类
		
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP://要让事件传递到up必须要down被消费
			Logger.e("SldeView", "slideMode：" + beside);
			// 向左offsetX为正
			int offsetX = getScrollX();
			if (offsetX < slideMenuWidth / 2 && mode != SlideMode.ONLYRIGHT) {
				this.smoothScrollTo(0, 0);
				isLeftOpen = true;
				isClose = false;
				Logger.e("SldeView", "打开了左边菜单");
			} else if (offsetX > slideMenuWidth / 2 * 3
					&& mode != SlideMode.ONLYLEFT) {
				this.smoothScrollTo(slideMenuWidth + slideMenuWidth, 0);
				isRightOpen = true;
				isClose = false;
				Logger.e("SldeView", "打开了右边菜单");
			} else if (offsetX > slideMenuWidth / 2
					&& mode == SlideMode.ONLYRIGHT) {
				Logger.e("SldeView", "ONLYRIGHT打开了右边菜单");
				this.smoothScrollTo(slideMenuWidth, 0);
				isRightOpen = true;
				isClose = false;
			} else {
				// 关闭
				if (mode == SlideMode.ONLYRIGHT) {
					this.smoothScrollTo(0, 0);
				} else {
					this.smoothScrollTo(slideMenuWidth, 0);
				}
				isClose = true;
				isRightOpen = false;
				isLeftOpen = false;
			}
//			return false;
		}
//		return super.onTouchEvent(ev);//当这里打开 就需要在up处返回true 因为如果没有返回true或在false的话 就会走这段代码走到父类 滑倒哪里滚到哪里
		return true;//必须返回true down返回true，，move才能够拿到事件 才能滚动(滚动实在move事件下完成的)
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (mode == SlideMode.ONLYRIGHT) {
			l = l + slideMenuWidth;
		}
		float scale = l * 1.0f / slideMenuWidth;
		float scale2 = (l - slideMenuWidth) * 1.0f / slideMenuWidth;

		float leftScale = 1 - 0.3f * scale;
		float contentScale = 0.8f + scale * 0.2f;
		float contentScale2 = 1.2f - scale * 0.2f;
		float rightScale = 0.7f + 0.3f * scale2;

		if (l > slideMenuWidth && l < slideMenuWidth + screenWidth) {
			if (dm_right_menu != null && mode != SlideMode.ONLYLEFT) {
				ViewHelper.setScaleX(dm_right_menu, rightScale);
				ViewHelper.setScaleY(dm_right_menu, rightScale);
				ViewHelper.setAlpha(dm_right_menu, 0.6f + 0.4f * (scale2));
				ViewHelper.setTranslationX(dm_right_menu, -10 * (1 - scale2));
			}
			ViewHelper.setPivotX(dm_content, screenWidth);
			ViewHelper.setPivotY(dm_content, dm_content.getHeight() / 2);
			ViewHelper.setScaleX(dm_content, contentScale2);
			ViewHelper.setScaleY(dm_content, contentScale2);

		} else if (l <= slideMenuWidth) {
			if (dm_left_menu != null && mode != SlideMode.ONLYRIGHT) {
				ViewHelper.setScaleX(dm_left_menu, leftScale);
				ViewHelper.setScaleY(dm_left_menu, leftScale);
				ViewHelper.setAlpha(dm_left_menu, 0.6f + 0.4f * (1 - scale));
				ViewHelper.setTranslationX(dm_left_menu, slideMenuWidth * scale
						* 0.7f);
			}
			ViewHelper.setPivotX(dm_content, 0);
			ViewHelper.setPivotY(dm_content, dm_content.getHeight() / 2);
			ViewHelper.setScaleX(dm_content, contentScale);
			ViewHelper.setScaleY(dm_content, contentScale);
		}

		super.onScrollChanged(l, t, oldl, oldt);
	}

	/**
	 * 打开右边菜单
	 */
	public void openRightMenu() {
		if (isLeftOpen || isRightOpen || mode == SlideMode.ONLYLEFT) {
			return;
		}
		if (mode == SlideMode.ONLYRIGHT) {
			this.smoothScrollTo(slideMenuWidth, 0);
		} else {
			this.smoothScrollTo(slideMenuWidth + slideMenuWidth, 0);
		}
		isClose = false;
		isRightOpen = true;
	}

	/**
	 * 打开左边菜单
	 */
	public void openLeftMenu() {
		if (isLeftOpen || isRightOpen || mode == SlideMode.ONLYRIGHT) {
			return;
		}

		this.smoothScrollTo(0, 0);
		isLeftOpen = true;
		isClose = false;
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu() {
		if (isLeftOpen) {
			isLeftOpen = false;
			Logger.e("SldeView", "关闭了左菜单");
		} else if (isRightOpen) {
			isRightOpen = false;
			Logger.e("SldeView", "关闭了右菜单");
		}
		if (mode == SlideMode.ONLYRIGHT) {
			this.smoothScrollTo(0, 0);
		} else {
			this.smoothScrollTo(slideMenuWidth, 0);
		}
		isClose = true;
		isRightOpen = false;
		isLeftOpen = false;
	}

	/**
	 * 切换菜单 true 打开左边菜单
	 */
	public void switchMenu(boolean dir) {
		if (isLeftOpen || isRightOpen) {
			closeMenu();
		} else {
			if (dir) {
				openLeftMenu();
			} else {
				openRightMenu();
			}
		}
	}

	/**
	 * 获得屏幕宽度
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public SlideMode getMode() {
		return mode;
	}

	public void setMode(SlideMode mode) {
		Logger.e("SldeView", "setMode");
		if (menu_num != 2) {
			Logger.e("SldeView", "setMode--只有一个侧边菜单无法改变侧滑模式");
			return;
		}

		if (mode == SlideMode.ONLYLEFT) {
			beside = "left";
			dm_right_menu.getLayoutParams().width = 0;
		} else if (mode == SlideMode.ONLYRIGHT) {
//			beside = "right";
//			dm_left_menu.getLayoutParams().width = 0;
			Log.e("error", "在两边都有菜单的时候切换到ONLYRIGHT存在bug！");
			return;
		} else if (mode == SlideMode.NONE) {
		} else {
			dm_right_menu.getLayoutParams().width = slideMenuWidth;
			dm_left_menu.getLayoutParams().width = slideMenuWidth;
		}
		this.mode = mode;
	}
	
	/**
	 * 设置是否显示log
	 * @param show
	 */
	public void setShowLog(boolean show){
		Logger.LOWEST_LOG_LEVEL=show?0:8;
	}
}
