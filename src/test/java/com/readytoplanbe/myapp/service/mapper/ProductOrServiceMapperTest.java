package com.readytoplanbe.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductOrServiceMapperTest {

    private ProductOrServiceMapper productOrServiceMapper;

    @BeforeEach
    public void setUp() {
        productOrServiceMapper = new ProductOrServiceMapperImpl();
    }
}
