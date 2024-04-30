package ua.kiev.prog.oauth2.loginviagoogle.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import ua.kiev.prog.oauth2.loginviagoogle.model.Task;

public class TaskDTO {

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "UTC")
  private final Date date;

  private final String address;

  private final String phone;

  private final int quantity;

  private final long price;

  private final String taskOwner;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;


  @JsonCreator
  public TaskDTO(@JsonProperty(required = true) Date date,
      @JsonProperty(required = true) String address,
      @JsonProperty(required = true) int quantity,
      @JsonProperty(required = true) String phone,
      @JsonProperty(required = true)long price,
      @JsonProperty String taskOwner) {
    this.date = date;
    this.address = address;
    this.quantity = quantity;
    this.price = price;
    this.phone = phone;
    this.taskOwner = taskOwner;
  }

  public TaskDTO(Long id, Date date, String address, String phone, int quantity, long price,
      String taskOwner, TaskStatus status) {
    this.id = id;
    this.date = date;
    this.address = address;
    this.quantity = quantity;
    this.price = price;
    this.phone = phone;
    this.taskOwner = taskOwner;
    this.status = status;
  }


  public static TaskDTO of(Long id, Date date, String address, String phone, int quantity,
      long price, String taskOwner, TaskStatus status) {
    return new TaskDTO(id, date, address, phone, quantity, price, taskOwner, status);
  }

  public Long getId() {
    return id;
  }

  public Date getDate() {
    return date;
  }

  public String getAddress() {
    return address;
  }

  public int getQuantity() {
    return quantity;
  }

  public long getPrice() {
    return price;
  }

  public String getPhone() {
    return phone;
  }

  public String getTaskOwner() {
    return taskOwner;
  }

  public TaskStatus getStatus() {
    return status;
  }
}
