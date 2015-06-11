package com.tot.totcheck;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListViewRowAdapter extends ArrayAdapter<ListViewRowItem> {
	
	private Context context;
	private List<ListViewRowItem> list;
	private boolean showProvince;

	public ListViewRowAdapter(Context context, List<ListViewRowItem> list, boolean showProvince) {
		super(context, R.layout.listview_row, list);
		this.context = context;
		this.list = list;
		this.showProvince = showProvince;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.listview_row, parent, false);
		
		TextView number = (TextView) row.findViewById(R.id.textViewNumber);
		number.setText(String.valueOf(list.get(position).getId_nu()));
		
		TextView device = (TextView) row.findViewById(R.id.textViewDevice);
		device.setText(list.get(position).getNode_name());
		
		TextView ip = (TextView) row.findViewById(R.id.textViewIp);
		ip.setText(list.get(position).getNode_ip());
		
		TextView date = (TextView) row.findViewById(R.id.textViewDate);
		date.setText(list.get(position).getNode_time_down());
		
		TextView province = (TextView) row.findViewById(R.id.textViewProvince);
		province.setText(list.get(position).getProvince());
		
		if (list.get(position).getNode_name().equals("ไม่มีรายการ")) {
			TextView down = (TextView) row.findViewById(R.id.textViewDown);
			down.setVisibility(View.GONE);
		}
		
		if (showProvince)
			province.setVisibility(View.VISIBLE);
		
		return row;
	}
}
