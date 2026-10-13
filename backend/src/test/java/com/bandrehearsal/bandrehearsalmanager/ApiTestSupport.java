package com.bandrehearsal.bandrehearsalmanager;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Shared MockMvc setup and helpers for the integration tests. */
abstract class ApiTestSupport {

    protected static final String ADMIN = "admin";
    protected static final String ADMIN_PASSWORD = "admin123";
    protected static final String USER = "user";
    protected static final String USER_PASSWORD = "user123";

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mvc;

    @BeforeEach
    void setUpMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    protected MvcResult login(String username, String password) throws Exception {
        return mvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(credentials(username, password)))
                .andExpect(status().isOk())
                .andReturn();
    }

    protected String accessToken(String username, String password) throws Exception {
        return read(login(username, password), "$.accessToken");
    }

    /** POSTs the body as ADMIN, expects 201 Created and returns the new id. */
    protected long create(String url, String json, String adminToken) throws Exception {
        MvcResult result = mvc.perform(post(url)
                        .header(AUTHORIZATION, bearer(adminToken))
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        return ((Number) JsonPath.read(result.getResponse().getContentAsString(), "$.id")).longValue();
    }

    protected static String read(MvcResult result, String path) throws Exception {
        return JsonPath.read(result.getResponse().getContentAsString(), path);
    }

    protected static String bearer(String token) {
        return "Bearer " + token;
    }

    protected static String credentials(String username, String password) {
        return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    }

    protected static String refreshBody(String refreshToken) {
        return "{\"refreshToken\":\"" + refreshToken + "\"}";
    }
}
