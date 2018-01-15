package com.example.aimimusic.adapter;

import java.util.List;

import com.example.aimimusic.R;
import com.example.aimimusic.element.Lrc;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MusicLrcAdapter extends RecyclerView.Adapter{
	
	private Context context;
	private List<Lrc> lineLrcs;
	private int location;
	
	public MusicLrcAdapter(Context context, List<Lrc> lineLrcs, int location)
	{
		this.context = context;
		this.lineLrcs = lineLrcs;
		this.location = location;
	}

	@Override
	public int getItemCount() {
		return lineLrcs.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		LrcViewHolder viewHolder = (LrcViewHolder)arg0;
		viewHolder.txtLrc.setText(lineLrcs.get(arg1).text);
		if(arg1 == location)
		{
			viewHolder.txtLrc.setTextColor(Color.WHITE);
		}
		else {
			viewHolder.txtLrc.setTextColor(Color.parseColor("#666666"));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_music_lrc, arg0, false);
		LrcViewHolder viewHolder = new LrcViewHolder(view);
		return viewHolder;
	}
	
	class LrcViewHolder extends ViewHolder
	{
		TextView txtLrc;

		public LrcViewHolder(View arg0) {
			super(arg0);
			txtLrc = (TextView)arg0.findViewById(R.id.txt_music_lrc);
		}
		
	}

}
