package com.atipera;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@SpringBootTest(
        classes = AtiperaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.profiles.active=test"
        }
)
@AutoConfigureWireMock
public abstract class BaseIntegrationTest {
}
