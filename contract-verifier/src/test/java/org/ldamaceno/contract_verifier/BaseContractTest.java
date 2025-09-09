package org.ldamaceno.contract_verifier;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.ldamaceno.contract_verifier.controller.UserController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(properties = "server.port=0")
public class BaseContractTest {

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new UserController());
    }
}
