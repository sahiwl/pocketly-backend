package live.sahiwl.pocketlybe.service;

import live.sahiwl.pocketlybe.dto.SignupRequestDTO;
import live.sahiwl.pocketlybe.dto.AuthResponseDTO;
import live.sahiwl.pocketlybe.dto.UserResponseDTO;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public AuthResponseDTO registerUser(SignupRequestDTO request) {

        String username = request.getUsername();
        String email = request.getEmail();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username '" + username + "' already exists.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email '" + email + "' already registered.");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(encoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        // Generate token for auto-login after signup
        String token = jwtService.generateToken(
                savedUser.getUsername(),
                savedUser.getId());

        return AuthResponseDTO.builder()
                .token(token)
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Transactional(readOnly = true)
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new RuntimeException(
                                "User not found with username or email: " + usernameOrEmail)));
    }

    // Fetch all users, map entities to DTOs
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> UserResponseDTO.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .build())
                .collect(Collectors.toList());
    }
}