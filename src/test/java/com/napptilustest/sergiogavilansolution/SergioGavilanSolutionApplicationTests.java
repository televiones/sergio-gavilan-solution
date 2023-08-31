package com.napptilustest.sergiogavilansolution;


import com.napptilustest.sergiogavilansolution.model.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SergioGavilanSolutionApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    /**
     * Checks that for product id=1 we get 3 similar products
     */
    @Test
    public void testGetProductSimilar() {
        Integer productId = 1;

        webTestClient.get()
                .uri("/product/{productId}/similar", productId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductDTO.class)
                .hasSize(3);
    }

    /**
     * Checks that for product id=5 we get HTTP 404 status
     */
    @Test
    public void testGetProductSimilarNotFound() {
        Integer productId = 5;

        webTestClient.get()
                .uri("/product/{productId}/similar", productId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).isEqualTo("Product not found");
    }

    /**
     * Tests that for id=4 we get only 2 recommendations
     */
    @Test
    public void testGetProductSimilarWithEmptyResult() {
        Integer productId = 4;

        webTestClient.get()
                .uri("/product/{productId}/similar", productId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductDTO.class)
                .hasSize(2);
    }
}
