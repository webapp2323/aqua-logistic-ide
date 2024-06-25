package ua.kiev.prog.oauth2.loginviagoogle.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ua.kiev.prog.oauth2.loginviagoogle.dto.UserRole;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;
import ua.kiev.prog.oauth2.loginviagoogle.repos.AccountRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;

    public String getEmailFromPrincipal(Object principal) {
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            return ((OAuth2User) principal).getAttribute("email");
        }
        return null;
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
        }
        return false;
    }

    public Account registerOAuthUserIfNotExist(String email) {
        Account user = accountRepository.findByEmail(email);
        if (Objects.isNull(user)) {
            Account newUser = new Account();
            newUser.setEmail(email);
            newUser.setName("Google:"+ email);
            newUser.setRole(UserRole.USER);
            accountRepository.save(newUser);
            return newUser;
        }

        return user;
    }
}