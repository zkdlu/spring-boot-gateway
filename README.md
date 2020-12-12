- [Spring-boot-cloud](https://github.com/zkdlu/spring-boot-cloud)에서 부족했던 지식을 보완해서 만든 MSA를 위한 Dynamic routing 프로젝트


## 1. Config server
- Routing에 대한 설정 정보를 중앙에서 관리하기 위해 사용

1. 의존성 추가
```gradle
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-config-server'
}
```
2. @EnableConfigServer어노테이션 추가
```java
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {
```
3. application.yml 작성
```yml
server:
  port: 8088
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/zkdlu/spring-boot-gateway.git
          search-paths: configurations
```
> 설정 내용을 관리할 수 있는 서버로는 git, file system, vault, jdbc이 있음.


## 2. Gateway
- Zuul를 사용한 간단한 API gateway

1. 의존성 추가
```gradle
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-config'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-zuul'
}
```
> Config Server의 client이기 떄문에 Config client 의존성을 추가한다.

2. @EnableZuulProxy 어노테이션 추가
```java
@EnableZuulProxy
@SpringBootApplication
public class GatewayApplication {
```

3. bootstrap.yml 작성
- spring boot app이 로드 되면서 application.properties를 읽어오는데 bootstrap과정에서는 bootstrap.properties를 읽어온다.
```yml
spring:
  application:
    name: gateway  
  cloud:
    config:
      uri: http://localhost:8088
      
management:
  endpoints:
    web:
      exposure:
        include: *
```
> application name은 config server에서 읽어들일 properties 파일을 구분하기 위해 사용한다.