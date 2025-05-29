package com.readytoplanbe.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnterpriseMapperTest {

    private EnterpriseMapper enterpriseMapper;

    @BeforeEach
    public void setUp() {
        enterpriseMapper = new EnterpriseMapperImpl();
    }
}
