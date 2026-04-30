package com.packt.registry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

class RegistryApplicationUnitTests {

    @Test
    void applicationClassIsAnnotatedAsSpringBootApplication() {
        assertThat(RegistryApplication.class).hasAnnotation(SpringBootApplication.class);
    }

    @Test
    void applicationClassEnablesEurekaServer() {
        assertThat(RegistryApplication.class).hasAnnotation(EnableEurekaServer.class);
    }
}
