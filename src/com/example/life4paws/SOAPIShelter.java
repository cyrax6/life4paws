package com.example.life4paws;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class SOAPIShelter
{

	private static final String	URL			= "http://ws.ishelters.com/server.php";
	private static final String	SOAP_ACTION	= "";
	private static final String	NAMESPACE	= "http://ws.ishelters.com/";
	private static final String	METHOD_NAME	= "getAnimal";

	private static String		SOAP_KEY	= null;

	public SOAPIShelter(String key)
	{
		SOAP_KEY = key;
	}
	
	public final String getDogProfile(String id)
	{
		String dog_id = null;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("animalId", id);
		request.addProperty("key", SOAP_KEY);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE http_transport = new HttpTransportSE(URL);
		http_transport.debug = true;
		try
		{
			http_transport.call(SOAP_ACTION, envelope);
			SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;

			String response_str = http_transport.responseDump.toString();
			String req_str = http_transport.requestDump.toString();
			System.out.println("Response::" + resultsRequestSOAP.toString());
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Log.v("TEST", "io wrong");
		}
		catch (XmlPullParserException e)
		{
			String ex_str = e.getMessage();
			Log.v("SOAP EXCEPTION:", ex_str);
			e.printStackTrace();
		}

		return dog_id;
	}

}
