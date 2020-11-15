package com.ebs.banglalinkbangladhol.others;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ebs.banglalinkbangladhol.bean.Playlist;
import com.ebs.banglalinkbangladhol.bean.PlaylistItem;

import java.util.ArrayList;

public class DBHelperNew extends SQLiteOpenHelper {

	public static final String DB_NAME = "Playlist_BlinkBD";
	public static final int VERSION = 1;

	/* Playlist List tags
	* */

	public static final String TABLE_NAME_PLAYLIST = "tblPlaylist";
	public static final String ID_FIELD_PLAYLIST = "_ID";
	public static final String TITLE_FIELD_PLAYLIST = "PlayListName";

	/* Playlist Items tags
	* */

	public static final String TABLE_NAME_CONTENT = "tblPlaylist_Content";
	public static final String ID_FIELD_CONTENT = "_ID";
	public static final String FK_ID_FIELD_CONTENT = "FK_ID";
	public static final String TITLE_FIELD_CONTENT = "SongTitle";
	public static final String ARTIST_FIELD_CONTENT = "SongArtist";
	public static final String ALBUM_FIELD_CONTENT = "SongAlbum";
	public static final String IMAGE_FIELD_CONTENT = "SongImage";
	public static final String URL_FIELD_CONTENT= "SongUrl";
	public static final String URLWOWZA_FIELD_CONTENT= "SongUrlWowza";
	public static final String CODE_FIELD_CONTENT = "SongCode";
	public static final String ALBUMCODE_FIELD_CONTENT = "SongAlbumCode";
	public static final String LENGTH_FIELD_CONTENT = "SongLength";
	public static final String CP_FIELD_CONTENT = "SongCp";
	public static final String GENRE_FIELD_CONTENT = "SongGenre";
	public static final String CATCODE_FIELD_CONTENT = "SongCatCode";

	public static final String TABLE_NAME_QUEUE = "tblSongQueue";

	/*
	* sql for creating table*/

	public static final String TABLE_SQL_PLAYLIST = "CREATE TABLE " + TABLE_NAME_PLAYLIST + " ("
			+ ID_FIELD_PLAYLIST + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_FIELD_PLAYLIST
			+ " TEXT)";

	public static final String TABLE_SQL_CONTENT = "CREATE TABLE " + TABLE_NAME_CONTENT + " ("
			+ ID_FIELD_CONTENT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FK_ID_FIELD_CONTENT + " INTEGER ," + TITLE_FIELD_CONTENT
			+ " TEXT, " + ARTIST_FIELD_CONTENT + " TEXT, " + ALBUM_FIELD_CONTENT + " TEXT, "
			+ IMAGE_FIELD_CONTENT + " TEXT, " + URL_FIELD_CONTENT + " TEXT, " + URLWOWZA_FIELD_CONTENT + " TEXT, "
			+ CP_FIELD_CONTENT + " TEXT, " + GENRE_FIELD_CONTENT + " TEXT, " + CATCODE_FIELD_CONTENT + " TEXT, "
			+ CODE_FIELD_CONTENT + " TEXT, " + ALBUMCODE_FIELD_CONTENT + " TEXT, " + LENGTH_FIELD_CONTENT + " TEXT)";

	public static final String TABLE_QUEUE = "CREATE TABLE " + TABLE_NAME_QUEUE + " ("
			+ ID_FIELD_CONTENT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_FIELD_CONTENT
			+ " TEXT, " + ARTIST_FIELD_CONTENT + " TEXT, " + ALBUM_FIELD_CONTENT + " TEXT, "
			+ IMAGE_FIELD_CONTENT + " TEXT, " + URL_FIELD_CONTENT + " TEXT, " + CODE_FIELD_CONTENT
			+ " TEXT, " + ALBUMCODE_FIELD_CONTENT + " TEXT, " + LENGTH_FIELD_CONTENT + " TEXT)";


	public DBHelperNew(Context context) {
		super(context, DB_NAME, null, VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create table
		db.execSQL(TABLE_SQL_PLAYLIST);
		db.execSQL(TABLE_SQL_CONTENT);
		db.execSQL(TABLE_QUEUE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	public void deleteRows(){
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME_QUEUE, null, null);
		
	}

	public long insertSong(Playlist playlist) {

		SQLiteDatabase db = this.getWritableDatabase();
		// insert song into playlist
		ContentValues values = new ContentValues();
		values.put(TITLE_FIELD_CONTENT, playlist.getSongTitle());
		values.put(ARTIST_FIELD_CONTENT, playlist.getSongArtist());
		values.put(ALBUM_FIELD_CONTENT, playlist.getSongAlbum());
		values.put(IMAGE_FIELD_CONTENT, playlist.getSongImage());
		values.put(URL_FIELD_CONTENT, playlist.getSongUrl());
		values.put(CODE_FIELD_CONTENT, playlist.getSongCode());
		values.put(ALBUMCODE_FIELD_CONTENT, playlist.getSongAlbumCode());
		values.put(LENGTH_FIELD_CONTENT, playlist.getSongLength());

		long inserted = db.insert(TABLE_NAME_QUEUE, "", values);
		db.close();
		return inserted;
	}

	public ArrayList<Playlist> getAllPlaylistData() {

		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Playlist> all = new ArrayList<Playlist>();

		Cursor cursor = db.query(TABLE_NAME_QUEUE, null, null, null, null, null, null);

		if (cursor != null) {
			// int count = cursor.getCount();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();

				do {

					int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD_CONTENT));

					String songTitle = cursor.getString(cursor
							.getColumnIndex(TITLE_FIELD_CONTENT));
					String songArtist = cursor.getString(cursor
							.getColumnIndex(ARTIST_FIELD_CONTENT));
					String songAlbum = cursor.getString(cursor
							.getColumnIndex(ALBUM_FIELD_CONTENT));
					String songImage = cursor.getString(cursor
							.getColumnIndex(IMAGE_FIELD_CONTENT));
					String songPath = cursor.getString(cursor
							.getColumnIndex(URL_FIELD_CONTENT));
					String songCode = cursor.getString(cursor
							.getColumnIndex(CODE_FIELD_CONTENT));
					String songAlbumCode = cursor.getString(cursor
							.getColumnIndex(ALBUMCODE_FIELD_CONTENT));
					String songLength = cursor.getString(cursor
							.getColumnIndex(LENGTH_FIELD_CONTENT));
					Playlist p = new Playlist(id, songTitle, songArtist,
							songAlbum, songImage, songPath, songCode, songAlbumCode, songLength);
					all.add(p);
				} while (cursor.moveToNext());
			}

		}

		cursor.close();
		db.close();

		return all;
	}


	public long insertPlayList(String playlistName) {

		SQLiteDatabase db = this.getWritableDatabase();
		// insert song into playlist
		ContentValues values = new ContentValues();
		values.put(TITLE_FIELD_PLAYLIST, playlistName);

		long inserted = db.insert(TABLE_NAME_PLAYLIST, "", values);
		db.close();

		return inserted;
	}

	public boolean searchSongExistsInPlaylist(int fk_id , String songCode){

		String fk_idOfPlayList = String.valueOf(fk_id);

		int check = 0;

		try{

			SQLiteDatabase db = this.getWritableDatabase();

			Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_CONTENT + " WHERE " + FK_ID_FIELD_CONTENT +
					" = ?"+ " AND " + CODE_FIELD_CONTENT + " = ?",new String[]{ fk_idOfPlayList , songCode });

			while (cursor.moveToNext()) {
				check = 1;
			}

		} catch (Exception e){
			e.printStackTrace();
		}

		if (check == 0) {
			return false;
		} else {
			return true;
		}

	}

	public long insertSongContent(Playlist playlist) {

		SQLiteDatabase db = this.getWritableDatabase();
		// insert song into playlist
		ContentValues values = new ContentValues();
		values.put(FK_ID_FIELD_CONTENT, playlist.getSongFk_Id());
		values.put(TITLE_FIELD_CONTENT, playlist.getSongTitle());
		values.put(ARTIST_FIELD_CONTENT, playlist.getSongArtist());
		values.put(ALBUM_FIELD_CONTENT, playlist.getSongAlbum());
		values.put(IMAGE_FIELD_CONTENT, playlist.getSongImage());
		values.put(URL_FIELD_CONTENT, playlist.getSongUrl());
		values.put(URLWOWZA_FIELD_CONTENT, playlist.getSongUrlWowza());
		values.put(CODE_FIELD_CONTENT, playlist.getSongCode());
		values.put(ALBUMCODE_FIELD_CONTENT, playlist.getSongAlbumCode());
		values.put(LENGTH_FIELD_CONTENT, playlist.getSongLength());
		values.put(CP_FIELD_CONTENT, playlist.getSongCp());
		values.put(GENRE_FIELD_CONTENT, playlist.getSongGenre());
		values.put(CATCODE_FIELD_CONTENT, playlist.getSongCatCode());

		long inserted = db.insert(TABLE_NAME_CONTENT, "", values);
		db.close();
		return inserted;
	}

    public ArrayList<Playlist> getAllPlaylistSongs(int fk_idOfPlayList) {

        String fkidOfPlayList = String.valueOf(fk_idOfPlayList);
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Playlist> all = new ArrayList<Playlist>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_CONTENT + " WHERE " + FK_ID_FIELD_CONTENT +
                " = ?",new String[]{ fkidOfPlayList});

        if (cursor != null) {

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();

                do {

                    int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD_CONTENT));

                    int fk_id = cursor.getInt(cursor.getColumnIndex(FK_ID_FIELD_CONTENT));

                    String songTitle = cursor.getString(cursor
                            .getColumnIndex(TITLE_FIELD_CONTENT));
                    String songArtist = cursor.getString(cursor
                            .getColumnIndex(ARTIST_FIELD_CONTENT));
                    String songAlbum = cursor.getString(cursor
                            .getColumnIndex(ALBUM_FIELD_CONTENT));
                    String songImage = cursor.getString(cursor
                            .getColumnIndex(IMAGE_FIELD_CONTENT));
                    String songUrl = cursor.getString(cursor
                            .getColumnIndex(URL_FIELD_CONTENT));
					String songUrlWowza = cursor.getString(cursor
							.getColumnIndex(URLWOWZA_FIELD_CONTENT));
                    String songCode = cursor.getString(cursor
                            .getColumnIndex(CODE_FIELD_CONTENT));
                    String songAlbumCode = cursor.getString(cursor
                            .getColumnIndex(ALBUMCODE_FIELD_CONTENT));
                    String songLength = cursor.getString(cursor
                            .getColumnIndex(LENGTH_FIELD_CONTENT));
					String songCp = cursor.getString(cursor
							.getColumnIndex(CP_FIELD_CONTENT));
					String songGenre = cursor.getString(cursor
							.getColumnIndex(GENRE_FIELD_CONTENT));
					String songCatCode = cursor.getString(cursor
							.getColumnIndex(CATCODE_FIELD_CONTENT));


                    Playlist p = new Playlist(id, fk_id, songTitle, songArtist, songAlbum,
							songImage, songUrl, songUrlWowza, songCode, songAlbumCode, songLength,
							songCp, songGenre, songCatCode);

                    all.add(p);

                } while (cursor.moveToNext());
            }

        }

        cursor.close();
        db.close();

        return all;
    }


	public ArrayList<PlaylistItem> getAllPlaylist() {

		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<PlaylistItem> all = new ArrayList<PlaylistItem>();

		Cursor cursor = db.query(TABLE_NAME_PLAYLIST, null, null, null, null, null, null);

		if (cursor != null) {

			if (cursor.getCount() > 0) {

				cursor.moveToFirst();

				do {
					int  id = cursor.getInt(cursor.getColumnIndex(ID_FIELD_PLAYLIST));

					String playlistName = cursor.getString(cursor
							.getColumnIndex(TITLE_FIELD_PLAYLIST));

					PlaylistItem p = new PlaylistItem(id, playlistName);
					all.add(p);

				} while (cursor.moveToNext());
			}

		}

		cursor.close();
		db.close();

		return all;
	}

	public boolean deletePlaylist(int fk_id) {

		SQLiteDatabase db = this.getWritableDatabase();

		long val1 = db.delete(TABLE_NAME_PLAYLIST, ID_FIELD_PLAYLIST + "=" + fk_id, null);
		long val2 = db.delete(TABLE_NAME_CONTENT, FK_ID_FIELD_CONTENT + "=" + fk_id, null);

		if ((val1!=0) &&(val2!=0)){
			return true;
		}else{
			return false;
		}

		/*SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TABLE_NAME_PLAYLIST, ID_FIELD_PLAYLIST + "=" + fk_id, null) > 0;*/
	}

	// play list and playlist content delete//

	public boolean deletePlaylistContent(int fk_id) {

	SQLiteDatabase db = this.getWritableDatabase();

	long val1 = db.delete(TABLE_NAME_PLAYLIST, ID_FIELD_PLAYLIST + "=" + fk_id, null);
	long val2 = db.delete(TABLE_NAME_CONTENT, FK_ID_FIELD_CONTENT + "=" + fk_id, null);

    if ((val1!=0) &&(val2!=0)){
		return true;
	}else{
		return false;
	}

}

	/*public ArrayList<PlayList> getAllSonginPlaylist() {

		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<PlayList> all = new ArrayList<PlayList>();

		Cursor cursor = db.query(TABLE_NAME_CONTENT, null, null, null, null, null, null);

		if (cursor != null) {

			if (cursor.getCount() > 0) {

				cursor.moveToFirst();

				do {

					int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD_CONTENT));

					int fk_id = cursor.getInt(cursor.getColumnIndex(FK_ID_FIELD_CONTENT));

					String songTitle = cursor.getString(cursor
							.getColumnIndex(TITLE_FIELD_CONTENT));
					String songArtist = cursor.getString(cursor
							.getColumnIndex(ARTIST_FIELD_CONTENT));
					String songAlbum = cursor.getString(cursor
							.getColumnIndex(ALBUM_FIELD_CONTENT));
					String songImage = cursor.getString(cursor
							.getColumnIndex(IMAGE_FIELD_CONTENT));
					String songPath = cursor.getString(cursor
							.getColumnIndex(PATH_FIELD_CONTENT));
					String songCode = cursor.getString(cursor
							.getColumnIndex(CODE_FIELD_CONTENT));
					String songAlbumCode = cursor.getString(cursor
							.getColumnIndex(ALBUMCODE_FIELD_CONTENT));
					String songLength = cursor.getString(cursor
							.getColumnIndex(LENGTH_FIELD_CONTENT));

					PlayList p = new PlayList(id, fk_id, songTitle, songArtist,
							songAlbum, songImage, songPath, songCode, songAlbumCode, songLength);

					all.add(p);
				} while (cursor.moveToNext());
			}

		}

		cursor.close();
		db.close();

		return all;
	}*/

	//  song in playlist display //


}
