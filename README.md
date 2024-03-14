# 예약 구매 프로젝트
다량의 요청을 오류없이 처리하는 예약상품 구매 서비스입니다.
유저들은 특정시각에 오픈하는 예약상품, 상시 구매가 가능한 일반상품을 구매할 수 있습니다.

## 개발환경
- JAVA 17
- Spring Boot
- MySql
- JPA
- Redis
- Feign Client


## ERD
![Untitledd](https://github.com/skroy0513/hugeTraffic/assets/117910568/707b28fc-8c72-4b97-9e9d-542ce55b8743)


## 아키텍처
![reservesite](https://github.com/skroy0513/hugeTraffic/assets/117910568/c6f2e074-c3a2-4a51-9b4b-a7fe0ac638c4)


## 프로젝트 기능
### 상품 기능 (Product-Service)
- 상품의 리스트, 정보 확인
- 상품의 재고 확인
- 오픈시각 확인
### 상품 구매 기능 (Pay-Service)
- 상품 구매화면 진입
- 상품 구매
### 주문 기능 (Order-Service)
- 주문서 생성
- 주문서 확인
### 재고 기능 (Stock-Service)
- 상품의 재고 확인
- 상품의 재고 증/감 하기

<details>
<summary>API 보기</summary>
<div markdown="1">

![pay-service](https://github.com/skroy0513/hugeTraffic/assets/117910568/c91b66d4-ebf6-427c-bd9d-45d1ab6bf46a)
![product-service](https://github.com/skroy0513/hugeTraffic/assets/117910568/40beb68c-a863-4b7c-b053-6f64445e575f)
![order-service](https://github.com/skroy0513/hugeTraffic/assets/117910568/edf477cc-bdbe-4830-b41d-0bdab1c92388)
![stock-service](https://github.com/skroy0513/hugeTraffic/assets/117910568/d2953703-16ec-4845-8ecf-d4d0205cc386)

</div>
</details>

## 트러블슈팅
[![Tistory's Card] "트러블슈팅 작성한 링크 첨부하기" (https://github-readme-tistory-card.vercel.app/api?name=skroy0513&postId=)](https://github.com/loosie/github-readme-tistory-card)
- 준비된 재고만큼 정확한 구매 (동시성 이슈)
- 오픈 시간 전에 예약 상품 구매 방지
