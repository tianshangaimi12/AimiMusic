package com.example.aimimusic.element;

public class WeatherInfo {
	
	private String city;
	private String weather;
	private String temp;
	private String winddirect;
	private String windpower;
	private String templow;
	private String temphigh;
	public String getTemplow() {
		return templow;
	}
	public void setTemplow(String templow) {
		this.templow = templow;
	}
	public String getTemphigh() {
		return temphigh;
	}
	public void setTemphigh(String temphigh) {
		this.temphigh = temphigh;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getWinddirect() {
		return winddirect;
	}
	public void setWinddirect(String winddirect) {
		this.winddirect = winddirect;
	}
	public String getWindpower() {
		return windpower;
	}
	public void setWindpower(String windpower) {
		this.windpower = windpower;
	}
	
}
