package ua.kiev.prog.oauth2.loginviagoogle.controllers;

import java.util.Collection;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.services.GeneralService;

@Controller
public class MainController {

  private final GeneralService generalService;

  private final PasswordEncoder passwordEncoder;

  public MainController(GeneralService generalService, PasswordEncoder passwordEncoder) {
    this.generalService = generalService;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/")
  public String index(Model model) {
    User user = getCurrentUser();

    String login = user.getUsername();
//    CustomUser dbUser = userService.findByLogin(login);
//
//    model.addAttribute("login", login);
//    model.addAttribute("roles", user.getAuthorities());
//    model.addAttribute("admin", isAdmin(user));
//    model.addAttribute("email", dbUser.getEmail());
//    model.addAttribute("phone", dbUser.getPhone());
//    model.addAttribute("address", dbUser.getAddress());

    return "index.html";
  }

  @PostMapping(value = "/update")
  public String update(@RequestParam(required = false) String email,
      @RequestParam(required = false) String phone) {
    User user = getCurrentUser();

    String login = user.getUsername();
    //userService.updateUser(login, email, phone);

    return "redirect:/";
  }

  @PostMapping(value = "/newuser")
  public String update(@RequestParam String login,
      @RequestParam String password,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String address,
      Model model) {
    AccountDTO accountDTO = AccountDTO.of(
        email,
        passwordEncoder.encode(password),
        login,
        "pic"
    );

    generalService.addAccount(accountDTO);

//    String passHash = passwordEncoder.encode(password);
//
//    if (!userService.addUser(login, passHash, UserRole.USER, email, phone, address)) {
//      model.addAttribute("exists", true);
//      model.addAttribute("login", login);
//      return "register";
//    }

    return "redirect:/signUp.html";
  }

  @PostMapping(value = "/delete")
  public String delete(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
      Model model) {
//    userService.deleteUsers(ids);
//    model.addAttribute("users", userService.getAllUsers());
//
    return "admin";
  }

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/register")
  public String register() {
    return "register";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')") // SpEL !!!
  public String admin(Model model) {
//    model.addAttribute("users", userService.getAllUsers());
    return "admin";
  }

  @GetMapping("/unauthorized")
  public String unauthorized(Model model) {
    User user = getCurrentUser();
    model.addAttribute("login", user.getUsername());
    return "unauthorized";
  }

  // ----

  private User getCurrentUser() {
    return (User) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
  }

  private boolean isAdmin(User user) {
    Collection<GrantedAuthority> roles = user.getAuthorities();

    for (GrantedAuthority auth : roles) {
      if ("ROLE_ADMIN".equals(auth.getAuthority())) {
        return true;
      }
    }

    return false;
  }
}

