package com.ebs.banglalinkbangladhol.bean;

/**
 * Created by Rajuwan on 06-May-17.
 */

public class PlaylistItem {

    private int id;
    private String playlistTitle;

    public PlaylistItem(int id, String playlistTitle){

        this.id = id;
        this.playlistTitle = playlistTitle;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

}
