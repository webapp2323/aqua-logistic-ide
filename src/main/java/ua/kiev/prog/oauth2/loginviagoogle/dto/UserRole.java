package ua.kiev.prog.oauth2.loginviagoogle.dto;

public  enum UserRole {
  ADMIN, USER;

  @Override
  public String toString() {
    return "ROLE_" + name();
  }
}
