package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.AuthRequestDTO;
import live.sahiwl.pocketlybe.dto.AuthResponseDTO;
import live.sahiwl.pocketlybe.dto.UserRequestDTO;
import live.sahiwl.pocketlybe.dto.UserResponseDTO;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.UserRepository;
import live.sahiwl.pocketlybe.service.JwtService;
import live.sahiwl.pocketlybe.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    public UserController(UserService userSer, UserRepository userRepository) {
        this.userService = userSer;
        this.userRepository = userRepository;
    }

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@Valid @RequestBody AuthRequestDTO request) {
        try {
            AuthResponseDTO response = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(AuthResponseDTO.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        try {

            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(request.getUsername());

                AuthResponseDTO response = AuthResponseDTO.builder()
                        .token(token)
                        .username(request.getUsername())
                        .message("Login successful")
                        .build();

                return ResponseEntity.ok(response);
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

    /**
     * Logout endpoint - JWT is stateless, so this just provides confirmation
     * Frontend should remove the token from storage (localStorage/sessionStorage)
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponseDTO> logout() {
        return ResponseEntity.ok(AuthResponseDTO.builder()
                .message("Logged out successfully. Please remove the token from client storage.")
                .build());
    }

}
