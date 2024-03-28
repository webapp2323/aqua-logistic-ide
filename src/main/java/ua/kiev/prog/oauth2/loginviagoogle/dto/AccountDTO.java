package ua.kiev.prog.oauth2.loginviagoogle.dto;

public class AccountDTO {
    private final String email;

    private final String password;
    private final String name;
    private final String pictureUrl;

    private AccountDTO(String email, String password, String name, String pictureUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.pictureUrl = pictureUrl;
    }

    public static AccountDTO of(String email, String password, String name, String pictureUrl) {
        return new AccountDTO(email, password, name, pictureUrl);
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
}
