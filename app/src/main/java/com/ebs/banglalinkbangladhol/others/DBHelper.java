package com.ebs.banglalinkbangladhol.others;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ebs.banglalinkbangladhol.bean.Playlist;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "Playlist_BBD";
	public static final int VERSION = 1;

	public static final String TABLE_NAME = "tblSongs";
	public static final String ID_FIELD = "_ID";
	public static final String TITLE_FIELD = "SongTitle";
	public static final String ARTIST_FIELD = "SongArtist";
	public static final String ALBUM_FIELD = "SongAlbum";
	public static final String IMAGE_FIELD = "SongImage";
	public static final String PATH_FIELD = "SongPath";
	public static final String CODE_FIELD = "SongCode";
	public static final String LENGTH_FIELD = "SongLength";

	public static final String TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
			+ ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE_FIELD
			+ " TEXT, " + ARTIST_FIELD + " TEXT, " + ALBUM_FIELD + " TEXT, "
			+ IMAGE_FIELD + " TEXT, " + PATH_FIELD + " TEXT, " + CODE_FIELD
			+ " TEXT, " + LENGTH_FIELD + " TEXT)";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create table
		db.execSQL(TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	public void deleteRows(){
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		
	}

	public long insertSong(Playlist playlist) {

		SQLiteDatabase db = this.getWritableDatabase();
		// insert song into playlist
		ContentValues values = new ContentValues();
		values.put(TITLE_FIELD, playlist.getSongTitle());
		values.put(ARTIST_FIELD, playlist.getSongArtist());
		values.put(ALBUM_FIELD, playlist.getSongAlbum());
		values.put(IMAGE_FIELD, playlist.getSongImage());
		values.put(PATH_FIELD, playlist.getSongUrl());
		values.put(CODE_FIELD, playlist.getSongCode());
		values.put(LENGTH_FIELD, playlist.getSongLength());
		  
		long inserted = db.insert(TABLE_NAME, "", values);
		db.close();
		return inserted;
	}

	public ArrayList<Playlist> getAllPlaylistData() {

		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Playlist> all = new ArrayList<Playlist>();
		// String[] columns={NAME_FIELD, NUMBER_FIELD, EMAIL_FIELD};
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

		if (cursor != null) {
			// int count = cursor.getCount();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();

				do {

					int id = cursor.getInt(cursor.getColumnIndex(ID_FIELD));
					String songTitle = cursor.getString(cursor
							.getColumnIndex(TITLE_FIELD));
					String songArtist = cursor.getString(cursor
							.getColumnIndex(ARTIST_FIELD));
					String songAlbum = cursor.getString(cursor
							.getColumnIndex(ALBUM_FIELD));
					String songImage = cursor.getString(cursor
							.getColumnIndex(IMAGE_FIELD));
					String songPath = cursor.getString(cursor
							.getColumnIndex(PATH_FIELD));
					String songCode = cursor.getString(cursor
							.getColumnIndex(CODE_FIELD));
					String songLength = cursor.getString(cursor
							.getColumnIndex(LENGTH_FIELD));
					Playlist p = new Playlist(id, songTitle, songArtist,
							songAlbum, songImage, songPath, songCode, "", songLength);
					all.add(p);
				} while (cursor.moveToNext());
			}

		}

		cursor.close();
		db.close();

		return all;
	}

}
