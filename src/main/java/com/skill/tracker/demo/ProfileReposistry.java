package com.skill.tracker.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.skill.tracker.model.Profile;

public interface ProfileReposistry extends MongoRepository<Profile, String> {
	
	@Query("{ 'id' : ?0 }")
	Optional<Profile> findById(Integer id);
	
	@Query("{name:{$regex:?0}}")
	List<Profile> findByName(String name);
	
	@Query("{associateId : ?0 }")
	List<Profile> findByAssociateId(String associateId);
}
