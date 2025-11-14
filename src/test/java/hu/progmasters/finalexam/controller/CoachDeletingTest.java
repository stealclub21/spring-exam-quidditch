package hu.progmasters.finalexam.controller;

import hu.progmasters.finalexam.domain.Club;
import hu.progmasters.finalexam.domain.Coach;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class CoachDeletingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_deleteSuccessful() throws Exception {
        saveTestClubAndCoach();
        Coach coach = entityManager.find(Coach.class, 1);
        Club club = coach.getClub();
        assertTrue(club != null && club.getCoach() != null);
        assertFalse(coach.isDeleted());
        mockMvc.perform(delete("/api/coaches/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertNull(coach.getClub());
        assertTrue(coach.isDeleted());
    }

    @Test
    void test_coachNotExists() throws Exception {
        saveTestClubAndCoach();
        mockMvc.perform(delete("/api/coaches/2")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("coachId")))
                .andExpect(jsonPath("$[0].errorMessage", is("no coach found with id: 2")));
    }

    private void saveTestClubAndCoach() {
        entityManager.createNativeQuery(
                        "INSERT INTO club (name, wins) VALUES ('Wizard SE', 5); " +
                                "INSERT INTO coach (name, deleted, club_id) VALUES ('Kevin Coach', false, 1); "
                )
                .executeUpdate();
    }
}
