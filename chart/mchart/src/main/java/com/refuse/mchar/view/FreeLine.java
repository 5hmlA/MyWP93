package com.refuse.mchar.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 *  数学方程的y和安卓坐标的y相反
 * @author yun 
 * @version 1
 * @Time 2015/7/4
 * 
 * 无论控件是否是正方形 都在中间部分画图

提示线 分为 斜线部分和 横线部分

showLine 是否显示 提示线的总开关（默认true 默认显示）

TstartAtTouch 设置提示线的起点(true表示从点击的地方开始画提示线(此时nRadius设置效果失效) 默认false)
nRadius 设置 提示线的起点(TstartAtTouch为true时无效)(1:表示从外辅助圆(饼图)的一半开始，0表示从圆心开始)原理：该值其实是内辅助圆的半径即提示线的起点

AniLine 设置是否展示提示线的显示动画（默认为true显示）
ANIDURINGTIME 设置 提示线显示动画的时间
movingShowTX 旋转的时候是否显示 提示线(默认为false，当为true的时候AniLine将失效，失去提示线的显示动画)

TshLong 设定提示线横线部分的长度 默认一直画到控件两边


cleanWire 清除所有的提示线条（外部调用需要调用postInvalidate刷新）

lpading 设置外辅助圆的大小 (该值可动态调整饼图的大小)（该值是外圆与控件周边的距离(默认控件较短边的1/4)，其与外圆半径成反比，越小则提示线的转折点越靠外，不建议修改 一般该值要比提示线上文字的长度的最长的那个大一点）

TsWidth 提示线的宽度

TsColor 提示线的颜色 默认黑色

 */
@SuppressLint("DrawAllocation")
public class FreeLine extends View {
	
	/**
	 * 画出圆的时候 显示全部的 提示线
	 * 
	 * 重要前提 showInCenAngle 为true
	 * 表示 提示线条 显示在对角线
	 * 为true的时候 TstartAtTouch 将失效
	 * 提示线 显示 的动画失效
	 */
	private boolean showCenterAll;
	/**
	 * 刚展现
	 */
	protected boolean firstShowAllLine;
	/**
	 * 第一次是否是全部显示
	 * setShowCenterAll里面会对其进行设置
	 */
	private boolean showCenterAllfirstTime;
	
	/**
	 * 由饼图传递 该值  点中的扇形位置
	 */
	protected int selectedPosition;
	
	/**
	 * 由饼图传递 该值 已经旋转的角度
	 */
	protected float rotedAngle;
	
	/**
	 * 解析好的 角度集合
	 */
	private List<Float> anglesList = new ArrayList<Float>();
	/**
	 * 解析好的 提示信息集合
	 */
	private List<String> showMsg = new ArrayList<String>();
	/**
	 * 设置 提示线 显示在 扇形的对角线处   默认false 起始位置为内辅助圆 nRadius决定
	 * 其他设置 提示线的起始位置 将失效TstartAtTouch 
	 */
	protected boolean showInCenAngle ;
	/**
	 * 提示线 的转折点坐标 横方向的起点坐标 斜线的终点坐标 斜线与外圆的交点坐标
	 */
	private float turnX;
	/**
	 * 提示线 的转折点坐标 横方向的起点坐标 斜线的终点坐标 斜线与外圆的交点坐标
	 */
	private float turnY;
	/**
	 * 提示线 横方向的终点坐标
	 */
	private float endx;
	/**
	 * 提示线 横方向的终点坐标
	 */
	private float endy;

	/**
	 * 是否显示 辅助的外圆 (外辅助圆)提示线折点所在的圆 true显示 默认不显示
	 */
	public boolean showW = false;
	/**
	 * 提示线条 的斜线部分 从点处开始 默认为false 即不从点击处开始画提示线 默认提示线虫 中点(外辅助圆的)开始画 如过该值为true
	 * 那么nRadius将失去作用 无法自定义提示线的斜线长
	 */
	public boolean TstartAtTouch;
	/**
	 * 是否 动态画提示线
	 * 默认是 true 显示动画
	 */
	protected boolean AniLine = true;
	/**
	 * 提示线的 动画时长
	 */
	public long ANIDURINGTIME = 500;
	/**
	 * 内辅助圆的半径 设定提示线的斜线 多长 默认是外圆半径的一半 内辅助圆半径 默认 1 表示 斜线长为外辅助圆半径的一半 默认提示线从
	 * 中点(外辅助圆的)开始画 值为0 表示 提示线从圆心开始画
	 */
	public float nRadius = 1;
	/**
	 * 旋转的时候是否显示提示线条 默认false不显示 当旋转的时候 显示提示线条的话 move之后的up还会显示
	 * 为true的时候 旋转过程中 提示线动画被取消 没旋转则会有动画 无法取消
	 * AniLine设置无效
	 */
	public boolean movingShowTX;
	/**
	 * 清除所有 提示信息 外部调用 需要postinvate刷新
	 */
	public boolean cleanWire = false;
	/**
	 * 总开关 true显示 false不显示
	 */
	public boolean showLine = true;
	/**
	 * 提示线 的横线部分的 长度
	 */
	protected float TshLong;
	
	/**
	 * 提示线的宽度
	 */
	public float TsWidth;
	/**
	 * 提示线的颜色 默认黑色
	 */
	public int TsColor = Color.BLACK;
	/**
	 * 文字的颜色
	 */
	public int TextColor = Color.BLACK;
	
	/**
	 * 外辅助圆局四周的边距 决定着 外辅助圆的半径 该值和外辅助圆半径成反比
	 */
	protected float lpading = 50;
	/**
	 * 外辅助圆的半径 不能直接设定
	 */
	protected float wRadius;
	

	private float progress;
	private float b;
	private float k;
	private float down_y;
	private float down_x;

	private int width;
	private int height;
	protected int just;
	private float centX;
	private float centY;
	private Paint mPaint;
	private Paint pLine;
	private float Tstartx;
	private float Tstarty;
	// 动画展示 提示线 斜线部分
	private boolean drawAniLine;

	/**
	 * 判断是否执行过move动作
	 */
	private boolean ismoved = false;
	private Paint textP;
	private int textMarging = 5;
	/**
	 * 提示文字大小
	 */
	protected float TtextSize = 20;

	public FreeLine(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public FreeLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		pLine = new Paint(Paint.ANTI_ALIAS_FLAG);
		textP = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	public FreeLine(Context context) {
		super(context);
	}

	/**
	 * 此时 获取控件的 宽高
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = getWidth();
		height = getHeight();
		just = width > height ? height : width;
		centX = width * 1f / 2;
		centY = height * 1f / 2;
		// 设置默认值
		wRadius = just / 2 - lpading;
		if (TshLong == 0) {
			TshLong = width / 2;
		}
		lpading = just/4;
		//初始化
		pLine.setStrokeWidth(TsWidth);
		pLine.setColor(TsColor);
		if (TtextSize==10) {
			textP.setTextSize(10);
		}
	}

	// down的时候 清除所有提示线 move过则 up的时候 不显示提示线
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 设置默认值
		wRadius = just / 2 - lpading;
		if (nRadius == 1) {
			nRadius = wRadius / 2;
		}
		if (!showLine) {
			return;
		}
		if (cleanWire) {
			return;
		}
		// 无论控件 那边大 都在中间的正方形画饼图
		float mWidth = 0;// 宽比高宽多少
		float mHeight = 0;// 高比宽高多少
		if (width > height) {
			mWidth = width - height;
		} else {
			mHeight = height - width;
		}

		//==============================画辅助外圆==========================
		if (showW) {
			// 画 提示线转折点所在的 辅助 圆
			mPaint.setStyle(Style.STROKE);
			RectF oval4 = new RectF(mWidth / 2 + lpading,
					mHeight / 2 + lpading, just + mWidth / 2 - lpading, just
							+ mHeight / 2 - lpading);
			// 计算 外 辅助 圆的半径
			wRadius = just / 2 - lpading;
			canvas.drawArc(oval4, 0, 360, true, mPaint);
			
			//画内辅助圆
			canvas.drawCircle(centX, centY, nRadius, mPaint);
			//画圆心
			canvas.drawPoint(centX, centY, mPaint);
		}

		//===================================showAll====画出全部 提示线===========================================
		if (showInCenAngle&&showCenterAll) {
			if (anglesList.size()==0) {
				throw new RuntimeException("数据不足请先设置数据。。。调用setPieAngles()");
			}
			for (int i = 0; i < anglesList.size(); i++) {
				float cenAngle = anglesList.get(i)+rotedAngle;
				cenAngle = cenAngle>360?cenAngle-360:cenAngle;
				cenAngle = cenAngle<0?cenAngle+360:cenAngle;
				//获取起点坐标
				double[] startPoint = getTurnPoint(cenAngle, centX, centY, nRadius);
				float Tstartx2 = (float) startPoint[0];
				float Tstarty2 = (float) startPoint[1];
				//获取终点坐标
				double[] turnPoint2 = getTurnPoint(cenAngle, centX, centY, wRadius);
				float turnX2 = (float) turnPoint2[0];
				float turnY2 = (float) turnPoint2[1];
						
				canvas.drawLine(Tstartx2, Tstarty2, turnX2, turnY2, pLine);
				float endx2 = turnX2 + TshLong;
				if (cenAngle<270&&cenAngle>90) {
					endx2 = turnX2 - TshLong;
				}
				float endy2 = turnY2;
				canvas.drawLine(turnX2, turnY2, endx2, endy2, pLine);
				
				float textL = textP.measureText(showMsg.get(i));
				turnX2 = turnX2<width/2?turnX2-textL-textMarging:turnX2+textMarging;
				canvas.drawText(showMsg.get(i), turnX2, turnY2, textP);
			}
			
		}
		//=======================================画提示线(有无动画)============================================
		if (!AniLine) {
			canvas.drawLine(Tstartx, Tstarty, turnX, turnY, pLine);
			endy = turnY;
			canvas.drawLine(turnX, turnY, endx, endy, pLine);
//			canvas.drawText(text, x, y, textP);
			return;
		}
		//执行 画线 动画
		if (drawAniLine && !ismoved) {// 分母k不可以为0  移动的时候 无动画
			// pLine 画斜线
			// 画斜线动画
			canvas.drawLine(Tstartx, Tstarty, (-progress - b) / k, progress,
					pLine);
			// 当斜线画完之后 在画横线 误差允许范围内
			if (Math.abs(progress - turnY) < 0.0005) {
				// 画直线
				endy = turnY;
				canvas.drawLine(turnX, turnY, endx, endy, pLine);
			}
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// System.out.println("dispatchTouchEvent");
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!showLine) {
				return true;
			}
			ismoved = false;// 清除move状态
			cleanWire = true;// down的时候 取消提示线 true不显示提示线
			down_x = event.getX();
			down_y = event.getY();
			postInvalidate();
			return true; // 当事件被消费之后 后续事件才会被该控件处理
		case MotionEvent.ACTION_MOVE:
			if (!showLine) {
				return true;
			}
			float move_x = event.getX();
			float move_y = event.getY();
			
			//真机上 很容易出发move 所以给个允许误差0.001
			if (Math.abs(move_x-down_x)>0.0001||Math.abs(move_y-down_y)>0.0001) {
				ismoved = true;// 记录move状态
			}

			if (!movingShowTX) {
				// 如果 旋转的时候 不允许显示 提示线条的话 结束
				return true;// 不可以是false 否则后续事件将会丢失
			}

			cleanWire = false;// 表示 显示提示线
			
			//=========================================对角线显示============================
//			TODO  需要点中的那个扇形的位置
			//当设置为 提示线显示在扇形对角线时
			if (showInCenAngle) {
				showCenterAll = false;
				if (anglesList.size()==0||showMsg.size()==0) {
					throw new RuntimeException("数据不足请先设置数据。。。调用setPieAngles()");
				}
				showCenterGetPoint();
				AniLine = false;
				postInvalidate();
				break;//TODO  换成true
			}

			//=======================================内圆起点 经过点击点 显示 ===============================================
			// 获取 当前点 和圆心 引出的斜线 和 外辅助圆 的交点 拿到的是转折点的坐标
			double[] turnPoint = getTurnPoint(move_x, move_y, centX, centY,
					wRadius);

			// 折点就是交点
			turnX = (float) turnPoint[0];
			turnY = (float) turnPoint[1];

			// 根据点击的位置判断 提示线的横线 往哪个方向
			if (move_x > centX) {
				// endx = width;
				endx = turnX + TshLong;
//				Log.d("FreeLine-----横方向", "右边");
			} else {
				// endx = 0;
				endx = turnX - TshLong;
//				Log.d("FreeLine-----横方向", "左边");
			}

			// ==================TstartAtTouch为true表示 点在哪 提示线从哪开始画=============================================
			if (TstartAtTouch) {
				Tstartx = move_x;
				Tstarty = move_y;
			} else {
				// 获取 当前点 和圆心 引出的斜线 和 内辅助圆 的交点 拿到的是起点的坐标
				double[] turnPoint2 = getTurnPoint(move_x, move_y, centX,
						centY, nRadius);
				// 提示线 斜线 起点坐标
				Tstartx = (float) turnPoint2[0];
				Tstarty = (float) turnPoint2[1];
			}
			AniLine = false;
			postInvalidate();
			return true; // 当事件被消费之后 后续事件才会被该控件处理
		case MotionEvent.ACTION_UP:
			if (!showLine) {
				return true;
			}
			
			if (showInCenAngle) {
				cleanWire = false;//down的时候 设置为true了
				if (movingShowTX) {
					cleanWire = !movingShowTX;// 旋转的时候显示 那么 停止的时候也显示 提示线
					 AniLine = !ismoved;//没旋转 move的话 执行显示动画
					 //TODO  去掉下面那行 看看什么情况
					AniLine = false; //旋转的时候显示提示线 无论是否旋转都不会有动画 AniLine设置无效
				} else {
					cleanWire = ismoved;// 前提是：旋转的时候不显示，然后根据move的状态 决定是否 提示线
				}
				
				if (showCenterAllfirstTime) {
					//画出 所有 提示线 //无动画
					showCenterAll = true;
					postInvalidate();
				}else {
					showCenterGetPoint();
//					AniLine = false;
					if (AniLine) {
						drawAniLine(Tstarty,turnY);
					} else {
						postInvalidate();// 刷新界面
					}
				}
				break;
			}
			if (movingShowTX) {
				cleanWire = !movingShowTX;// 旋转的时候显示 那么 停止的时候也显示 提示线
				 AniLine = !ismoved;
//				AniLine = false; //旋转的时候显示提示线 无论是否旋转都不会有动画 AniLine设置无效
			} else {
				cleanWire = ismoved;// 前提是：旋转的时候不显示，然后根据move的状态 决定是否 提示线
			}
			float up_x = event.getX();
			float up_y = event.getY();

			// 设置 提示线的起点为默认圆心
			if (Tstartx == 0) {
				Tstartx = centX;
				Tstarty = centY;
			}

			// 获取 当前点 和圆心 引出的斜线 和 外辅助圆 的交点 拿的是提示线转折点坐标
			double[] turnPoint1 = getTurnPoint(up_x, up_y, centX, centY,
					wRadius);

			// 折点就是交点
			turnX = (float) turnPoint1[0];
			turnY = (float) turnPoint1[1];
			// 根据点击的位置判断 提示线的横线 往哪个方向
			if (up_x > centX) {
				// endx = width;
				endx = turnX + TshLong;
//				Log.d("FreeLine-----横方向", "右边");
			} else {
				// endx = 0;
				endx = turnX - TshLong;
//				Log.d("FreeLine-----横方向", "左边");
			}
			// TstartAtTouch为true表示 点在哪 提示线从哪开始画
			if (TstartAtTouch) {
				Tstartx = up_x;
				Tstarty = up_y;
			} else {
				// 获取 当前点 和圆心 引出的斜线 和 内辅助圆 的交点 拿的是 提示线 起点坐标
				double[] turnPoint21 = getTurnPoint(up_x, up_y, centX, centY,
						nRadius);
				// 提示线 斜线 起点坐标
				Tstartx = (float) turnPoint21[0];
				Tstarty = (float) turnPoint21[1];
			}
			// turnX = up_x;
			// turnY = up_y;

			if (AniLine) {
				drawAniLine(Tstarty,turnY);
			} else {
				postInvalidate();// 刷新界面
			}
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	/**
	 * 获取当前选中的扇形 应该显示提示线的 各点坐标
	 */
	public void showCenterGetPoint() {
		float cenAngle = anglesList.get(selectedPosition)+rotedAngle;
		cenAngle = cenAngle>360?cenAngle-360:cenAngle;
		cenAngle = cenAngle<0?cenAngle+360:cenAngle;
		
		//获取起点
		double[] startPoint = getTurnPoint(cenAngle, centX, centY, nRadius);
		Tstartx = (float) startPoint[0];
		Tstarty = (float) startPoint[1];
		double[] endPoint = getTurnPoint(cenAngle, centX, centY, wRadius);
		turnX = (float) endPoint[0];
		turnY = (float) endPoint[1];
//		AniLine = false;
		endx = turnX + TshLong;
		if (cenAngle<270&&cenAngle>90) {
			endx = turnX - TshLong;
		}
		float endy = turnY;
	}

	protected void drawAniLine(float Tstarty,float turnY2) {
		// TODO Auto-generated method stub
		drawAniLine = true;
		// ObjectAnimator的缺点是 该变化的属性 必须要有get和set方法
		// ObjectAnimator.ofFloat(this, "progress",
		// centY,turnY2).setDuration(1000).start();

		// 不需要写 get和set方法
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(Tstarty, turnY2)
				.setDuration(ANIDURINGTIME);
		// ValueAnimator valueAnimator = ValueAnimator.ofFloat(centY, turnY2)
		// .setDuration(ANIDURINGTIME);
		valueAnimator.setInterpolator(new AccelerateInterpolator(2));
		valueAnimator.start();
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				progress = (float) animation.getAnimatedValue();
				postInvalidate();// 必须刷新 界面 否则无效果
			}
		});
	}

	/**
	 * 
	 * 根据圆内 一点 以及圆心坐标 半径 得出 该点与圆心所在的直线和 圆的交点坐标（圆心射向某特定点的方向）
	 * 
	 * @param up_x
	 *            经过指定的点的坐标
	 * @param up_y
	 *            经过指定的点的坐标
	 * @param cenx
	 *            圆心坐标
	 * @param ceny
	 *            圆心坐标
	 * @param radius
	 *            圆的半径
	 * @return 返回 交点坐标 一个数组
	 */
	public double[] getTurnPoint(float up_x, float up_y, float cenx,
			float ceny, float radius) {

		// 计算 提示线 斜边的 直线方程(经过圆心) 需要注意的是 数学坐标系中的y轴和 安卓中的y轴相反
		// y = k*x + b
		k = (-up_y + ceny) / (up_x - cenx);
		b = -ceny - k * cenx;
		// 验证直线是否正确 由于是float数据 无法精确 必须允许误差存在 (允许误差0.005)
		if ((-up_y - k * up_x - b) < 0.0005) {
//			Log.d("FreeLine-----直线方程正确", "斜率:" + k + "====b:" + b);
//			Log.d("FreeLine-----当前点击", up_x + "====" + up_y);
		}

		// 计算 经过圆心的直线与圆的交点 可以通过几何解决 更方便
		// 1，获取直线与水平右的夹角
		double pointAngle = getPointAngle(up_x, up_y, cenx, ceny);
		// 2，通过直角三角函数解决问题 画图理解
		// sy 斜边与 圆相交是 内部直角三角形 y的长 为甚么选sin函数而不是cos（sin ++ - -）
		double sy = radius * Math.sin(pointAngle / 180 * Math.PI);
		// 交点 坐标 (xx1 , yy1)
		double yy1 = ceny + sy;
		// 根据直线方程 计算交点 横坐标 x = (y - b)/k 注意安卓的y和函数方程的y相反
		double xx1 = (-yy1 - b) / k;
		
		//根据圆方程 获取交点 比较复杂 但可以
//		double temp1 = Math.sqrt(radius*radius - Math.pow(-yy1 + centY, 2));
//		temp1 = up_x<centX?-temp1:temp1;
//		double xx1 = centX + temp1;

//		Log.d("FreeLine-----交点", xx1 + "=====" + yy1);

		double[] turnPoint = new double[2];
		turnPoint[0] = xx1;
		turnPoint[1] = yy1;

		return turnPoint;

	}
	
	/**
	 * 根据所在的 角度 获取该角度所在直线 与圆的交点
	 * @param cenAngle	已知角度
	 * @param cenx	圆心
	 * @param ceny   圆心
	 * @param radius		半径
	 * @return
	 */
	public double[] getTurnPoint(float cenAngle ,float cenx,
			float ceny, float radius) {
		//交点坐标
		double xx1 = 0;
		double yy1 = 0;
		if (cenAngle==0||cenAngle==360) {
			xx1 = centX  + radius;
			yy1 = centY;
		}
		if (cenAngle==90) {
			xx1 = centX;
			yy1 = centY + radius;
		}
		if (cenAngle==180) {
			xx1 = centX  - radius;
			yy1 = centY;
		}
		if (cenAngle==270) {
			xx1 = centX;
			yy1 = centY - radius;
		}
		// 计算 经过圆心的直线与圆的交点 可以通过几何解决 更方便
		// 1，获取直线与水平右的夹角
		double pointAngle = cenAngle;
		// 2，通过直角三角函数解决问题 画图理解
		// sy 斜边与 圆相交是 内部直角三角形 y的长 为甚么选sin函数而不是cos（sin ++ - -）
		double sy = radius * Math.sin(pointAngle / 180 * Math.PI);
		// 交点 坐标 (xx1 , yy1)
		yy1 = ceny + sy;
		// 根据 圆方程 计算xx1 坐标  (x-a)^2 - (y-b)^2 = r^2  注意安卓的y和函数方程的y相反
		//由于已知y对应圆方程 有两个解 需要判断
		//x-a = (+或者-) 根号(r^2 - (y-b)^2)
		double temp1 = Math.sqrt(radius*radius - Math.pow(-yy1 + centY, 2));
		temp1 = (cenAngle<270)&&(cenAngle>90)?-temp1:temp1;//左半圆 x更小 右半圆x更大
		xx1 = centX + temp1;
		
//		Log.d("FreeLine-----交点", xx1 + "=====" + yy1);
		
		double[] turnPoint = new double[2];
		turnPoint[0] = xx1;
		turnPoint[1] = yy1;
		
		return turnPoint;
	}
	
	/**
	 * 设置 需要 角度  需要显示的提示内容
	 * list 对应饼图扇形 顺序 数量
	 * Map数据  可按需要添加
	 * angle -- 角度值  float类型
	 * show --  提示内容 String类型
	 * 
	 * @param angles
	 */
	protected void setPieAngles(List<Map<String, Object>> showDatas){
		try {
			for (int i = 0; i < showDatas.size(); i++) {
				Map<String, Object> map = showDatas.get(i);
//		解析出 角度 集合
				float angle = (float) map.get("angle");
				anglesList.add(angle);
//		解析出 提示内容集合
				String show = (String) map.get("show");
				showMsg.add(show);
			}
			showInCenAngle = true;
		} catch (Exception e) {
			throw new RuntimeException("FreeLine====setPieAngles传入的 数据格式错误");
		}
	}

	/**
	 * 获取圆内 某指定点与圆心的连线 和 水平右的夹角
	 * 
	 * @param up_x
	 *            指定点坐标
	 * @param up_y
	 *            指定点坐标
	 * @return
	 */
	public double getPointAngle(float up_x, float up_y, float cenx, float ceny) {
		float a = up_x - cenx;
		float b = up_y - ceny;
		// 斜边
		double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		// 获取 弧度
		double acos = Math.acos(a / c);
		// 获取 角度 角度=弧度/PI * 180
		double clickAngle = acos / Math.PI * 180;// 注意 获取的只是0-180 还需要判断
		if (up_y > ceny) {
			// 点击位于 下半圆
		} else {
			// 点击位于 上半圆
			clickAngle = 2 * 180 - clickAngle;
		}
		return clickAngle;
	}

	public float getTstartx() {
		return Tstartx;
	}

	public void setTstartx(float tstartx) {
		Tstartx = tstartx;
	}

	public float getTstarty() {
		return Tstarty;
	}

	public void setTstarty(float tstarty) {
		Tstarty = tstarty;
	}
	
	

	public void setShowCenterAll(boolean showCenterAll) {
		this.showCenterAll = showCenterAll;
		showInCenAngle = showCenterAll;
		showCenterAllfirstTime = showCenterAll;
		postInvalidate();
	}

	/**
	 * 返回外辅助圆的半径 该半径不能直接设置 只能通过lpading设定 lpading表示外辅助圆局四周的边距 决定着 外辅助圆的半径
	 * 该值和外辅助圆半径成反比
	 * 
	 * @return
	 */
	public float getwRadius() {
		return wRadius;
	}

	public int getTextMarging() {
		return textMarging;
	}
	
	

	public float getTtextSize() {
		return TtextSize;
	}

	public void setTtextSize(float ttextSize) {
		TtextSize = ttextSize;
		textP.setTextSize(ttextSize);
	}

	public void setTextMarging(int textMarging) {
		this.textMarging = textMarging;
	}
	
	

}
