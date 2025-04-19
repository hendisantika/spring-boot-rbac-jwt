package id.my.hendisantika.rbacjwt.repository;

import id.my.hendisantika.rbacjwt.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername_WhenUserExists_ShouldReturnUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setName("Test User");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        User found = userRepository.findByUsername("testuser");

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("testuser");
        assertThat(found.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testFindByUsername_WhenUserDoesNotExist_ShouldReturnNull() {
        // Act
        User found = userRepository.findByUsername("nonexistentuser");

        // Assert
        assertThat(found).isNull();
    }
}