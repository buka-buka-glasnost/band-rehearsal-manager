package com.bandrehearsal.bandrehearsalmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class CrudIntegrationTests extends ApiTestSupport {

    private String admin;

    @BeforeEach
    void loginAsAdmin() throws Exception {
        admin = accessToken(ADMIN, ADMIN_PASSWORD);
    }

    @Test
    void missingRecordReturns404() throws Exception {
        mvc.perform(get("/band/get/999999").header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Band with id 999999 not found"));
    }

    @Test
    void invalidBodyReturns400WithFieldErrors() throws Exception {
        mvc.perform(post("/band/create")
                        .header(AUTHORIZATION, bearer(admin))
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"\",\"genre\":\"Rock\",\"foundedYear\":1800}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("Name is required"))
                .andExpect(jsonPath("$.errors.foundedYear").value("Founded year must be 1900 or later"));
    }

    @Test
    void malformedJsonReturns400() throws Exception {
        mvc.perform(post("/band/create")
                        .header(AUTHORIZATION, bearer(admin))
                        .contentType(APPLICATION_JSON)
                        .content("{ not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Request body is missing or malformed"));
    }

    @Test
    void updateWithoutIdReturns400AndUnknownIdReturns404() throws Exception {
        mvc.perform(put("/rehearsal-room/update")
                        .header(AUTHORIZATION, bearer(admin))
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"Room\",\"address\":\"Street 1\",\"capacity\":5}"))
                .andExpect(status().isBadRequest());

        mvc.perform(put("/rehearsal-room/update")
                        .header(AUTHORIZATION, bearer(admin))
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":999999,\"name\":\"Room\",\"address\":\"Street 1\",\"capacity\":5}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rehearsalWithUnknownBandReturns400() throws Exception {
        long roomId = create("/rehearsal-room/create",
                "{\"name\":\"Basement\",\"address\":\"Cellar Street 1\",\"capacity\":4}", admin);

        mvc.perform(post("/rehearsal/create")
                        .header(AUTHORIZATION, bearer(admin))
                        .contentType(APPLICATION_JSON)
                        .content("{\"date\":\"2026-11-20\",\"durationMinutes\":90,"
                                + "\"band\":{\"id\":999999},\"rehearsalRoom\":{\"id\":" + roomId + "}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Band with id 999999 does not exist"));
    }

    @Test
    void relationsWorkAcrossAllFiveTables() throws Exception {
        long bandId = create("/band/create",
                "{\"name\":\"Relation Testers\",\"genre\":\"Post-rock\",\"foundedYear\":2015}", admin);
        long roomId = create("/rehearsal-room/create",
                "{\"name\":\"Studio A\",\"address\":\"Main Street 5\",\"capacity\":6}", admin);

        // ManyToMany: the musician is linked to the band by id only, the response has the full band
        long musicianId = create("/musician/create",
                "{\"name\":\"Ana Drummer\",\"instrument\":\"Drums\",\"bands\":[{\"id\":" + bandId + "}]}", admin);
        mvc.perform(get("/musician/get/" + musicianId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bands", hasSize(1)))
                .andExpect(jsonPath("$.bands[0].name").value("Relation Testers"));

        // OneToMany: band -> rehearsals and room -> rehearsals
        long rehearsalId = create("/rehearsal/create",
                "{\"date\":\"2026-11-20\",\"durationMinutes\":120,"
                        + "\"band\":{\"id\":" + bandId + "},\"rehearsalRoom\":{\"id\":" + roomId + "}}", admin);
        mvc.perform(get("/rehearsal/get/" + rehearsalId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-11-20"))
                .andExpect(jsonPath("$.band.name").value("Relation Testers"))
                .andExpect(jsonPath("$.rehearsalRoom.name").value("Studio A"));

        // OneToMany: musician -> attendances and rehearsal -> attendances
        String attendanceJson = "{\"musician\":{\"id\":" + musicianId + "},\"rehearsal\":{\"id\":" + rehearsalId
                + "},\"present\":true}";
        long attendanceId = create("/attendance/create", attendanceJson, admin);

        // The same musician cannot be recorded twice for one rehearsal
        mvc.perform(post("/attendance/create")
                        .header(AUTHORIZATION, bearer(admin))
                        .contentType(APPLICATION_JSON)
                        .content(attendanceJson))
                .andExpect(status().isConflict());

        // Records that are still referenced cannot be deleted
        mvc.perform(delete("/band/delete/" + bandId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isConflict());
        mvc.perform(delete("/rehearsal-room/delete/" + roomId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isConflict());
        mvc.perform(delete("/rehearsal/delete/" + rehearsalId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isConflict());
        mvc.perform(delete("/musician/delete/" + musicianId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isConflict());

        // Deleting from the bottom up works
        mvc.perform(delete("/attendance/delete/" + attendanceId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isNoContent());
        mvc.perform(delete("/rehearsal/delete/" + rehearsalId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isNoContent());
        mvc.perform(delete("/band/delete/" + bandId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isNoContent());

        // Deleting the band also removed it from the musician (join table row)
        mvc.perform(get("/musician/get/" + musicianId).header(AUTHORIZATION, bearer(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bands", hasSize(0)));
    }

    @Test
    void updateChangesTheRecord() throws Exception {
        long id = create("/band/create", "{\"name\":\"Old Name\",\"genre\":\"Blues\",\"foundedYear\":1999}", admin);

        mvc.perform(put("/band/update")
                        .header(AUTHORIZATION, bearer(admin))
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":" + id + ",\"name\":\"New Name\",\"genre\":\"Blues\",\"foundedYear\":1999}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));

        mvc.perform(get("/band/get/" + id).header(AUTHORIZATION, bearer(admin)))
                .andExpect(jsonPath("$.name").value("New Name"));
    }
}
