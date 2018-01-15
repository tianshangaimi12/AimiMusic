package com.example.aimimusic;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aimimusic.element.Lrc;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.utils.BroadCastUtils;
import com.example.aimimusic.utils.HttpUtils;
import com.example.aimimusic.utils.ImgUtils;
import com.squareup.picasso.Picasso;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicCoverFragment extends Fragment{
	
	private Song song;
	private List<Lrc> lineLrcs;
	private Animation rotateAnimation;
	private Animation centerAnimation;
	private Receiver receiver;
	private int time;
	private Timer timer;
	private boolean barAnimation;
	
	private TextView mTextView;
	private ImageView mImgCenter;
	private ImageView mImgCircle;
	private ImageView mImgBar;
	
	private final String TAG = "MusicCoverFragment";
	
	public MusicCoverFragment(Song song)
	{
		this.song = song;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter(BroadCastUtils.SERVICE_CMD);
		receiver = new Receiver();
		getActivity().registerReceiver(receiver, intentFilter);
		barAnimation = true;
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
		View view = inflater.inflate(R.layout.fragment_music_play_cover, container, false);
		init(view);
		startBarAnimation();
		getMusicLrc(Integer.valueOf(song.getSong_id()));
		return view;
	}
	
	public void init(View view)
	{
		lineLrcs = new ArrayList<Lrc>();
		mTextView = (TextView)view.findViewById(R.id.txt_play_music_single_lrc);
		mImgCenter = (ImageView)view.findViewById(R.id.img_play_music_cover);
		mImgCircle = (ImageView)view.findViewById(R.id.img_play_music_circle);
		mImgBar = (ImageView)view.findViewById(R.id.img_play_music_bar);
		if(!TextUtils.isEmpty(song.getPic_radio()))
		{
			Picasso.with(getActivity())
			.load(song.getPic_radio())
			.placeholder(R.drawable.play_page_default_cover)
			.centerCrop()
			.resize(ImgUtils.dp2pix(getActivity(), 180), ImgUtils.dp2pix(getActivity(), 180))
			.into(mImgCenter);
		}
		rotateAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.img_rotate);
		centerAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.center_rotate);
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
	
	/**
	 * @param lrc
	 */
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
				Lrc lineLrc = new Lrc();
				lineLrc.time = lrcs[i].substring(1, 6);
				lineLrc.text = lrcs[i].substring(10);
				lineLrc.text.trim();
				lineLrcs.add(lineLrc);
			}
		}
	}
	
	public void startPlayMusic()
	{
		time = 0;
		mImgCenter.startAnimation(centerAnimation);
		mImgCircle.startAnimation(rotateAnimation);
		startTimer();
		startBarAnimation();
	}
	
	public void pausePlayMusic()
	{
		rotateAnimation.cancel();
		centerAnimation.cancel();
		if(timer != null)
		{
			timer.cancel();
		}
		startBarAnimation();
	}
	
	public void startBarAnimation()
	{
		if(barAnimation == true)
		{
			ObjectAnimator animator = ObjectAnimator.ofFloat(mImgBar, "rotation", 0, -20);
			mImgBar.setPivotX(20);
			mImgBar.setPivotY(20);
			animator.setDuration(500);
			animator.start();
			barAnimation = false;
		}
		else
		{
			ObjectAnimator animator = ObjectAnimator.ofFloat(mImgBar, "rotation", -20, 0);
			mImgBar.setPivotX(20);
			mImgBar.setPivotY(20);
			animator.setDuration(500);
			animator.start();
			barAnimation = true;
		}
	}
	
	public void startTimer()
	{
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				time++;
				for(final Lrc lrc : lineLrcs)
				{
					String lrcTime = lrc.time;
					if(ImgUtils.getMusicTime(time).equals(lrcTime))
					{
						mTextView.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								mTextView.setText(lrc.text);
							}
						}, 300);
					}
				}
			}
		}, 0, 1000);
	}
	
	private class Receiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			int cmd = intent.getIntExtra(BroadCastUtils.CMD, 0);
			if(cmd == BroadCastUtils.CMD_PLAY)
			{
				startPlayMusic();
			}
			else if(cmd == BroadCastUtils.CMD_RESUME)
			{
				startTimer();
				mImgCenter.startAnimation(centerAnimation);
				mImgCircle.startAnimation(rotateAnimation);
				startBarAnimation();
			}
			else if(cmd == BroadCastUtils.CMD_PAUSE)
			{
				pausePlayMusic();
			}
			else if(cmd == BroadCastUtils.CMD_CHANGE_SONG)
			{
				Song receiveSong = (Song) intent.getSerializableExtra(BroadCastUtils.CMD_SONG);
				if(receiveSong != song)
				{
					getMusicLrc(Integer.valueOf(receiveSong.getSong_id()));
					Picasso.with(getActivity())
					.load(receiveSong.getPic_radio())
					.placeholder(R.drawable.play_page_default_cover)
					.centerCrop()
					.resize(ImgUtils.dp2pix(getActivity(), 180), ImgUtils.dp2pix(getActivity(), 180))
					.into(mImgCenter);
				}
				rotateAnimation.cancel();
				centerAnimation.cancel();
				if(timer != null)
				{
					timer.cancel();
				}
				if(barAnimation)
				{
					startBarAnimation();
				}
			}
			else if(cmd == BroadCastUtils.CMD_CHANGE_PROGRESS)
			{
				int progress = intent.getIntExtra(BroadCastUtils.CMD_SONG_PROGRESS, 0);
				time = progress;
			}
		}
		
	}

}
