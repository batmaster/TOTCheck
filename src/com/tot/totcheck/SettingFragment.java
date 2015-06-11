package com.tot.totcheck;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SettingFragment extends Fragment {
	
	private LinearLayout linearLayoutSetting;
	
	public SettingFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		
		linearLayoutSetting = (LinearLayout) view.findViewById(R.id.linearLayoutSetting);
		
		
		return view;
	}

}
