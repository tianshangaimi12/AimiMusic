package com.example.aimimusic;

import com.example.aimimusic.adapter.MusicInfoAdapter;
import com.example.aimimusic.element.SongList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MusicListActivity extends Activity{
	
	private MusicInfoAdapter adapter;
	private SongList songList;
	
	private Toolbar mToolbar;
	private TextView mTextView;
	private RecyclerView mRecyclerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_list);
		init();
		
	}
	
	public void init()
	{
		Intent intent = getIntent();
		final SongList songList = (SongList) intent.getSerializableExtra("songlist");
		mToolbar = (Toolbar)findViewById(R.id.toolbar_music_list);
		mToolbar.setNavigationIcon(R.drawable.ic_back);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MusicListActivity.this.finish();
			}
		});
		mTextView = (TextView)findViewById(R.id.txt_music_list_title);
		mTextView.setText(songList.getBillboard().getName());
		mRecyclerView = (RecyclerView)findViewById(R.id.list_music_list);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new MusicInfoAdapter(this, songList,true);
		adapter.setItemClickListener(new ItemClickListener() {
			
			@Override
			public void onclick(View view, int position) {
				Intent intent = new Intent(MusicListActivity.this, PlayMusicActivity.class);
				intent.putExtra("song", songList.getSong_list().get(position));
				startActivity(intent);
			}
		});
		mRecyclerView.setAdapter(adapter);
	}

}
