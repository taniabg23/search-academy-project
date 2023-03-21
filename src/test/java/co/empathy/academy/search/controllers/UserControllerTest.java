package co.empathy.academy.search.controllers;

import co.empathy.academy.search.user.controller.UserController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
                        .content("{\"id\":2,\"name\":\"user2\",\"email\":\"user2@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user2@email"));

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":2,\"name\":\"user2\",\"email\":\"user2@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void givenOneUser_whenGetUserThatDoesntExist_thenReturnsNotFoundStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":3,\"name\":\"user3\",\"email\":\"user3@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user3@email"));

        mvc.perform(MockMvcRequestBuilders.get("/users/{id}", 4))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenOneUser_whenGetUserThatExists_thenReturnsTheUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":5,\"name\":\"user5\",\"email\":\"user5@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user5@email"));

        mvc.perform(MockMvcRequestBuilders.get("/users/{id}", 5))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user5@email"));
    }

    @Test
    void givenOneUser_whenUpdateUserThatDoesntExist_thenReturnsNotFoundStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":6,\"name\":\"user6\",\"email\":\"user6@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user6@email"));

        mvc.perform(MockMvcRequestBuilders.put("/users")
                        .content("{\"id\":7,\"name\":\"user7\",\"email\":\"user7@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenOneUser_whenUpdateUserThatExists_thenReturnsTheUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":8,\"name\":\"user8\",\"email\":\"user8@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user8@email"));

        mvc.perform(MockMvcRequestBuilders.put("/users")
                        .content("{\"id\":8,\"name\":\"user9\",\"email\":\"user9@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user9"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user9@email"));
    }

    @Test
    void givenOneUser_whenDeleteUserThatDoesntExist_thenReturnsNotFoundStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":10,\"name\":\"user10\",\"email\":\"user10@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user10@email"));

        mvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 11))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenOneUser_whenDeleteUserThatExists_thenReturnsTheUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":12,\"name\":\"user12\",\"email\":\"user12@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user12"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user12@email"));

        mvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 12))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user12"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user12@email"));
    }

    @Test
    void givenMoreThanOneUser_whenAskForAllUsers_thenReturnsListWithUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":14,\"name\":\"user14\",\"email\":\"user14@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user14@email"));

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":15,\"name\":\"user15\",\"email\":\"user15@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(15))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user15@email"));

        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .content("{\"id\":16,\"name\":\"user16\",\"email\":\"user16@email\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(16))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user16"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("user16@email"));

        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{" +
                        "\"id\":" + 14 + "," +
                        "\"name\":" + "\"user14\"" + "," +
                        "\"email\":" + "\"user14@email\"" +
                        "}," +
                        "{" +
                        "\"id\":" + 15 + "," +
                        "\"name\":" + "\"user15\"" + "," +
                        "\"email\":" + "\"user15@email\"" +
                        "}," +
                        "{" +
                        "\"id\":" + 16 + "," +
                        "\"name\":" + "\"user16\"" + "," +
                        "\"email\":" + "\"user16@email\"" +
                        "}]"));
    }

    @Test
    void givenNoUser_whenAddUserWithFile_thenReturnsUsersData() throws Exception {
        String content = "[{" +
                "\"id\":17," +
                "\"name\":\"user17\"," +
                "\"email\":\"user17@email.com\"" +
                "}," +
                "{" +
                "\"id\":18," +
                "\"name\":\"user18\"," +
                "\"email\":\"user18@email.com\"" +
                "}," +
                "{" +
                "\"id\":19," +
                "\"name\":\"user19\"," +
                "\"email\":\"user19@email.com\"" +
                "}]";
        MockMultipartFile file = new MockMultipartFile("file", "users.json",
                MediaType.APPLICATION_JSON.getType(), content.getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/users/file").file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(content));
    }
}
