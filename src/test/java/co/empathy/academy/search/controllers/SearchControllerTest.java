package co.empathy.academy.search.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {
    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private SearchController searchController;

    @Test
    void givenQuery_whenSearch_thenReturnsSearchAndClusterName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/search?query=abc"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((MockMvcResultMatchers.content().string("{\n\t\"query\": \"abc\",\n\t\"clusterName\": \"docker-cluster\"\n}")));
    }
}
