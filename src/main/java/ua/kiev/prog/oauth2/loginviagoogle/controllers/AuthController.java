package ua.kiev.prog.oauth2.loginviagoogle.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ua.kiev.prog.oauth2.loginviagoogle.dto.AccountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;
import ua.kiev.prog.oauth2.loginviagoogle.services.AuthService;
import ua.kiev.prog.oauth2.loginviagoogle.services.GeneralService;
import ua.kiev.prog.oauth2.loginviagoogle.services.GoogleTokenVerifier;
import ua.kiev.prog.oauth2.loginviagoogle.services.jwt.JwtTokenUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final GeneralService generalService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody AccountDTO accountDTO) {
        Account account = generalService.getAccountByEmail(accountDTO.getEmail());
        if (account == null) {
            accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
            generalService.addAccount(accountDTO);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody MultiValueMap<String, String> formData) {
        String email = formData.getFirst("email");
        String password = formData.getFirst("password");
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @PostMapping("/oauth2/success")
    public ResponseEntity<?> oauth2Success(@RequestHeader("X-Google-Token") String googleToken) {
        try {
            GoogleIdToken.Payload payload = googleTokenVerifier.verify(googleToken);
            if (payload == null) {
                log.error("Google token verification failed.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OAuth token");
            }

            String email = payload.getEmail();

            Account user = authService.registerOAuthUserIfNotExist(email);
            UserDetails userDetails = User.builder()
                    .username(user.getEmail())
                    .password("")
                    .authorities(List.of(new SimpleGrantedAuthority(user.getRole().toString())))
                    .build();
            String jwtToken = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying OAuth token");
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAuthToken(@RequestParam String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (Boolean.TRUE.equals(jwtTokenUtil.validateRefreshToken(token, userDetails.getUsername(), false))) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new AuthResponse(refreshedToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    public static class AuthResponse {
        private final String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}