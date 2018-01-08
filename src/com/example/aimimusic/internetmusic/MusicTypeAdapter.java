package com.example.aimimusic.internetmusic;

import java.util.List;

import com.example.aimimusic.R;
import com.example.aimimusic.R.id;
import com.example.aimimusic.element.SongList;
import com.example.aimimusic.element.BillBoard;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.renderscript.Type;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicTypeAdapter extends RecyclerView.Adapter{
	
	private Context context;
	private List<SongList> songLists;
	
	private final int TYPE_TITLE = 1;
	private final int TYPE_MUSIC = 2;
	
	
	public MusicTypeAdapter(Context context, List<SongList> songLists)
	{
		this.context = context;
		this.songLists = songLists;
	}
	
	@Override
	public int getItemViewType(int position) {
		if(position == 0 || position == 3)
			return TYPE_TITLE;
		else return TYPE_MUSIC;
	}

	@Override
	public int getItemCount() {
		return songLists.size()+2;
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		if(arg0 instanceof TitleViewHolder)
		{
			TitleViewHolder viewHolder = (TitleViewHolder)arg0;
			if(arg1 == 0)
				viewHolder.txtType.setText(context.getResources().getString(R.string.main_music_list));
			else viewHolder.txtType.setText(context.getResources().getString(R.string.kind_music_list));
		}
		else if(arg0 instanceof MusicViewHolder)
		{
			int position = arg1 - 1;
			if(arg1 >= 4)
			{
				position = arg1 -2 ;
			}
			SongList songList = songLists.get(position);
			BillBoard billBoard = songList.getBillboard();
			MusicViewHolder viewHolder = (MusicViewHolder) arg0;
			Picasso.with(context)
			.load(billBoard.getPic_s192())
			.placeholder(R.drawable.default_cover)
			.resize(dp2pix(context, 100), dp2pix(context, 100))
			.into(viewHolder.imageView);
			viewHolder.txtOneMusic.setText("1."+songList.getSong_list().get(0).getTitle()+"-"+songList.getSong_list().get(0).getArtist_name());
			viewHolder.txtTwoMusic.setText("2."+songList.getSong_list().get(1).getTitle());
			viewHolder.txtThreeMusic.setText("3."+songList.getSong_list().get(2).getTitle());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		if(arg1 == TYPE_TITLE)
		{
			View view = LayoutInflater.from(context).inflate(R.layout.item_online_music_type_title, arg0, false);
			TitleViewHolder viewHolder = new TitleViewHolder(view);
			return viewHolder;
		}
		else if(arg1 == TYPE_MUSIC)
		{
			View view = LayoutInflater.from(context).inflate(R.layout.item_online_music_type, arg0, false);
			MusicViewHolder viewHolder = new MusicViewHolder(view);
			return viewHolder;
		}
		return null;
	}
	
	class MusicViewHolder extends ViewHolder
	{
		ImageView imageView;
		TextView txtOneMusic;
		TextView txtTwoMusic;
		TextView txtThreeMusic;

		public MusicViewHolder(View arg0) {
			super(arg0);
			imageView = (ImageView)arg0.findViewById(R.id.img_music_type);
			txtOneMusic = (TextView)arg0.findViewById(R.id.txt_type_music_one);
			txtTwoMusic = (TextView)arg0.findViewById(R.id.txt_type_music_two);
			txtThreeMusic = (TextView)arg0.findViewById(R.id.txt_type_music_three);
		}
		
	}
	
	class TitleViewHolder extends ViewHolder
	{
		TextView txtType;
		public TitleViewHolder(View arg0) {
			super(arg0);
			txtType = (TextView)arg0.findViewById(R.id.txt_type_music);
		}
		
	}

	public static int dp2pix(Context context, float dpValue)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (scale*dpValue+0.5f);
	}
}
