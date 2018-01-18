package com.example.aimimusic.adapter;

import java.util.List;

import com.example.aimimusic.ItemClickListener;
import com.example.aimimusic.R;
import com.example.aimimusic.element.SearchSong;
import com.example.aimimusic.element.SearchSongList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchMusicAdapter extends RecyclerView.Adapter{
	
	private Context context;
	private List<SearchSong> searchSongs;
	private ItemClickListener listener;
	
	public SearchMusicAdapter(Context context, List<SearchSong> searchSongs)
	{
		this.context = context;
		this.searchSongs = searchSongs;
	}

	@Override
	public int getItemCount() {
		return searchSongs.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, final int arg1) {
		SearchSong song = searchSongs.get(arg1);
		SearchHolder holder = (SearchHolder) arg0;
		holder.txtTitle.setText(song.getSongname());
		holder.txtArtist.setText(song.getArtistname());
		holder.itemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null)
				{
					listener.onclick(v, arg1);
				}
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, arg0, false);
		SearchHolder holder = new SearchHolder(view);
		return holder;
	}

	class SearchHolder extends ViewHolder
	{
		TextView txtTitle;
		TextView txtArtist;
		ImageView imgEdit;

		public SearchHolder(View arg0) {
			super(arg0);
			txtTitle = (TextView)arg0.findViewById(R.id.txt_search_title);
			txtArtist = (TextView)arg0.findViewById(R.id.txt_search_artist);
			imgEdit = (ImageView)arg0.findViewById(R.id.img_search_result_edit);
		}
		
	}
	
	public void setItemClickListener(ItemClickListener listener)
	{
		this.listener = listener;
	}
}
