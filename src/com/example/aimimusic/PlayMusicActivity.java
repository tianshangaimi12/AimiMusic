package com.example.aimimusic;

import java.util.ArrayList;
import java.util.List;

import com.example.aimimusic.adapter.FragmentAdapter;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.utils.BroadCastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayMusicActivity extends FragmentActivity implements OnClickListener{
	private Song song;
	private MusicCoverFragment musicCoverFragment;
	private MusicLrcFragment musicLrcFragment;
	private FragmentAdapter adapter;
	private List<Fragment> fragments;
	private int switchType;
	
	private ViewPager mPager;
	private ImageButton mImgClose;
	private ImageView mImgFirst;
	private ImageView mImgSecond;
	private ImageView mImgSwitch;
	private TextView mTxtTitle;
	private TextView mTxtArtist;
	private TextView mTxtStart;
	private TextView mTxtEnd;
	private SeekBar mSeekBarProgress;
	
	private final int TYPE_CIRCLE = 1;
	private final int TYPE_RANDOM = 2;
	private final int TYPE_SINGLE = 3;
	private final int SWITCH_PLAY = 1;
	private final int SWITCH_PAUSE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_play);
		initData();
		initView();
	}
	
	public void initData()
	{
		Intent intent = getIntent();
		song = (Song) intent.getSerializableExtra("song");
		musicCoverFragment = new MusicCoverFragment(song);
		musicLrcFragment = new MusicLrcFragment(song);
		fragments = new ArrayList<Fragment>();
		fragments.add(musicCoverFragment);
		fragments.add(musicLrcFragment);
		adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
		switchType = SWITCH_PAUSE;
	}
	
	public void initView()
	{
		mPager = (ViewPager)findViewById(R.id.vp_play_music);
		mPager.setAdapter(adapter);
		mImgClose = (ImageButton)findViewById(R.id.ibtn_close_play);
		mImgClose.setOnClickListener(this);
		mImgFirst = (ImageView)findViewById(R.id.img_first_index);
		mImgSecond = (ImageView)findViewById(R.id.img_second_index);
		mTxtTitle = (TextView)findViewById(R.id.txt_play_music_title);
		mTxtTitle.setText(song.getTitle());
		mTxtArtist = (TextView)findViewById(R.id.txt_play_music_artist);
		mTxtArtist.setText(song.getArtist_name());
		mTxtStart = (TextView)findViewById(R.id.txt_play_music_start);
		mTxtStart.setText(getMusicTime(0));
		mTxtEnd = (TextView)findViewById(R.id.txt_play_music_end);
		mTxtEnd.setText(getMusicTime(song.getFile_duration()));
		mImgSwitch = (ImageView)findViewById(R.id.img_play_music_switch);
		mImgSwitch.setOnClickListener(this);
		mPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(arg0 == 0)
				{
					mImgFirst.setImageResource(R.drawable.ic_white_index);
					mImgSecond.setImageResource(R.drawable.ic_gray_index);
				}
				else if(arg0 == 1)
				{
					mImgSecond.setImageResource(R.drawable.ic_white_index);
					mImgFirst.setImageResource(R.drawable.ic_gray_index);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_close_play:
			PlayMusicActivity.this.finish();
			break;
		case R.id.img_play_music_switch:
			if(switchType == SWITCH_PAUSE)
			{
				switchType = SWITCH_PLAY;
				mImgSwitch.setImageResource(R.drawable.ic_play_btn_pause);
				sendControll(BroadCastUtils.CMD_PLAY, Integer.valueOf(song.getSong_id()));
			}
			else if(switchType == SWITCH_PLAY)
			{
				switchType = SWITCH_PAUSE;
				mImgSwitch.setImageResource(R.drawable.ic_play_btn_play);
				sendControll(BroadCastUtils.CMD_PAUSE, 0);
			}
		default:
			break;
		}
	}

	public String getMusicTime(int duration)
	{
		String time = "";
		if(duration == 0)
		{
			time = "00:00";
		}
		else
		{
			int minute = duration/60;
			int second = duration%60;	
			time = "0"+minute+":"+(second<10 ? "0"+second : second+"");
		}
		return time;	
	}
	
	public void sendControll(int cmdType, int songId)
	{
		Intent intent = new Intent(BroadCastUtils.MUSIC_SERVICE);
		intent.putExtra(BroadCastUtils.CMD, cmdType);
		if(songId != 0)
		{
			intent.putExtra(BroadCastUtils.CMD_SONGID, songId);
		}
		sendBroadcast(intent);
	}
	
}
