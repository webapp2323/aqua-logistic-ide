package ua.kiev.prog.oauth2.loginviagoogle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TaskToNotifyDTO {
    private final String email;
    private final Date date;
    private final String text;


}
