package com.skill.tracker.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.skill.tracker.model.Profile;
import com.skill.tracker.model.TechnicalSkill;

public interface ProfileReposistry extends MongoRepository<Profile, String> {
	
	@Query("{ 'id' : ?0 }")
	Optional<Profile> findById(Integer id);
	
	@Query("{name:{$regex:?0}}")
	List<Profile> findByName(String name);
	
	@Query("{associateId : ?0 }")
	List<Profile> findByAssociateId(String associateId);
	
	//@Query("{ technicalSkills: { html_Css_Javascript: ?0 } }")
	@Query(value = "{'technicalSkills.?1': {$gt : 10 }}")
	List<Profile> findBySkill(int skill, String name);
	
//	@Update("{ '$set' : { 'associateId' : ?0 } }")
//	void updateAssociateId(String associateId);
	
//	@Query("{'author' : { $in : ?0}, 'pageNumber' : { $gt : ?1, $lt : ?2},'publishedYear' : {$nin : ?3}}")
//	List<Book> findBy(String[] authors, int pageNumLower, int pageNumUpper, int[] excludeYears);
	
}
