package com.skill.tracker.demo;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.skill.tracker.model.Profile;

public class AddProfileEvent extends Event {
	
	public void addEvent(List<Profile> profileList, Producer<String, List<Profile>> producer) {
		
		final ProducerRecord<String, List<Profile>> record = new ProducerRecord<String, List<Profile>>(IKafkaConstants.TOPIC_ADD_PROFILE,
				"record ",profileList);
		try {
			RecordMetadata metadata = producer.send(record).get();
//			System.out.println("record = "+record);
//			System.out.println("metadata = "+metadata);

		} catch (ExecutionException e) {
			System.out.println(e);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		
	}
}
