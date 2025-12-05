package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.AuthResponseDTO;
import live.sahiwl.pocketlybe.dto.SignupRequestDTO;
import live.sahiwl.pocketlybe.dto.LoginRequestDTO;
import live.sahiwl.pocketlybe.dto.UserResponseDTO;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.UserRepository;
import live.sahiwl.pocketlybe.service.JwtService;
import live.sahiwl.pocketlybe.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

        @Value("${COOKIE_SECURE}") // false for dev, true for prod
        private boolean cookieSecure;

        @Value("${SAME_SITE}")
        private String sameSite;

        public UserController(UserService userSer, UserRepository userRepository) {
                this.userService = userSer;
                this.userRepository = userRepository;
        }

        @PostMapping("/signup")
        public ResponseEntity<AuthResponseDTO> signup(@Valid @RequestBody SignupRequestDTO request,
                        HttpServletResponse response) {
                try {
                        AuthResponseDTO authResponse = userService.registerUser(request);

                        // Auto-login after signup by setting JWT cookie
                        User user = userService.findByUsername(request.getUsername());

                        String token = jwtService.generateToken(
                                        user.getUsername(),
                                        user.getId());

                        // Use ResponseCookie for SameSite support
                        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", token)
                                        .httpOnly(true)
                                        .secure(cookieSecure)
                                        .path("/")
                                        .maxAge(3 * 24 * 60 * 60) // 3 days
                                        .sameSite(sameSite) // Critical for cross-origin requests
                                        .build();

                        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

                        authResponse.setToken(token);

                        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
                } catch (Exception e) {
                        return ResponseEntity.badRequest()
                                        .body(AuthResponseDTO.builder()
                                                        .message(e.getMessage())
                                                        .build());
                }
        }

        @PostMapping("/signin")
        public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request,
                        HttpServletResponse response) {
                try {
                        // usernameOrEmail can be either username or email
                        Authentication authentication = authenticationManager
                                        .authenticate(
                                                        new UsernamePasswordAuthenticationToken(
                                                                        request.getUsernameOrEmail(),
                                                                        request.getPassword()));

                        if (authentication.isAuthenticated()) {
                                User user = userService.findByUsernameOrEmail(request.getUsernameOrEmail());

                                String token = jwtService.generateToken(
                                                user.getUsername(),
                                                user.getId());

                                // Use ResponseCookie for SameSite support
                                ResponseCookie jwtCookie = ResponseCookie.from("accessToken", token)
                                                .httpOnly(true)
                                                .secure(cookieSecure)
                                                .path("/")
                                                .maxAge(3 * 24 * 60 * 60) // 3 days
                                                .sameSite(sameSite) // Critical for cross-origin requests
                                                .build();

                                response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

                                AuthResponseDTO authResponse = AuthResponseDTO.builder()
                                                .token(token)
                                                .username(user.getUsername())
                                                .email(user.getEmail())
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
                                                        .message("Invalid username or email, or incorrect password")
                                                        .build());
                }
        }

        @GetMapping("/showallusers")
        public List<UserResponseDTO> getUsers() {
                return userService.getAllUsers();
        }

        @GetMapping("/me")
        public ResponseEntity<?> currUser(){
                try {           

                                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        

                                if (authentication == null || !authentication.isAuthenticated() 
                                                || authentication.getPrincipal().equals("anonymousUser")) {
                                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                                        .body(AuthResponseDTO.builder()
                                                                        .message("Not authenticated")
                                                                        .build());
                                }


                                String username = authentication.getName();

                                User user = userService.findByUsername(username);

                                UserResponseDTO userRes = UserResponseDTO.builder()
                                                                .id(user.getId())
                                                                .username(user.getUsername())
                                                                .email(user.getEmail())
                                                                .build();

                                return ResponseEntity.ok(userRes);
                } catch (Exception e) {
                          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(AuthResponseDTO.builder()
                                                        .message("Not authenticated")
                                                        .build());
                }
                
        }

        @PostMapping("/logout")
        public ResponseEntity<AuthResponseDTO> logout(HttpServletResponse response) {
                // Clear cookie with same settings
                ResponseCookie jwtCookie = ResponseCookie.from("accessToken", "")
                                .httpOnly(true)
                                .secure(cookieSecure)
                                .path("/")
                                .maxAge(0)
                                .sameSite(sameSite)
                                .build();

                response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

                return ResponseEntity.ok(AuthResponseDTO.builder()
                                .message("Logged out successfully")
                                .build());
        }

}
