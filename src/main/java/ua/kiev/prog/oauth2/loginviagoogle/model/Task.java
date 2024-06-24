package ua.kiev.prog.oauth2.loginviagoogle.model;

import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  private String address;
  private String phone;
  private int quantity;
  private long price;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  public Task() {}

  public Task(Date date, String address, String phone, int quantity, long price, TaskStatus status, Product product) {
    this.date = date;
    this.address = address;
    this.phone = phone;
    this.quantity = quantity;
    this.price = price;
    this.status = status;
    this.product = product;
  }

  public static Task of(Date date, String address, String phone, int quantity, long price, TaskStatus status, Product product) {
    return new Task(date, address, phone, quantity, price, status, product);
  }

  public static Task fromDTO(TaskDTO taskDTO, Product product) {
    return Task.of(taskDTO.getDate(), taskDTO.getAddress(), taskDTO.getPhone(), taskDTO.getQuantity(), taskDTO.getPrice(), taskDTO.getStatus(), product);
  }

  public TaskDTO toDTO() {
    return TaskDTO.of(id, date, address, phone, quantity, price, account != null ? account.getEmail() : null, status, product != null ? product.getId() : null);
  }
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }
}
