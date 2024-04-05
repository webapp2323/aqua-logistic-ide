package ua.kiev.prog.oauth2.loginviagoogle.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.TaskDTO;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;
import ua.kiev.prog.oauth2.loginviagoogle.services.GeneralService;

@Component
public class AuthHandler implements AuthenticationSuccessHandler {

  private final GeneralService generalService;

  public AuthHandler(GeneralService generalService) {
    this.generalService = generalService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Authentication authentication) throws IOException {
    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
    OAuth2User user = token.getPrincipal();

    Map<String, Object> attributes = user.getAttributes();

    String email = (String) attributes.get("email");
    String password = (String) attributes.get("password");

    AccountDTO accountDTO = AccountDTO.of(
        email,
        password,
        (String) attributes.get("name"),
        getPictureUrl(attributes)
    );

    List<TaskDTO> tasks = Arrays.asList(
        TaskDTO.of(new Date(), "Test task 1"),
        TaskDTO.of(new Date(), "Test task 2"),
        TaskDTO.of(new Date(), "Test task 3"),
        TaskDTO.of(new Date(), "Test task 4"),
        TaskDTO.of(new Date(), "Test task 5"),
        TaskDTO.of(new Date(), "Test task 6")
    );

    Account account = generalService.getAccountByEmail(email);
    if (account == null) {
      generalService.addAccountWithTasks(accountDTO, tasks);
    }

    httpServletResponse.sendRedirect("/index1.html");
  }

  private String getPictureUrl(Map<String, Object> attributes) {
    String pictureUrl = attributes.get("picture").toString();
    if (pictureUrl.length() > 251) {
      pictureUrl = pictureUrl.substring(0, 250);
    }
    return pictureUrl;
  }
}
