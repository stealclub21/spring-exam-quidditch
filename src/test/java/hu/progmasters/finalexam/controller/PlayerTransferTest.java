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
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class PlayerTransferTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_saveSuccessful() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(put("/api/players/4/club/2")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.joined", is(LocalDate.now().toString())))
                .andExpect(jsonPath("$.clubName", is("Witch SE")));
    }

    @Test
    void test_playerNotExists() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(put("/api/players/12/club/2")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("playerId")))
                .andExpect(jsonPath("$[0].errorMessage", is("no player found with id: 12")));
    }

    @Test
    void test_clubNotExists() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(put("/api/players/1/club/3")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("no club found with id: 3")));
    }

    @Test
    void test_playerAlreadyJoinedClub() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(put("/api/players/1/club/2")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("player (id: 1) already joined club (id: 2)")));
    }

    @Test
    void test_clubDoNotNeedMoreSeeker() throws Exception {
        saveTestClubAndPlayers2();
        mockMvc.perform(put("/api/players/8/club/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("club (id: 1) has enough player from type: SEEKER")));
    }

    @Test
    void test_clubDoNotNeedMoreKeeper() throws Exception {
        saveTestClubAndPlayers2();
        mockMvc.perform(put("/api/players/9/club/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("club (id: 1) has enough player from type: KEEPER")));
    }

    @Test
    void test_clubDoNotNeedMoreBeater() throws Exception {
        saveTestClubAndPlayers2();
        mockMvc.perform(put("/api/players/10/club/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("club (id: 1) has enough player from type: BEATER")));
    }

    @Test
    void test_clubDoNotNeedMoreChaser() throws Exception {
        saveTestClubAndPlayers2();
        mockMvc.perform(put("/api/players/11/club/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("club (id: 1) has enough player from type: CHASER")));
    }

    private void saveTestClubAndPlayers() {
        entityManager.createNativeQuery(
                        "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
                        "INSERT INTO club (name, wins) VALUES ('Witch SE', 7); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Warren Wizard', '2009-11-11', 'BEATER', 1, 2); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Winona Witch', '2009-11-11', 'SEEKER', 1, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Walter Wizard', '2011-11-11', 'KEEPER', 3, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wyatt Wizard', '2011-11-11', 'BEATER', 10, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('William Wizard', '2010-11-11', 'BEATER', 5, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Willow Witch', '2011-11-11', 'CHASER', 0, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Whitney Witch', '2010-11-11', 'CHASER', 11, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wendy Witch', '2010-11-11', 'CHASER', 8, 1); "
                )
                .executeUpdate();
    }

    private void saveTestClubAndPlayers2() {
        entityManager.createNativeQuery(
                        "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
                                "INSERT INTO club (name, wins) VALUES ('Witch SE', 7); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Winona Witch', '2009-11-11', 'SEEKER', 1, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Walter Wizard', '2011-11-11', 'KEEPER', 3, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wyatt Wizard', '2011-11-11', 'BEATER', 10, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('William Wizard', '2010-11-11', 'BEATER', 5, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Willow Witch', '2011-11-11', 'CHASER', 0, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Whitney Witch', '2010-11-11', 'CHASER', 11, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wendy Witch', '2010-11-11', 'CHASER', 8, 1); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wren Witch', '2009-11-11', 'SEEKER', 1, 2); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wesley Wizard', '2011-11-11', 'KEEPER', 3, 2); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Warren Wizard', '2011-11-11', 'BEATER', 10, 2); " +
                                "INSERT INTO player (name, joined, player_type, wins, club_id) VALUES ('Wanda Witch', '2011-11-11', 'CHASER', 0, 2); "
                )
                .executeUpdate();
    }
}
