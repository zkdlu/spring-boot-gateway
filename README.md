[Spring-boot-cloud](https://github.com/zkdlu/spring-boot-cloud)에서 부족했던 지식을 보완해서 만든 MSA를 위한 Dynamic routing 프로젝트

어째선지 자꾸 zuul에서 eureka랑 같이 id로 라우팅을 하면 Forwardind Error가 떴다. (될 때도 있고 안될 때도 있고 이유를 모르겠네;;)

# Micro Service
- Service A : localhost:8081/
- Service B : localhost:8082/

# Step 1. Zuul로 Gateway 만들기
1. Zuul 의존성 추가
```gradle
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-zuul'
}
```
2. EnableZuulProxy 어노테이션 추가
```java
@EnableZuulProxy
@SpringBootApplication
public class GatewayApplication {
```

3. Zuul routing 설정 (application.yml)
```yml
zuul:
  prefix: /api
  routes:
    servicea:
      path: /a/**
      url: http://localhost:8081
    serviceb:
      path: /b/**
      url: http://localhost:8082
```
> localhost:8080/api/a 또는 localhost:8080/api/b 로 사용 
