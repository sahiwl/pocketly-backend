package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.AuthRequestDTO;
import live.sahiwl.pocketlybe.dto.AuthResponseDTO;
import live.sahiwl.pocketlybe.dto.UserResponseDTO;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.UserRepository;
import live.sahiwl.pocketlybe.service.JwtService;
import live.sahiwl.pocketlybe.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Value("${COOKIE_SECURE}") //true for prod
    private boolean cookieSecure;

    public UserController(UserService userSer, UserRepository userRepository) {
        this.userService = userSer;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@Valid @RequestBody AuthRequestDTO request,
            HttpServletResponse response) {
        try {
            AuthResponseDTO authResponse = userService.registerUser(request);

            // Auto-login after signup by setting JWT cookie
            User user = userService.findByUsername(request.getUsername());

            String token = jwtService.generateToken(
                    user.getUsername(),
                    user.getId());

            // Set JWT cookie (HttpOnly, 3 days)
            Cookie jwtCookie = new Cookie("accessToken", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(cookieSecure);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3 * 24 * 60 * 60); // 3 days
            response.addCookie(jwtCookie);

            authResponse.setToken(token); // Update token in response

            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(AuthResponseDTO.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request,
            HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            if (authentication.isAuthenticated()) {
                User user = userService.findByUsername(request.getUsername());


                String token = jwtService.generateToken(
                        user.getUsername(),
                        user.getId());


                Cookie jwtCookie = new Cookie("accessToken", token);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setSecure(cookieSecure); // true for HTTPS in production
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(3 * 24 * 60 * 60); // 3 days
                response.addCookie(jwtCookie);

                AuthResponseDTO authResponse = AuthResponseDTO.builder()
                        .token(token)
                        .username(user.getUsername())
                        .message("Login successful")
                        .build();

                return ResponseEntity.ok(authResponse);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponseDTO.builder()
                            .message("Invalid credentials")
                            .build());

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponseDTO.builder()
                            .message("Invalid username or password")
                            .build());
        }
    }

    @GetMapping("/showallusers")
    public List<UserResponseDTO> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponseDTO> logout(HttpServletResponse response) {

        Cookie jwtCookie = new Cookie("accessToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(cookieSecure);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .message("Logged out successfully")
                .build());
    }

}
