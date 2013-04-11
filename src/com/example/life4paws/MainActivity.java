package com.example.life4paws;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.Header;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class MainActivity extends FragmentActivity
{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter		mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager					mViewPager;

	DefaultHttpClient			http_client					= new DefaultHttpClient();

	private static final int	MEDIA_IMAGE_REQUEST_CODE	= 2;
	private static Uri			dog_pic						= null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		performLogin("life4paws@life4paws.org", "Heather01");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void postDetails(View v)
	{
		dog_pic = null;
		// checkLogin();
		// performPostDogDetails();
		performSearch("66666669");
		// pickPicUri();

	}

	@Override
	protected final void onActivityResult(final int requestCode, final int resultCode, final Intent i)
	{
		super.onActivityResult(requestCode, resultCode, i);
		if (resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
				case MEDIA_IMAGE_REQUEST_CODE:
					dog_pic = i.getData();
					uploadDogPicture("75719", "w00f", dog_pic);
					break;
			}
		}
	}

	private final void pickPicUri()
	{
		try
		{
			Intent img_gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(img_gallery_intent, MEDIA_IMAGE_REQUEST_CODE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private final void checkLogin()
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

	private final void printResponse(HttpResponse response)
	{
		HttpEntity entity = response.getEntity();
		BufferedReader in;
		try
		{
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null)
			{
				sb.append(line + NL);
			}
			in.close();
			String page = sb.toString();
			Log.d("Result", page);
			WebView wv = (WebView) findViewById(R.id.webView1);
			wv.loadData(page, "text/html", null);
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private final String getDogBreedId(String breed)
	{
		return "59177";
	}

	private final String getSpeciesId(String species)
	{
		return "1287";
	}
	
	private final String getEditTextValue(int resource)
	{
		String result = ((EditText) findViewById(resource)).getText().toString();
		return result;
	}

	private final void performPostDogDetails()
	{
		// Post the dog details here
		String url = "http://ishelter.ishelters.com/as/similar.php";
		List<NameValuePair> name_value_pair = new ArrayList<NameValuePair>(1);
		name_value_pair.add(new BasicNameValuePair("n", getEditTextValue(R.id.editText1)));
		name_value_pair.add(new BasicNameValuePair("s", getSpeciesId("dog"))); // 1287 is for
																	// a dog.
																	// meh
		name_value_pair.add(new BasicNameValuePair("b", getDogBreedId("")));
		name_value_pair.add(new BasicNameValuePair("c", getEditTextValue(R.id.editText2)));

		HttpResponse response = performPost(url, name_value_pair);
		printResponse(response);
	}

	private final void performLogin(String login, String passwd)
	{
		String url = "http://ishelter.ishelters.com/loginProcess.php";
		List<NameValuePair> name_value_pair = new ArrayList<NameValuePair>(2);
		name_value_pair.add(new BasicNameValuePair("email", login));
		name_value_pair.add(new BasicNameValuePair("password", passwd));
		performPost(url, name_value_pair);
	}

	private final void performSearch(String code)
	{
		String url = "http://ishelter.ishelters.com/qs.php";
		List<NameValuePair> name_value_pair = new ArrayList<NameValuePair>(1);
		name_value_pair.add(new BasicNameValuePair("submit", ""));
		name_value_pair.add(new BasicNameValuePair("s", code));
		HttpResponse response = performPost(url, name_value_pair);
		getDogId(response);
		printResponse(response);
	}

	private final String getDogId(HttpResponse response)
	{
		String id = null;
		try
		{
			InputStream is = response.getEntity().getContent();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
		}
		catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return id;
	}
	
	private final void uploadDogPicture(String id, String title, Uri img_path)
	{
		String url = "http://ishelter.ishelters.com/ip/addProcess.php";
		List<NameValuePair> name_value_pair = new ArrayList<NameValuePair>(1);
		name_value_pair.add(new BasicNameValuePair("id", id));
		name_value_pair.add(new BasicNameValuePair("t", title));
		HttpResponse response = performMultipartPost(url, name_value_pair, img_path);
		printResponse(response);

	}

	private final HttpResponse performMultipartPost(String url, List<NameValuePair> nameValuePairs, Uri img_uri)
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

			InputStream in_stream = getContentResolver().openInputStream(img_uri);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Bitmap bm = BitmapFactory.decodeStream(in_stream);
			bm.compress(CompressFormat.JPEG, 60, bos);
			ImageView img_view = (ImageView) findViewById(R.id.imageView1);
			img_view.setImageBitmap(bm);
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
		return response;
	}

	private final HttpResponse performPost(String url, List<NameValuePair> name_value_pair)
	{
		HttpPost http_post = new HttpPost(url);

		// Url Encoding the POST parameters
		try
		{
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

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			if (position == 0)
			{
				fragment = new AnimalSectionFragment();
				Bundle args = new Bundle();
				args.putInt(AnimalSectionFragment.ANIMAL_SECTION_NAME, position + 1);
				fragment.setArguments(args);
			}
			else if (position == 1)
			{
				fragment = new AnimalRetrieveFragment();
				Bundle args = new Bundle();
				args.putInt(AnimalRetrieveFragment.ANIMAL_SECTION_NAME, position + 1);
				fragment.setArguments(args);
			}
			return fragment;
		}

		@Override
		public int getCount()
		{
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			// Locale l = Locale.getDefault();
			switch (position)
			{
				case 0:
					return getString(R.string.animal_details);
				case 1:
					return getString(R.string.animal_details);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String	ARG_SECTION_NUMBER	= "section_number";

		public DummySectionFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			String key = Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER));
			dummyTextView.setText(key);
			return rootView;
		}
	}

	public static class AnimalSectionFragment extends Fragment
	{
		public static final String	ANIMAL_SECTION_NAME	= "animal_info";

		public AnimalSectionFragment()
		{

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View root_view = inflater.inflate(R.layout.animal_detail, container, false);
			return root_view;
		}
	}

	public static class AnimalRetrieveFragment extends Fragment
	{
		public static final String	ANIMAL_SECTION_NAME	= "animal_retrieve";

		public AnimalRetrieveFragment()
		{

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View root_view = inflater.inflate(R.layout.animal_retrieve, container, false);
			return root_view;
		}
	}
}
