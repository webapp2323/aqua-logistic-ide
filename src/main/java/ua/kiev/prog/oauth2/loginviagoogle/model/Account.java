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

import lombok.*;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.UserRole;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

  public static Account of(UserRole role, String email, String password, String name, String pictureUrl) {
    return new Account(null, email, password, name, pictureUrl, role, new ArrayList<>());
  }

  public static Account fromDTO(AccountDTO accountDTO) {
    return Account.of(accountDTO.getRole(), accountDTO.getEmail(), accountDTO.getPassword(), accountDTO.getName(), accountDTO.getPictureUrl());
  }

  public void addTask(Task task) {
    task.setAccount(this);
    tasks.add(task);
  }

  public AccountDTO toDTO() {
    return AccountDTO.of(email,role, password, name, pictureUrl);
  }
}
