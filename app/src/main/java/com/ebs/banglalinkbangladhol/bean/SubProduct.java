package com.ebs.banglalinkbangladhol.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SubProduct implements Parcelable {

	private int id;
	private String name;
	private String pack;
	private String text;
	private String regmsg;

	public SubProduct() {
		super();
	}

	private SubProduct(Parcel in) {
		super();
		this.id = in.readInt();
		this.name = in.readString();
		this.pack = in.readString();
		this.text = in.readString();
		this.regmsg = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(getId());
		parcel.writeString(getName());
		parcel.writeString(getPack());
		parcel.writeString(getText());
		parcel.writeString(getRegMsg());
	}

	public static final Creator<SubProduct> CREATOR = new Creator<SubProduct>() {
		public SubProduct createFromParcel(Parcel in) {
			return new SubProduct(in);
		}

		public SubProduct[] newArray(int size) {
			return new SubProduct[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRegMsg() {
		return regmsg;
	}

	public void setRegMsg(String regmsg) {
		this.regmsg = regmsg;
	}

	public static Creator<SubProduct> getCreator() {
		return CREATOR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		SubProduct other = (SubProduct) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubProduct [id=" + id + ", name=" + name + ", pack=" + pack
				+ ",  title=" + text + ", regmsg=" + regmsg + "]";
	}

}

