package kr.co.hugetraffic.web.controller;

import kr.co.hugetraffic.dto.PayDto;
import kr.co.hugetraffic.entity.OrderInfo;
import kr.co.hugetraffic.entity.Product;
import kr.co.hugetraffic.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /*
    상품 리스트를 불러온다.(2개)
     */
    @GetMapping("/list")
    public ResponseEntity<List<Product>> getList(){
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    /*
    상품의 상세정보를 불러온다.
     */
    @GetMapping("/detail")
    public ResponseEntity<Product> getdetail(@RequestParam("id") Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    /*
    상품의 현재 재고상황을 불러온다.
     */
    @GetMapping("/stock")
    public ResponseEntity<Integer> getstock(@RequestParam("id") Long productId) {
        int stock = productService.getStockById(productId);
        return ResponseEntity.ok(stock);
    }

    /*
    결제 성공한 경우 주문정보를 불러온다.
     */
    @GetMapping("/order/info")
    public ResponseEntity<OrderInfo> orderInfo(@RequestParam("id") Long productId,
                                               @RequestHeader HttpHeaders headers) {
        // 유저정보와 제품아이디를 가지고 해당 상품의 구매 이력이 있는지 확인
        // 있다면 주문정보를 return
        Long userId = Long.valueOf(headers.get("userId").get(0));
        OrderInfo orderInfo = productService.getOrderInfo(userId, productId);
        return ResponseEntity.ok(orderInfo);
    }


}
