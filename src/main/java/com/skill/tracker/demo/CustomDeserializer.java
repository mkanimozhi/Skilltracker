package com.skill.tracker.demo;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomDeserializer<T extends Serializable> implements Deserializer<T> {

	private ObjectMapper objectMapper = new ObjectMapper();

	public static final String VALUE_CLASS_NAME_CONFIG = "value.class.name";

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(String topic, byte[] objectData) {
		return (objectData == null) ? null : (T) SerializationUtils.deserialize(objectData);
	}

	@Override
	public void close() {

	}

}