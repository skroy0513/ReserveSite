package kr.co.hugetraffic.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "sotck-service")
public interface StockClient {
}
