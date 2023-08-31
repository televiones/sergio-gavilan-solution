package com.napptilustest.sergiogavilansolution.service;

import com.napptilustest.sergiogavilansolution.exceptions.NotFoundException;
import com.napptilustest.sergiogavilansolution.model.ProductDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Service that encapsulates all the logic behind getting similar products
 */
@Service
public class ProductService {

    private final WebClient webClient;
    private static final String GET_PRODUCT_DETAIL_ENDPOINT = "/product/{productId}";
    private static final String GET_PRODUCT_SIMILAR_ENDPOINT = "/product/{productId}/similarids";

    public ProductService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * @param productId Product id
     * @return Returns a Flux of ProductDTO instances with each similar product's detail
     */
    @Cacheable(cacheNames = "productCache", key = "#productId")
    public Flux<ProductDTO> findSimilarProducts(Integer productId) {
        return getProductById(productId)
                .flatMapMany(product -> getRelatedProductIds(productId) // Get related Ids
                        .flatMapMany(Flux::fromIterable) // List to Flux
                        .flatMap(this::getProductById) // For each product, get its details
                        .onErrorResume(throwable -> Mono.empty()) // If an error occurs, we ignore that product and move on
                ).cache(Duration.ofSeconds(60));
    }

    /**
     * @param productId Product id
     * @return Mono with a list of ids of the related products
     */
    private Mono<List<Integer>> getRelatedProductIds(Integer productId) {
        return webClient.get()
                .uri(GET_PRODUCT_SIMILAR_ENDPOINT, productId)
                .retrieve()
                .bodyToFlux(Integer.class)
                .collectList().log();
    }

    /**
     * @param productId Product id
     * @return Mono with a ProductDTO instance if found. If not, returns an error code.
     */
    private Mono<ProductDTO> getProductById(Integer productId) {
        return webClient.get()
                .uri(GET_PRODUCT_DETAIL_ENDPOINT, productId)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.error(new NotFoundException("Product Not Found"))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        response -> Mono.error(new ResponseStatusException(response.statusCode()))
                )
                .bodyToMono(ProductDTO.class).log();
    }
}
