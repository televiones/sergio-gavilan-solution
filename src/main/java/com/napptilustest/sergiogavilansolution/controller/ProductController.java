package com.napptilustest.sergiogavilansolution.controller;

import com.napptilustest.sergiogavilansolution.exceptions.NotFoundException;
import com.napptilustest.sergiogavilansolution.model.ProductDTO;
import com.napptilustest.sergiogavilansolution.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * @param productId Product id *required
     * @return Flux of products with its details
     */
    @GetMapping("/{productId}/similar")
    public Flux<ProductDTO> getProductSimilar(@PathVariable("productId") Integer productId) {
        return productService.findSimilarProducts(productId);
    }

    /**
     * @return HTTP Status 404 with a custom message
     */
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> notFoundExceptionHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

}
