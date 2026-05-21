package com.packt.football;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.packt.football.config.FootballContainersConfig;

@SpringBootTest
@Import(FootballContainersConfig.class)
class FootballApplicationTests {

	@Test
	void contextLoads() {
	}
}
