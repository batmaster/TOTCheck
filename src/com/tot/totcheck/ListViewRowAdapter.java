package com.tot.totcheck;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
		
		TextView province = (TextView) row.findViewById(R.id.textViewProvince);
		province.setText(list.get(position).getProvince());
		
		TextView device = (TextView) row.findViewById(R.id.textViewDevice);
		device.setText(list.get(position).getNode_name());
		
		TextView ip = (TextView) row.findViewById(R.id.textViewIp);
		ip.setText(list.get(position).getNode_ip());
		
		TextView date = (TextView) row.findViewById(R.id.textViewDate);
		date.setText(list.get(position).getNode_time_down());
		
		TextView elapse = (TextView) row.findViewById(R.id.textViewElapse);
		try {
			DateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			Date d = format.parse(list.get(position).getNode_time_down());
			
			elapse.setText(getDateDiff(d, new Date()));
			
		} catch (ParseException e) { }
		
		TextView temp = (TextView) row.findViewById(R.id.textViewTemp);
		temp.setText(list.get(position).getTemp());
		if (list.get(position).getTemp().equals(""))
			temp.setVisibility(View.GONE);
		
		if (!showProvince)
			province.setVisibility(View.GONE);
		

		if (list.get(position).getNode_name().equals("ไม่มีรายการ")) {
			TextView down = (TextView) row.findViewById(R.id.textViewDown);
			down.setVisibility(View.GONE);
			province.setVisibility(View.GONE);
			ip.setVisibility(View.GONE);
			date.setVisibility(View.GONE);
			elapse.setVisibility(View.GONE);
			temp.setVisibility(View.GONE);
		}
		
		return row;
	}
	
	private String getDateDiff(Date before, Date after) {
	    long diffInMillies = after.getTime() - before.getTime();
	    
	    if (TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS) < 60) {
	    	return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS) + " น.";
	    }
	    else if (TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS) < 24) {
	    	long hours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    	diffInMillies -= TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS);
	    	long mins = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
    		return hours + " ชม. " + mins + " น.";
	    }
	    else {
	    	long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    	diffInMillies -= TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS);
	    	long hours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    		return days + " วัน " + hours + " ชม.";
	    }
	}
}
