package io.chetan.kamadinni.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Client {

	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/hi")
	public String hi() {
		ResponseEntity<String> exchange = restTemplate.exchange("http://service/hello", HttpMethod.GET, null, String.class);
		return exchange.getBody();
	}
}
