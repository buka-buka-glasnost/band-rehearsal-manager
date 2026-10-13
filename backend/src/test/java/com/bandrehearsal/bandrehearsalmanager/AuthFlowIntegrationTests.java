package com.bandrehearsal.bandrehearsalmanager;

import com.bandrehearsal.bandrehearsalmanager.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class AuthFlowIntegrationTests extends ApiTestSupport {

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    void protectedEndpointWithoutTokenReturns401() throws Exception {
        mvc.perform(get("/band/get-list"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.detail").value("Authentication is required to access this resource"));
    }

    @Test
    void loginReturnsTokenPairAndRole() throws Exception {
        mvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(credentials(ADMIN, ADMIN_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(900))
                .andExpect(jsonPath("$.username").value(ADMIN))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void wrongPasswordReturns401() throws Exception {
        mvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(credentials(ADMIN, "wrong-password")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Invalid username or password"));
    }

    @Test
    void userRoleCanReadButCannotWrite() throws Exception {
        String token = accessToken(USER, USER_PASSWORD);

        mvc.perform(get("/band/get-list").header(AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk());

        mvc.perform(post("/band/create")
                        .header(AUTHORIZATION, bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"Not Allowed\",\"genre\":\"Noise\",\"foundedYear\":2000}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.detail").value("Your role does not allow this operation"));
    }

    @Test
    void adminRoleCanWrite() throws Exception {
        String token = accessToken(ADMIN, ADMIN_PASSWORD);

        mvc.perform(post("/band/create")
                        .header(AUTHORIZATION, bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"Admin Band\",\"genre\":\"Jazz\",\"foundedYear\":2001}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Admin Band"));
    }

    @Test
    void meReturnsTheUserFromTheToken() throws Exception {
        String token = accessToken(USER, USER_PASSWORD);

        mvc.perform(get("/auth/me").header(AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USER))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void malformedTokenReturns401() throws Exception {
        mvc.perform(get("/band/get-list").header(AUTHORIZATION, bearer("not.a.jwt")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Access token is invalid"));
    }

    @Test
    void tokenSignedWithAnotherKeyReturns401() throws Exception {
        var otherKey = Keys.hmacShaKeyFor(new byte[64]);
        String forged = Jwts.builder().subject(ADMIN).claim("role", "ADMIN")
                .expiration(Date.from(Instant.now().plusSeconds(600)))
                .signWith(otherKey)
                .compact();

        mvc.perform(get("/band/get-list").header(AUTHORIZATION, bearer(forged)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Access token is invalid"));
    }

    @Test
    void expiredTokenReturns401WithExpiredMessage() throws Exception {
        var key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
        String expired = Jwts.builder().subject(ADMIN).claim("role", "ADMIN")
                .issuedAt(Date.from(Instant.now().minusSeconds(3600)))
                .expiration(Date.from(Instant.now().minusSeconds(60)))
                .signWith(key)
                .compact();

        mvc.perform(get("/band/get-list").header(AUTHORIZATION, bearer(expired)))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(WWW_AUTHENTICATE, containsString("invalid_token")))
                .andExpect(jsonPath("$.detail").value("Access token has expired"));
    }

    @Test
    void refreshRotatesTokensAndDetectsReuse() throws Exception {
        String firstRefresh = read(login(USER, USER_PASSWORD), "$.refreshToken");

        MvcResult refreshed = mvc.perform(post("/auth/refresh")
                        .contentType(APPLICATION_JSON)
                        .content(refreshBody(firstRefresh)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn();
        String newAccess = read(refreshed, "$.accessToken");
        String secondRefresh = read(refreshed, "$.refreshToken");
        assertThat(secondRefresh).isNotEqualTo(firstRefresh);

        // The new access token works
        mvc.perform(get("/band/get-list").header(AUTHORIZATION, bearer(newAccess)))
                .andExpect(status().isOk());

        // The used refresh token cannot be used again...
        mvc.perform(post("/auth/refresh").contentType(APPLICATION_JSON).content(refreshBody(firstRefresh)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Refresh token has already been used or revoked"));

        // ...and trying to reuse it revoked every session of the user, including the newest one
        mvc.perform(post("/auth/refresh").contentType(APPLICATION_JSON).content(refreshBody(secondRefresh)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unknownRefreshTokenReturns401() throws Exception {
        mvc.perform(post("/auth/refresh").contentType(APPLICATION_JSON).content(refreshBody("made-up-token")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Refresh token is invalid"));
    }

    @Test
    void logoutRevokesTheRefreshToken() throws Exception {
        String refreshToken = read(login(ADMIN, ADMIN_PASSWORD), "$.refreshToken");

        mvc.perform(post("/auth/logout").contentType(APPLICATION_JSON).content(refreshBody(refreshToken)))
                .andExpect(status().isNoContent());

        mvc.perform(post("/auth/refresh").contentType(APPLICATION_JSON).content(refreshBody(refreshToken)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerCreatesAUserRoleAccountAndRejectsDuplicates() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(credentials("new.member", "secret1")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("new.member"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());

        mvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(credentials("new.member", "secret1")))
                .andExpect(status().isConflict());
    }

    @Test
    void registerValidatesInput() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(credentials("x", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }
}
