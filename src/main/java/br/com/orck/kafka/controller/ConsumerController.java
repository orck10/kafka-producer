package br.com.orck.kafka.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.orck.kafka.presenter.KafkaProperties;
import br.com.orck.kafka.service.GeneralKafkaServie;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {"Produtor"})
@RestController
@RequestMapping("/produtor/v1")
public class ConsumerController {
	
	@Autowired
	private GeneralKafkaServie<KafkaProperties> generalKafkaServie;
	
	 @ApiResponses(value = { 
			 @ApiResponse(code = 201, message = "CREATED", response = KafkaProperties.class),
	})
	@PostMapping(value = "/post")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> post(
    		@ApiParam(value = "" ,required=true )  @Valid @RequestBody KafkaProperties body){
		generalKafkaServie.postKafkaTopic(body);
		return ResponseEntity.status(HttpStatus.CREATED).body("");
	}
}
