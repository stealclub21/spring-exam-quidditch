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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class PlayerListAllTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_atStart_emptyList() throws Exception {
        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void test_listSuccessful() throws Exception {
        saveTestClubAndPlayers();
        mockMvc.perform(get("/api/players")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Willow Witch")))
                .andExpect(jsonPath("$[1].name", is("Walter Wizard")))
                .andExpect(jsonPath("$[2].name", is("Wyatt Wizard")))
                .andExpect(jsonPath("$[3].name", is("William Wizard")))
                .andExpect(jsonPath("$[4].name", is("Wendy Witch")))
                .andExpect(jsonPath("$[5].name", is("Whitney Witch")))
                .andExpect(jsonPath("$[6].name", is("Winona Witch")));
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
}
