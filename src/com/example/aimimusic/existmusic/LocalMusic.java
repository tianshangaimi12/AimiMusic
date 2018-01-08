package com.example.aimimusic.existmusic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.aimimusic.element.Song;

public class LocalMusic {

	public static List<Song> getLocalMusicList(Context context)
	{
		List<Song> localSongs = new ArrayList<Song>();
		 Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
	                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
	        if (cursor != null) {
	            while (cursor.moveToNext()) {
	                Song song = new Song();
	                song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
	                song.setArtist_name(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
	                song.setPlayUrl(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
	                song.setFile_duration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
	                song.setSong_id(cursor.getString(cursor.getColumnIndexOrThrow( MediaStore.Audio.Media.TITLE_KEY)));
	                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
	                if (size > 1000 * 800) {
	                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
	                    if (song.getTitle().contains("-")) {
	                        String[] str = song.getTitle().split("-");
	                        song.setArtist_name(str[0]);
	                        song.setTitle(str[1]);
	                    }
	                    localSongs.add(song);
	                }
	            }
	            // 释放资源
	            cursor.close();
	        }
	        return localSongs;
	}
}
