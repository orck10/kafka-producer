package br.com.orck.kafka.presenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaProperties {
	private String mechanism;
	private String protocol;
	private String servers;
	private String user;
	private String password;
	private String topic;
	private String port;
	private String message;
}
