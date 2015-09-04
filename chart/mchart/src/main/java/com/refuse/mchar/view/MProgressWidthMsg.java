package com.refuse.mchar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.refuse.mchar.R;
import com.refuse.mchar.utills.DisplayUtils;

/**
 * Created by jiangzuyun on 2015/9/2.
 */
public class MProgressWidthMsg extends View {

    private int mColor;
    private Paint mPaint;
    /**
     * 右边文字左侧 距 进度条右边的距离
     */
    private float mTextToRec;
    private int mHeight;
    private int mWidth;
    //    private float mNumber = 100;
    /**
     * 单位
     */
    private String mUnit = "M";
    private int textColor = Color.BLACK;
    /**
     * 进度条的最大值
     */
    private float MaxProgress = 100;
    /**
     * 当前进度
     */
    private float CurrProgress = 100;
    private int mRecColor = Color.RED;
    /**
     * 进度条的圆角半径
     */
    private float recRound;

    private boolean showPerence;

    public MProgressWidthMsg(Context context) {
        this(context, null);
    }

    public MProgressWidthMsg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MProgressWidthMsg(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        if (attrs != null) {
            //attrs!=null说明 来自布局
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MProgressWidthMsg, defStyle, 0);
            mRecColor = typedArray.getColor(R.styleable.MProgressWidthMsg_recColor, Color.RED);
            textColor = typedArray.getColor(R.styleable.MProgressWidthMsg_textColor, Color.BLACK);
            mUnit = typedArray.getString(R.styleable.MProgressWidthMsg_unit);
            recRound = typedArray.getDimension(R.styleable.MProgressWidthMsg_recRound, 5);
            mTextToRec = typedArray.getDimension(R.styleable.MProgressWidthMsg_TextToProgress, 1);
            MaxProgress = typedArray.getFloat(R.styleable.MProgressWidthMsg_MaxProgress, 100);
            CurrProgress = typedArray.getFloat(R.styleable.MProgressWidthMsg_CurrProgress, 50);
            showPerence = typedArray.getBoolean(R.styleable.MProgressWidthMsg_percent, false);
            typedArray.recycle();
        }
        mUnit = TextUtils.isEmpty(mUnit) ? "" : mUnit;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mTextToRec = DisplayUtils.dip2px(getContext(), 2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //        mPaint.setColor(Color.GREEN);
        //        RectF mrect2 = new RectF(0, 0, mWidth, mHeight);
        //        canvas.drawRect(mrect2,mPaint);

        mPaint.setColor(textColor);
        //setTextSize 效果为设置文字高度一样
        mPaint.setTextSize(mHeight);
        Rect bounds = new Rect();
        String msg = CurrProgress + "";
        msg = (msg.endsWith(".0") ? msg.substring(0, msg.length() - 2) : msg) + mUnit;
        mPaint.getTextBounds(msg, 0, msg.length(), bounds);
        float textWidth = bounds.width();
        float textHeight = bounds.height();
        float recLenth = mWidth - textWidth - mTextToRec - 4;

        //画出 文字信息
        if (!showPerence) {
            canvas.drawText(msg, CurrProgress / MaxProgress * recLenth + mTextToRec, mHeight / 2 + textHeight / 2, mPaint);
        } else {
            canvas.drawText(CurrProgress * 100 / MaxProgress + "%", CurrProgress / MaxProgress * recLenth + mTextToRec, mHeight / 2 + textHeight / 2, mPaint);
        }

        RectF mrect = new RectF(0, 0, CurrProgress / MaxProgress * recLenth, mHeight);
        mPaint.setColor(mRecColor);
        canvas.drawRoundRect(mrect, recRound, recRound, mPaint);

        //        canvas.drawLine(0,mHeight/2,mWidth,mHeight/2,mPaint);

    }
}
