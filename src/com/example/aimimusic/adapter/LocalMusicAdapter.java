package com.example.aimimusic.adapter;

import java.util.List;

import com.example.aimimusic.ItemClickListener;
import com.example.aimimusic.R;
import com.example.aimimusic.element.Song;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicHolder>{
	
	private Context context;
	private List<Song> localSongs;
	private ItemClickListener listener;
	
	public LocalMusicAdapter(Context context, List<Song> localSongs)
	{
		this.context = context;
		this.localSongs = localSongs;
	}
	

	@Override
	public int getItemCount() {
		return localSongs.size();
	}

	@Override
	public void onBindViewHolder(LocalMusicHolder arg0, final int arg1) {
		Song song = localSongs.get(arg1);
		arg0.txtTitle.setText(song.getTitle());
		arg0.txtArtist.setText(song.getArtist_name());
		arg0.imgEdit.setVisibility(View.INVISIBLE);
		arg0.itemView.setOnClickListener(new OnClickListener() {
			
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
	public LocalMusicHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, arg0, false);
		LocalMusicHolder holder = new LocalMusicHolder(view);
		return holder;
	}
	
	public void setItemClickListener(ItemClickListener listener)
	{
		this.listener = listener;
	}
	

}

class LocalMusicHolder extends ViewHolder
{

	TextView txtTitle;
	TextView txtArtist;
	ImageView imgEdit;
	public LocalMusicHolder(View arg0) {
		super(arg0);
		txtTitle = (TextView)arg0.findViewById(R.id.txt_search_title);
		txtArtist = (TextView)arg0.findViewById(R.id.txt_search_artist);
		imgEdit = (ImageView)arg0.findViewById(R.id.img_search_result_edit);
	}
	
}
