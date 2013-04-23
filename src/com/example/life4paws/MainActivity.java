package com.example.life4paws;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

// import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class MainActivity extends FragmentActivity
{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter		mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager					mViewPager;

	private HTTPIShelter		http_interface				= null;
	private Volunteer[]			volunteers					= null;

	private static final int	MEDIA_IMAGE_REQUEST_CODE	= 2;
	// private static Uri dog_pic = null; // Member variables are a bad idea in multi threaded env
	private static String		dog_id						= null; // Don't like this but I have trouble with intent not passing on the dog_id
	private static final String	SOAP_KEY					= "";	// k9ppr

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		findViewById(11);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Performs login based on the given credentials. Does not do much with the soap key so it doesn't have that listed
	 * 
	 * @param v
	 */
	public void performLogin(View v)
	{
		String login = getEditTextValue(R.id.loginText);
		String password = getEditTextValue(R.id.passwordText);
		WebView web_view = (WebView) findViewById(R.id.webView1);
		http_interface = new HTTPIShelter(login, password, web_view);
		performLogin(login, password);
		volunteers = fetchVolunteers(http_interface);
		fillVolunteerList(volunteers, R.id.autoCompleteTextView1);
		fillVolunteerList(volunteers, R.id.autoCompleteTextView3);
	}

	/**
	 * Called when the post details button is pushed. Will do the following in one go Create a dog profile. Does not check if the dog exists explicitly Asks the
	 * user to pick a picture and uploads it, else the dog won't have a pic Ofcourse throws a fit if you are not logged in
	 * 
	 * @param v
	 */
	public void postDetails(View v)
	{
		if (http_interface == null)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Sentry Dog sez: Please login before posting").setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int id)
						{

						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
		else
		{
			dog_id = null;
			dog_id = "76073";
			// Deliberately disabled for now.
			// checkLogin();
			// dog_id = performPostDogDetails(); // Uncomment if you want to do something
			assignFosterParent(dog_id);
			// pickPicUri(dog_id); // Uncomment if you want to post pic

			// String dog_id = getDogId(getEditTextValue(R.id.editText2));
			// String dog_id = getDogIdWS("75719");
			// String dog_id = getDogIdWS("44949");
			// String dog_id = getDogIdWS("70415");
		}
	}

	/**
	 * Async dog pic upload. Need to refine this further as I don't want to lock up the main thread
	 */
	@Override
	protected final void onActivityResult(final int requestCode, final int resultCode, final Intent i)
	{
		super.onActivityResult(requestCode, resultCode, i);
		if (resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
				case MEDIA_IMAGE_REQUEST_CODE:
					Uri dog_pic = i.getData();
					// String dog_id = extras.getString("ID"); // TODO: Intent doesn't seem to give the extras properly
					uploadDogPicture(dog_id, "default", dog_pic);
					break;
			}
		}
	}
	
	// TODO: How to pass the dog id as a part of the intent instead of storing it as part of the class
	private final void pickPicUri(String dog_id)
	{
		try
		{
			Intent img_gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			img_gallery_intent.putExtra("ID", dog_id); // TODO: This doesn't seem to be working
			startActivityForResult(img_gallery_intent, MEDIA_IMAGE_REQUEST_CODE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private final String getDogBreedId(String breed)
	{
		int dog_id = DogBreedIdx.getBreedID(breed);
		return Integer.toString(dog_id);
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

	private final String getGender(int radio)
	{
		RadioGroup rd = (RadioGroup) findViewById(radio);
		if (rd.getCheckedRadioButtonId() == R.id.radio0)
			return "313"; // Male
		else if (rd.getCheckedRadioButtonId() == R.id.radio1)
			return "314";

		return ""; // wut?
	}
	
	private final String getDogName()
	{
		return getEditTextValue(R.id.editText1);
	}
	
	
	private final String getDefaultAssignerId()
	{
		return Volunteer.getVolunteerId(volunteers, getEditTextValue(R.id.autoCompleteTextView1));
	}
	
	private final String getAssignee()
	{
		return getEditTextValue(R.id.autoCompleteTextView1);
	}
	
	private final String getCurrentTime()
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		String format = "MM/dd/yy 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String date_time = sdf.format(new Date());
		return date_time;
	}

	private final void assignFosterParent(String dog_id)
	{
		String url = "http://ishelter.ishelters.com/asp/addProcess.php";
		List<NameValuePair> name_value_pair = new ArrayList<NameValuePair>(1);
		name_value_pair.add(new BasicNameValuePair("a", dog_id));
		name_value_pair.add(new BasicNameValuePair("aa", getDogName()));
		name_value_pair.add(new BasicNameValuePair("pp", getAssignee()));
		name_value_pair.add(new BasicNameValuePair("ap", getDefaultAssignerId()));
		name_value_pair.add(new BasicNameValuePair("sd", getCurrentTime()));
		name_value_pair.add(new BasicNameValuePair("submit", "Add"));
		name_value_pair.add(new BasicNameValuePair("p", getDefaultAssignerId()));

		HttpResponse response = http_interface.performPost(url, name_value_pair, false);
		http_interface.printResponse(response);
	}
	
	private final String performPostDogDetails()
	{
		// Post the dog details here
		String url = "http://ishelter.ishelters.com/as/addProcess.php"; // Skipping the server side check for now on similar.php
		List<NameValuePair> name_value_pair = new ArrayList<NameValuePair>(1);
		name_value_pair.add(new BasicNameValuePair("n", getDogName()));
		name_value_pair.add(new BasicNameValuePair("s", getSpeciesId("dog")));
		name_value_pair.add(new BasicNameValuePair("b", getDogBreedId(getEditTextValue(R.id.autoCompleteTextView2))));
		name_value_pair.add(new BasicNameValuePair("c", getEditTextValue(R.id.editText2)));
		name_value_pair.add(new BasicNameValuePair("g", getGender(R.id.radioGroup1)));

		HttpResponse response = http_interface.performPost(url, name_value_pair, false);
		String dog_id = HTTPIShelter.getDogId(response);
		http_interface.printResponse(response);
		return dog_id;
	}

	private final void performLogin(String login, String passwd)
	{
		String url = "http://ishelter.ishelters.com/loginProcess.php";
		List<NameValuePair> name_value_pair = new ArrayList<NameValuePair>(2);
		name_value_pair.add(new BasicNameValuePair("email", login));
		name_value_pair.add(new BasicNameValuePair("password", passwd));
		http_interface.performPost(url, name_value_pair, true);
	}

	private final void uploadDogPicture(String id, String title, Uri img_path)
	{
		String url = "http://ishelter.ishelters.com/ip/addProcess.php";
		List<NameValuePair> name_value_pair = new ArrayList<NameValuePair>(1);
		name_value_pair.add(new BasicNameValuePair("id", id));
		name_value_pair.add(new BasicNameValuePair("t", title));

		try
		{
			ImageView img_view = (ImageView) findViewById(R.id.imageView1);
			img_view.setImageURI(img_path);
			InputStream in_stream = getContentResolver().openInputStream(img_path);
			http_interface.performMultipartPost(url, name_value_pair, in_stream, img_path);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private final Volunteer[] fetchVolunteers(HTTPIShelter http_interface)
	{
		VolunteerScraper scraper = new VolunteerScraper();
		List<Volunteer> volunteers = scraper.BuildVolunteerList(http_interface);
		Volunteer vol_list[] = volunteers.toArray(new Volunteer[volunteers.size()]);

		return vol_list;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
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
				fragment = new LoginSectionFragment();
				Bundle args = new Bundle();
				args.putInt(LoginSectionFragment.LOGIN_SECTION_NAME, position + 1);
				fragment.setArguments(args);
			}
			else if (position == 1)
			{
				fragment = new AnimalSectionFragment();
				Bundle args = new Bundle();
				args.putInt(AnimalSectionFragment.ANIMAL_SECTION_NAME, position + 1);
				fragment.setArguments(args);
			}
			else if (position == 2)
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
					return getString(R.string.login_details);
				case 1:
					return getString(R.string.animal_details);
				case 2:
					return getString(R.string.animal_details);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment
	{
		/**
		 * The fragment argument representing the section number for this fragment.
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
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, DogBreedIdx.dog_breeds);
			AutoCompleteTextView textView = (AutoCompleteTextView) root_view.findViewById(R.id.autoCompleteTextView2);
			textView.setAdapter(adapter);
			return root_view;
		}
	}

	private final void fillDogBreedList(String[] dog_breeds, int resource_id)
	{

	}
	
	private final void fillVolunteerList(Volunteer[] volunteers, int resource_id)
	{
		List<String> names = new ArrayList<String>();
		for(Volunteer vol : volunteers)
		{
			names.add(vol.name);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, names);
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(resource_id);
		textView.setAdapter(adapter);
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

	public static class LoginSectionFragment extends Fragment
	{
		public static final String	LOGIN_SECTION_NAME	= "login";

		public LoginSectionFragment()
		{

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View root_view = inflater.inflate(R.layout.login, container, false);
			return root_view;
		}
	}
}
