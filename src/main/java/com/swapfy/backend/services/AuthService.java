package com.swapfy.backend.services;

import com.swapfy.backend.models.Role;
import com.swapfy.backend.models.User;
import com.swapfy.backend.repositories.RoleRepository;
import com.swapfy.backend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User registerUser(@Valid User user) {
        // Validación de campos vacíos o nulos
        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getName() == null || user.getName().isEmpty()) {
            throw new RuntimeException("Email, nombre y contraseña son requeridos");
        }

        // Validar si el email tiene formato válido
        if (!isValidEmail(user.getEmail())) {
            throw new RuntimeException("El email no es válido");
        }

        // Verificar si el email ya está registrado
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        //Por ahora lo dejamos así, para el desarrollo inicial

        // Validar contraseña fuerte
        //if (!isPasswordStrong(user.getPassword())) {
        //    throw new RuntimeException("La contraseña debe tener al menos 8 caracteres, un número, una letra y un símbolo.");
        // }

        // Setear contraseña, créditos y fecha de registro
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCredits(100); // inicializar créditos
        user.setRegistrationDate(LocalDateTime.now()); // registrar fecha actual

        // Asignar rol USER por defecto
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        user.setRole(userRole);

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        // Validación de campos vacíos o nulos
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException("Email y contraseña son requeridos");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = optionalUser.get(); // Extraemos el usuario

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return user;


    }

    // Validar si el email es válido
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    // Validar si la contraseña es fuerte
    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 &&
                password.matches(".*\\d.*") &&        // al menos un número
                password.matches(".*[a-zA-Z].*") &&   // al menos una letra
                password.matches(".*[^a-zA-Z0-9].*"); // al menos un símbolo
    }
}
