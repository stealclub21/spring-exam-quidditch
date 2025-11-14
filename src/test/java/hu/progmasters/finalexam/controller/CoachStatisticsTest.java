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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class CoachStatisticsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_getStatsSuccessful() throws Exception {
        saveTestClubAndCoach();
        mockMvc.perform(get("/api/coaches/statistics/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clubWins", is(5)))
                .andExpect(jsonPath("$.averagePlayerWins", is(6.0)))
                .andExpect(jsonPath("$.maxPlayerWins", is(11)))
                .andExpect(jsonPath("$.minPlayerWins", is(2)));
    }

    @Test
    void test_getStatsSuccessful_noPlayers() throws Exception {
        saveTestClubAndCoach2();
        mockMvc.perform(get("/api/coaches/statistics/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("coachId")))
                .andExpect(jsonPath("$[0].errorMessage", is("no players in the club of coach with id: 1")));
    }

    @Test
    void test_coachNotExists() throws Exception {
        saveTestClubAndCoach();
        mockMvc.perform(get("/api/coaches/statistics/2")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("coachId")))
                .andExpect(jsonPath("$[0].errorMessage", is("no coach found with id: 2")));
    }

    private void saveTestClubAndCoach() {
        entityManager.createNativeQuery(
                        "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
                                "INSERT INTO coach (name, deleted, club_id) VALUES ('Kevin Coach', false, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Winona Witch', '2009-11-11', 'SEEKER', 2, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Walter Wizard', '2011-11-11', 'KEEPER', 3, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wyatt Wizard', '2011-11-11', 'BEATER', 11, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('William Wizard', '2010-11-11', 'BEATER', 5, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Willow Witch', '2011-11-11', 'CHASER', 2, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Whitney Witch', '2010-11-11', 'CHASER', 11, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wendy Witch', '2010-11-11', 'CHASER', 8, 1); "
                )
                .executeUpdate();
    }

    private void saveTestClubAndCoach2() {
        entityManager.createNativeQuery(
                        "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
                                "INSERT INTO coach (name, deleted, club_id) VALUES ('Kevin Coach', false, 1); "
                )
                .executeUpdate();
    }
}
