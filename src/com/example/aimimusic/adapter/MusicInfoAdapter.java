package com.example.aimimusic.adapter;

import com.example.aimimusic.ItemClickListener;
import com.example.aimimusic.R;
import com.example.aimimusic.element.BillBoard;
import com.example.aimimusic.element.Song;
import com.example.aimimusic.element.SongList;
import com.example.aimimusic.utils.ImgUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicInfoAdapter extends RecyclerView.Adapter{

	private Context context;
	private SongList songList;
	private boolean onLineList;
	private ItemClickListener listener;
	
	private final int TYPE_TITLE = 1;
	private final int TYPE_MUSIC = 2;
	
	public MusicInfoAdapter(Context context, SongList songList, boolean onLineList)
	{
		this.context = context;
		this.songList = songList;
		this.onLineList = onLineList;
	}
	
	@Override
	public int getItemCount() {
		if(songList.getSong_list() != null)
		{
			if(onLineList)
			{
				return songList.getSong_list().size()+1;
			}
			else {
				return songList.getSong_list().size();
			}
		}
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if(onLineList && position == 0)
			return TYPE_TITLE;
		else return TYPE_MUSIC;
	}
	
	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		if(arg0 instanceof MusicInfoHolder)
		{
			MusicInfoHolder holder = (MusicInfoHolder) arg0;
			if(onLineList)
				arg1 = arg1-1;
			final int position = arg1;
			Song song = songList.getSong_list().get(arg1);
			Picasso.with(context).load(song.getPic_small())
			.resize(ImgUtils.dp2pix(context, 50), ImgUtils.dp2pix(context, 50))
			.into(holder.imgMusic);
			holder.txtTitle.setText(song.getTitle());
			holder.txtInfo.setText(song.getArtist_name()+"-"+song.getAlbum_title());
			holder.itemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener != null)
					{
						listener.onclick(v, position);
					}
				}
			});
		}
		else if(arg0 instanceof OnlineTitleHolder)
		{
			final OnlineTitleHolder holder = (OnlineTitleHolder) arg0;
			BillBoard billBoard = songList.getBillboard();
			Picasso.with(context).load(billBoard.getPic_s260())
			.resize(ImgUtils.dp2pix(context, 120), ImgUtils.dp2pix(context, 120))
			.into(holder.imgList, new Callback() {
				
				@SuppressLint("NewApi") @Override
				public void onSuccess() {
					BitmapDrawable drawable = (BitmapDrawable) holder.imgList.getDrawable();
					Bitmap bitmap = Bitmap.createBitmap(drawable.getBitmap(), 0, 0, 50, 100);
					holder.itemView.setBackground(new BitmapDrawable(bitmap));
				}
				
				@Override
				public void onError() {
				}
			});
			holder.txtListTitle.setText(billBoard.getName());
			holder.txtListInfo.setText(billBoard.getComment());
			holder.txtListDate.setText("最近更新:"+billBoard.getUpdate_date());
			
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		if(arg1 == TYPE_MUSIC)
		{
			View view = LayoutInflater.from(context).inflate(R.layout.item_music_info, arg0, false);
			MusicInfoHolder holder = new MusicInfoHolder(view);
			return holder;
		}
		else {
			View view = LayoutInflater.from(context).inflate(R.layout.item_music_list_info, arg0, false);
			OnlineTitleHolder holder = new OnlineTitleHolder(view);
			return holder;
		}
	}
	
	class MusicInfoHolder extends ViewHolder
	{
		ImageView imgMusic;
		TextView txtTitle;
		TextView txtInfo;
		ImageView imgEdit;
		public MusicInfoHolder(View arg0) {
			super(arg0);
			imgMusic = (ImageView)arg0.findViewById(R.id.img_music_img);
			txtTitle = (TextView)arg0.findViewById(R.id.txt_music_title);
			txtInfo = (TextView)arg0.findViewById(R.id.txt_music_info);
			imgEdit = (ImageView)arg0.findViewById(R.id.img_music_edit);
		}
		
	}
	
	class OnlineTitleHolder extends ViewHolder
	{
		ImageView imgList;
		TextView txtListTitle;
		TextView txtListDate;
		TextView txtListInfo;
		public OnlineTitleHolder(View arg0) {
			super(arg0);
			imgList = (ImageView)arg0.findViewById(R.id.img_music_list_back);
			txtListTitle = (TextView)arg0.findViewById(R.id.txt_music_list_back_title);
			txtListDate = (TextView)arg0.findViewById(R.id.txt_music_list_back_date);
			txtListInfo = (TextView)arg0.findViewById(R.id.txt_music_list_back_info);
		}
		
	}
	
	public void setItemClickListener(ItemClickListener listener)
	{
		this.listener = listener;
	}

}
