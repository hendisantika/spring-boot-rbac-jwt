package id.my.hendisantika.rbacjwt.repository;

import id.my.hendisantika.rbacjwt.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindRoleByName_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        Role role = new Role();
        role.setName("ADMIN");
        role.setDescription("Administrator role");
        entityManager.persist(role);
        entityManager.flush();

        // Act
        Role found = roleRepository.findRoleByName("ADMIN");

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("ADMIN");
        assertThat(found.getDescription()).isEqualTo("Administrator role");
    }

    @Test
    public void testFindRoleByName_WhenRoleDoesNotExist_ShouldReturnNull() {
        // Act
        Role found = roleRepository.findRoleByName("NONEXISTENT_ROLE");

        // Assert
        assertThat(found).isNull();
    }
}