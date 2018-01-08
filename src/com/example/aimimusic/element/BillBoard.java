package com.example.aimimusic.element;

import java.io.Serializable;

public class BillBoard implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1665137868072611580L;
	private String name;
	private String pic_s192;
	private String pic_s260;
	public String getPic_s260() {
		return pic_s260;
	}
	public void setPic_s260(String pic_s260) {
		this.pic_s260 = pic_s260;
	}
	private String comment;
	private String update_date;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic_s192() {
		return pic_s192;
	}
	public void setPic_s192(String pic_s192) {
		this.pic_s192 = pic_s192;
	}
}
