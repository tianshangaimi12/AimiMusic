package com.example.aimimusic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aimimusic.adapter.MusicLrcAdapter;
import com.example.aimimusic.element.Lrc;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.utils.HttpUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MusicLrcFragment extends Fragment{
	
	private Song song;
	private List<Lrc> lineLrcs;
	private MusicLrcAdapter adapter;
	
	private RecyclerView mRecyclerView;
	
	private final String TAG = "MusicLrcFragment";
	
	public MusicLrcFragment(Song song)
	{
		this.song = song;
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
		lrc = lrc.replace("\r", "");
		String[] lrcs = lrc.split("\n");
		for(int i=0;i<lrcs.length;i++)
		{
			//Log.d(TAG, "length "+lrcs[i]+" = "+lrcs[i].length());
			if(lrcs[i].length() > 10)
			{
				Lrc lineLrc = new Lrc();
				lineLrc.time = lrcs[i].substring(1, 6);
				lineLrc.text = lrcs[i].substring(10);
				lineLrc.text = lineLrc.text.trim();
				Log.d(TAG, "lineLrc="+lineLrc.text+":length="+lineLrc.text.length());
				lineLrcs.add(lineLrc);
			}
		}
		adapter = new MusicLrcAdapter(getActivity(), lineLrcs);
		mRecyclerView.setAdapter(adapter);
	}
}
