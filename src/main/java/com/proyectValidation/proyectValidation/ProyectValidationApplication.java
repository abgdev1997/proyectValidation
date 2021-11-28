package com.proyectValidation.proyectValidation;

import com.proyectValidation.proyectValidation.dto.RolDto;
import com.proyectValidation.proyectValidation.models.User;
import com.proyectValidation.proyectValidation.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProyectValidationApplication {

	public static void main(String[] args) {

		ApplicationContext context =
				SpringApplication.run(ProyectValidationApplication.class, args);
		//Instancia de UserRepository
		UserRepository userRepository = context.getBean(UserRepository.class);
		//Instanciamos un passwordEncoder
		PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

		//Crea el usuario Administrador
		User userAdmin = new User(null,
				"admin",
				passwordEncoder.encode("1234"),
				"admin@admin.com");
		/*Se da privilegios de administrador y se verifica la cuenta de forma
		  manual.	*/
		//TODO hacer el set del rol y ponerlo como administrador
		userAdmin.setRol(RolDto.ADMIN);
		userAdmin.setVerified(true);
		//Guarda el usuario en la base de datos
		userRepository.save(userAdmin);


	}
}
