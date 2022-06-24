package com.skill.tracker.demo;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.skill.tracker.model.Profile;

public class ProducerCreator {

	public static KafkaProducer<String, Profile> createProducer() {

		KafkaProducer<String, Profile> kafkaProducer= getKafkaProducer();

		return kafkaProducer;

	}



	public static KafkaProducer<String, List<Profile>> createProducerReqList() {

		KafkaProducer<String, List<Profile>> kafkaProducer= getKafkaProducerReqList();

		return kafkaProducer;

	}



	public static KafkaProducer<String, String> createProducer2() {

		KafkaProducer<String, String> kafkaProducer= getKafkaProducer2();
		return kafkaProducer;
	}



	public static KafkaProducer<String, Profile> getKafkaProducer(){

		KafkaProducer<String, Profile> kafkaProducer=new KafkaProducer<String,Profile>(getKafkaProducerConfig());

		return kafkaProducer;

	}



	public static KafkaProducer<String, List<Profile>> getKafkaProducerReqList(){

		KafkaProducer<String, List<Profile>> kafkaProducer=new KafkaProducer<String,List<Profile>>(getKafkaProducerConfig());

		return kafkaProducer;

	}



	public static KafkaProducer<String, String> getKafkaProducer2(){

		KafkaProducer<String, String> kafkaProducer=new KafkaProducer<String,String>(getKafkaProducerConfig2());

		return kafkaProducer;

	}



	private static Properties getKafkaProducerConfig(){

		Properties props = new Properties();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);

		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());

		return props;

	}



	private static Properties getKafkaProducerConfig2(){

		Properties props = new Properties();

		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);

		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		return props;

	}



/*	public static ClaimRequest getClaimRequest(int order){
		ClaimRequest claimRequest=new ClaimRequest();
		claimRequest.setClaimIdentifier(Integer.toString(order));
		claimRequest.setClaimStatus("Processing");
		claimRequest.setClaimAmt("100");
		return claimRequest;
	} */



/*	public static Profile getOrder(int orderNo){

		Request request=new Request();
		request.setRequestId(Integer.toString(orderNo));

		int claimSize = orderNo * 2;
		List<ClaimRequest> claimList = new ArrayList<ClaimRequest>();

		for (int i = 1; i <= claimSize; i++) {
			ClaimRequest claimReq = ProducerCreator.getClaimRequest(i);
			claimList.add(claimReq);
		}

		request.setClaimList(claimList);
		return request;
	} */

}