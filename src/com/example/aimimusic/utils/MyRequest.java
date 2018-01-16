package com.example.aimimusic.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

/**
 * setParams设置请求参数，第一个参数的key带？
 * setHeaders设置请求头
 * @author zhangchong
 *
 */

public class MyRequest extends JsonRequest<JSONObject> {

	private Map<String, String> headers;
	private Map<String, String> params;
	private Listener<JSONObject> listener;
	private ErrorListener errorListener;
	
	private static final String PROTOCOL_CHARSET = HTTP.UTF_8;

	public MyRequest(int method, String url, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(method, url, null, listener, errorListener);
		this.listener = listener;
		this.errorListener = errorListener;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setParams(Map<String, String> params)
	{
		this.params = params;
	}
	
	@Override
	public byte[] getBody() {
		if(params != null)
		{
			return encodeParameters(params, PROTOCOL_CHARSET);
		}
		return null;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(
			NetworkResponse networkResponse) {

		String json = null;
		try {
			json = new String(networkResponse.data,
					HttpHeaderParser.parseCharset(networkResponse.headers));

			return Response.success(new JSONObject(json),
					HttpHeaderParser.parseCacheHeaders(networkResponse));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		listener.onResponse(response);
	}

	private byte[] encodeParameters(Map<String, String> params,String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(),paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(),paramsEncoding));
				encodedParams.append('&');
			}

			return encodedParams.toString().getBytes(paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: "
					+ paramsEncoding, uee);
		}
	}

}
