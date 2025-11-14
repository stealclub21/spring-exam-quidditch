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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class ClubWinTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_winSuccessful() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(put("/api/clubs/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Wizard SE")))
                .andExpect(jsonPath("$.wins", is(6)))
                .andExpect(jsonPath("$.coachName", is("Kevin Coach")))
                .andExpect(jsonPath("$.players[0].wins", is(2)))
                .andExpect(jsonPath("$.players[1].wins", is(4)))
                .andExpect(jsonPath("$.players[2].wins", is(11)))
                .andExpect(jsonPath("$.players[3].wins", is(6)))
                .andExpect(jsonPath("$.players[4].wins", is(1)))
                .andExpect(jsonPath("$.players[5].wins", is(12)))
                .andExpect(jsonPath("$.players[6].wins", is(9)));
    }

    @Test
    void test_ClubNotExists() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(put("/api/clubs/2")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("no club found with id: 2")));
    }

    private void saveTestClubAndPlayers() {
        entityManager.createNativeQuery(
                        "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Winona Witch', '2011-11-11', 'SEEKER', 1, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Walter Wizard', '2011-11-11', 'KEEPER', 3, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wyatt Wizard', '2011-11-11', 'BEATER', 10, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('William Wizard', '2011-11-11', 'BEATER', 5, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Willow Witch', '2011-11-11', 'CHASER', 0, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Whitney Witch', '2011-11-11', 'CHASER', 11, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wendy Witch', '2011-11-11', 'CHASER', 8, 1); " +
                                "INSERT INTO coach (name, deleted, club_id) VALUES ('Kevin Coach', false, 1); "
                )
                .executeUpdate();
    }
}
