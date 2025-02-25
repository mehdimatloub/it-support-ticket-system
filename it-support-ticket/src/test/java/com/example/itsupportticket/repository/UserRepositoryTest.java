package com.example.itsupportticket.repository;

import com.example.itsupportticket.entity.User;
import com.example.itsupportticket.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        // Crée un nouvel utilisateur avant chaque test
        user = new User();
        user.setUsername("johndoe");
        user.setRole(Role.EMPLOYEE);
    }

    @AfterEach
    public void tearDown() {
        // Nettoie la base de données après chaque test
        userRepository.deleteAll();
    }

    @Test
    public void testCreateAndFindUserByUsername() {
        // Sauvegarde l'utilisateur dans la base de données
        userRepository.save(user);

        // Vérifie que l'utilisateur a bien été enregistré
        assertThat(userRepository.count()).isEqualTo(1);

        // Recherche l'utilisateur par son nom d'utilisateur
        User foundUser = userRepository.findByUsername("johndoe");

        // Vérifie que l'utilisateur est bien retrouvé et que les informations sont correctes
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
        assertThat(foundUser.getRole()).isEqualTo(Role.EMPLOYEE);
    }

    @Test
    public void testUserNotFound() {
        // Recherche un utilisateur qui n'existe pas
        User foundUser = userRepository.findByUsername("nonexistentuser");

        // Vérifie que l'utilisateur n'est pas trouvé
        assertThat(foundUser).isNull();
    }
}
