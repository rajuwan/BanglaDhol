package com.ebs.banglalinkbangladhol.bean;

public class LoginJson {

	public String result;
	public String token;
	public int play;
	public String packcode;
	public String packname;
	public String packtext;
	public String username;
	public int enforce;
	public String enforcetext;
	public int version;
	public int concurrent;
	public String concurrenttext;
	public int showad;
	//public int consent;
	//public String consenttext;
	//public String consenturl;

	public LoginJson(String result, String token, int play, String packcode,
			String packname, String packtext, String username, int enforce,
			String enforcetext, int version, int concurrent,
			String concurrenttext, int showad) {

		this.result = result;
		this.token = token;
		this.play = play;
		this.packcode = packcode;
		this.packname = packname;
		this.packtext = packtext;
		this.username = username;
		this.enforce = enforce;
		this.enforcetext = enforcetext;
		this.version = version;
		this.concurrent = concurrent;
		this.concurrenttext = concurrenttext;
		this.showad = showad;
		//this.consent = consent;
		//this.consenttext = consenttext;
		//this.consenturl = consenturl;

	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getPlay() {
		return play;
	}

	public void setPlay(int play) {
		this.play = play;
	}

	public String getPackCode() {
		return packcode;
	}

	public void setPackCode(String packcode) {
		this.packcode = packcode;
	}

	public String getPackName() {
		return packname;
	}

	public void setPackName(String packname) {
		this.packname = packname;
	}

	public String getPackText() {
		return packtext;
	}

	public void setPackText(String packtext) {
		this.packtext = packtext;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public int getEnforce() {
		return enforce;
	}

	public void setEnforce(int enforce) {
		this.enforce = enforce;
	}

	public String getEnforceText() {
		return enforcetext;
	}

	public void setEnforceText(String enforcetext) {
		this.enforcetext = enforcetext;
	}

	public int getCurrentVersion() {
		return version;
	}

	public void setCurrentVersion(int version) {
		this.version = version;
	}

	public int getConCurrent() {
		return concurrent;
	}

	public void setConCurrent(int concurrent) {
		this.concurrent = concurrent;
	}

	public String getConCurrentText() {
		return concurrenttext;
	}

	public void setConCurrentText(String concurrenttext) {
		this.concurrenttext = concurrenttext;
	}

	public int getShowAd() {
		return showad;
	}

	public void setShowAd(int showad) {
		this.showad = showad;
	}

	/*public int getConsent() {
		return consent;
	}

	public void setConsent(int consent) {
		this.consent = consent;
	}

	public String getConsentText() {
		return consenttext;
	}

	public void setConsentText(String consenttext) {
		this.consenttext = consenttext;
	}

	public String getConsentUrl() {
		return consenturl;
	}

	public void setConsentUrl(String consenturl) {
		this.consenturl = consenturl;
	}*/

}
