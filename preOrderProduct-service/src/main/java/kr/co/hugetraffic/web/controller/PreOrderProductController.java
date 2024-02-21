package kr.co.hugetraffic.web.controller;

import kr.co.hugetraffic.entity.PreOrderProduct;
import kr.co.hugetraffic.service.PreOrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pre/product")
@RequiredArgsConstructor
public class PreOrderProductController {

    private final PreOrderProductService preOrderProductService;

    /*
    상품 리스트를 불러온다.(2개)
     */
    @GetMapping("/list")
    public ResponseEntity<List<PreOrderProduct>> getList(){
        List<PreOrderProduct> productList = preOrderProductService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    /*
    상품의 상세정보를 불러온다.
     */
    @GetMapping("/detail")
    public ResponseEntity<PreOrderProduct> getdetail(@RequestParam("id") Long productId) {
        PreOrderProduct product = preOrderProductService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    /*
    상품의 현재 재고상황을 불러온다.
     */
    @GetMapping("/stock")
    public ResponseEntity<Integer> getstock(@RequestParam("id") Long productId) {
        int stock = preOrderProductService.getStockById(productId);
        return ResponseEntity.ok(stock);
    }

    /*
    상품의 오픈시간을 불러온다.
     */
    @GetMapping("/feign/getOpenTime/{productId}")
    public ResponseEntity<LocalDateTime> getOpenTime(@PathVariable Long productId) {
        LocalDateTime openTime = preOrderProductService.getOpenTime(productId);
        return ResponseEntity.ok(openTime);
    }


}
