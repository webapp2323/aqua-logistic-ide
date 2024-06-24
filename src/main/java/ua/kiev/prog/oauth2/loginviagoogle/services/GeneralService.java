package ua.kiev.prog.oauth2.loginviagoogle.services;


import org.springframework.data.domain.Pageable;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskStatus;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskToNotifyDTO;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;

import java.util.Date;
import java.util.List;


public interface GeneralService {
    Account getAccountByEmail(String email);
    void addAccount(AccountDTO accountDTO);
    void addAccountWithTasks(AccountDTO accountDTO, List<TaskDTO> tasks);
    void addTask(String email, TaskDTO taskDTO);
    List<TaskDTO> getTasksByStatus(String email, TaskStatus status, Pageable pageable);
    List<TaskDTO> getAllTasks(Pageable pageable);
    List<TaskToNotifyDTO> getTasksToNotify(Date now);
    Long count(String email);
    int countAllTasks();
    void delete(List<Long> idList);
    void deleteTasks(List<Long> ids);
    long getTotalTasksCount(String email);
}
