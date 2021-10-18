package br.com.orck.kafka.service;

public interface GeneralKafkaServie<T> {
	void postKafkaTopic (T t);
}
