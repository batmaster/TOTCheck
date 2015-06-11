package com.tot.totcheck;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PreferenceBoxView extends RelativeLayout {
	
	private TextView textViewTitle;
	private TextView textViewText;
	private CheckBox checkBoxEnable;
	private String pref;

	private String key;
	
	private static int titleId = 1000000;

	public PreferenceBoxView(Context context) {
		this(context, null);
	}

	public PreferenceBoxView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public PreferenceBoxView(final Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPadding(30, 30, 30, 30);

		pref = SharedValues.TOT_PREF_SETTINGS;
		
		RelativeLayout.LayoutParams textViewTitleLayoutParam = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textViewTitleLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		textViewTitle = new TextView(context);
		textViewTitle.setId(titleId++);
		textViewTitle.setTypeface(null, Typeface.BOLD);
		textViewTitle.setTextColor(Color.parseColor("#323232"));
		addView(textViewTitle, textViewTitleLayoutParam);
		
		RelativeLayout.LayoutParams textViewTextLayoutParam = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textViewTextLayoutParam.addRule(RelativeLayout.BELOW, textViewTitle.getId());
		textViewText = new TextView(context);
		textViewText.setTextColor(Color.parseColor("#323232"));
		addView(textViewText, textViewTextLayoutParam);
		
		RelativeLayout.LayoutParams checkBoxEnableLayoutParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		checkBoxEnableLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		checkBoxEnable = new CheckBox(context);
		addView(checkBoxEnable, checkBoxEnableLayoutParam);
		
		TypedArray arrs = context.obtainStyledAttributes(attrs, R.styleable.PreferenceBoxView);
		textViewTitle.setText(arrs.getString(R.styleable.PreferenceBoxView_title));
		textViewText.setText(arrs.getString(R.styleable.PreferenceBoxView_text));
		checkBoxEnable.setChecked(arrs.getBoolean(R.styleable.PreferenceBoxView_checked, false));
		key = arrs.getString(R.styleable.PreferenceBoxView_key) ;
		
		checkBoxEnable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedValues.setEnableStatePref(context, pref, key, isChecked);
			}
		});
		
		if (textViewText.getText().toString().equals(""))
			textViewText.setVisibility(View.GONE);
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

	public String getPref() {
		return pref;
	}

	public void setPref(String pref) {
		this.pref = pref;
	}

}
