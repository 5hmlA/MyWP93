package com.yun.mslidingmenu_mastet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.yun.mslidingmenu.SlideMenu;


public class MainActivity extends Activity
{

	private SlideMenu dm ;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		Window window = getWindow();
//		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		dm = (SlideMenu) this.findViewById(R.id.dm);
	}
	public void onlyRight(View v) {
		dm.setMode(SlideMenu.SlideMode.ONLYRIGHT);
		dm.switchMenu(true);
	}
	public void onlyLeft(View v) {
		dm.setMode(SlideMenu.SlideMode.ONLYLEFT);
		dm.switchMenu(true);
	}
	public void NONE(View v) {
		dm.setMode(SlideMenu.SlideMode.NONE);
		dm.switchMenu(true);
	}
	public void BOUTH(View v) {
		dm.setMode(SlideMenu.SlideMode.BOUTH);
		dm.switchMenu(true);
	}
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.ibtn_icon1:
//			dm.switchMenu(true);
			dm.setMode(SlideMenu.SlideMode.ONLYLEFT);
			break;
		case R.id.ibtn_icon2:
//			dm.switchMenu(false);
			dm.setMode(SlideMenu.SlideMode.ONLYRIGHT);
			break;
		}
	}

}
