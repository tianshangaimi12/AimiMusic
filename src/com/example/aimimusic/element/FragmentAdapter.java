package com.example.aimimusic.element;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter{
	public FragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	private List<Fragment> fragments;
	

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
