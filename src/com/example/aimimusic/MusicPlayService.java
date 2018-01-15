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
import android.util.Log;

public class MusicPlayService extends Service{
	
	private int musicPlayType;
	private int musicPlaySwitch;
	private SongList songList;
	private List<Song> songs;
	private int songId;
	private int songIndex;
	private Song song;
	private MediaPlayer mediaPlayer;
	
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
				getMusicPlayUrl(Integer.valueOf(song.getSong_id()));
				
			}
		});
		return super.onStartCommand(intent, flags, startId);
	}
	
	class MusicControllReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			int cmd = intent.getIntExtra(BroadCastUtils.CMD, 0);
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
				songIndex--;
				if(songIndex < 0)
				{
					songIndex = songs.size()-1;
				}
				song = songs.get(songIndex);
				Intent preIntent = new Intent(BroadCastUtils.SERVICE_CMD);
				preIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_CHANGE_SONG);
				preIntent.putExtra(BroadCastUtils.CMD_SONG, song);
				sendBroadcast(preIntent);
				getMusicPlayUrl(Integer.valueOf(song.getSong_id()));
				
			}
			else if(cmd == BroadCastUtils.CMD_NEXT)
			{
				songIndex++;
				if(songIndex > songs.size()-1)
				{
					songIndex = 0;
				}
				song = songs.get(songIndex);
				Intent preIntent = new Intent(BroadCastUtils.SERVICE_CMD);
				preIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_CHANGE_SONG);
				preIntent.putExtra(BroadCastUtils.CMD_SONG, song);
				sendBroadcast(preIntent);
				getMusicPlayUrl(Integer.valueOf(song.getSong_id()));
			}
			else if(cmd == BroadCastUtils.CMD_PLAY)
			{
				if(songId != intent.getIntExtra(BroadCastUtils.CMD_SONGID, 0))
				{
					songId = intent.getIntExtra(BroadCastUtils.CMD_SONGID, 0);
					songIndex = getSongIndex(songId);
					song = songs.get(songIndex);
					getMusicPlayUrl(songId);
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
				int progress = intent.getIntExtra(BroadCastUtils.CMD_SONG_PROGRESS, 0);
				mediaPlayer.seekTo(progress);
				Intent aIntent = new Intent(BroadCastUtils.SERVICE_CMD);
				aIntent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_CHANGE_PROGRESS);
				aIntent.putExtra(BroadCastUtils.CMD_SONG_PROGRESS, progress);
				sendBroadcast(aIntent);
			}
		}
		
	}
	
	public int getSongIndex(int songId)
	{
		for(int i = 0;i<songs.size();i++)
		{
			Song song = songs.get(i);
			if(Integer.valueOf(song.getSong_id()) == songId)
			{
				return i;
			}
		}
		return 0;
	}
	
	public void getMusicPlayUrl(int songId)
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
						try {
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
