package com.example.aimimusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aimimusic.adapter.MusicLrcAdapter;
import com.example.aimimusic.element.Lrc;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.utils.BroadCastUtils;
import com.example.aimimusic.utils.HttpUtils;
import com.example.aimimusic.utils.ImgUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MusicLrcFragment extends Fragment{
	
	private Song song;
	private List<Lrc> lineLrcs;
	private MusicLrcAdapter adapter;
	private Receiver receiver;
	private Timer timer;
	private int time;
	
	private RecyclerView mRecyclerView;
	private TextView mTextView;
	
	private final String TAG = "MusicLrcFragment";
	
	public MusicLrcFragment(Song song)
	{
		this.song = song;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter(BroadCastUtils.SERVICE_CMD);
		receiver = new Receiver();
		getActivity().registerReceiver(receiver, intentFilter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(receiver != null)
		{
			getActivity().unregisterReceiver(receiver);
		}
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_music_play_lrc, container, false);
		init(view);
		getMusicLrc(Integer.valueOf(song.getSong_id()));
		return view;
	}

	public void init(View view)
	{
		mTextView = (TextView)view.findViewById(R.id.txt_null_lrc);
		mRecyclerView = (RecyclerView)view.findViewById(R.id.list_play_music_lrc);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		lineLrcs = new ArrayList<Lrc>();
	}
	
	public void getMusicLrc(int songId)
	{
		String url = HttpUtils.getMusicLrcUrl(songId);
		Log.d(TAG, "url="+url);
		JsonObjectRequest request = new JsonObjectRequest(url, null, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, "response="+response.toString());
						getLrc(response.optString("lrcContent"));
					}
			
				},
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		MusicApplication.getRequestQueue(getActivity()).add(request);
	}
	
	public void getLrc(String lrc)
	{
		lineLrcs.clear();
		lrc = lrc.replace("\r", "");
		String[] lrcs = lrc.split("\n");
		for(int i=0;i<lrcs.length;i++)
		{
			Log.d(TAG, "length "+lrcs[i]+" = "+lrcs[i].length());
			if(lrcs[i].length() > 10)
			{
				int lastTimePosition = lrcs[i].lastIndexOf("]");
				String text = lrcs[i].substring(lastTimePosition+1);
				for(int j=0;j<=lastTimePosition;j++)
				{
					if(lrcs[i].charAt(j) == '[')
					{
						Lrc lineLrc = new Lrc();
						lineLrc.time = lrcs[i].substring(j+1, j+6);
						lineLrc.text = text;
						lineLrc.text.trim();
						if(!TextUtils.isEmpty(lineLrc.text))
						{
							lineLrcs.add(lineLrc);
							Log.d(TAG, "time:"+lineLrc.time);
							Log.d(TAG, "text:"+lineLrc.text);
						}
					}
				}
			}
		}
		if(lineLrcs.size() == 0)
		{
			mTextView.setVisibility(View.VISIBLE);
		}
		else {
			mTextView.setVisibility(View.INVISIBLE);
		}
		Collections.sort(lineLrcs, new Comparator<Lrc>() {

			@Override
			public int compare(Lrc lhs, Lrc rhs) {
				String lTime = lhs.time;
				String rTime = rhs.time;
				for(int i=0;i<=3;i++)
				{
					char l = lTime.charAt(i);
					char r = rTime.charAt(i);
					if(l > r)
					{
						return 1;
					}
					else if(l < r)
					{
						return -1;
					}
					else if(l == r)
					{
						continue;
					}
				}
				return 0;
			}
		});
		for(int j=0;j<lineLrcs.size();j++)
		{

			Lrc lineLrc = lineLrcs.get(j);
			Log.d(TAG, "after time:" + lineLrc.time);
			Log.d(TAG, "after text:" + lineLrc.text);

		}
		adapter = new MusicLrcAdapter(getActivity(), lineLrcs, 0);
		mRecyclerView.setAdapter(adapter);
	}
	
	private class Receiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			int cmd = intent.getIntExtra(BroadCastUtils.CMD, 0);
			if(cmd == BroadCastUtils.CMD_PLAY)
			{
				time = 0;
				startTimer();
			}
			else if(cmd == BroadCastUtils.CMD_RESUME)
			{
				startTimer();
			}
			else if(cmd == BroadCastUtils.CMD_PAUSE)
			{
				timer.cancel();
			}
			else if(cmd == BroadCastUtils.CMD_CHANGE_SONG)
			{
				mTextView.setVisibility(View.INVISIBLE);
				Song receiveSong = (Song) intent.getSerializableExtra(BroadCastUtils.CMD_SONG);
				if(receiveSong != song)
				{
					getMusicLrc(Integer.valueOf(receiveSong.getSong_id()));
				}
				if(timer != null)
				{
					timer.cancel();
				}
			}
			else if(cmd == BroadCastUtils.CMD_CHANGE_PROGRESS)
			{
				int progress = intent.getIntExtra(BroadCastUtils.CMD_SONG_PROGRESS, 0);
				time = progress;
			}
		}
		
	}
	
	public void startTimer()
	{
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				time++;
				for(int i=0;i< lineLrcs.size();i++)
				{
					final int location = i;
					Lrc lrc = lineLrcs.get(i);
					String lrcTime = lrc.time;
					if(ImgUtils.getMusicTime(time).equals(lrcTime))
					{
						mRecyclerView.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								adapter = new MusicLrcAdapter(getActivity(), lineLrcs, location);
								mRecyclerView.setAdapter(adapter);
								mRecyclerView.scrollToPosition(location);
							}
						}, 300);
					}
				}
			}
		}, 0, 1000);
	}
}
