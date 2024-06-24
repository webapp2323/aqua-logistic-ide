package ua.kiev.prog.oauth2.loginviagoogle.services;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskStatus;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskToNotifyDTO;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;
import ua.kiev.prog.oauth2.loginviagoogle.model.Product;
import ua.kiev.prog.oauth2.loginviagoogle.model.Task;
import ua.kiev.prog.oauth2.loginviagoogle.repos.AccountRepository;
import ua.kiev.prog.oauth2.loginviagoogle.repos.ProductRepository;
import ua.kiev.prog.oauth2.loginviagoogle.repos.TaskRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    private final AccountRepository accountRepository;
    private final TaskRepository taskRepository;
    private final ProductRepository productRepository;

    @Override
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public void addAccount(AccountDTO accountDTO) {
        if (!accountRepository.existsByEmail(accountDTO.getEmail())) {
            Account account = Account.fromDTO(accountDTO);
            accountRepository.save(account);
        }
    }

    @Transactional
    @Override
    public void addAccountWithTasks(AccountDTO accountDTO, List<TaskDTO> tasks) {
        if (!accountRepository.existsByEmail(accountDTO.getEmail())) {
            Account account = Account.fromDTO(accountDTO);
            List<Task> taskEntities = tasks.stream().map(taskDTO -> {
                Product product = productRepository.findById(taskDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
                long price = taskDTO.getQuantity() * product.getUnitPrice();
                taskDTO.setPrice(price);
                return Task.fromDTO(taskDTO, product);
            }).toList();
            taskEntities.forEach(account::addTask);
            accountRepository.save(account);
        }
    }

    @Transactional
    @Override
    public void addTask(String email, TaskDTO taskDTO) {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            Product product = productRepository.findById(taskDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
            Task task = Task.fromDTO(taskDTO, product);
            account.addTask(task);
            accountRepository.save(account);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskDTO> getTasksByStatus(String email, TaskStatus status, Pageable pageable) {
        return taskRepository.findByAccountEmailAndStatus(email, status, pageable).stream().map(Task::toDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).stream().map(Task::toDTO).toList();
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

    @Transactional(readOnly = true)
    @Override
    public int countAllTasks() {
        return (int) taskRepository.count();
    }

    @Transactional
    @Override
    public void delete(List<Long> idList) {
        List<Task> tasks = taskRepository.findAllById(idList);
        tasks.forEach(task -> task.setStatus(TaskStatus.DELETE));
        taskRepository.saveAll(tasks);
    }

    @Override
    public long getTotalTasksCount(String email) {
        return taskRepository.countByAccountEmail(email);
    }

    public void deleteTasks(List<Long> ids) {
        for (Long id : ids) {
            Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task:" + id + " does not exist"));
            taskRepository.delete(task);
        }
    }
}
