package com.example.aimimusic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aimimusic.adapter.SearchMusicAdapter;
import com.example.aimimusic.element.SearchSong;
import com.example.aimimusic.element.SearchSongList;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.element.SongList;
import com.example.aimimusic.utils.HttpUtils;
import com.example.aimimusic.utils.ImgUtils;
import com.example.aimimusic.utils.KeyBoardUtils;
import com.example.aimimusic.view.EditTextWithDelete;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class SearchMusicActivity extends AppCompatActivity implements ItemClickListener{
	private Toolbar mToolbar;
	private EditTextWithDelete mEditText;
	private RecyclerView mRecyclerView;
	
	private SearchSongList searchSongList;
	private List<SearchSong> searchSongs;
	private List<Song> songs;
	private SearchMusicAdapter adapter;
	
	private final String TAG = "SearchMusicActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_music);
		initView();
		initData();
	}
	
	public void initView()
	{
		View status = findViewById(R.id.search_statusBarView);
    	ViewGroup.LayoutParams layoutParams = status.getLayoutParams();
    	layoutParams.height = ImgUtils.getStatusBarHeight(this);
    	mToolbar = (Toolbar)findViewById(R.id.toolbar_search);
    	mToolbar.setTitle("");
    	setSupportActionBar(mToolbar);
    	mToolbar.setNavigationIcon(R.drawable.ic_back);
    	mToolbar.setNavigationOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SearchMusicActivity.this.finish();
			}
		});
    	mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				switch (arg0.getItemId()) {
				case R.id.menu_search:
					if(!TextUtils.isEmpty(mEditText.getText().toString()))
					{
						getSearchResult(mEditText.getText().toString());
						KeyBoardUtils.closeKeybord(mEditText, SearchMusicActivity.this);
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
    	mEditText = (EditTextWithDelete)findViewById(R.id.edt_search);
    	mEditText.setHintTextColor(Color.parseColor("#99ffffff"));
    	mRecyclerView = (RecyclerView)findViewById(R.id.list_search_result);
    	mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
	}
	
	public void initData()
	{
		songs = new ArrayList<Song>();
		searchSongList = new SearchSongList();
		searchSongs = new ArrayList<SearchSong>();
		adapter = new SearchMusicAdapter(this, searchSongs);
		mRecyclerView.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_menu, menu);
		return true;
	}
	
	public void getSearchResult(String query)
	{
		String url = HttpUtils.getSearchUrl(query);
		Log.d(TAG, "url:"+url);
		JsonObjectRequest request = new JsonObjectRequest(url, null, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, "response:"+response.toString());
						if(response.optInt("error_code") == 22000)
						{
							Gson gson = new Gson();
							Type type = new TypeToken<ArrayList<SearchSong>>(){}.getType();
							List<SearchSong> nSearchSongs = gson.fromJson(response.optJSONArray("song").toString(), type);
							generateSongs(nSearchSongs);
							adapter = new SearchMusicAdapter(SearchMusicActivity.this, nSearchSongs);
							adapter.setItemClickListener(SearchMusicActivity.this);
							mRecyclerView.setAdapter(adapter);
						}
						else if(response.optInt("error_code") == 22001)
						{
							Toast.makeText(SearchMusicActivity.this, getResources().getString(R.string.search_null_toast), Toast.LENGTH_SHORT).show();
						}
					}
				}, 
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		MusicApplication.getRequestQueue(this).add(request);
	}

	
	public void generateSongs(List<SearchSong> searchSongs)
	{
		songs.clear();
		for(int i=0;i<searchSongs.size();i++)
		{
			SearchSong searchSong = searchSongs.get(i);
			Song song = new Song(searchSong.getSongid(), searchSong.getSongname(), searchSong.getArtistname());
			songs.add(song);
		}
		SongList songList = new SongList();
		songList.setSong_list(songs);
		Intent serviceIntent = new Intent(SearchMusicActivity.this,MusicPlayService.class);
		serviceIntent.putExtra("songlist", songList);
		startService(serviceIntent);
	}
	
	@Override
	public void onclick(View view, int position) {
		Intent intent = new Intent(SearchMusicActivity.this, PlayMusicActivity.class);
		intent.putExtra("song", songs.get(position));
		startActivity(intent);
	}

}
