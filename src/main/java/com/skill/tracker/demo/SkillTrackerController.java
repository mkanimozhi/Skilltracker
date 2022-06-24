package com.skill.tracker.demo;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skill.tracker.model.Message;
import com.skill.tracker.model.NonTechnicalSkill;
import com.skill.tracker.model.NonTechnicalSkills;
import com.skill.tracker.model.Profile;
import com.skill.tracker.model.Profiles;
import com.skill.tracker.model.Remarks;
import com.skill.tracker.model.ResponseHeader;
import com.skill.tracker.model.TechnicalSkill;
import com.skill.tracker.model.TechnicalSkills;
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
	
	Producer<String, List<Profile>> producer;
	Consumer<String, List<Profile>> kafkaConsumerAdd;
	Consumer<String, List<Profile>> kafkaConsumerFind;
	
	@PostConstruct
	public void registerKafkaEvents() {
		System.out.println("registerKafkaEvents ...");
		producer = ProducerCreator.createProducerReqList();
		kafkaConsumerAdd = ConsumerCreator.createConsumerReqList();
		kafkaConsumerFind = ConsumerCreator.createConsumerReqListFind();
	}

	@GetMapping(path="/api/v1/engineer/get-profile")
	public List<Profile> getProfiles() {
		System.out.println("Called getProfiles method");
		List<Profile> allProfiles = profileRepo.findAll();
		sortSkills(allProfiles);
		return allProfiles;
	}

	@PostMapping(path="/api/v1/engineer/add-profile")
	public ProfileResponse addProfiles(@RequestBody Profile profile) {
		System.out.println("Inside addProfiles... "+profile);
		if(profile != null) {
			List<Profile> inputData = new ArrayList<>();
			inputData.add(profile);
			AddProfileEvent addProfileEvent = new AddProfileEvent(); 
			addProfileEvent.addEvent(inputData, producer);
		}

		ProfileResponse response = new ProfileResponse();
		if(validateProfileRequest(profile, response)) {
			runConsumerReqListForSave(response);
			return response;
		}
		return response;
	}

	private void runConsumerReqListForSave(ProfileResponse responseBody) {
		System.out.println("kafkaConsumer="+kafkaConsumerAdd);
		int i = 2;
		while (i > 0) {
			List<Profile> outputList = new ArrayList<>();
			ConsumerRecords<String, List<Profile>> orderRecords = kafkaConsumerAdd.poll(100);
			Profiles profiles = new Profiles();

			for (ConsumerRecord<String, List<Profile>> consumerRecord : orderRecords) {
				if(consumerRecord.value() != null) {
					outputList = consumerRecord.value();
					if(outputList != null && !outputList.isEmpty()) {
						for (Profile profile : outputList) {
							List<Profile> allProfiles = profileRepo.findAll();
							int id = allProfiles.size() + 1;
							profile.setId(id);
							profileRepo.save(profile);
							List<Profile> dbProfiles = getProfiles();
							profiles.setProfileList(dbProfiles);
						}
					}
				}
			}
			i--;
			responseBody.setResponseBody(profiles);
		}
		kafkaConsumerAdd.commitAsync();
		//kafkaConsumerAdd.close();
	}

	@PostMapping(path="/api/v1/engineer/edit-profile")
	public ProfileResponse editProfiles(@RequestBody Profile profile) {
		System.out.println("Inside addProfiles... "+profile);
		ProfileResponse response = new ProfileResponse();
		if(validateProfileRequest(profile, response)) {
			Optional<Profile> profileFound = profileRepo.findById(profile.getId());
			if(profileFound.isPresent()) {
				Profile profile2 = profileFound.get();
			}
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
			if(profile != null) {
				List<Profile> inputData = new ArrayList<>();
				inputData.add(profile);
				
				FindProfileEvent findProfileEvent = new FindProfileEvent(); 
				findProfileEvent.addEvent(inputData, producer);
			}
			//End of Kafka Producer
			runConsumerReqListForFind(response);
			return response;
		}
		return response;
	}
	
	private void runConsumerReqListForFind(ProfileResponse responseBody) {
//		Consumer<String, List<Profile>> kafkaConsumer = ConsumerCreator.createConsumerReqList();
		System.out.println("kafkaConsumer="+kafkaConsumerFind);
		int i = 2;
		while (i > 0) {
			List<Profile> outputList = new ArrayList<>();
			ConsumerRecords<String, List<Profile>> orderRecords = kafkaConsumerFind.poll(100);
			System.out.println("orderRecords="+orderRecords);                     
			Profiles profiles = new Profiles();

			for (ConsumerRecord<String, List<Profile>> consumerRecord : orderRecords) {
				if(consumerRecord.value() != null) {
					outputList = consumerRecord.value();
					System.out.println("outputList="+outputList);
					if(outputList != null && !outputList.isEmpty()) {
						for (Profile profile : outputList) {
							///--------------
							List<Profile> allProfiles = new ArrayList<>();
							String name = profile.getName();
							String associateId = profile.getAssociateId();
							TechnicalSkill technicalSkills = profile.getTechnicalSkills();
							int skill = 0;
							if(StringUtils.isNotBlank(name)) {
								allProfiles = profileRepo.findByName(name);
							} else if (StringUtils.isNotBlank(associateId)) {
								allProfiles = profileRepo.findByAssociateId(associateId);
							} else if (technicalSkills != null) {
								//} else if (StringUtils.isNotBlank(skill)) {
								skill = technicalSkills.getHtml_Css_Javascript();
								System.out.println("skill = "+skill);
								allProfiles = profileRepo.findBySkill(skill, "html_Css_Javascript");
							}
							sortSkills(allProfiles);
							profiles.setProfileList(allProfiles);
							responseBody.setResponseBody(profiles);
						}
					}
				}
			}
			i--;
		}
		kafkaConsumerFind.commitAsync();
		//kafkaConsumerFind.close();
	}

	private void sortSkills(List<Profile> allProfiles) {
		if(allProfiles != null && !allProfiles.isEmpty()) {
			for (Profile eachProfile : allProfiles) {
				TechnicalSkill tskill = eachProfile.getTechnicalSkills();
				TechnicalSkills[] techSkillArr = new TechnicalSkills[10];
				if(tskill != null) {
					techSkillArr[0] = new TechnicalSkills("html_Css_Javascript", tskill.getHtml_Css_Javascript());
					techSkillArr[1] = new TechnicalSkills("angular", tskill.getAngular());
					techSkillArr[2] = new TechnicalSkills("react", tskill.getReact());
					techSkillArr[3] = new TechnicalSkills("spring", tskill.getSpring());
					techSkillArr[4] = new TechnicalSkills("restful", tskill.getRestful());
					techSkillArr[5] = new TechnicalSkills("hibernate", tskill.getHibernate());
					techSkillArr[6] = new TechnicalSkills("git", tskill.getGit());
					techSkillArr[7] = new TechnicalSkills("docker", tskill.getDocker());
					techSkillArr[8] = new TechnicalSkills("jenkins", tskill.getJenkins());
					techSkillArr[9] = new TechnicalSkills("aws", tskill.getAws());
				}
				Arrays.sort(techSkillArr);
				eachProfile.setTechnicalSkill(techSkillArr);
				NonTechnicalSkill ntskill = eachProfile.getNonTechnicalSkills();
				NonTechnicalSkills[] ntechSkillArr = new NonTechnicalSkills[3];
				if(tskill != null) {
					ntechSkillArr[0] = new NonTechnicalSkills("spoken", ntskill.getSpoken());
					ntechSkillArr[1] = new NonTechnicalSkills("communication", ntskill.getCommunication());
					ntechSkillArr[2] = new NonTechnicalSkills("aptitude", ntskill.getAptitude());
				}
				Arrays.sort(ntechSkillArr);
				eachProfile.setNonTechnicalSkill(ntechSkillArr);
			}
		}
	}

	private boolean validateFindProfileRequest(Profile request, ProfileResponse response) {
		List<Message> messages = new ArrayList<>();
		boolean isValid = true;
		if(null != request) {
			if(StringUtils.isBlank(request.getName()) && StringUtils.isBlank(request.getAssociateId()) &&
					(null == request.getTechnicalSkills())) {
				isValid = false;
				addMessages(messages, "404", "Bad Request", "Any Search attribute such as name|associateId|technicalSkills|nonTechnicalSkills should be present.");
			}
			if(isValid) {
				ZonedDateTime currentTimestamp = ZonedDateTime.now();
				ZonedDateTimeWriteConverter timeWriteConverter = new ZonedDateTimeWriteConverter();
				Date date = timeWriteConverter.convert(currentTimestamp);
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
				ZonedDateTimeWriteConverter timeWriteConverter = new ZonedDateTimeWriteConverter();
				Date date = timeWriteConverter.convert(currentTimestamp);
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
