package com.ebs.banglalinkbangladhol.bean;

public class MsisdnJson {

	public String msisdn;
	public int network;
	public String infotext;
	public String tickertext;
	public int showad;

	public MsisdnJson(String msisdn, int network, String infotext, String tickertext, int showad) {

		this.msisdn = msisdn;
		this.network = network;
		this.infotext = infotext;
		this.tickertext = tickertext;
		this.showad = showad;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public int getNetwork() {
		return network;
	}

	public void setNetwork(int network) {
		this.network = network;
	}

	public String getInfoText() {
		return infotext;
	}

	public void setInfoText(String infotext) {
		this.infotext = infotext;
	}

	public String getTickerText() {
		return tickertext;
	}

	public void setTickerText(String tickertext) {
		this.tickertext = tickertext;
	}

	public int getShowAdHome() {
		return showad;
	}

	public void setShowAdHome(int showad) {
		this.showad = showad;
	}

}
