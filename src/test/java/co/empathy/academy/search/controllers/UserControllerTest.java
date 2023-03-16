package co.empathy.academy.search.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private UserController userController;

    @Test
    void givenNoUsers_whenAddNewUser_thenReturnsTheUserAdded() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":1,\"name\":\"user1\",\"email\":\"user1@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1@email"));
    }

    @Test
    void givenOneUser_whenAddRepeatedUser_thenReturnsConflictStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":1,\"name\":\"user1\",\"email\":\"user1@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1@email"));

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":1,\"name\":\"user1\",\"email\":\"user1@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void givenNoUsers_whenAskForAllUsers_thenReturnsEmptyList() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void givenOneUser_whenAskForAllUsers_thenReturnsListWithOneUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":1,\"name\":\"user1\",\"email\":\"user1@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1@email"));

        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[" +
                        "{" +
                        "\"id\":" + 1 + "," +
                        "\"name\":" + "\"user1\"" + "," +
                        "\"email\":" + "\"user1@email\"" +
                        "}" +
                        "]"));
    }

    @Test
    void givenThreeUsers_whenAskForAllUsers_thenReturnsListWithThreeUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":1,\"name\":\"user1\",\"email\":\"user1@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user1@email"));

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":2,\"name\":\"user2\",\"email\":\"user2@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user2@email"));

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":3,\"name\":\"user3\",\"email\":\"user3@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user3@email"));

        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[" +
                        "{" +
                        "\"id\":" + 1 + "," +
                        "\"name\":" + "\"user1\"" + "," +
                        "\"email\":" + "\"user1@email\"" +
                        "}," +
                        "{" +
                        "\"id\":" + 2 + "," +
                        "\"name\":" + "\"user2\"" + "," +
                        "\"email\":" + "\"user2@email\"" +
                        "}," +
                        "{" +
                        "\"id\":" + 3 + "," +
                        "\"name\":" + "\"user3\"" + "," +
                        "\"email\":" + "\"user3@email\"" +
                        "}" +
                        "]"));
    }
}
