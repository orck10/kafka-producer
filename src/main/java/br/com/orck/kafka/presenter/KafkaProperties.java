package br.com.orck.kafka.presenter;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaProperties {
	private String mechanism;
	private String protocol;
	private String servers;
	private String user;
	private String password;
	private String topic;
	private String message;
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
