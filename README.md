# Monolithic Architecture

- 소프트웨어의 모든 구성요소가 한 프로젝트에 통합되어 있는 아키텍쳐

  > 서비스가 커질 수록 전체 시스템 구조 파악이 어려움
  >
  > 빌드 시간, 배포 시간이 늘어남
  >
  > 서비스를 부분적으로 scale-out이 어려움
  >
  > 일부 서비스의 장애가 전체 서비스의 장애로 이어짐

- User Interface -> Business Logic -> Data Access Layer -> DB



# Micro Service Architecture

- 하나의 큰 어플리케이션을 여러개의 작은 어플리케이션으로 쪼개어 변경과 조합이 가능하도록 만든 아키텍쳐

  > 서비스별로 배포가 용이함
  >
  > 서비스를 확장하기 용이함 (클라우드 서비스에 적합한 아키텍쳐)
  >
  > 부분적인 장애에 격리가 수월함

  > 서비스가 분리되어 있어 테스트와 트랜잭션 복잡도가 증가
  >
  > 데이터가 여러 서비스에 걸쳐 분산되기 떄문에 관리가 어려움

- User Interface -> Micro service  -> DB

  ​							-> Micro service -> DB

- 각각의 마이크로 서비스는 하나의 모놀리틱 아키텍쳐와 유사함

- 각각의 서비스는 독립적으로 배포가 가능해야 함

- 각각의 서비스는 개별적인 프로세스로 구동되며 REST와 같은 가벼운 방식으로 통신 되어야 함

- 각각의 서비스는 다른 서비스에 대한 의존성이 최소화 되어야 함



## 1. API Gateway

MSA는 수십,수백개의 작은 서비스로 나뉘어져 운영되며, 만약 이를 클라이언트에서 서비스를 직접 호출하는 형태라면 다음과 같은 문제점이 생길 수 있습니다.

- 각 서비스마다 인증/인가 등 공통된 로직을 구현해야하는 번거로움
- 수많은 API 호출을 기록하고 관리하기 어려움
- 클라이언트에서 여러 마이크로 서비스에 대한 번거로운 호출을 해야 함
- 내부 비즈니스 로직이 드러나 보안에 취약해짐

### API Gateway 주요 기능

1. 인증 및 인가
2. 요청 절차의 단순화
3. 라우팅 및 로드밸런싱
4. 서비스 오케스트레이션
5. 서비스 디스커버리

### 고려할 점

1. API Gateway는 Scale-out 적용이 유연하게 적용하지 않을 경우 병목지점이 되어 성능 저하가 일어 날 수 있음.
2. 추가적인 계층이 만들어지기 떄문에 네트워크 latency가 증가함.



## 2. Service Mesh

마이크로 서비스간의 통신을 담당하는 요소. 

### Service Mesh 주요 기능

1. Service Discovery
2. 로드밸런싱
3. 동적 라우팅
4. Circuit Breaking
5. 암호화(TLS)
6. 보안
7. Health check, Retry and Timeout
8. Metric 수집

### API Gateway와 차이점

|  | API Gateway | Service Mesh |
| ---| ----------- | -------------- |
| 네트워크 | 마이크로 서비스 그룹의 외부 경계에 위치 | 마이크로 서비스 그룹 내부에 위치 |
| 아키텍쳐 | 중앙 집중형 아키텍쳐 | 분산형 아키텍쳐 |
| 프록시 패턴 | Gateway proxy pattern | Sidecar proxy pattern |
| 로드밸런싱 | 단일 엔드포인트를 제공하고 요청을 내부 요소에 redirection하여 내부 요소가 처리함 | Service Registry에서 서비스 목록을 수신함 |

### 종류

1. PaaS (Platform as a Service)의 일부로 서비스 코드에 포함된느 유형

   프레임워크 기반의 프로그래밍 모델이기 때문에 구현하기 위한 코드가 필요함

2. 라이브러리로 구현되어 API 호출을 통해 Service Mesh에 결합되는 유형

   프레임워크 라이브러리를 사용하는 형태

   > Spring Cloud, Netflix OSS(ribbon,hystrix, eureka, archaius)

## 3. Backing Service
- 네트워크를 통해서 사용할 수 있는 모든 서비스, MySQL, 캐쉬 시스템 등 어플리케이션과 통신하는 리소스들을 지칭하는 포괄적인 개념
- 메시지 큐를 활용한 비동기 통신 패턴을 사용

  > 하나의 트랜잭션이 여러 마이크로 서비스들과 강하게 결합되어 처리하는 방식의 경우 진행 중인 트랜잭션이 끊어지면 해당 서비스 요청을 보존할 수 없음.
  >
  > Message Queue를 활용하여 서비스의 영속성을 유지할 수 있음

### Message Queue

일반적인 웹 서비스에서 서버-클라이언트 사이의 통신은 결합도가 높은 구조이며, 동기방식으로 진행하기 때문에 요소들 간에 의존성이 높아 시스템에 많은 영향을 끼침.

서비스 간 결합도가 낮아야 하는 MSA에서 데이터 송수신 방법으로는 비동기 방식으로 메시지를 사용하는 것이 효율적임

> Apache Kafka
>
> - 대용량 및 실시간 처리에 특화
> - 단순한 TCP 기반 프로토콜로 오버헤드가 적음
> - 메모리가 아닌 파일시스템에 저장
> - OS에서 처리하는 페이지 캐시를 이용하여  속도가 빠름
> - Consumer가 pull 방식으로 메시지를 소비하며 batch 처리가 가능



## 4. Telemetry

서비스들의 상태를 모니터링하고 서비스별로 발생하는 이슈들에 대응할 수 있도록 환경을 구성하는 역할

![Telemetry](https://media.vlpt.us/post-images/tedigom/f29a6530-22d3-11ea-b7db-993b8ac645fc/Telemetry-.png)



# 키워드

- Command and Query Responsibility Segregation (명령과 조회의 책임 분리)

  시스템에서 명령을 하는 책임과 조회를 하는 책임을 분리 하는 것

  > Command를 하면 Event Brokder에 publish를 하고 Subscriber하여 DB에 반영을 하면 Query가 조회를 하는 느낌

- Eventually Consisteny

  분산 컴퓨팅에 쓰이는 일관성 모델. 데이터 항목에 새로운 업데이트가 없으면 최종적으로 해당 항목에 대한 모든 접근들은 마지막으로 업데이트 된 값을 반환하는 것을 비공식적으로 보장하는 고가용성을 달성

- Circuit Breaker

  MSA의 단점 중 A 서비스가 장애가 난 B 서비스를 호출하면 호출하는 종속된 A 서비스까지 장애가 전파가 되는 특성이 있다.

  Circuit Breaker 패턴은 서비스와 서비스 중간에 위치하여 B 서비스로의 모든 호출은 Circuit Breaker를 통하게 되고 B 서비스가 정상적인 상황에는 트래픽을 bypass한다. 

  만약 B 서비스에 문제가 생겼음을 Circuit Breaker가 감지한 경우 B 서비스로의 호출을 강제로 끊어 A  서비스의 스레드들이 요청을 기다리지 않도록 해서 장애가 전파하는 것을 방지한다.

  > Netflix의 Hysrix [참고 https://bcho.tistory.com/1247]





### 추가. 대용량 트래픽을 위해선 무엇을 해야 할까?

캐시, Circuit Breaker

비동기 Non-blocking 시스템

Spring WebFlux??
