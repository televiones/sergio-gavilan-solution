package com.napptilustest.sergiogavilansolution.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Product DTO to store its details
 */
@Data
public class ProductDTO {
    @JsonProperty(value = "id", required = true)
    private String id;
    @JsonProperty(value = "name", required = true)
    private String name;
    @JsonProperty(value = "price", required = true)
    private Double price;
    @JsonProperty(value = "availability", required = true)
    private Boolean availability;
}
