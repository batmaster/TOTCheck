package com.tot.totcheck;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Temporary version.
 * @author batmaster
 *
 */
public class ListViewRowItem {
	
	private String id_nu;
	private String node_id;
	private String node_ip;
	private String node_time_down;
	private String smsdown;
	private String smsup;
	
	private String node_name;
	
	public ListViewRowItem() {
		this("", "", "", "", "", "", "ไม่มีรายการ");
	}
	
	public ListViewRowItem(String id_nu, String node_id, String node_ip, String node_time_down, String smsdown, String smsup, String node_name) {
		this.id_nu = id_nu;
		this.node_id = node_id;
		this.node_ip = node_ip;
		try {
			this.node_time_down = new SimpleDateFormat("yyyy.MM.dd\nHH:mm:ss").format(new Timestamp(Long.parseLong(node_time_down) * 1000));
		} catch (NumberFormatException e) {
			this.node_time_down = "";
		}
		this.smsdown = smsdown;
		this.smsup = smsup;
		this.node_name = node_name;
	}

	public String getId_nu() {
		return id_nu;
	}

	public void setId_nu(String id_nu) {
		this.id_nu = id_nu;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getNode_ip() {
		return node_ip;
	}

	public void setNode_ip(String node_ip) {
		this.node_ip = node_ip;
	}

	public String getNode_time_down() {
		return node_time_down;
	}

	public void setNode_time_down(String node_time_down) {
		this.node_time_down = node_time_down;
	}

	public String getSmsdown() {
		return smsdown;
	}

	public void setSmsdown(String smsdown) {
		this.smsdown = smsdown;
	}

	public String getSmsup() {
		return smsup;
	}

	public void setSmsup(String smsup) {
		this.smsup = smsup;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
}
