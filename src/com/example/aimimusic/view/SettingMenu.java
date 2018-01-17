package com.example.aimimusic.view;

import com.example.aimimusic.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingMenu extends LinearLayout{
	
	private ImageView imgSetting;
	private TextView txtSetting;
	private LinearLayout layout;
	
	public SettingMenu(Context context)
	{
		this(context, null);
	}

	public SettingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.menu_setting, this);
		imgSetting = (ImageView)layout.findViewById(R.id.img_menu_setting);
		txtSetting = (TextView)layout.findViewById(R.id.txt_menu_setting);
	}
	
	public void setResource(int imgId, int txtId)
	{
		imgSetting.setImageResource(imgId);
		txtSetting.setText(txtId);
	}

}
