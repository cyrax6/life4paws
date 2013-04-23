package com.example.life4paws;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VolunteerScraper
{

	public List<Volunteer> BuildVolunteerList(HTTPIShelter http_interface)
	{
		List<Volunteer> volunteers = new ArrayList<Volunteer>();
		HttpResponse response = http_interface.performPost("http://ishelter.ishelters.com/ps/staff.php", null, false);
		String page = HTTPIShelter.getResponseAsString(response);
		if (page != null)
		{
			Document doc = Jsoup.parse(page, "http://ishelter.ishelters.com/");

			Element box = doc.select("div.box").first();
			Elements links = box.select("a[href]");
			for (Element link : links)
			{
				String link_str = link.attr("href");
				String[] split = link_str.split("profile.php\\?id=");
				if (split.length > 1)
				{
					String vol_id = split[1];
					String vol_name = link.text();
					volunteers.add(new Volunteer(vol_name, vol_id));
				}
			}

			int i = 0;
			i = i + 1;

		}

		return volunteers;
	}
}
