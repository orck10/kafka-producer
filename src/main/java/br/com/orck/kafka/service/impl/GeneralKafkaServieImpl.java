package br.com.orck.kafka.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import br.com.orck.kafka.presenter.KafkaProperties;
import br.com.orck.kafka.service.GeneralKafkaServie;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Service
public class GeneralKafkaServieImpl implements GeneralKafkaServie<KafkaProperties>{

	@Override
	public void postKafkaTopic(KafkaProperties t) {
		log.info("Try to post -> " + t.toString());
		try {
			kafkaTemplate(t).send(t.getTopic() ,t.getMessage());
			log.info("Post sucess -> " + t.toString());
		}catch (Exception e) {
			log.error("Post error -> " + t.toString());
			log.error("Error mesage : " + e.getMessage());
			throw e;
		}
	}
	
	
	private ProducerFactory<String, String> generateProducer(KafkaProperties properties){
		Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getServers());
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";

        String jaasCfg = String.format(jaasTemplate, properties.getUser(), properties.getPassword());
        if(!(properties.getProtocol() == null  || properties.getProtocol().isBlank() || properties.getProtocol().isEmpty())) {
        	configProps.put("security.protocol", properties.getProtocol());
        }
        configProps.put("sasl.mechanism", properties.getMechanism());
        configProps.put("sasl.jaas.config", jaasCfg);

        return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	public KafkaTemplate<String, String> kafkaTemplate(KafkaProperties properties) {
        return new KafkaTemplate<>(generateProducer(properties));
    }
}
