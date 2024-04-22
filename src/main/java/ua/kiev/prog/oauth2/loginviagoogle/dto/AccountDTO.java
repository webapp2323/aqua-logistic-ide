package ua.kiev.prog.oauth2.loginviagoogle.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class AccountDTO {
    private final String email;

    private final String password;
    private final String name;
    private final String pictureUrl;

    @Enumerated(EnumType.STRING)
    private final UserRole role;

    private AccountDTO(UserRole role, String email, String password, String name, String pictureUrl) {
        this.role = role;
        this.email = email;
        this.password = password;
        this.name = name;
        this.pictureUrl = pictureUrl;
    }

    public static AccountDTO of(UserRole role, String email, String password, String name, String pictureUrl) {
        return new AccountDTO(role, email, password, name, pictureUrl);
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
