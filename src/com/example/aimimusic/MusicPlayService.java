package com.example.aimimusic;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.element.SongList;
import com.example.aimimusic.utils.BroadCastUtils;
import com.example.aimimusic.utils.HttpUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.text.style.TtsSpan.ElectronicBuilder;
import android.util.Log;

public class MusicPlayService extends Service{
	
	private int musicPlayType;
	private int musicPlaySwitch;
	private SongList songList;
	private List<Song> songs;
	private String songId;
	private int songIndex;
	private Song song;
	private MediaPlayer mediaPlayer;
	private int playMusicType;
	
	private final int TYPE_CIRCLE = 1;
	private final int TYPE_RANDOM = 2;
	private final int TYPE_SINGLE = 3;
	private final int SWITCH_PLAY = 1;
	private final int SWITCH_PAUSE = 2;
	private final String TAG = "MusicPlayService";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		musicPlayType = TYPE_CIRCLE;
		playMusicType = BroadCastUtils.TYPE_ONLINE;
		songId = "";
		MusicControllReceiver receiver = new MusicControllReceiver();
		IntentFilter filter = new IntentFilter(BroadCastUtils.MUSIC_SERVICE);
		registerReceiver(receiver, filter);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		songList = (SongList) intent.getSerializableExtra("songlist");
		songs = songList.getSong_list();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				if(musicPlayType == TYPE_CIRCLE)
				{
					songIndex++;
					if(songIndex > songs.size()-1)
					{
						songIndex = 0;
					}
					song = songs.get(songIndex);
				}
				else if(musicPlayType == TYPE_RANDOM)
				{
					Random random = new Random();
					int randomNum = random.nextInt(songs.size());
					while(randomNum == songIndex)
					{
						randomNum = random.nextInt(songs.size());
					}
					songIndex = randomNum;
					song = songs.get(songIndex);
				}
				else if(musicPlayType == TYPE_SINGLE)
				{
					
				}
				Intent intent = new Intent(BroadCastUtils.SERVICE_CMD);
				intent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_CHANGE_SONG);
				intent.putExtra(BroadCastUtils.CMD_SONG, song);
				sendBroadcast(intent);
				if(playMusicType == BroadCastUtils.TYPE_ONLINE)
				{
					getMusicPlayUrl(song.getSong_id());
				}
				else {
					mediaPlayer.reset();
					try {
						mediaPlayer.setDataSource(song.getPlayUrl());
						mediaPlayer.prepare();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mediaPlayer.start();
					musicPlaySwitch = SWITCH_PLAY;
					Intent antent = new Intent(BroadCastUtils.SERVICE_CMD);
					antent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_PLAY);
					sendBroadcast(antent);
				}
				
			}
		});
		return super.onStartCommand(intent, flags, startId);
	}
	
	class MusicControllReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			int cmd = intent.getIntExtra(BroadCastUtils.CMD, 0);
			Log.d(TAG, "receive activity cmd:"+cmd);
			playMusicType = intent.getIntExtra(BroadCastUtils.EXTRA_PLAY_MUSIC_TYPE, BroadCastUtils.TYPE_ONLINE);
			if(cmd == BroadCastUtils.CMD_CHANGE_TYPE)
			{
				if(musicPlayType == TYPE_CIRCLE)
				{
					musicPlayType = TYPE_RANDOM;
				}
				else if(musicPlayType == TYPE_RANDOM)
				{
					musicPlayType = TYPE_SINGLE;
				}
				else musicPlayType = TYPE_CIRCLE;
			}
			else if(cmd == BroadCastUtils.CMD_PREVIOUS)
			{
				if(musicPlayType == TYPE_CIRCLE)
				{
					songIndex--;
					if(songIndex < 0)
					{
						songIndex = songs.size()-1;
					}
				}
				else if(musicPlayType == TYPE_RANDOM)
				{
					Random random = new Random();
					int randomNum = random.nextInt(songs.size());
					while(randomNum == songIndex)
					{
						randomNum = random.nextInt(songs.size());
					}
					songIndex = randomNum;
				}
				else if(musicPlayType == TYPE_SINGLE)
				{
					
				}
				song = songs.get(songIndex);
				Intent preIntent = new Intent(BroadCastUtils.SERVICE_CMD);
				preIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_CHANGE_SONG);
				preIntent.putExtra(BroadCastUtils.CMD_SONG, song);
				sendBroadcast(preIntent);
				if(playMusicType == BroadCastUtils.TYPE_ONLINE)
				{
					getMusicPlayUrl(song.getSong_id());
				}
				else {
					mediaPlayer.reset();
					try {
						mediaPlayer.setDataSource(song.getPlayUrl());
						mediaPlayer.prepare();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mediaPlayer.start();
					musicPlaySwitch = SWITCH_PLAY;
					Intent aIntent = new Intent(BroadCastUtils.SERVICE_CMD);
					aIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_PLAY);
					sendBroadcast(aIntent);
				}
				
			}
			else if(cmd == BroadCastUtils.CMD_NEXT)
			{
				
				if(musicPlayType == TYPE_CIRCLE)
				{
					songIndex++;
					if(songIndex > songs.size()-1)
					{
						songIndex = 0;
					}
				}
				else if(musicPlayType == TYPE_RANDOM)
				{
					Random random = new Random();
					int randomNum = random.nextInt(songs.size());
					while(randomNum == songIndex)
					{
						randomNum = random.nextInt(songs.size());
					}
					songIndex = randomNum;
				}
				else if(musicPlayType == TYPE_SINGLE)
				{
					
				}
				song = songs.get(songIndex);
				Intent preIntent = new Intent(BroadCastUtils.SERVICE_CMD);
				preIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_CHANGE_SONG);
				preIntent.putExtra(BroadCastUtils.CMD_SONG, song);
				sendBroadcast(preIntent);
				if(playMusicType == BroadCastUtils.TYPE_ONLINE)
				{
					getMusicPlayUrl(song.getSong_id());
				}
				else {
					mediaPlayer.reset();
					try {
						mediaPlayer.setDataSource(song.getPlayUrl());
						mediaPlayer.prepare();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mediaPlayer.start();
					musicPlaySwitch = SWITCH_PLAY;
					Intent aIntent = new Intent(BroadCastUtils.SERVICE_CMD);
					aIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_PLAY);
					sendBroadcast(aIntent);
				}
			}
			else if(cmd == BroadCastUtils.CMD_PLAY)
			{
				if(!songId.equals(intent.getStringExtra(BroadCastUtils.CMD_SONGID)))
				{
					songId = intent.getStringExtra(BroadCastUtils.CMD_SONGID);
					songIndex = getSongIndex(songId);
					song = songs.get(songIndex);
					if(playMusicType == BroadCastUtils.TYPE_ONLINE)
					{
						getMusicPlayUrl(song.getSong_id());
					}
					else {
						mediaPlayer.reset();
						try {
							mediaPlayer.setDataSource(song.getPlayUrl());
							mediaPlayer.prepare();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						mediaPlayer.start();
						musicPlaySwitch = SWITCH_PLAY;
						Intent intent1 = new Intent(BroadCastUtils.SERVICE_CMD);
						intent1.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_PLAY);
						sendBroadcast(intent1);
					}
				}
				else 
				{
					if(mediaPlayer != null && musicPlaySwitch == SWITCH_PAUSE)
					{
						mediaPlayer.start();
						Intent aIntent = new Intent(BroadCastUtils.SERVICE_CMD);
						aIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_RESUME);
						sendBroadcast(aIntent);
					}
				}
			}
			else if(cmd == BroadCastUtils.CMD_PAUSE)
			{
				if(mediaPlayer != null)
				{
					mediaPlayer.pause();
					musicPlaySwitch = SWITCH_PAUSE;
				}
				Intent aIntent = new Intent(BroadCastUtils.SERVICE_CMD);
				aIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_PAUSE);
				sendBroadcast(aIntent);
			}
			else if(cmd == BroadCastUtils.CMD_CHANGE_PROGRESS)
			{
				Log.d(TAG, "before progress:"+mediaPlayer.getCurrentPosition());
				int progress = intent.getIntExtra(BroadCastUtils.CMD_SONG_PROGRESS, 0);
				mediaPlayer.seekTo(progress*1000);
				Log.d(TAG, "after progress:"+mediaPlayer.getCurrentPosition());
				Intent aIntent = new Intent(BroadCastUtils.SERVICE_CMD);
				aIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_CHANGE_PROGRESS);
				aIntent.putExtra(BroadCastUtils.CMD_SONG_PROGRESS, progress);
				sendBroadcast(aIntent);
			}
		}
		
	}
	
	public int getSongIndex(String songId)
	{
		for(int i = 0;i<songs.size();i++)
		{
			Song song = songs.get(i);
			if(song.getSong_id().equals(songId))
			{
				return i;
			}
		}
		return 0;
	}
	
	public void getMusicPlayUrl(String songId)
	{
		String url = HttpUtils.getPlayMusicUrl(songId);
		Log.d(TAG, "url="+url);
		JsonObjectRequest request = new JsonObjectRequest(url, null, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, "response="+response.toString());
						JSONObject bitrate = response.optJSONObject("bitrate");
						String file_link = bitrate.optString("file_link");
						int file_duration = bitrate.optInt("file_duration");
						JSONObject songinfo = response.optJSONObject("songinfo");
						String pic_radio = songinfo.optString("pic_radio");
						song.setFile_duration(file_duration);
						song.setPic_radio(pic_radio);
						Intent aIntent = new Intent(BroadCastUtils.SERVICE_CMD);
						aIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_UPDATE);
						aIntent.putExtra(BroadCastUtils.CMD_SONG, song);
						sendBroadcast(aIntent);
						try {
							mediaPlayer.reset();
							mediaPlayer.setDataSource(file_link);
							mediaPlayer.prepare();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						mediaPlayer.start();
						musicPlaySwitch = SWITCH_PLAY;
						Intent intent = new Intent(BroadCastUtils.SERVICE_CMD);
						intent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_PLAY);
						sendBroadcast(intent);
					}
			
				},
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				}
				);
		MusicApplication.getRequestQueue(this).add(request);
	}
}
