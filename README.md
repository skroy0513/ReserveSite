# 예약 구매 프로젝트
## 📜 프로젝트 소개
다량의 요청을 오류없이 처리하는 예약상품 구매 서비스입니다.<br>
유저들은 특정시각에 오픈하는 예약상품, 상시 구매가 가능한 일반상품을 구매할 수 있습니다.

## 💻 개발환경
- JAVA 17
- Spring Boot
- MySql
- JPA
- Redis
- Feign Client


## 📝 ERD
![Untitledd](https://github.com/skroy0513/hugeTraffic/assets/117910568/707b28fc-8c72-4b97-9e9d-542ce55b8743)


## 📡 아키텍처
- 마이크로서비스 아키텍처(MSA)로 구현을 하였습니다.
- 예약 상품을 구매하기 위해 대량의 트래픽이 몰렸을 때, 해당 서비스만 Scale-out하기 위해 예약 상품 구매 서비스를 분리하였습니다.
- 일반 상품 구매 또한 분리하여 일반 상품을 구매하는 유저들이 예약상품 서비스로 인한 장애를 겪지 않기 위해 MSA 방식을 택하였습니다.
![reservesite](https://github.com/skroy0513/hugeTraffic/assets/117910568/c6f2e074-c3a2-4a51-9b4b-a7fe0ac638c4)


## 💡 프로젝트 기능
### 상품 기능 (Product-Service)
- 유저는 상품의 가격, 재고, 제조사 등 기본 적인 정보를 확인할 수 있습니다.
- 예약 상품의 경우 오픈 시간을 확인할 수 있습니다.
### 상품 구매 기능 (Pay-Service)
- 상품을 구매하기 위해 정보 입력 페이지로 진입하는 기능과 결제를 위한 결제 기능이 있습니다.
- 예약 상품의 경우 오픈 시간 이전에는 구매가 불가능합니다.
### 주문 기능 (Order-Service)
- 상품을 구매하는 과정에서 주문서를 생성하는 기능입니다.
- 유저가 결제 정보를 입력하는 페이지에 진입하면 주문서를 생성하고, 결제에 성공하면 주문서의 상태를 "성공"으로 바꿉니다.
### 재고 기능 (Stock-Service)
- 예약상품의 재고 현황은 Redis에 저장합니다.
- 해당 재품의 재고 상황을 Redis에서 불러오고, 증감하는 기능이 있습니다.

<details>
<summary><b>📚 API 보기</b></summary>
<div markdown="1">

![pay-service](https://github.com/skroy0513/hugeTraffic/assets/117910568/c91b66d4-ebf6-427c-bd9d-45d1ab6bf46a)
![product-service](https://github.com/skroy0513/hugeTraffic/assets/117910568/40beb68c-a863-4b7c-b053-6f64445e575f)
![order-service](https://github.com/skroy0513/hugeTraffic/assets/117910568/edf477cc-bdbe-4830-b41d-0bdab1c92388)
![stock-service](https://github.com/skroy0513/hugeTraffic/assets/117910568/d2953703-16ec-4845-8ecf-d4d0205cc386)

</div>
</details>

## 🛠️ 트러블슈팅 경험
- <b>동시성 이슈 해결</b><br>
&nbsp;&nbsp;준비된 재고보다 10배 넘는 유저가 해당 상품을 동시에 구매를 시도하는 경우 동시성 이슈가 발생하여 오버셀링이 일어났습니다. <br>Redisson 분산락과 Redis를 In-Memory DB로 사용하는 방식을 적용해보고 성능이 더 좋았던 Redis를 DB로 사용하는 방식을 선택하여 적용하였습니다. [자세히 보기](https://skroy0513.tistory.com/39)

- <b>상품의 오픈시각과 현재시각을 일관성 있게 비교</b><br>
&nbsp;&nbsp;현재시각을 LocalDateTime.now()로 해서 오픈시각과 비교하면 유저가 서비스를 접속하는 지역에 따라 오픈시각 이전에 구매를 하는 문제가 발생하였습니다. <br>이 문제를 해결하기 위해 서비스가 위치하는 서버의 실행 위치인 ZoneId를 불러와 모든 유저가 서버의 시간으로 비교를 해서 어뷰징을 방지하였습니다.
    ```java
    @Transactional
    public OrderDto preCreate(Long userId, Long productId) {
      // 서버의 시간대를 가져옴
      ZoneId serverZoneId = ZoneId.systemDefault();
      // 유저의 현재시각을 서버의 시간대로 변환
      ZonedDateTime serverNow = ZonedDateTime.now(serverZoneId);
    	
      // 저장된 오픈시간을 서버의 시간대로 변환
      LocalDateTime opentime =preOrderProductClient.getOpenTime(productId); 
      ZonedDateTime serverOpenTime = openTime.atZone(serverZoneId);
    
      if (serverNow.isBefore(serverOpenTime)) {
        throw new RuntimeException("아직 상품을 구매할 수 없습니다.");
      }
    	... 구매 진행 로직
    }
    ```
