package com.nanoka.warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nanoka.warehouse.Model.Entity.User;
import com.nanoka.warehouse.Model.Enum.Role;
import com.nanoka.warehouse.Repository.UserRepository;

@SpringBootApplication
public class WarehouseApplication implements CommandLineRunner{

	@Autowired
	UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(WarehouseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        if(!userRepository.existsByUsername("admin"))
		{
			User user = User.builder()
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				.name("admin")
				.role(Role.ADMIN)
				.build();

			userRepository.save(user);
		}
    }

}
