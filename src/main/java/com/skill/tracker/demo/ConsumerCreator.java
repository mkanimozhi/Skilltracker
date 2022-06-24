package com.skill.tracker.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.skill.tracker.model.Profile;

public class ConsumerCreator {

	public static KafkaConsumer<String, String> createConsumer2() {
		KafkaConsumer<String, String> kafkaConsumer= getKafkaConsumer2();
		kafkaConsumer.subscribe(Collections.singletonList(IKafkaConstants.TOPIC_ADD_PROFILE));
		return kafkaConsumer;
	}

	public static KafkaConsumer<String, Profile> createConsumer() {
		KafkaConsumer<String, Profile> kafkaConsumer= getKafkaConsumer();
		kafkaConsumer.subscribe(Collections.singletonList(IKafkaConstants.TOPIC_ADD_PROFILE));
		return kafkaConsumer;
	}

	public static KafkaConsumer<String, List<Profile>> createConsumerReqList() {
		KafkaConsumer<String, List<Profile>> kafkaConsumer= getKafkaConsumerReqList();
				kafkaConsumer.subscribe(Collections.singletonList(IKafkaConstants.TOPIC_ADD_PROFILE));
		return kafkaConsumer;
	} 

	public static KafkaConsumer<String, List<Profile>> createConsumerReqListFind() {
		KafkaConsumer<String, List<Profile>> kafkaConsumer= getKafkaConsumerReqList();
				kafkaConsumer.subscribe(Collections.singletonList(IKafkaConstants.TOPIC_FIND_PROFILE));
		return kafkaConsumer;
	}
	
	public static <V> KafkaConsumer<String, V> createConsumer11() {
		KafkaConsumer<String, V> kafkaConsumer = getKafkaConsumer11();
		List<String> topics = new ArrayList<>();
		topics.add(IKafkaConstants.TOPIC_ADD_PROFILE);
		topics.add("mytopic3");
		kafkaConsumer.subscribe(topics);
		return kafkaConsumer;
	}

	public static KafkaConsumer<String,String> getKafkaConsumer2(){
		KafkaConsumer<String, String> kafkaConsumer=new KafkaConsumer<String,String>(getKafkaConsumerConfig2());
		return kafkaConsumer;
	}

	public static KafkaConsumer<String,Profile> getKafkaConsumer(){
		KafkaConsumer<String, Profile> kafkaConsumer=new KafkaConsumer<String,Profile>(getKafkaConsumerConfig());
		return kafkaConsumer;
	}

	public static KafkaConsumer<String,List<Profile>> getKafkaConsumerReqList(){
		KafkaConsumer<String, List<Profile>> kafkaConsumer=new KafkaConsumer<String,List<Profile>>(getKafkaConsumerConfig());
		return kafkaConsumer;
	}

	public static <V> KafkaConsumer<String,V> getKafkaConsumer11(){
		KafkaConsumer<String, V> kafkaConsumer=new KafkaConsumer<String,V>(getKafkaConsumerConfig());
		return kafkaConsumer;
	}

	private static Properties getKafkaConsumerConfig(){
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
		//                props.put(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.GROUP_ID_CONFIG);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.GROUP_ID_CONFIG_BATCH);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_EARLIER);
		return props;
	}

	private static Properties getKafkaConsumerConfig2(){
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
		//                props.put(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.GROUP_ID_CONFIG);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.GROUP_ID_CONFIG_BATCH);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_EARLIER);
		return props;
	}

}