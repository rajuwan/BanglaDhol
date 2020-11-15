package com.ebs.banglalinkbangladhol.others;

import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {

	public SongsManager() {

	}

	public static ArrayList<HashMap<String, String>> _songsList = new ArrayList<HashMap<String, String>>();

	public ArrayList<HashMap<String, String>> getPlayList() {

		return _songsList;
	}

}
