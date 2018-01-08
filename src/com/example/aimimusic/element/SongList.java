package com.example.aimimusic.element;

import java.io.Serializable;
import java.util.List;

public class SongList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8304205766465453665L;
	private int error_code;
	private BillBoard billboard;
	private List<Song> song_list;
	
	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public BillBoard getBillboard() {
		return billboard;
	}

	public void setBillboard(BillBoard billboard) {
		this.billboard = billboard;
	}

	public List<Song> getSong_list() {
		return song_list;
	}

	public void setSong_list(List<Song> song_list) {
		this.song_list = song_list;
	}

	
}
