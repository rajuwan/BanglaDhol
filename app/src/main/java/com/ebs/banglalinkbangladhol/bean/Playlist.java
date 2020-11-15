package com.ebs.banglalinkbangladhol.bean;

public class Playlist {

	private int id;
	private int songFk_Id;

	private String songTitle;
	private String songArtist;
	private String songAlbum;
	private String songImage;
	private String songUrl;
	private String songUrlWowza;
	private String songCode;
	private String songAlbumCode;
	private String songLength;
	private String songCp;
	private String songGenre;
	private String songCatCode;

	private String playlistTitle;

	public Playlist() {

	}

	public Playlist(int id, String playlistTitle){

		this.id = id;
		this.playlistTitle = playlistTitle;

	}

	public Playlist(int songFk_Id, String songTitle, String songArtist, String songAlbum, String songImage,
					String songUrl, String songUrlWowza, String songCode, String songAlbumCode, String songLength,
					String songCp, String songGenre, String songCatCode) {

		this.songFk_Id = songFk_Id;
		this.songTitle = songTitle;
		this.songArtist = songArtist;
		this.songAlbum = songAlbum;
		this.songImage = songImage;
		this.songUrl = songUrl;
		this.songUrlWowza = songUrlWowza;
		this.songCode = songCode;
		this.songAlbumCode = songAlbumCode;
		this.songLength = songLength;
		this.songCp = songCp;
		this.songGenre = songGenre;
		this.songCatCode = songCatCode;
	}

	public Playlist(int id, int songFk_Id, String songTitle, String songArtist, String songAlbum, String songImage,
					String songUrl, String songUrlWowza, String songCode, String songAlbumCode, String songLength,
					String songCp, String songGenre, String songCatCode) {

		this.id = id;
		this.songFk_Id = songFk_Id;
		this.songTitle = songTitle;
		this.songArtist = songArtist;
		this.songAlbum = songAlbum;
		this.songImage = songImage;
		this.songUrl = songUrl;
		this.songUrlWowza = songUrlWowza;
		this.songCode = songCode;
		this.songAlbumCode = songAlbumCode;
		this.songLength = songLength;
		this.songCp = songCp;
		this.songGenre = songGenre;
		this.songCatCode = songCatCode;
	}

	public Playlist(String songTitle, String songArtist, String songAlbum, String songImage, String songUrl,
					String songCode, String songAlbumCode, String songLength) {

		this.songTitle = songTitle;
		this.songArtist = songArtist;
		this.songAlbum = songAlbum;
		this.songImage = songImage;
		this.songUrl = songUrl;
		this.songCode = songCode;
		this.songAlbumCode = songAlbumCode;
		this.songLength = songLength;
	}

	public Playlist(int id, String songTitle, String songArtist, String songAlbum, String songImage,
					String songUrl, String songCode, String songAlbumCode, String songLength) {

		this.id = id;
		this.songTitle = songTitle;
		this.songArtist = songArtist;
		this.songAlbum = songAlbum;
		this.songImage = songImage;
		this.songUrl = songUrl;
		this.songCode = songCode;
		this.songAlbumCode = songAlbumCode;
		this.songLength = songLength;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSongFk_Id() {
		return songFk_Id;
	}

	public void setSongFk_Id(int songFk_Id) {
		this.songFk_Id = songFk_Id;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getSongArtist() {
		return songArtist;
	}

	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}

	public String getSongAlbum() {
		return songAlbum;
	}

	public void setSongAlbum(String songAlbum) {
		this.songAlbum = songAlbum;
	}

	public String getSongImage() {
		return songImage;
	}

	public void setSongImage(String songImage) {
		this.songImage = songImage;
	}

	public String getSongUrl() {
		return songUrl;
	}

	public void setSongUrl(String songUrl) {
		this.songUrl = songUrl;
	}

	public String getSongUrlWowza() {
		return songUrlWowza;
	}

	public void setSongUrlWowza(String songUrlWowza) {
		this.songUrlWowza = songUrlWowza;
	}

	public String getSongCode() {
		return songCode;
	}

	public void setSongCode(String songCode) {
		this.songCode = songCode;
	}

	public String getSongAlbumCode() {
		return songAlbumCode;
	}

	public void setSongAlbumCode(String songAlbumCode) {
		this.songAlbumCode = songAlbumCode;
	}

	public String getSongLength() {
		return songLength;
	}

	public void setSongLength(String songLength) {
		this.songLength = songLength;
	}

	public String getSongCp() {
		return songCp;
	}

	public void setSongCp(String songCp) {
		this.songCp = songCp;
	}

	public String getSongGenre() {
		return songGenre;
	}

	public void setSongGenre(String songGenre) {
		this.songGenre = songGenre;
	}

	public String getSongCatCode() {
		return songCatCode;
	}

	public void setSongCatCode(String songCatCode) {
		this.songCatCode = songCatCode;
	}

	public String getPlaylistTitle() {
		return playlistTitle;
	}

	public void setPlaylistTitle(String playlistTitle) {
		this.playlistTitle = playlistTitle;
	}


	public String toString() {
		return songTitle + ";" + songArtist + ";" + songAlbum + ";" + songImage
				+ ";" + songUrl + ";" + songLength;
	}

}
