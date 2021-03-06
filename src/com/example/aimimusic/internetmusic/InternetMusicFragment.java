package com.example.aimimusic.internetmusic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.aimimusic.ItemClickListener;
import com.example.aimimusic.MusicApplication;
import com.example.aimimusic.MusicListActivity;
import com.example.aimimusic.MusicPlayService;
import com.example.aimimusic.R;
import com.example.aimimusic.adapter.MusicTypeAdapter;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.element.SongList;
import com.example.aimimusic.utils.HttpUtils;
import com.google.gson.Gson;

import android.R.anim;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class InternetMusicFragment extends Fragment{
	
	private Gson gson;
	private List<Song> newSongs;
	private List<Song> hotSongs;
	private List<Song> rockSongs;
	private List<Song> classicSongs;
	private List<Song> onlineSongs;
	private SongList newSongList;
	private SongList hotSongList;
	private SongList rockSongList;
	private SongList classicSongList;
	private SongList onlineSongList;
	private List<SongList> songLists;
	private MusicTypeAdapter adapter;
	private int songListNum;
	
	private RecyclerView mMusicList;
	private LinearLayout mLoadingLayout;
	private ImageView mLoadingImg;
	private AnimationDrawable anim;
	
	private final String TAG = "InternetMusicFragment";
	private final int SONG_SIZE = 20;
	private final int UPDATE_LIST = 1;
	private final int[] SONG_TYPES = {1, 2, 11, 22, 25};
	
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what == UPDATE_LIST)
			{
				if(newSongList != null)
				{
					songLists.add(newSongList);
				}
				if(hotSongList != null)
				{
					songLists.add(hotSongList);
				}
				if(rockSongList != null)
				{
					songLists.add(rockSongList);
				}
				if(classicSongList != null)
				{
					songLists.add(classicSongList);
				}
				if(onlineSongList != null)
				{
					songLists.add(onlineSongList);
				}
				adapter.notifyDataSetChanged();
				mMusicList.setAdapter(adapter);
				anim.stop();
				mLoadingLayout.setVisibility(View.GONE);
			}
		};
	};
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gson = new Gson();
		newSongs = new ArrayList<Song>();
		hotSongs = new ArrayList<Song>();
		rockSongs = new ArrayList<Song>();
		classicSongs = new ArrayList<Song>();
		onlineSongs = new ArrayList<Song>();
		songLists = new ArrayList<SongList>();
		songListNum = 0;
		adapter = new MusicTypeAdapter(getActivity(), songLists);
		adapter.setClickListener(new ItemClickListener() {
			
			@Override
			public void onclick(View view, int position) {
				Intent serviceIntent = new Intent(getActivity(),MusicPlayService.class);
				serviceIntent.putExtra("songlist", songLists.get(position));
				getActivity().startService(serviceIntent);
				Intent intent = new Intent(getActivity(), MusicListActivity.class);
				intent.putExtra("songlist", songLists.get(position));
				startActivity(intent);
			}
		});
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_internet_music, container,false);
		mLoadingLayout = (LinearLayout)view.findViewById(R.id.ll_loading);
		mLoadingImg = (ImageView)view.findViewById(R.id.img_ic_loading);
		anim = (AnimationDrawable) mLoadingImg.getBackground();
		anim.start();
		mMusicList = (RecyclerView)view.findViewById(R.id.list_internet_musiclist);
		mMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
		for(int i : SONG_TYPES)
		{
			getMusicList(i, SONG_SIZE);
		}
		return view;
	}
	
	public void getMusicList(final int type,int size)
	{
		String url = HttpUtils.getMusicListUrl(type, size);
		Log.d(TAG, "url="+url);
		JsonObjectRequest request = new JsonObjectRequest(url, null, 
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						
						Log.d(TAG, response.toString());
						setSongList(type, response);
					}
				}, 
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getActivity(), "Error in getting song list type "+type+"!", Toast.LENGTH_LONG).show();
					}
				});
		MusicApplication.getRequestQueue(getActivity()).add(request);
	}
	
	public void setSongList(int type, JSONObject response)
	{
		songListNum++;
		SongList songList = gson.fromJson(response.toString(), SongList.class);
		if(songList.getError_code() != 22000 || TextUtils.isEmpty(songList.getBillboard().getName()))
		{
			Toast.makeText(getActivity(), "Error in getting song list type "+type+"!", Toast.LENGTH_LONG).show();
			if(songListNum == SONG_TYPES.length)
			{
				handler.sendEmptyMessage(UPDATE_LIST);
			}
			return;
		}
		if(songListNum == SONG_TYPES.length)
		{
			handler.sendEmptyMessage(UPDATE_LIST);
		}
		switch (type) {
		case 1:
			newSongList = songList;
			newSongs = songList.getSong_list();
			break;
		case 2:
			hotSongList = songList;
			hotSongs = songList.getSong_list();
			break;
		case 11:
			rockSongList = songList;
			rockSongs = songList.getSong_list();
			break;
		case 22:
			classicSongList = songList;
			classicSongs = songList.getSong_list();
			break;
		case 25:
			onlineSongList = songList;
			onlineSongs = songList.getSong_list();
			break;
		default:
			break;
		}
	}
	
}
