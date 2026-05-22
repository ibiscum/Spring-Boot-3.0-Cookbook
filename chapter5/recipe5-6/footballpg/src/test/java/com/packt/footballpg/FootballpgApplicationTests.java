package com.packt.footballpg;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.packt.footballpg.config.FootballContainersConfig;

@SpringBootTest
@Import(FootballContainersConfig.class)
class FootballpgApplicationTests {

	@Test
	void contextLoads() {
	}

}
