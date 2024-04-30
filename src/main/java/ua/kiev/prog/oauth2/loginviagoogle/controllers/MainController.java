package ua.kiev.prog.oauth2.loginviagoogle.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.PageCountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskStatus;
import ua.kiev.prog.oauth2.loginviagoogle.dto.UserRole;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;
import ua.kiev.prog.oauth2.loginviagoogle.services.GeneralService;

@RestController
public class MainController {

  private static final int PAGE_SIZE = 5;
  private final GeneralService generalService;
  private final PasswordEncoder passwordEncoder;

  public MainController(GeneralService generalService, PasswordEncoder passwordEncoder) {
    this.generalService = generalService;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/")
  public void index(HttpServletResponse response) throws IOException {
    response.sendRedirect("/index.html");
  }

//  @PostMapping(value = "/update")
//  public String update(@RequestParam(required = false) String email,
//      @RequestParam(required = false) String phone) {
//    User user = getCurrentUser();
//
//    String login = user.getUsername();
////    userService.updateUser(login, email, phone);
//
//    return "redirect:/";
//  }

  @PostMapping(value = "/addTask")
  public ResponseEntity addTask(AbstractAuthenticationToken auth,
      @RequestBody TaskDTO task) {
    String userEmail = getEmailFromAuthenticationToken(auth);
    generalService.addTask(userEmail, task);
    return ResponseEntity.ok().build();
  }

  private String getEmailFromAuthenticationToken(AbstractAuthenticationToken auth) {
    if (auth instanceof OAuth2AuthenticationToken token) {
      return token.getPrincipal().getAttribute("email");
    } else if (auth instanceof UsernamePasswordAuthenticationToken token) {
      return ((User) token.getPrincipal()).getUsername();
    }
    return null;
  }

  /**
   * Is called when user registers via website.
   *
   * @param response
   * @param login
   * @param password
   * @param email
   * @throws IOException
   */
  @PostMapping(value = "/newuser")
  public void addNewUser(HttpServletResponse response,
      @RequestParam String login,
      @RequestParam String password,
      @RequestParam(required = false) String email) throws IOException {
    Account account = generalService.getAccountByEmail(email);
    if (account == null) {
      AccountDTO accountDTO = AccountDTO.of(UserRole.USER, email,
          passwordEncoder.encode(password), login, "pic");
      generalService.addAccount(accountDTO);
    }
    response.sendRedirect("/signUp.html");
  }

  @GetMapping("/account")
  public Account getPicture(AbstractAuthenticationToken auth) {
    String email = getEmailFromAuthenticationToken(auth);
    return generalService.getAccountByEmail(email);
  }

  @GetMapping("/tasks")
  public List<TaskDTO> getAllTasks(AbstractAuthenticationToken auth) {
    String email = getEmailFromAuthenticationToken(auth);
    Collection<GrantedAuthority> roles = getRoleFromAuthenticationToken(auth);
    boolean isAdmin = isAdmin(roles);
    if (isAdmin) {
      return generalService.getAllTasks();
    }
    return generalService.getTasksByStatus(email, TaskStatus.NEW);
  }

  @PostMapping("/deleteTasks")
  public void deleteTasks(@RequestParam(name = "toDelete[]") List<Long> ids) {
    generalService.delete(ids);
  }

  private Collection<GrantedAuthority> getRoleFromAuthenticationToken(
      AbstractAuthenticationToken auth) {
    if (auth instanceof OAuth2AuthenticationToken token) {
      return (Collection<GrantedAuthority>) token.getPrincipal().getAuthorities();
    } else if (auth instanceof UsernamePasswordAuthenticationToken token) {
      return ((User) token.getPrincipal()).getAuthorities();
    }
    return null;
  }

//  @PostMapping(value = "/delete")
//  public String delete(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
//      Model model) {
////    userService.deleteUsers(ids);
////    model.addAttribute("users", userService.getAllUsers());
////
//    return "admin";
//  }

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/register")
  public String register() {
    return "register";
  }

  @GetMapping("/count")
  public PageCountDTO count(OAuth2AuthenticationToken auth) {
    String email = getEmailFromAuthenticationToken(auth);
    return PageCountDTO.of(generalService.count(email), PAGE_SIZE);
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

  private boolean isAdmin(Collection<GrantedAuthority> roles) {
    if (roles != null) {
      for (GrantedAuthority auth : roles) {
        if ("ROLE_ADMIN".equalsIgnoreCase(auth.getAuthority())) {
          return true;
        }
      }
    }
    return false;
  }
}

