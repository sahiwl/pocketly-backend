package live.sahiwl.pocketlybe;

import live.sahiwl.pocketlybe.model.Link;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.LinkRepository;
import live.sahiwl.pocketlybe.repository.UserRepository;
import live.sahiwl.pocketlybe.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PocketlyBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PocketlyBeApplication.class, args);
    }

    @Bean
    CommandLineRunner testDb(UserRepository userRepo, LinkRepository linkRepo, UserService userService) {
//            return args-> {
//                User user = User.builder()
//                        .username("testuser")
//                        .password("secret")
//                        .build();
//                userRepo.save(user);
//
//                // Create link for user
//                Link link = Link.builder()
//                        .hash("xyz123")
//                        .user(user)
//                        .build();
//                linkRepo.save(link);

//                System.out.println("Users in DB: " + userRepo.findAll());
//                System.out.println("Links for user: " + linkRepo.findByUserId(user.getId()));

        return args -> {
            userService.getAllUsers().forEach(u -> System.out.println(u.getUsername()));
        };
    }
}
