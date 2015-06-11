package com.tot.totcheck;

public class SpinnerRowItem {
	
	private String province;
	private int amount;
	
	public SpinnerRowItem(String province, int amount) {
		this.province = province;
		this.amount = amount;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
