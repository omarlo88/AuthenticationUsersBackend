package omar.lo;

import com.fasterxml.jackson.databind.ObjectMapper;
import omar.lo.entities.AppRole;
import omar.lo.entities.AppUser;
import omar.lo.metier.AccountServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class GestionAuthenticationUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionAuthenticationUsersApplication.class, args);
	}

	@Bean
	CommandLineRunner start(AccountServiceImpl accountService){
		return args -> {
            /*accountService.saveUser(new AppUser(null, "Lo", "Omar", "admin", "123",
                    "admin@gmail.com", true, true, true, true, null,
                    null, new Date(), null));
            accountService.saveUser(new AppUser(null, "Savadogo", "Haoua", "user", "123",
                    "user@gmail.com", true, true, true, true, null, null, new Date(), null));

            accountService.saveRole(new AppRole(null, "ADMIN"));
            accountService.saveRole(new AppRole(null, "USER"));

            accountService.addRoleToUser("admin", "ADMIN");
            accountService.addRoleToUser("admin", "USER");
            accountService.addRoleToUser("user", "USER");
*/
            //accountService.getAppUsers().forEach(System.out::println);
		};

	}

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}

}// GestionAuthenticationUsersApplication
