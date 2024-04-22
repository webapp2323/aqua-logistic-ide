package ua.kiev.prog.oauth2.loginviagoogle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.dto.UserRole;
import ua.kiev.prog.oauth2.loginviagoogle.services.GeneralService;

@Configuration
@EnableScheduling
@PropertySource("classpath:application.properties")
public class AppConfig {

  // @Value("${spring.mail.username}")
  @Value("${spring.mail.username}")
  private String fromAddress;

  @Bean
  @Scope("prototype")
  public SimpleMailMessage messageTemplate() {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setSubject("Task notification");
    message.setText("Please do not forget to complete the task:\n\n [%s] %s");
    message.setFrom(fromAddress);

    return message;
  }

  @Bean
  public CommandLineRunner demo(final GeneralService generalService,
      final PasswordEncoder passwordEncoder) {
    return strings -> {
      AccountDTO accountDTO = AccountDTO.of(UserRole.ADMIN,
          "admin@admin.com",
          passwordEncoder.encode("password"),
          "Admin", "");
      if (generalService.getAccountByEmail("admin@admin.com") == null) {
        generalService.addAccount(accountDTO);
      }
    };
  }
}
