# service-discovery-demo
Demo of service discovery functionality

In the world of micro-services, each service may communicate with every other service over the network for some functionality. To know where the service is hosted and on which port it is communicating
is very difficult. Also if the app spins-up multiple instance each instance has its own port. To know all the details we use service-discovery. Netflix-Eureka server is one such implemenation.

To start an eureka-server. Create a spring-boot app with the below dependency
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
	<version>2.2.2.RELEASE</version>
</dependency>
```    

Add `@EnableEurekaServer` annotation to the spring-boot app. Also add the below properties to the application.properties file
spring.application.name=discovery-server <br/>
server.port=8761 <br/>
eureka.client.register-with-eureka=false <br/>
eureka.client.fetch-registry=false <br/>

Start the application and you can now access the eureka-server at the following location http://localhost:8761/

Now services should register to this server so that it knows the details and it can serve different clients.
To start a service. Create a spring-boot app with below dependency
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	<version>2.2.2.RELEASE</version>
</dependency>
```   
Add `@EnableEurekaClient` annotation to the spring-boot app. 
Also provide the implemenation for all the related service end-points (controllers) which you want to expose. Add the following properties to application.properties <br/>
spring.application.name=service <br/>
server.port=8081 <br/>

Start the application and now this service will go and register itself to the eureka-server.

Now, the final step. Client needs to call this service via the eureka-server.
To do this, create a spring-boot app with below dependency
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	<version>2.2.2.RELEASE</version>
</dependency>
```
Add `@EnableEurekaClient` annotation to the spring-boot app.
Also provide end-points (controllers) which you want to call from the client app. Add the following properties to application.properties <br/>
  spring.application.name=client <br/>
  server.port=8083 <br/>
  eureka.client.register-with-eureka=false <br/>
>We are setting this register with eureka property as false because this is a client app

Create a bean for rest-template which is a rest client as follows
```java
@Bean
@LoadBalanced
public RestTemplate restTemplate(RestTemplateBuilder builder) {
   return builder.build();
}
``` 
Use this rest-template to make the call to service as follows
```java
@GetMapping("/hi")
public String hi() {
	ResponseEntity<String> exchange = restTemplate.exchange("http://service/hello", HttpMethod.GET, null, String.class);
	return exchange.getBody();
}
```
Now you can hit the client URL http://localhost:8083/hi which internally calls the service via the eureka-server.


