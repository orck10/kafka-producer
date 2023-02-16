package br.com.orck.kafka.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import br.com.orck.kafka.presenter.KafkaProperties;
import br.com.orck.kafka.testConfig.KafkaConsumerTest;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class ConsumerControllerTest {
	
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsumerController controller;
    
    @Autowired
    private KafkaConsumerTest consumer;
    
    
    @Value("${test.topic}")
    private String topic;

    
    @Test
    public void postSuccess() throws Exception {
    	var requestObj = newKafkaProperties();
    	var resp = mockMvc.perform(post("/produtor/v1/post")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(requestObj.toString())).andExpect(status().isCreated())
    			.andReturn()
    			.getResponse()
    			.getContentAsString()
    	;
    	
    	boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
    	assertTrue(messageConsumed);
    	assertEquals("SUUUCESSUUUU", resp);
    	assertThat(consumer.getPayload().contains(requestObj.getMessage()));
    }
    
    @Test
    public void postSuccess2() throws Exception {
    	var requestObj = newKafkaProperties();
    	var resp = controller.post(requestObj);
    	
    	boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
    	
    	assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    	assertTrue(messageConsumed);
    	assertThat(consumer.getPayload().contains(requestObj.getMessage()));
    }
    
    @Test
    public void post_Internal_Sever_Error() throws Exception {
    	var requestObj = newKafkaProperties();
    	requestObj.setProtocol("FAIL_PROTOCOL_XPTO");
    	mockMvc.perform(post("/produtor/v1/post")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(requestObj.toString())).andExpect(status().is5xxServerError())
    	;
    }
    
    @Test
    public void post_Internal_Sever_Error2() throws Exception {
    	var requestObj = newKafkaProperties();
    	requestObj.setProtocol("FAIL_PROTOCOL_XPTO");

    	var resp = controller.post(requestObj);
    	
    	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
    	assertEquals("ERROR", resp.getBody());
    }
    
    private KafkaProperties newKafkaProperties() {
    	return KafkaProperties.builder()
    			.mechanism(null)
    			.message(UUID.randomUUID().toString())
    			.password(null)
    			.protocol(null)
    			.servers("localhost:9092")
    			.topic(topic)
    			.user(null)
    			.build();
    }
}
