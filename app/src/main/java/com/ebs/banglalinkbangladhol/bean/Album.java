package com.ebs.banglalinkbangladhol.bean;

public class Album {

	public String albumCode;
	public String catname;
	public String artistname;
	public String albumName;
	public String cp;
	public String release;
	public int count;
	public String albumImgUrl;

	public Album(String albumCode, String catname, String artistname, String albumName, String cp,
				 String release, int count, String albumImgUrl) {

		this.albumCode = albumCode;
		this.catname = catname;
		this.artistname = artistname;
		this.albumName = albumName;
		this.cp = cp;
		this.release = release;
		this.count = count;
		this.albumImgUrl = albumImgUrl;
	}

	public String getAlbumCode() {
		return albumCode;
	}

	public void setAlbumCode(String albumCode) {
		this.albumCode = albumCode;
	}

	public String getCatname() {
		return catname;
	}

	public void setCatname(String catname) {
		this.catname = catname;
	}

	public String getArtistname() {
		return artistname;
	}

	public void setArtistname(String artistname) {
		this.artistname = artistname;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getAlbumImgUrl() {
		return albumImgUrl;
	}

	public void setAlbumImgUrl(String albumImgUrl) {
		this.albumImgUrl = albumImgUrl;
	}

}
