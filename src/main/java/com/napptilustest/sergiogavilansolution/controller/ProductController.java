package com.napptilustest.sergiogavilansolution.controller;

import com.napptilustest.sergiogavilansolution.exceptions.NotFoundException;
import com.napptilustest.sergiogavilansolution.model.ProductDTO;
import com.napptilustest.sergiogavilansolution.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductController {

    final ProductService productService;
    private static final Logger logger = LogManager.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}/similar")
    public ResponseEntity<HttpStatus> getProductSimilar(@PathVariable("productId") long id) {
        Mono<ProductDTO> product = productService.getProductDetail(id);
        logger.info(product.subscribe(System.out::println,
                Throwable::printStackTrace,
                () -> System.out.println("completed without a value")));
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> notFoundExceptionHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

}
