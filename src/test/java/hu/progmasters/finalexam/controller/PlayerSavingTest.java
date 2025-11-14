package hu.progmasters.finalexam.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class PlayerSavingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_saveSuccessful() throws Exception {
        saveTestClub();
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"joined\": \"2011-11-11\",\n" +
                "    \"playerType\": \"CHASER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Willy Wizard")))
                .andExpect(jsonPath("$.joined", is("2011-11-11")))
                .andExpect(jsonPath("$.playerType", is("CHASER")))
                .andExpect(jsonPath("$.wins", is(4)))
                .andExpect(jsonPath("$.clubName", is("Wizard SE")));
    }

    @Test
    void test_nameNotValid() throws Exception {
        saveTestClub();
        String inputCommand = "{\n" +
                "    \"name\": \"    \",\n" +
                "    \"joined\": \"2011-11-11\",\n" +
                "    \"playerType\": \"SEEKER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].errorMessage", is("must not be blank")));
    }

    @Test
    void test_joinedTimeNull() throws Exception {
        saveTestClub();
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"playerType\": \"SEEKER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("joined")))
                .andExpect(jsonPath("$[0].errorMessage", is("must not be null")));
    }

    @Test
    void test_joinedTimeInFuture() throws Exception {
        saveTestClub();
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"joined\": \"2099-11-11\",\n" +
                "    \"playerType\": \"SEEKER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("joined")))
                .andExpect(jsonPath("$[0].errorMessage", is("must be in the past")));
    }

    @Test
    void test_playerTypeNull() throws Exception {
        saveTestClub();
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"joined\": \"2011-11-11\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("playerType")))
                .andExpect(jsonPath("$[0].errorMessage", is("must not be null")));
    }

    @Test
    void test_clubNotExists() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"joined\": \"2011-11-11\",\n" +
                "    \"playerType\": \"SEEKER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("no club found with id: 1")));
    }

    @Test
    void test_clubDoNotNeedMoreSeeker() throws Exception {
        saveTestClubAndPlayers();
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"joined\": \"2011-11-11\",\n" +
                "    \"playerType\": \"SEEKER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("club (id: 1) has enough player from type: SEEKER")));
    }

    @Test
    void test_clubDoNotNeedMoreKeeper() throws Exception {
        saveTestClubAndPlayers();
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"joined\": \"2011-11-11\",\n" +
                "    \"playerType\": \"KEEPER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("club (id: 1) has enough player from type: KEEPER")));
    }

    @Test
    void test_clubDoNotNeedMoreBeater() throws Exception {
        saveTestClubAndPlayers();
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"joined\": \"2011-11-11\",\n" +
                "    \"playerType\": \"BEATER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("club (id: 1) has enough player from type: BEATER")));
    }

    @Test
    void test_clubDoNotNeedMoreChaser() throws Exception {
        saveTestClubAndPlayers();
        String inputCommand = "{\n" +
                "    \"name\": \"Willy Wizard\",\n" +
                "    \"joined\": \"2011-11-11\",\n" +
                "    \"playerType\": \"CHASER\",\n" +
                "    \"wins\": 4,\n" +
                "    \"clubId\": 1\n" +
                "}";

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("club (id: 1) has enough player from type: CHASER")));
    }

    private void saveTestClub() {
        entityManager.createNativeQuery(
                "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Willow Witch', '2011-11-11', 'CHASER', 10, 1); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Whitney Witch', '2011-11-11', 'CHASER', 10, 1); "
        ).executeUpdate();
    }

    private void saveTestClubAndPlayers() {
        entityManager.createNativeQuery(
                "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Winona Witch', '2011-11-11', 'SEEKER', 10, 1); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Walter Wizard', '2011-11-11', 'KEEPER', 10, 1); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wyatt Wizard', '2011-11-11', 'BEATER', 10, 1); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('William Wizard', '2011-11-11', 'BEATER', 10, 1); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Willow Witch', '2011-11-11', 'CHASER', 10, 1); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Whitney Witch', '2011-11-11', 'CHASER', 10, 1); " +
                        "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wendy Witch', '2011-11-11', 'CHASER', 10, 1); "
        ).executeUpdate();
    }
}
