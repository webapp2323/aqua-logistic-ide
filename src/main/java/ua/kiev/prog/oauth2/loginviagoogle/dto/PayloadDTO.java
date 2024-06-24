package ua.kiev.prog.oauth2.loginviagoogle.dto;

import lombok.Data;

@Data
public class PayloadDTO {
    private String email;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
}