package com.example.aimimusic;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.aimimusic.adapter.FragmentAdapter;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.utils.BroadCastUtils;
import com.example.aimimusic.utils.ImgUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayMusicActivity extends FragmentActivity implements OnClickListener{
	private Song song;
	private MusicCoverFragment musicCoverFragment;
	private MusicLrcFragment musicLrcFragment;
	private FragmentAdapter adapter;
	private List<Fragment> fragments;
	private int switchType;
	private Receiver receiver;
	private Timer timer;
	private int time;
	private int playMode;
	private Toast toast;
	private boolean progressBarDraged;
	
	private ViewPager mPager;
	private ImageButton mImgClose;
	private ImageView mImgFirst;
	private ImageView mImgSecond;
	private ImageView mImgSwitch;
	private ImageView mImgMode;
	private ImageView mImgPre;
	private ImageView mImgNext;
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
	private final String TAG = "PlayMusicActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_play);
		initData();
		initView();
		IntentFilter intentFilter = new IntentFilter(BroadCastUtils.SERVICE_CMD);
		receiver = new Receiver();
		registerReceiver(receiver, intentFilter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(receiver != null)
		{
			unregisterReceiver(receiver);
		}
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
		playMode = TYPE_CIRCLE;
		progressBarDraged = false;
	}
	
	public void initView()
	{
		View status = findViewById(R.id.play_statusBarView);
    	ViewGroup.LayoutParams layoutParams = status.getLayoutParams();
    	layoutParams.height = ImgUtils.getStatusBarHeight(this);
		mPager = (ViewPager)findViewById(R.id.vp_play_music);
		mPager.setAdapter(adapter);
		mImgClose = (ImageButton)findViewById(R.id.ibtn_close_play);
		mImgClose.setOnClickListener(this);
		mImgMode = (ImageView)findViewById(R.id.img_play_music_mode);
		mImgMode.setOnClickListener(this);
		mImgPre = (ImageView)findViewById(R.id.img_play_music_previous);
		mImgPre.setOnClickListener(this);
		mImgNext = (ImageView)findViewById(R.id.img_play_music_next);
		mImgNext.setOnClickListener(this);
		mImgFirst = (ImageView)findViewById(R.id.img_first_index);
		mImgSecond = (ImageView)findViewById(R.id.img_second_index);
		mTxtTitle = (TextView)findViewById(R.id.txt_play_music_title);
		mTxtTitle.setText(song.getTitle());
		mTxtArtist = (TextView)findViewById(R.id.txt_play_music_artist);
		mTxtArtist.setText(song.getArtist_name());
		mTxtStart = (TextView)findViewById(R.id.txt_play_music_start);
		mTxtStart.setText(ImgUtils.getMusicTime(0));
		mTxtEnd = (TextView)findViewById(R.id.txt_play_music_end);
		mTxtEnd.setText(ImgUtils.getMusicTime(song.getFile_duration()));
		mImgSwitch = (ImageView)findViewById(R.id.img_play_music_switch);
		mImgSwitch.setOnClickListener(this);
		mSeekBarProgress = (SeekBar)findViewById(R.id.sb_paly_music_progress);
		mSeekBarProgress.setMax(song.getFile_duration());
		mSeekBarProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				progressBarDraged = true;
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				progressBarDraged = false;
			}
			
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(progressBarDraged == true)
				{
					Intent intent = new Intent(BroadCastUtils.MUSIC_SERVICE);
					intent.putExtra(BroadCastUtils.CMD, BroadCastUtils.CMD_CHANGE_PROGRESS);
					intent.putExtra(BroadCastUtils.CMD_SONG_PROGRESS, progress);
					sendBroadcast(intent);
				}
			}
		});
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
			break;
		case R.id.img_play_music_mode:
			sendControll(BroadCastUtils.CMD_CHANGE_TYPE, 0);
			if(playMode == TYPE_CIRCLE)
			{
				playMode = TYPE_RANDOM;
				mImgMode.setImageResource(R.drawable.ic_play_btn_shuffle);
				sendToast(getResources().getString(R.string.random_mode));
			}
			else if(playMode == TYPE_RANDOM)
			{
				playMode = TYPE_SINGLE;
				mImgMode.setImageResource(R.drawable.ic_play_btn_one);
				sendToast(getResources().getString(R.string.single_mode));
			}
			else if(playMode == TYPE_SINGLE)
			{
				playMode = TYPE_CIRCLE;
				mImgMode.setImageResource(R.drawable.ic_play_btn_loop);
				sendToast(getResources().getString(R.string.cicle_mode));
			}
			break;
		case R.id.img_play_music_previous:
			sendControll(BroadCastUtils.CMD_PREVIOUS, 0);
			break;
		case R.id.img_play_music_next:
			sendControll(BroadCastUtils.CMD_NEXT, 0);
			break;
		default:
			break;
		}
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
				Song receiveSong = (Song) intent.getSerializableExtra(BroadCastUtils.CMD_SONG);
				if(receiveSong != null)
				{
					song = receiveSong;
					mTxtTitle.setText(receiveSong.getTitle());
					mTxtArtist.setText(receiveSong.getArtist_name());
					mTxtEnd.setText(ImgUtils.getMusicTime(receiveSong.getFile_duration()));
					mSeekBarProgress.setMax(receiveSong.getFile_duration());
					mSeekBarProgress.setProgress(0);
					switchType = SWITCH_PLAY;
					mImgSwitch.setImageResource(R.drawable.ic_play_btn_pause);
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
				mSeekBarProgress.setProgress(time);
				Log.d(TAG, "change progress:"+progress);
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
				if(time > song.getFile_duration())
				{
					time = song.getFile_duration();
				}
				PlayMusicActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mSeekBarProgress.setProgress(time);
						mTxtStart.setText(ImgUtils.getMusicTime(time));
					}
				});
				
			}
		}, 0, 1000);
	}
	
	public void sendToast(String text)
	{
		if(toast == null)
		{
			toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
		}
		else {
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.show();
	}
	
}
