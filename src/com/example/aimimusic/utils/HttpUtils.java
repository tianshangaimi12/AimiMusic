package com.example.aimimusic.utils;

public class HttpUtils {
	
	public static final String BING_PIC_URL = "http://guolin.tech/api/bing_pic";
	public static final String WEATHER_URL = "http://jisutqybmf.market.alicloudapi.com/weather/query";
	public static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=";
	
	/**
	 * type: 1、新歌榜，2、热歌榜，
        	11、摇滚榜，12、爵士，16、流行
        	21、欧美金曲榜，22、经典老歌榜，23、情歌对唱榜，24、影视金曲榜，25、网络歌曲榜
	 * size: 10 返回条目数量
	 */
	public static String getMusicListUrl(int type,int size)
	{
		return BASE_URL+"baidu.ting.billboard.billList"+"&type="+type+"&size="+size;
	}
	
	public static String getSearchUrl(String query)
	{
		return BASE_URL+"baidu.ting.search.catalogSug"+"?query="+query;
	}
	
	public static String getPlayMusicUrl(int songid)
	{
		return BASE_URL+"baidu.ting.song.play"+"&songid="+songid;
	}
	
	public static String getMusicLrcUrl(int songid)
	{
		return BASE_URL+"baidu.ting.song.lry"+"&songid="+songid;
	}
	
	public static String getMusicDownloadUrl(int songid)
	{
		return BASE_URL+"baidu.ting.song.downWeb"+"&songid="+songid+"&bit=128";
	}
}
