package kr.co.hugetraffic.web.controller;

import kr.co.hugetraffic.entity.Product;
import kr.co.hugetraffic.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
