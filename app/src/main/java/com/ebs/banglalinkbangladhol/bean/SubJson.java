package com.ebs.banglalinkbangladhol.bean;

public class SubJson {

	public int userinfo;
	public String response;
	public String userdetail;
	public int play;
	public String sdpurl;

	public SubJson(int userinfo, String response, String userdetail, int play) {

		this.userinfo = userinfo;
		this.response = response;
		this.userdetail = userdetail;
		this.play = play;
	}

	public SubJson(int userinfo, String response, String userdetail, int play, String sdpurl) {

		this.userinfo = userinfo;
		this.response = response;
		this.userdetail = userdetail;
		this.play = play;
		this.sdpurl = sdpurl;
	}

	public int getUserInfo() {
		return userinfo;
	}

	public void setResult(int userinfo) {
		this.userinfo = userinfo;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getUserDetail() {
		return userdetail;
	}

	public void setUserDetail(String userdetail) {
		this.userdetail = userdetail;
	}

	public int getPlay() {
		return play;
	}

	public void setPlay(int play) {
		this.play = play;
	}

	public String getSdpUrl() {
		return sdpurl;
	}

	public void setSdpUrl(String sdpurl) {
		this.sdpurl = sdpurl;
	}

}
