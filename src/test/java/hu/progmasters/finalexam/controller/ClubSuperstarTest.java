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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class ClubSuperstarTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_clubHasSuperstar() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(get("/api/clubs/superstar/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Has a superstar player!")));
    }

    @Test
    void test_clubDoesNothaveSuperstar() throws Exception {
        saveTestClubAndPlayers2();
        mockMvc.perform(get("/api/clubs/superstar/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("No superstar player.")));
    }

    @Test
    void test_ClubNotExists() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(get("/api/clubs/superstar/3")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("clubId")))
                .andExpect(jsonPath("$[0].errorMessage", is("no club found with id: 3")));
    }

    private void saveTestClubAndPlayers() {
        entityManager.createNativeQuery(
                        "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
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
                        "INSERT INTO club (name, wins) VALUES ('Wizard SE', 15); " +
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
}
