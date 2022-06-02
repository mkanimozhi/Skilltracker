package com.skill.tracker.demo;

import org.springframework.stereotype.Repository;

import com.skill.tracker.model.Profile;
import com.skill.tracker.model.Profiles;

@Repository
public class ProfileDAO {

	private static Profiles list = new Profiles(); 
	
	static
	{
//		list.getProfileList().add(new Profile(1, "kanimozhi", "777338", "9488886923", "kanimozhi.m3@cognizant.com"));
//		list.getProfileList().add(new Profile(2, "babitha", "888338", "9486326923", "babitha.m3@cognizant.com"));
//		list.getProfileList().add(new Profile(3, "babitha", "999338", "9486326150", "sharma.m3@cognizant.com"));
		
	}

	public Profiles getList() {
		return list;
	}

	public void addProfile(Profile profile) {
//		list.getProfileList().add(profile);
	}
	
}
