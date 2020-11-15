package com.ebs.banglalinkbangladhol.bean;

public class AlbumSong {

    public String contentid;
    public String name;
    public String artist;
    public String url;
    public String urlwowza;
    public String img_url;
    public String duration;
    public String genre;
    public String catcode;
    public String albumcode;

    public AlbumSong(String contentid, String name, String artist, String url, String urlwowoza,
                     String img_url, String duration, String genre, String catcode, String albumcode) {

        this.contentid = contentid;
        this.name = name;
        this.artist = artist;
        this.url = url;
        this.urlwowza = urlwowoza;
        this.img_url = img_url;
        this.duration = duration;
        this.genre = genre;
        this.catcode = catcode;
        this.albumcode = albumcode;
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

    public String getImageUrl() {
        return img_url;
    }

    public void setImageUrl(String img_url) {
        this.img_url = img_url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getAlbumCode() {
        return albumcode;
    }

    public void setAlbumCode(String albumcode) {
        this.albumcode = albumcode;
    }


}

