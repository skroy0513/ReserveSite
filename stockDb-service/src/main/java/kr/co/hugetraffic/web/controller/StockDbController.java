package kr.co.hugetraffic.web.controller;

import kr.co.hugetraffic.service.StockDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/db/stock")
@RequiredArgsConstructor
public class StockDbController {

    private final StockDbService stockService;

    // DB에 저장된 재고수량을 불러오기
    @GetMapping("/{productId}")
    public ResponseEntity<Integer> getStock(@PathVariable Long productId) {
        int stock = stockService.getStockById(productId);
        return ResponseEntity.ok(stock);
    }

    // DB에 저장된 예약상품 재고수량을 불러오기
    @GetMapping("/pre/{productId}")
    public ResponseEntity<Integer> getPreOrderStock(@PathVariable Long productId) {
        int stock = stockService.getPreOrderStockById(productId);
        return ResponseEntity.ok(stock);
    }

    // db에 저장된 재고수량을 감소(결제 진입 -> 결제 성공)
    @PostMapping("/decrease/{productId}")
    public ResponseEntity<Integer> decreaseStock(@PathVariable Long productId) {
        int stock = stockService.decreaseStock(productId);
        return ResponseEntity.ok(stock);
    }

    //db에 저장된 재고수량을 증가(결제 진입 -> 고객 상황으로 취소)
    @PostMapping("/increase/{productId}")
    public ResponseEntity<Integer> increaseStock(@PathVariable Long productId) {
        int stock = stockService.increaseStock(productId);
        return ResponseEntity.ok(stock);
    }

    // db에 저장된 예약상품 재고수량을 감소(결제 진입 -> 결제 성공)
    @PostMapping("/pre/decrease/{productId}")
    public ResponseEntity<Integer> decreasePreStock(@PathVariable Long productId) {
        int stock = stockService.decreasePreStock(productId);
        return ResponseEntity.ok(stock);
    }

    // redis에 저장된 재고가 0일경우(아니어도) redis의 재고를 db에 저장
    @PostMapping("/pre/update/{productId}")
    public ResponseEntity<Integer> updateStock(@PathVariable Long productId,
                                               @RequestParam("stock") int stock) {
        int dbstock = stockService.updateStock(productId, stock);
        return ResponseEntity.ok(dbstock);
    }
}

