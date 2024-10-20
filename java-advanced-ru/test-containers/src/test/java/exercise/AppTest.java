package exercise;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.http.MediaType;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.bouncycastle.pqc.crypto.ExchangePair;

@SpringBootTest
@AutoConfigureMockMvc

// BEGIN
@Testcontainers
@Transactional
// END
public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    // BEGIN
    @Container
    private static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("test")
            .withUsername("sa")
            .withPassword("sa")
            .withInitScript("init.sql");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    @Test
    void testReadPersons() throws Exception {
        MockHttpServletResponse responsePersons = mockMvc
                .perform(
                        get("/people")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePersons.getStatus()).isEqualTo(200);
        assertThat(responsePersons.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(responsePersons.getContentAsString()).isEqualTo("[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\"},{\"id\":2,\"firstName\":\"Jack\",\"lastName\":\"Doe\"},{\"id\":3,\"firstName\":\"Jassica\",\"lastName\":\"Simpson\"},{\"id\":4,\"firstName\":\"Robert\",\"lastName\":\"Lock\"}]");
    }

    @Test
    void testReadPersonById() throws Exception {
        MockHttpServletResponse responsePerson = mockMvc
                .perform(
                        get("/people/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePerson.getStatus()).isEqualTo(200);
        assertThat(responsePerson.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(responsePerson.getContentAsString()).isEqualTo("{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\"}");
    }

    @Test
    void testDeletePersonById() throws Exception {
        MockHttpServletResponse responseDelete = mockMvc
                .perform(
                        delete("/people/{id}", 1)
                )
                .andReturn()
                .getResponse();

        assertThat(responseDelete.getStatus()).isEqualTo(200);

        MockHttpServletResponse responsePerson = mockMvc
                .perform(
                        get("/people")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePerson.getContentAsString()).doesNotContain("John", "Smith");
    }
    // END

    @Test
    void testCreatePerson() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
            .perform(
                post("/people")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"firstName\": \"Jackson\", \"lastName\": \"Bind\"}")
            )
            .andReturn()
            .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        MockHttpServletResponse response = mockMvc
            .perform(get("/people"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Jackson", "Bind");
    }
}
