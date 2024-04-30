package ua.kiev.prog.oauth2.loginviagoogle.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskStatus;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskToNotifyDTO;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;
import ua.kiev.prog.oauth2.loginviagoogle.model.Task;
import ua.kiev.prog.oauth2.loginviagoogle.repos.AccountRepository;
import ua.kiev.prog.oauth2.loginviagoogle.repos.TaskRepository;

@Service
public class GeneralServiceImpl implements GeneralService {

  private final AccountRepository accountRepository;
  private final TaskRepository taskRepository;

  public GeneralServiceImpl(AccountRepository accountRepository, TaskRepository taskRepository) {
    this.accountRepository = accountRepository;
    this.taskRepository = taskRepository;
  }

  // DB -> E(20) -> R -> S <-> DTO -> C -> V(5) / JSON

  @Override
  public Account getAccountByEmail(String email) {
    return accountRepository.findByEmail(email);
  }

  @Override
  public void addAccount(AccountDTO accountDTO) {
    Account account = Account.fromDTO(accountDTO);
    accountRepository.save(account);
  }

  @Transactional
  @Override
  public void addAccountWithTasks(AccountDTO accountDTO, List<TaskDTO> tasks) {
    if (accountRepository.existsByEmail(accountDTO.getEmail())) {
      return; // do nothing
    }

    Account account = Account.fromDTO(accountDTO);

    tasks.forEach((x) -> {
      Task task = Task.fromDTO(x);
      account.addTask(task);
    });

    accountRepository.save(account);
  }

  @Transactional
  @Override
  public void addTask(String email, TaskDTO taskDTO) {
    Account account = accountRepository.findByEmail(email);
    Task task = Task.fromDTO(taskDTO);

    account.addTask(task);

    accountRepository.save(account);
  }

  @Transactional(readOnly = true)
  @Override
  public List<TaskDTO> getTasks(String email) {
    List<TaskDTO> result = new ArrayList<>();
    List<Task> tasks = taskRepository.findByAccountEmail(email);

    tasks.forEach((x) -> result.add(x.toDTO()));
    return result;
  }

  @Override
  public List<TaskDTO> getTasksByStatus(String email, TaskStatus status) {
    List<TaskDTO> result = new ArrayList<>();
    List<Task> tasks = taskRepository.findByAccountEmailAndStatus(email, status);

    tasks.forEach((x) -> result.add(x.toDTO()));
    return result;
  }

  @Override
  public List<TaskDTO> getAllTasks() {
    List<TaskDTO> result = new ArrayList<>();
    List<Task> tasks = taskRepository.findAll();
    tasks.forEach((x) -> result.add(x.toDTO()));
    return result;
  }

  @Transactional(readOnly = true)
  @Override
  public List<TaskToNotifyDTO> getTasksToNotify(Date now) {
    Calendar calendar = Calendar.getInstance();

    calendar.setTime(now);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    Date from = calendar.getTime();

    calendar.add(Calendar.MINUTE, 1);
    Date to = calendar.getTime();

    return taskRepository.findTasksToNotify(from, to);
  }

  @Transactional(readOnly = true)
  @Override
  public Long count(String email) {
    return taskRepository.countByAccountEmail(email);
  }

  @Transactional
  @Override
  public void delete(List<Long> idList) {
    List<Task> tasks = taskRepository.findAllById(idList);
    for (Task task : tasks) {
      task.setStatus(TaskStatus.DELETE);
    }
    taskRepository.saveAll(tasks);
  }
}
