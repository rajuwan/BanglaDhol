package com.ebs.banglalinkbangladhol.bean;

public class AlbumSingle {

	public String albumSongGenre;
	public String albumSongTitle;
	public String albumSongArtist;
	public String albumSongCp;
	public String albumContents;

	public AlbumSingle(String albumSongGenre, String albumSongTitle, String albumSongArtist,
					   String albumSongCp, String albumContents) {

		this.albumSongGenre = albumSongGenre;
		this.albumSongTitle = albumSongTitle;
		this.albumSongArtist = albumSongArtist;
		this.albumSongCp = albumSongCp;
		this.albumContents = albumContents;
	}

	public String getAlbumSongGenre() {
		return albumSongGenre;
	}

	public void setAlbumSongGenre(String albumSongGenre) {
		this.albumSongGenre = albumSongGenre;
	}

	public String getAlbumSongTitle() {
		return albumSongTitle;
	}

	public void setAlbumSongTitle(String albumSongTitle) {
		this.albumSongTitle = albumSongTitle;
	}

	public String getAlbumSongArtist() {
		return albumSongArtist;
	}

	public void setAlbumSongArtist(String albumSongArtist) {
		this.albumSongArtist = albumSongArtist;
	}

	public String getAlbumSongCp() {
		return albumSongCp;
	}

	public void setAlbumSongCp(String albumSongCp) {
		this.albumSongCp = albumSongCp;
	}

	public String getAlbumContents() {
		return albumContents;
	}

	public void setAlbumContents(String albumContents) {
		this.albumContents = albumContents;
	}

}
