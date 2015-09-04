package com.refuse.mchart_master;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.refuse.mchar.view.AniPie;

import java.util.ArrayList;
import java.util.List;

public class MChartActivity extends AppCompatActivity implements View.OnClickListener{

    private AniPie pie;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //		requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
        pie = (AniPie) findViewById(R.id.rp);
        pie.setOnClickListener(this);
        pie.setShowCenterAll(true);
        setdata();
    }

    public void showAni(View v) {

    }

    public void showLine(View v) {
        pie.setShowCenterAll(true);

    }

    @Override
    public void onClick(View v) {
        pie.invalidate();
    }

    public void clickSelector(View v) {

    }
    public void rotateAble(View v) {

    }

    long[] mHits = new long[5];
    public void lianji(View v){
		/*
		 * src the source array to copy the content.    copy的原数组
		srcPos the starting index of the content in src.   在数据哪个位置开复制
		dst the destination array to copy the data into.   复制到哪个目标数组
		dstPos the starting index for the copied content in dst. 在目标数组的那个位置开始复制
		length the number of elements to be copied.   // 复制数组的长度
		 *
		 *
		 */
        System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);  //  数组的值 向左移动1位
        mHits[mHits.length-1] = SystemClock.uptimeMillis(); //SystemClock.uptimeMillis();  距离开机的时间
        if (mHits[0] >= (SystemClock.uptimeMillis()-1000)) {
            // 三击事件
            Toast.makeText(getApplicationContext(), "5击事件", 0).show();

        }
    }
    private void setdata() {
        List<Float> data = new ArrayList<Float>();
        data.add(45f);
        data.add(35f);
        data.add(42f);
        data.add(15f);
        data.add(35f);
        pie.setPiedata(data);
        List<String> desc = new ArrayList<>();
        desc.add("33");
        desc.add("23");
        desc.add("13");
        desc.add("13");
        desc.add("13");
        pie.setDescPiedata(desc);
    }
}
