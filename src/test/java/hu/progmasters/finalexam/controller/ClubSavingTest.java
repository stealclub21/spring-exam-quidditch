package hu.progmasters.finalexam.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClubSavingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_saveSuccessful() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"Wizard SE\",\n" +
                "    \"wins\": 5\n" +
                "}";

        mockMvc.perform(post("/api/clubs")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Wizard SE")))
                .andExpect(jsonPath("$.wins", is(5)));
    }

    @Test
    void test_NameNotValid() throws Exception {
        String inputCommand = "{\n" +
                "    \"name\": \"     \",\n" +
                "    \"wins\": 5\n" +
                "}";

        mockMvc.perform(post("/api/clubs")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputCommand))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].errorMessage", is("must not be blank")));
    }
}
