package com.example.aimimusic;

import com.example.aimimusic.utils.ImgUtils;
import com.example.aimimusic.view.EditTextWithDelete;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class SearchMusicActivity extends AppCompatActivity{
	private Toolbar mToolbar;
	private EditTextWithDelete mEditText;
	private RecyclerView mRecyclerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_music);
		initView();
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
				return false;
			}
		});
    	mEditText = (EditTextWithDelete)findViewById(R.id.edt_search);
    	mEditText.setHintTextColor(Color.parseColor("#99ffffff"));
    	mRecyclerView = (RecyclerView)findViewById(R.id.list_search_result);
    	mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_menu, menu);
		return true;
	}

}
