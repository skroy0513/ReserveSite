package kr.co.hugetraffic.web.controller;

import kr.co.hugetraffic.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // DB에 저장된 재고수량을 불러오기
    @GetMapping("/{productId}")
    public ResponseEntity<Integer> getStock(@PathVariable Long productId) {
        int stock = stockService.getStockById(productId);
        return ResponseEntity.ok(stock);
    }

    // redis에 저장된 재고수량을 감소(결제 진입 -> 결제 성공)
    @PostMapping("/decrease/{productId}")
    public ResponseEntity<Integer> decreaseStock(@PathVariable Long productId) {
        int stock = stockService.decreaseStock(productId);
        return ResponseEntity.ok(stock);
    }

    // redis에 저장된 재고수량을 증가(결제 진입 -> 결제 실패)
    @PostMapping("/increase/{productId}")
    public ResponseEntity<Integer> increaseStock(@PathVariable Long productId) {
        int stock = stockService.increaseStock(productId);
        return ResponseEntity.ok(stock);
    }

}

