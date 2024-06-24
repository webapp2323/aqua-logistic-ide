package ua.kiev.prog.oauth2.loginviagoogle.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskStatus;
import ua.kiev.prog.oauth2.loginviagoogle.services.AuthService;
import ua.kiev.prog.oauth2.loginviagoogle.services.GeneralService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private static final int PAGE_SIZE = 5;
    private final GeneralService generalService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Void> addTask(@AuthenticationPrincipal Object principal, @RequestBody TaskDTO task) {
        String userEmail = authService.getEmailFromPrincipal(principal);
        generalService.addTask(userEmail, task);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@AuthenticationPrincipal Object principal, @RequestParam(defaultValue = "0") int page) {
        String email = authService.getEmailFromPrincipal(principal);
        boolean isAdmin = authService.isAdmin(principal);
        List<TaskDTO> tasks = isAdmin
                ? generalService.getAllTasks(PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"))
                : generalService.getTasksByStatus(email, TaskStatus.NEW, PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteTasks(@AuthenticationPrincipal Object principal, @RequestBody List<Long> ids) {
        generalService.deleteTasks(ids);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCount(@AuthenticationPrincipal Object principal) {
        String email = authService.getEmailFromPrincipal(principal);
        long count = generalService.getTotalTasksCount(email);
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("pageSize", PAGE_SIZE);
        return ResponseEntity.ok(response);
    }
}