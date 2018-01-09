package com.example.aimimusic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aimimusic.element.Lrc;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.utils.HttpUtils;
import com.example.aimimusic.utils.ImgUtils;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicCoverFragment extends Fragment{
	
	private Song song;
	private List<Lrc> lineLrcs;
	
	private TextView mTextView;
	private ImageView mImgCenter;
	private ImageView mImgCircle;
	
	private final String TAG = "MusicCoverFragment";
	
	public MusicCoverFragment(Song song)
	{
		this.song = song;
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_music_play_cover, container, false);
		init(view);
		getMusicLrc(Integer.valueOf(song.getSong_id()));
		return view;
	}
	
	public void init(View view)
	{
		lineLrcs = new ArrayList<Lrc>();
		mTextView = (TextView)view.findViewById(R.id.txt_play_music_single_lrc);
		mImgCenter = (ImageView)view.findViewById(R.id.img_play_music_cover);
		mImgCircle = (ImageView)view.findViewById(R.id.img_play_music_circle);
		if(!TextUtils.isEmpty(song.getPic_radio()))
		{
			Picasso.with(getActivity())
			.load(song.getPic_radio())
			.placeholder(R.drawable.play_page_default_cover)
			.centerCrop()
			.resize(ImgUtils.dp2pix(getActivity(), 180), ImgUtils.dp2pix(getActivity(), 180))
			.into(mImgCenter);
		}
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
			Log.d(TAG, "length "+lrcs[i]+" = "+lrcs[i].length());
			if(lrcs[i].length() > 10)
			{
				Lrc lineLrc = new Lrc();
				lineLrc.time = lrcs[i].substring(1, 6);
				lineLrc.text = lrcs[i].substring(10);
				lineLrc.text.trim();
				lineLrcs.add(lineLrc);
			}
		}
	}

}
