package com.airline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Airline Application Tests")
class AirlineApplicationTests {

	@Test
	@DisplayName("Should load application context successfully")
	void contextLoads() {
		// This test verifies that the Spring application context loads successfully
	}

}
