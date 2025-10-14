package live.sahiwl.pocketlybe.service;

import live.sahiwl.pocketlybe.dto.AuthRequestDTO;
import live.sahiwl.pocketlybe.dto.AuthResponseDTO;
import live.sahiwl.pocketlybe.dto.UserResponseDTO;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Transactional
    public AuthResponseDTO registerUser(AuthRequestDTO request){

        if(request.getEmail()== null || request.getEmail().trim().isEmpty()){
            throw new RuntimeException("Email is required for registration");
        }

         if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Username " + request.getUsername() + " already exists.");
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email " + request.getEmail() + " already registered.");
        }

         User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

         // Generate token for auto-login after signup
        String token = jwtService.generateToken(savedUser.getUsername());

         return AuthResponseDTO.builder()
                .token(token)
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();
    }

    // public User saveUser(User user) {
    //     user.setPassword(encoder.encode(user.getPassword()));
    //     System.out.println(user.getPassword());
    //     return userRepository.save(user);
    // }

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

    // // Create a new user using username + password from client
    // @Transactional
    // public UserResponseDTO createUser(String username, String password) {
    //     User user = User.builder()
    //             .username(username)
    //             .password(password)
    //             .build();
    //     userRepository.save(user);
    //     return UserResponseDTO.builder()
    //             .id(user.getId())
    //             .username(user.getUsername())
    //             .build();
    // }
}