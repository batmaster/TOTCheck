package com.tot.totcheck;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreferenceBoxView extends LinearLayout {
	
	private TextView textViewTitle;
	private TextView textViewText;
	private CheckBox checkBoxEnable;
	private String key;

	public PreferenceBoxView(Context context) {
		this(context, null);
	}

	public PreferenceBoxView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public PreferenceBoxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOrientation(VERTICAL);
//		
//		RelativeLayout firstHorizontal = new RelativeLayout(context);
//		RelativeLayout.LayoutParams firstLayoutParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		firstLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//		textViewTitle = new TextView(context);
//		firstHorizontal.addView(textViewTitle, firstLayoutParam);
//		
//		checkBoxEnable = new CheckBox(context);
//		RelativeLayout.LayoutParams secondLayoutParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		firstLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		firstHorizontal.addView(checkBoxEnable, secondLayoutParam);
//		
//		addView(firstHorizontal);
//		
		textViewText = new TextView(context);
		addView(textViewText);
	}
	
	public void setTitle(String title) {
		textViewTitle.setText(title);
	}
	
	public String getTitle() {
		return textViewTitle.getText().toString();
	}
	
	public void setText(String text) {
		textViewText.setText(text);
	}
	
	public String getText() {
		return textViewText.getText().toString();
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setChecked(boolean checked) {
		checkBoxEnable.setChecked(checked);
	}
	
	public boolean getChecked() {
		return checkBoxEnable.isChecked();
	}



}
