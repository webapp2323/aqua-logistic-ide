package ua.kiev.prog.oauth2.loginviagoogle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@AllArgsConstructor
public class AccountDTO {
    private String email;

    private String password;
    private String name;
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    public static AccountDTO of(String email, UserRole role, String password, String name, String pictureUrl) {
        return new AccountDTO(email, password, name, pictureUrl, role);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public UserRole getRole() {
        return role;
    }

}
