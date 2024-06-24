package ua.kiev.prog.oauth2.loginviagoogle.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskDTO {

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "UTC")
  private Date date;

  private String address;
  private String phone;
  private int quantity;
  private long price;
  private String taskOwner;

  @Enumerated(EnumType.STRING)
  private TaskStatus status = TaskStatus.NEW;

  private Long productId;

  @JsonCreator
  public TaskDTO(@JsonProperty(required = true) Date date,
                 @JsonProperty(required = true) String address,
                 @JsonProperty(required = true) int quantity,
                 @JsonProperty(required = true) String phone,
                 @JsonProperty(required = true) long price,
                 @JsonProperty String taskOwner,
                 @JsonProperty Long productId) {
    this.date = date;
    this.address = address;
    this.quantity = quantity;
    this.price = price;
    this.phone = phone;
    this.taskOwner = taskOwner;
    this.productId = productId;
  }

  public static TaskDTO of(Long id, Date date, String address, String phone, int quantity,
                           long price, String taskOwner, TaskStatus status, Long productId) {
    TaskDTO taskDTO = new TaskDTO(date, address, quantity, phone, price, taskOwner, productId);
    taskDTO.setId(id);
    taskDTO.setStatus(status);
    return taskDTO;
  }
}