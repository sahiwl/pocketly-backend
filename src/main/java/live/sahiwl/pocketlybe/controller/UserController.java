package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.UserRequestDTO;
import live.sahiwl.pocketlybe.dto.UserResponseDTO;
import live.sahiwl.pocketlybe.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userSer){
        this.userService = userSer;
    }

    @GetMapping
    public List<UserResponseDTO> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO req){
        return userService.createUser(req.getUsername(), req.getPassword());
    }
}
