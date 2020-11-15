package com.ebs.banglalinkbangladhol.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

	private String albumcode;
	private String name;
	private String artist;
	private String labelname;
	private int songs;
	private String img_url;
	private String catcode;

	public Product() {
		super();
	}

	public String getAlbumcode() {
		return albumcode;
	}

	public void setAlbumcode(String albumcode) {
		this.albumcode = albumcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getLabelname() {
		return labelname;
	}

	public void setLabelname(String labelname) {
		this.labelname = labelname;
	}

	public int getSongs() {
		return songs;
	}

	public void setSongs(int songs) {
		this.songs = songs;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getCatcode() {
		return catcode;
	}

	public void setCatcode(String catcode) {
		this.catcode = catcode;
	}

	private Product(Parcel in) {
		super();

		this.albumcode = in.readString();
		this.name = in.readString();
		this.artist = in.readString();
		this.labelname = in.readString();
		this.songs = in.readInt();
		this.img_url = in.readString();
		this.catcode = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {

		parcel.writeString(getAlbumcode());
		parcel.writeString(getName());
		parcel.writeString(getArtist());
		parcel.writeString(getLabelname());
		parcel.writeInt(getSongs());
		parcel.writeString(getImg_url());
		parcel.writeString(getCatcode());
	}

	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}

		public Product[] newArray(int size) {
			return new Product[size];
		}
	};


	public static Parcelable.Creator<Product> getCreator() {
		return CREATOR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + songs;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (songs != other.songs)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [contentid=" + albumcode + ", name=" + name
				+ ", img_url=" + img_url + ", size=" + songs + ", catcode="
				+ catcode + "]";
	}
}
