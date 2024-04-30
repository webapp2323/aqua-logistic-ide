package ua.kiev.prog.oauth2.loginviagoogle.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskStatus;

@Entity
public class Task {

  @Id
  @GeneratedValue
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  private String address;
  private String phone;
  private int quantity;
  private long price;

  //TODO: add status to constructor methods below
//  @Enumerated(EnumType.STRING)
//  private TaskStatus status;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  public Task() {}

  public Task(Date date, String address, String phone, int quantity, long price) {
    this.date = date;
    this.address = address;
    this.phone = phone;
    this.quantity = quantity;
    this.price = price;
  }

  public static Task of(Date date, String address, String phone, int quantity, long price) {
    return new Task(date, address, phone, quantity, price);
  }

  public static Task fromDTO(TaskDTO taskDTO) {
    return Task.of(taskDTO.getDate(), taskDTO.getAddress(), taskDTO.getPhone(), taskDTO.getQuantity(), taskDTO.getPrice());
  }


  public TaskDTO toDTO() {
    return TaskDTO.of(id, date, address, phone, quantity, price, account.getEmail());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }
}
