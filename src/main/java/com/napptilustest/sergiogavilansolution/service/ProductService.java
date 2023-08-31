package com.napptilustest.sergiogavilansolution.service;

import com.napptilustest.sergiogavilansolution.exceptions.NotFoundException;
import com.napptilustest.sergiogavilansolution.model.ProductDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    WebClient webClient;

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    public Mono<ProductDTO> getProductDetail(long id) {
        return webClient
                .get()
                .uri("/product/" + id)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == 404,
                        error -> Mono.error(new NotFoundException("Product Not Found")))
                .bodyToMono(ProductDTO.class);
    }

    public Flux<ProductDTO> getProductSimilar(long id) {
        return webClient
                .get()
                .uri("/product/" + id + "/similarids")
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == 404,
                        error -> Mono.error(new NotFoundException("Product Not Found")))
                .bodyToFlux(ProductDTO.class)

                .doOnError(throwable -> logger.error("Error in Product external API", throwable));
    }
}
