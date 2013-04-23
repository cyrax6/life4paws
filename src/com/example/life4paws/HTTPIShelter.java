package com.example.life4paws;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;

public class HTTPIShelter
{
	private DefaultHttpClient	http_client	= new DefaultHttpClient();
	private WebView				web_view	= null;
	private String				username	= null;
	private String				passwd		= null;

	public HTTPIShelter(String in_username, String in_passwd, WebView wv)
	{
		web_view = wv;
		username = in_username;
		passwd = in_passwd;
	}

	public final HttpResponse performMultipartPost(String url, List<NameValuePair> nameValuePairs, InputStream in_stream, Uri img_uri)
	{
		HttpContext local_content = new BasicHttpContext();
		HttpPost http_post = new HttpPost(url);
		HttpResponse response = null;

		try
		{
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			for (int index = 0; index < nameValuePairs.size(); index++)
			{
				// Normal string data
				entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Bitmap bm = BitmapFactory.decodeStream(in_stream);
			bm.compress(CompressFormat.JPEG, 60, bos);
			ContentBody content_body = new ByteArrayBody(bos.toByteArray(), "image/jpeg", img_uri.getPath());

			entity.addPart("i", content_body);

			http_post.setEntity(entity);

			response = http_client.execute(http_post, local_content);
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		printResponse(response);
		return response;
	}

	public final HttpResponse performPost(String url, List<NameValuePair> name_value_pair, Boolean should_redirect)
	{
		HttpPost http_post = new HttpPost(url);
		HttpParams params = http_post.getParams();
		HttpClientParams.setRedirecting(params, should_redirect);

		// Url Encoding the POST parameters
		try
		{
			if(name_value_pair != null)
				http_post.setEntity(new UrlEncodedFormEntity(name_value_pair));
		}
		catch (UnsupportedEncodingException e)
		{
			// writing error to Log
			e.printStackTrace();
		}

		HttpResponse response = null;
		try
		{
			response = http_client.execute(http_post);
			Log.d("Http Response:", response.toString());
			final int status = response.getStatusLine().getStatusCode();
			Log.d("Status:", String.valueOf(status));
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return response;
	}

	public final void printResponse(HttpResponse response)
	{
		if (web_view != null)
		{
			String page = getResponseAsString(response);
			web_view.loadData(page, "text/html", null);
		}
	}

	public static String getResponseAsString(HttpResponse response) 
	{
		String page = null;
		try
		{
			HttpEntity entity = response.getEntity();
			BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null)
			{
				sb.append(line + NL);
			}
			in.close();
			page = sb.toString();
			// Log.d("Result", page);
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return page;
	}
	
	public final void checkLogin()
	{
		HttpGet request = new HttpGet("http://ishelter.ishelters.com/index.php");
		try
		{
			HttpResponse response = http_client.execute(request);
			Log.d("Http Response:", response.toString());
			final int status = response.getStatusLine().getStatusCode();
			Log.d("Status:", String.valueOf(status));
			printResponse(response);
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static final String getDogId(HttpResponse response)
	{
		String id = null;
		try
		{
			String location = response.getLastHeader("Location").getValue();
			id = location.split("=")[1];
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}

		return id;
	}
}
