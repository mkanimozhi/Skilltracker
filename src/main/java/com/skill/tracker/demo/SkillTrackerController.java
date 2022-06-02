package com.skill.tracker.demo;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skill.tracker.model.Message;
import com.skill.tracker.model.NonTechnicalSkill;
import com.skill.tracker.model.Profile;
import com.skill.tracker.model.Profiles;
import com.skill.tracker.model.Remarks;
import com.skill.tracker.model.ResponseHeader;
import com.skill.tracker.model.TechnicalSkill;
import com.skill.tracker.model.TransactionNotification;

@RestController
@RequestMapping(path="/skill-tracker")
@CrossOrigin
@EnableMongoRepositories
public class SkillTrackerController {

	@Value("${spring.data.mongodb.database}")
	private String mongodb;
	
	private final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

	@Autowired
	ProfileReposistry profileRepo;

	@GetMapping(path="/api/v1/engineer/get-profile")
	public List<Profile> getProfiles() {
		System.out.println("Called getProfiles method");
		return profileRepo.findAll();
	}

	@PostMapping(path="/api/v1/engineer/add-profile")
	public ProfileResponse addProfiles(@RequestBody Profile profile) {

		ProfileResponse response = new ProfileResponse();
		if(validateProfileRequest(profile, response)) {
			List<Profile> allProfiles = profileRepo.findAll();
			int id = allProfiles.size() + 1;
			profile.setId(id);
			System.out.println("getCreatedTimestamp = "+profile.getCreatedTimestamp());
			profileRepo.save(profile);
			List<Profile> dbProfiles = getProfiles();
			Profiles profiles = new Profiles();
			profiles.setProfileList(dbProfiles);
			response.setResponseBody(profiles);
			return response;
		}
		return response;
	}
	
	@PostMapping(path="/api/v1/engineer/find-profile")
	public ProfileResponse findProfiles(@RequestBody Profile profile) {

		ProfileResponse response = new ProfileResponse();
		if(validateFindProfileRequest(profile, response)) {
//			Optional<Profile> allProfiles = profileRepo.findById(10);
			List<Profile> allProfiles = new ArrayList<>();
			String name = profile.getName();
			String associateId = profile.getAssociateId();
			if(StringUtils.isNotBlank(name)) {
				allProfiles = profileRepo.findByName(name);
			} else if (StringUtils.isNotBlank(associateId)) {
				allProfiles = profileRepo.findByAssociateId(associateId);
			}
			System.out.println("allProfiles = "+allProfiles);
			Profiles profiles = new Profiles();
//			if(allProfiles.isPresent()) {
//			Profile findProfile = allProfiles.get();
			System.out.println("findProfile = "+allProfiles);
//			System.out.println("getCreatedTimestamp = "+findProfile.getCreatedTimestamp());
//			profileRepo.save(profile);
//			List<Profile> dbProfiles = getProfiles();
//			List<Profile> newlist = new ArrayList<>();
//			newlist.add(findProfile);
			profiles.setProfileList(allProfiles);
//			}
			response.setResponseBody(profiles);
			return response;
		}
		return response;
	}
	
	private boolean validateFindProfileRequest(Profile request, ProfileResponse response) {
		List<Message> messages = new ArrayList<>();
		boolean isValid = true;
		if(null != request) {
			if(StringUtils.isBlank(request.getName()) || StringUtils.isBlank(request.getAssociateId()) ||
					(null == request.getTechnicalSkills())) {
				isValid = false;
				addMessages(messages, "404", "Bad Request", "Any Search attribute such as name|associateId|technicalSkills|nonTechnicalSkills should be present.");
			}
			if(isValid) {
				ZonedDateTime currentTimestamp = ZonedDateTime.now();
				System.out.println("currentTimestamp ="+currentTimestamp);
				ZonedDateTimeWriteConverter timeWriteConverter = new ZonedDateTimeWriteConverter();
				Date date = timeWriteConverter.convert(currentTimestamp);
				System.out.println("date ="+date);
//				request.setCreatedTimestamp(date);
				request.setUpdatedTimestamp(date);
			}
		}
		ResponseHeader responseHeader = new ResponseHeader();
		TransactionNotification transactionNotification = new TransactionNotification();

		transactionNotification.setStatus("SUCCESS");
		transactionNotification.setStatusCode("1");
		transactionNotification.setResponseDateTime(new Date());
		String transactionId = UUID.randomUUID().toString();
		transactionNotification.setTransactionId(transactionId);
		Remarks remarks = new Remarks();
		remarks.setMessages(messages);
		transactionNotification.setRemarks(remarks);
		responseHeader.setTransactionNotification(transactionNotification);
		response.setResponseHeader(responseHeader);
		return isValid;
	}

	private boolean validateProfileRequest(Profile request, ProfileResponse response) {
		List<Message> messages = new ArrayList<>();
		boolean isValid = true;
		if(null != request) {
			if(isValid && StringUtils.isBlank(request.getName())) {
				isValid = false;
				addMessages(messages, "404", "Bad Request", "Mandatory attribute : name should not be Blank or Empty.");
			}
			if(isValid && StringUtils.isNotBlank(request.getName())) {
				int length = request.getName().length();
				if(length < 5 || length > 30) {
					isValid = false;
					addMessages(messages, "404", "Bad Request", "Attribute : name min(5)-max(30) characters are allowed.");
				}
			}
			if(isValid && StringUtils.isBlank(request.getAssociateId())) {
				isValid = false;
				addMessages(messages, "404", "Bad Request", "Mandatory attribute : associateId should not be Blank or Empty.");
			}
			if(isValid && StringUtils.isNotBlank(request.getAssociateId())) {
				int length = request.getAssociateId().length();
				if(length < 5 || length > 30) {
					isValid = false;
					addMessages(messages, "404", "Bad Request", "Attribute : associateId min(5)-max(30) characters are allowed.");
				}
				if(!request.getAssociateId().startsWith("CTS")) {
					isValid = false;
					addMessages(messages, "404", "Bad Request", "Attribute : associateId should starts with Prefix - CTS");
				}
			}
			if(isValid && StringUtils.isBlank(request.getMobile())) {
				isValid = false;
				addMessages(messages, "404", "Bad Request", "Mandatory attribute : mobile should not be Blank or Empty.");
			}
			if(isValid && StringUtils.isNotBlank(request.getMobile())) {
				int length = request.getMobile().length();
				if(length != 10) {
					isValid = false;
					addMessages(messages, "404", "Bad Request", "Attribute : mobile Only 10 characters are allowed.");
				}
				if(!isNumeric(request.getMobile())) {
					isValid = false;
					addMessages(messages, "404", "Bad Request", "Attribute : mobile should be Numeric");
				}
			}
			if(isValid && StringUtils.isBlank(request.getEmail())) {
				isValid = false;
				addMessages(messages, "404", "Bad Request", "Mandatory attribute : email should not be Blank or Empty.");
			}
			if(isValid && StringUtils.isNotBlank(request.getEmail())) {
				if(!isValidEmail(request.getEmail())) {
					isValid = false;
					addMessages(messages, "404", "Bad Request", "Attribute : email is invalid");
				}
			}
			if(isValid && null == request.getTechnicalSkills()) {
				isValid = false;
				addMessages(messages, "404", "Bad Request", "Mandatory attribute : technicalSkills should not be Blank or Empty.");
			}
			if(isValid) {
				ZonedDateTime currentTimestamp = ZonedDateTime.now();
				System.out.println("currentTimestamp ="+currentTimestamp);
				ZonedDateTimeWriteConverter timeWriteConverter = new ZonedDateTimeWriteConverter();
				Date date = timeWriteConverter.convert(currentTimestamp);
				System.out.println("date ="+date);
				request.setCreatedTimestamp(date);
				request.setUpdatedTimestamp(date);
			}
		}
		ResponseHeader responseHeader = new ResponseHeader();
		TransactionNotification transactionNotification = new TransactionNotification();

		transactionNotification.setStatus("SUCCESS");
		transactionNotification.setStatusCode("1");
		transactionNotification.setResponseDateTime(new Date());
		String transactionId = UUID.randomUUID().toString();
		transactionNotification.setTransactionId(transactionId);
		Remarks remarks = new Remarks();
		remarks.setMessages(messages);
		transactionNotification.setRemarks(remarks);
		responseHeader.setTransactionNotification(transactionNotification);
		response.setResponseHeader(responseHeader);
		return isValid;
	}

	private boolean isValidEmail(String email) {
		boolean result = false;
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		result = pattern.matcher(email).matches();
		return result;
	}

	private boolean checkValidExpertiseLevel(List<Message> messages, boolean isValid, String skillName, String skillValue) {
		if(isNumeric(skillValue)) {
			int expertiseLevel = Integer.parseInt(skillValue);
			if(expertiseLevel < 0 || expertiseLevel > 20) {
				isValid = false;
				addMessages(messages, "404", "Bad Request", "Attribute : " + skillName + " - expertiseLevel should be between (0-20)");
			}
		} else {
			isValid = false;
			addMessages(messages, "404", "Bad Request", "Attribute : " + skillName + " - expertiseLevel should be Numeric");
		}
		return isValid;
	}

	public boolean isNumeric(String source){
		boolean result = false;
		Pattern pattern = Pattern.compile("\\d+");
		result = pattern.matcher(source).matches();
		return result;
	}

	private void addMessages(List<Message> messages, String code, String message, String description) {
		Message msg = new Message();
		msg.setCode(code);
		msg.setMessage(message);
		msg.setDescription(description);
		messages.add(msg);	
	}

//	@Override
//	public MongoCustomConversions customConversions() {
//		converters.add(new ZonedDateTimeReadConverter());
//        converters.add(new ZonedDateTimeWriteConverter());
//        return new MongoCustomConversions(converters);
//	}
	
	



}
