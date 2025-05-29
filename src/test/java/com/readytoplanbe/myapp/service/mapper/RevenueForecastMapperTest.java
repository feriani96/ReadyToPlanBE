package com.readytoplanbe.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RevenueForecastMapperTest {

    private RevenueForecastMapper revenueForecastMapper;

    @BeforeEach
    public void setUp() {
        revenueForecastMapper = new RevenueForecastMapperImpl();
    }
}
