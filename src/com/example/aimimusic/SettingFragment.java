package com.example.aimimusic;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.aimimusic.element.WeatherInfo;
import com.example.aimimusic.utils.HttpUtils;
import com.example.aimimusic.utils.MyRequest;
import com.google.gson.Gson;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingFragment extends Fragment{
	
	private ImageView mImageView;
	private TextView mTxtClimate;
	private TextView mTxtCity;
	private TextView mTxtWind;
	private WeatherInfo weatherInfo;
	
	private final String TAG = "SettingFragment";
	private final int UPDATE_WEATHER = 1;
	
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			if(msg.what == UPDATE_WEATHER)
			{
				mTxtCity.setText(weatherInfo.getCity()+"市");
				mTxtClimate.setText(weatherInfo.getTemp()+"℃");
				mTxtWind.setText(weatherInfo.getWinddirect()+weatherInfo.getWindpower()
						+"  "+weatherInfo.getTemplow()+"℃-"+weatherInfo.getTemphigh()+"℃");
				setWeatherIco(weatherInfo.getWeather());
			}
		};
	};
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		initView(view);
		getWeatherInfo();
		return view;
	}
	
	public void initView(View view)
	{
		mImageView = (ImageView)view.findViewById(R.id.img_weather_ico);
		mTxtClimate = (TextView)view.findViewById(R.id.txt_weather_climate);
		mTxtCity = (TextView)view.findViewById(R.id.txt_weather_city);
		mTxtWind = (TextView)view.findViewById(R.id.txt_weather_info);
	}
	
	public void getWeatherInfo()
	{
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> params = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE c1d0a35deaa24c948bfb4ea296748612");
		params.put("?city", "武汉");
		String url = HttpUtils.WEATHER_URL+"?city=武汉";
		MyRequest request = new MyRequest(Method.GET, url,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, "response:"+response);
						String status = response.optString("status");
						if(status.equals("0"))
						{
							Gson gson = new Gson();
							weatherInfo = gson.fromJson(response.optJSONObject("result").toString(), WeatherInfo.class);
							handler.sendEmptyMessage(UPDATE_WEATHER);
						}
					}
				}, 
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "error:"+error.toString());
					}
				});
		request.setHeaders(headers);
		request.setParams(params);
		MusicApplication.getRequestQueue(getActivity()).add(request);
	}
	
	public void setWeatherIco(String weather)
	{
		if(weather.contains("雨"))
		{
			mImageView.setImageResource(R.drawable.ic_weather_rain);
		}
		else if(weather.contains("晴"))
		{
			mImageView.setImageResource(R.drawable.ic_weather_sunny);
		}
		else if(weather.contains("雪"))
		{
			mImageView.setImageResource(R.drawable.ic_weather_snow);
		}
		else if(weather.contains("云"))
		{
			mImageView.setImageResource(R.drawable.ic_weather_cloudy);
		}
		else if(weather.contains("雾"))
		{
			mImageView.setImageResource(R.drawable.ic_weather_foggy);
		}
	}

}
