package live.sahiwl.pocketlybe;

import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.UserRepository;
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
        CommandLineRunner testDb(UserRepository userRepo){
            return args-> {
                User user = User.builder()
                        .username("testuser")
                        .password("secret")
                        .build();
                userRepo.save(user);

                System.out.println("Saved users: "+ userRepo.findAll());

            };
        }
    }
