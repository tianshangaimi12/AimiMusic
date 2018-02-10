package com.example.aimimusic.existmusic;

import java.util.List;

import com.example.aimimusic.ItemClickListener;
import com.example.aimimusic.MusicPlayService;
import com.example.aimimusic.PlayMusicActivity;
import com.example.aimimusic.R;
import com.example.aimimusic.SearchMusicActivity;
import com.example.aimimusic.adapter.LocalMusicAdapter;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.element.SongList;
import com.example.aimimusic.utils.BroadCastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExsitMusicFragment extends Fragment{
	private RecyclerView mRecyclerView;
	private LocalMusicAdapter adapter;
	private List<Song> songs;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exsit_music, container,false);
		init(view);
		startService();
		adapter.setItemClickListener(new ItemClickListener() {
			
			@Override
			public void onclick(View view, int position) {
				Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
				intent.putExtra("song", songs.get(position));
				intent.putExtra(BroadCastUtils.EXTRA_PLAY_MUSIC_TYPE, BroadCastUtils.TYPE_LOCAL);
				startActivity(intent);
			}
		});
		return view;
	}
	
	public void init(View view)
	{
		mRecyclerView = (RecyclerView)view.findViewById(R.id.list_local_musiclist);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		songs = LocalMusic.getLocalMusicList(getActivity());
		adapter = new LocalMusicAdapter(getActivity(), songs);
		mRecyclerView.setAdapter(adapter);
		
	}
	
	public void startService()
	{
		SongList songList = new SongList();
		songList.setSong_list(songs);
		Intent serviceIntent = new Intent(getActivity(),MusicPlayService.class);
		serviceIntent.putExtra("songlist", songList);
		getActivity().startService(serviceIntent);
	}
}
