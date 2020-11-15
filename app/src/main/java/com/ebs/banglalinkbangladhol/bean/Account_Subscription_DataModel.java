package com.ebs.banglalinkbangladhol.bean;

public class Account_Subscription_DataModel {

	public String date_time;
	public String purpose;
	public String charge_type;
	public String charge_amount;

	public Account_Subscription_DataModel(String date_time, String purpose, String charge_type,
			String charge_amount) {

		this.date_time = date_time;
		this.purpose = purpose;
		this.charge_type = charge_type;
		this.charge_amount = charge_amount;

	}

	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getCharge_type() {
		return charge_type;
	}

	public void setCharge_type(String charge_type) {
		this.charge_type = charge_type;
	}

	public String getCharge_amount() {
		return charge_amount;
	}

	public void setCharge_amount(String charge_amount) {
		this.charge_amount = charge_amount;
	}

}
