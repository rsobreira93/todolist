package br.com.romulosobreira.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.romulosobreira.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    taskModel.setIdUser((UUID) request.getAttribute("idUser"));

    var currentDate = LocalDateTime.now();

    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start/End date most be after current date.");
    }

    if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date most be after start date.");
    }

    var task = this.taskRepository.save(taskModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(task);
  }

  @GetMapping("/")
  public List<TaskModel> list(HttpServletRequest request) {
    var userId = request.getAttribute("idUser");

    return this.taskRepository.findByIdUser((UUID) userId);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@RequestBody TaskModel taskModel, @PathVariable UUID id,
      HttpServletRequest request) {

    var taskExists = this.taskRepository.findById(id).orElse(null);

    if (taskExists == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
    }

    var userId = request.getAttribute("idUser");

    // var userOwnerTask = this.taskRepository.findByIdAndIdUser(id, (UUID) userId);

    if (!taskExists.getIdUser().equals(userId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed");
    }

    Utils.copyNonNullProperty(taskModel, taskExists);

    var taskUpdated = this.taskRepository.save(taskExists);

    return ResponseEntity.ok().body(taskUpdated);
  }
}
