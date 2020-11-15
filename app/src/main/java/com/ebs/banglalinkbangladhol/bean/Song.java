package com.ebs.banglalinkbangladhol.bean;

public class Song {

    public String contentid;
    public String name;
    public String artist;
    public String albumname;
    public String albumcode;
    public String image;
    public String url;
    public String urlwowza;
    public String duration;
    public String cp;
    public String genre;
    public String catcode;

    public Song(String contentid, String name, String artist, String albumname, String albumcode,
                String image, String url, String urlwowoza, String duration, String cp,
                String genre, String catcode) {

        this.contentid = contentid;
        this.name = name;
        this.artist = artist;
        this.albumname = albumname;
        this.albumcode = albumcode;
        this.image = image;
        this.url = url;
        this.urlwowza = urlwowoza;
        this.duration = duration;
        this.cp = cp;
        this.genre = genre;
        this.catcode = catcode;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
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

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAlbumcode() {
        return albumcode;
    }

    public void setAlbumcode(String albumcode) {
        this.albumcode = albumcode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlWowza() {
        return urlwowza;
    }

    public void setUrlWowza(String urlwowza) {
        this.urlwowza = urlwowza;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCatcode() {
        return catcode;
    }

    public void setCatcode(String catcode) {
        this.catcode = catcode;
    }


}

