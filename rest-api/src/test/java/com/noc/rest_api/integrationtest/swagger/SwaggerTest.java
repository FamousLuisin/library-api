package com.noc.rest_api.integrationtest.swagger;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static io.restassured.RestAssured.*;

import com.noc.rest_api.config.TestConfigs;
import com.noc.rest_api.integrationtest.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class SwaggerTest extends AbstractIntegrationTest{

	@Test
	void shouldDisplaySwaggerUIPage() {
		String content = given()
			.basePath("/swagger-ui/index.html")
			.port(TestConfigs.SERVER_PORT)
			.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body()
					.asString();

		assertTrue(content.contains("Swagger UI"));
	}

}
