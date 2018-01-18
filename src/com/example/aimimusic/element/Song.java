package com.example.aimimusic.element;

import java.io.Serializable;


public class Song implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -435440040679697091L;
	private String song_id;
	private String title;
	private String pic_small;
	private String pic_radio;
	private String artist_name;
	private String artist_id;
	private String publishtime;
	private String playUrl;
	private int file_duration;
	private String lrclink;
	private String album_title;
	
	
	public Song()
	{
		
	}
	
	public Song(String song_id, String title, String artist_name)
	{
		this.song_id = song_id;
		this.title = title;
		this.artist_name = artist_name;
	}
	
	public int getFile_duration() {
		return file_duration;
	}
	public void setFile_duration(int file_duration) {
		this.file_duration = file_duration;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public String getArtist_id() {
		return artist_id;
	}
	public void setArtist_id(String artist_id) {
		this.artist_id = artist_id;
	}
	public String getPublishtime() {
		return publishtime;
	}
	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}
	public String getAlbum_title() {
		return album_title;
	}
	public void setAlbum_title(String album_title) {
		this.album_title = album_title;
	}
	public String getSong_id() {
		return song_id;
	}
	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic_small() {
		return pic_small;
	}
	public void setPic_small(String pic_small) {
		this.pic_small = pic_small;
	}
	public String getPic_radio() {
		return pic_radio;
	}
	public void setPic_radio(String pic_radio) {
		this.pic_radio = pic_radio;
	}
	public String getArtist_name() {
		return artist_name;
	}
	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}
	public String getLrclink() {
		return lrclink;
	}
	public void setLrclink(String lrclink) {
		this.lrclink = lrclink;
	}
}
