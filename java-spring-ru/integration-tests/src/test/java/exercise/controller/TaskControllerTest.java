package exercise.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    private Task testTask;

    @BeforeEach
    public void setUp() {
        testTask = createTestTask();
        taskRepository.save(testTask);
    }

    private Task createTestTask() {
        Task task = new Task();
        task.setTitle(faker.lorem().word());
        task.setDescription(faker.lorem().paragraph());
        return task;
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform((get("/tasks/{id}", testTask.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(testTask)));
    }

    @Test
    public void testSave() throws Exception {
        Task testTask = createTestTask();

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTask));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated());

        assertThat(taskRepository.findAll()).hasSize(2);
    }

    @Test
    public void update() throws Exception {
        Task testTask = createTestTask();
        taskRepository.save(testTask);

        testTask.setTitle("new title");
        testTask.setDescription("new description");

        var request = put("/tasks/{id}", testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTask));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var actualTask = taskRepository.findById(testTask.getId()).get();

        assertThat(actualTask.getTitle()).isEqualTo(testTask.getTitle());
        assertThat(actualTask.getDescription()).isEqualTo(testTask.getDescription());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", testTask.getId()))
                .andExpect(status().isOk());

        assertThat(taskRepository.findAll()).isEmpty();
    }
    // END
}
