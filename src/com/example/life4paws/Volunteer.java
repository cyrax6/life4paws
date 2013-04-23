package com.example.life4paws;

public class Volunteer
{

	public String	name;
	public String	profile_id;

	public Volunteer(String in_name, String in_profile_id)
	{
		name = in_name;
		profile_id = in_profile_id;
	}
	
	static public String getVolunteerId(Volunteer[] volunteers, String volunteer_name)
	{
		String id = null;
		for(Volunteer volunteer : volunteers)
		{
			if(volunteer.name.equals(volunteer_name) == true)
			{
				id = volunteer.profile_id;
			}
		}
		return id;
	}
}
