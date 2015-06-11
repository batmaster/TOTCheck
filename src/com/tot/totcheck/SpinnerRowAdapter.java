package com.tot.totcheck;

import java.sql.Array;
import java.util.List;

import javax.crypto.spec.PSource;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class SpinnerRowAdapter extends ArrayAdapter<SpinnerRowItem> {

	private Context context;
	private List<SpinnerRowItem> list;

	public SpinnerRowAdapter(Context context, List<SpinnerRowItem> list) {
		super(context, R.layout.spinner_row, list);
		this.context = context;
		this.list = list;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
	    return getView(position, convertView, parent);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.spinner_row, parent, false);
		
		TextView province = (TextView) row.findViewById(R.id.textViewProvince);
		province.setText(list.get(position).getProvince());
		
		TextView amount = (TextView) row.findViewById(R.id.textViewAmount);
		amount.setText(String.valueOf(list.get(position).getAmount()));
		
		return row;
	}

}
