package com.example.aimimusic;

import java.util.ArrayList;
import java.util.List;

import com.example.aimimusic.adapter.FragmentAdapter;
import com.example.aimimusic.existmusic.ExsitMusicFragment;
import com.example.aimimusic.internetmusic.InternetMusicFragment;
import com.example.aimimusic.utils.ImgUtils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener{
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	private TextView mTextViewExist;
	private TextView mTextViewInternet;
	private ViewPager mViewPager;
	
	private List<Fragment> fragments;
	private ExsitMusicFragment exsitMusicFragment;
	private InternetMusicFragment internetMusicFragment;
	private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }
    
    public void initData()
    {
    	fragments = new ArrayList<Fragment>();
    	exsitMusicFragment = new ExsitMusicFragment();
    	internetMusicFragment = new InternetMusicFragment();
    	settingFragment = new SettingFragment();
    	fragments.add(internetMusicFragment);
    	fragments.add(exsitMusicFragment);
    	getSupportFragmentManager().beginTransaction().add(R.id.framlayout_setting, settingFragment).commit();
    }
    
    public void initView()
    {
    	View status = findViewById(R.id.main_statusBarView);
    	ViewGroup.LayoutParams layoutParams = status.getLayoutParams();
    	layoutParams.height = ImgUtils.getStatusBarHeight(this);
    	mToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout_main);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		});
         mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				switch (arg0.getItemId()) {
				case R.id.btn_search:
					Intent intent = new Intent(MainActivity.this, SearchMusicActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
				return true;
			}
		});
        mTextViewExist = (TextView)findViewById(R.id.txt_exist_music);
        mTextViewExist.setOnClickListener(this);
        mTextViewInternet = (TextView)findViewById(R.id.txt_internet_music);
        mTextViewExist.setTextColor(Color.parseColor("#aaaaaa"));
        mTextViewInternet.setOnClickListener(this);
        mViewPager = (ViewPager)findViewById(R.id.viewpager_main);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(arg0 == 0)
				{
					mTextViewInternet.setTextColor(Color.WHITE);
					mTextViewExist.setTextColor(Color.parseColor("#aaaaaa"));
				}
				else {
					mTextViewInternet.setTextColor(Color.parseColor("#aaaaaa"));
					mTextViewExist.setTextColor(Color.WHITE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			
			}
		});
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main_menu, menu);
    	return true;
    }
    
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
		case R.id.txt_exist_music:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.txt_internet_music:
			mViewPager.setCurrentItem(0);
			break;
		default:
			break;
		}
    }
}
