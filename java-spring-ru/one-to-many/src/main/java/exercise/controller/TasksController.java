package exercise.controller;

import java.util.List;
import java.util.Optional;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.model.Task;
import exercise.model.User;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskMapper taskMapper;

    // BEGIN
    @GetMapping
    public List<TaskDTO> getTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::map)
                .toList();
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("not found task with id " + id));

        return taskMapper.map(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO data) {
        Task task = taskMapper.map(data);
        User user = findUserById(data.getAssigneeId());

        user.addTask(task);
        userRepository.save(user);

        return taskMapper.map(task);
    }

    @PutMapping("/{id}")
    public TaskDTO update(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO data) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("not found task with id " + id));



        User user = findUserById(data.getAssigneeId());
        user.removeTask(task);

        taskMapper.update(data, task);

        user.addTask(task);

        userRepository.save(user);

        return taskMapper.map(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Optional<Task> maybeTask = taskRepository.findById(id);

        if (maybeTask.isEmpty()) {
            return;
        }

        Task task = maybeTask.get();

        User user = findUserById(task.getAssignee().getId());
        user.removeTask(task);

        userRepository.save(user);
    }

    private User findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("not found user with id " + userId));

        return user;
    }
    // END
}
