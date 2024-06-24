package ua.kiev.prog.oauth2.loginviagoogle.config;

import java.io.IOException;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.UserRole;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;
import ua.kiev.prog.oauth2.loginviagoogle.services.GeneralService;


@Component
public class AuthHandler implements AuthenticationSuccessHandler {

  private final GeneralService generalService;

  public AuthHandler(GeneralService generalService) {
    this.generalService = generalService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
    OAuth2User user = token.getPrincipal();
    Map<String, Object> attributes = user.getAttributes();

    String email = (String) attributes.get("email");
    String password = "N/A";

    AccountDTO accountDTO = AccountDTO.of(email, UserRole.USER , password, (String) attributes.get("name"), getPictureUrl(attributes));
    Account account = generalService.getAccountByEmail(email);
    if (account == null) {
      generalService.addAccount(accountDTO);
    }

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(response.getWriter(), accountDTO);
  }

  private String getPictureUrl(Map<String, Object> attributes) {
    String pictureUrl = attributes.get("picture").toString();
    if (pictureUrl.length() > 251) {
      pictureUrl = pictureUrl.substring(0, 250);
    }
    return pictureUrl;
  }
}