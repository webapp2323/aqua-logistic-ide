package ua.kiev.prog.oauth2.loginviagoogle.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class TaskDTO {

  private Long id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "UTC")
  private final Date date;

  private final String address;

  private final String phone;

  private final int quantity;

  private final long price;

//  TODO: add status to constructor methods below
//  @Enumerated(EnumType.STRING)
//  private TaskStatus status;


  @JsonCreator
  public TaskDTO(@JsonProperty(required = true) Date date,
      @JsonProperty(required = true) String address,
      @JsonProperty(required = true) int quantity,
      @JsonProperty(required = true) String phone,
      long price) {
    this.date = date;
    this.address = address;
    this.quantity = quantity;
    this.price = price;
    this.phone = phone;
  }

  public TaskDTO(Long id, Date date, String address, String phone, int quantity, long price) {
    this.id = id;
    this.date = date;
    this.address = address;
    this.quantity = quantity;
    this.price = price;
    this.phone = phone;
  }


  public static TaskDTO of(Long id, Date date, String address, String phone, int quantity,
      long price) {
    return new TaskDTO(id, date, address, phone, quantity, price);
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
}
