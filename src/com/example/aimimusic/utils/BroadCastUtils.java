package com.example.aimimusic.utils;

import android.R.fraction;

public class BroadCastUtils {
	
	public final static int CMD_CHANGE_TYPE = 1;
	
	public final static int CMD_PREVIOUS = 2;
	
	public final static int CMD_NEXT = 3;
	
	public final static int CMD_PLAY = 4;
	
	public final static int CMD_PAUSE = 5;
	
	public final static int CMD_RESUME = 6;
	
	public final static int CMD_CHANGE_SONG = 7;
	
	public final static int CMD_CHANGE_PROGRESS = 8;
	
	public final static int CMD_UPDATE = 9;
	
	public final static String CMD = "cmd";
	
	public final static String CMD_SONGID = "cmd_songid";
	
	public final static String CMD_SONG = "cmd_song";
	
	public final static String CMD_SONG_PROGRESS = "cmd_song_progress";
	
	public final static String MUSIC_SERVICE = "aimi.music.service";
	
	public final static String SERVICE_CMD = "service_cmd";
	
	public final static int TYPE_LOCAL = 1;
	
	public final static int TYPE_ONLINE = 2;
	
	public final static String EXTRA_PLAY_MUSIC_TYPE = "play_music_type";

}
