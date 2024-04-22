package ua.kiev.prog.oauth2.loginviagoogle.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.UserRole;

@Entity
public class Account {

  @Id
  @GeneratedValue
  private Long id;

  private String email;

  private String password;
  private String name;
  private String pictureUrl;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
  private List<Task> tasks = new ArrayList<>();

  public Account() {
  }

  private Account(UserRole role, String email, String password, String name, String pictureUrl) {
    this.role = role;
    this.email = email;
    this.name = name;
    this.password = password;
    this.pictureUrl = pictureUrl;
  }

  public static Account of(UserRole role, String email, String password, String name,
      String pictureUrl) {
    return new Account(role, email, password, name, pictureUrl);
  }

  public static Account fromDTO(AccountDTO accountDTO) {
    return Account.of(accountDTO.getRole(), accountDTO.getEmail(), accountDTO.getPassword(),
        accountDTO.getName(), accountDTO.getPictureUrl());
  }

  public void addTask(Task task) {
    task.setAccount(this);
    tasks.add(task);
  }

  public AccountDTO toDTO() {
    return AccountDTO.of(role, email, password, name, pictureUrl);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPictureUrl() {
    return pictureUrl;
  }

  public void setPictureUrl(String pictureUrl) {
    this.pictureUrl = pictureUrl;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public void setTasks(List<Task> tasks) {
    this.tasks = tasks;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }
}
