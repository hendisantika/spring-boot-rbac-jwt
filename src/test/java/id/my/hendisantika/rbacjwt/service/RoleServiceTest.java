package id.my.hendisantika.rbacjwt.service;

import id.my.hendisantika.rbacjwt.model.Role;
import id.my.hendisantika.rbacjwt.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role adminRole;
    private Role userRole;

    @BeforeEach
    public void setup() {
        // Setup roles
        adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ADMIN");
        adminRole.setDescription("Administrator role");

        userRole = new Role();
        userRole.setId(2L);
        userRole.setName("USER");
        userRole.setDescription("Regular user role");
    }

    @Test
    public void testFindByName_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        when(roleRepository.findRoleByName("ADMIN")).thenReturn(adminRole);

        // Act
        Role found = roleService.findByName("ADMIN");

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("ADMIN");
        assertThat(found.getDescription()).isEqualTo("Administrator role");
    }

    @Test
    public void testFindByName_WhenRoleDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(roleRepository.findRoleByName("NONEXISTENT_ROLE")).thenReturn(null);

        // Act
        Role found = roleService.findByName("NONEXISTENT_ROLE");

        // Assert
        assertThat(found).isNull();
    }
}