package com.tot.totcheck;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainFragmentActivity extends FragmentActivity {
	
	private ViewPager viewPager;
	private ActionBar actionBar;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_fragment);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		viewPager.setOffscreenPageLimit(3);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				actionBar.setSelectedNavigationItem(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		TabListener tabListener = new TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				viewPager.setCurrentItem(tab.getPosition());
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
		};
		
		ActionBar.Tab tabFilter = actionBar.newTab();
		tabFilter.setText("DOWN");
		tabFilter.setTabListener(tabListener);
		actionBar.addTab(tabFilter);
		
		ActionBar.Tab tabNotification = actionBar.newTab();
		tabNotification.setText("แจ้งเตือน");
		tabNotification.setTabListener(tabListener);
		actionBar.addTab(tabNotification);
		
		ActionBar.Tab tabSetting = actionBar.newTab();
		tabSetting.setText("ตั้งค่า");
		tabSetting.setTabListener(tabListener);
		actionBar.addTab(tabSetting);
		
		boolean start = isServiceRunning(NotificationService.class);
		if (!start) {
			Intent notificationService = new Intent(getApplicationContext(), NotificationService.class);
			startService(notificationService);
		}
		
		boolean notificationTab = getIntent().getBooleanExtra("notificationTab", false);
		if (notificationTab) {
			actionBar.setSelectedNavigationItem(1);
			viewPager.setCurrentItem(1);
		}
	}
	
	private boolean isServiceRunning(Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}

class PagerAdapter extends FragmentPagerAdapter {

	public PagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {

		Fragment fragment = null;
		
		if (arg0 == 0)
			fragment = new FilterFragment();
		else if (arg0 == 1)
			fragment = new NotificationFragment();
		else
			fragment = new SettingFragment();
		
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
}
