package id.my.hendisantika.rbacjwt.service;

import id.my.hendisantika.rbacjwt.model.Role;
import id.my.hendisantika.rbacjwt.model.User;
import id.my.hendisantika.rbacjwt.model.UserDto;
import id.my.hendisantika.rbacjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private BCryptPasswordEncoder bcryptEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Role userRole;
    private Role adminRole;
    private Role employeeRole;

    @BeforeEach
    public void setup() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setEmail("test@example.com");
        testUser.setPhone("1234567890");
        testUser.setName("Test User");

        // Setup roles
        userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");
        userRole.setDescription("Regular user role");

        adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ADMIN");
        adminRole.setDescription("Administrator role");

        employeeRole = new Role();
        employeeRole.setId(3L);
        employeeRole.setName("EMPLOYEE");
        employeeRole.setDescription("Employee role");

        // Setup user roles
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        testUser.setRoles(roles);
    }

    @Test
    public void testLoadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userService.loadUserByUsername("testuser");

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testuser");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        assertThat(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))).isTrue();
    }

    @Test
    public void testLoadUserByUsername_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistentuser");
        });
    }

    @Test
    public void testFindAll_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindOne_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // Act
        User result = userService.findOne("testuser");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testSave_WithRegularEmail_ShouldAssignUserRole() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("newuser");
        userDto.setPassword("password");
        userDto.setEmail("newuser@example.com");
        userDto.setPhone("9876543210");
        userDto.setName("New User");

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("password");
        newUser.setEmail("newuser@example.com");
        newUser.setPhone("9876543210");
        newUser.setName("New User");

        when(bcryptEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleService.findByName("USER")).thenReturn(userRole);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userService.save(userDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("newuser");
    }

    @Test
    public void testSave_WithAdminEmail_ShouldAssignAdminAndUserRoles() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("adminuser");
        userDto.setPassword("password");
        userDto.setEmail("admin@admin.edu");
        userDto.setPhone("9876543210");
        userDto.setName("Admin User");

        User adminUser = new User();
        adminUser.setUsername("adminuser");
        adminUser.setPassword("password");
        adminUser.setEmail("admin@admin.edu");
        adminUser.setPhone("9876543210");
        adminUser.setName("Admin User");

        when(bcryptEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleService.findByName("USER")).thenReturn(userRole);
        when(roleService.findByName("ADMIN")).thenReturn(adminRole);
        when(userRepository.save(any(User.class))).thenReturn(adminUser);

        // Act
        User result = userService.save(userDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("adminuser");
        assertThat(result.getEmail()).isEqualTo("admin@admin.edu");
    }

    @Test
    public void testCreateEmployee_ShouldAssignEmployeeAndUserRoles() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("employee");
        userDto.setPassword("password");
        userDto.setEmail("employee@example.com");
        userDto.setPhone("9876543210");
        userDto.setName("Employee User");

        User employeeUser = new User();
        employeeUser.setUsername("employee");
        employeeUser.setPassword("password");
        employeeUser.setEmail("employee@example.com");
        employeeUser.setPhone("9876543210");
        employeeUser.setName("Employee User");

        when(bcryptEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleService.findByName("EMPLOYEE")).thenReturn(employeeRole);
        when(roleService.findByName("USER")).thenReturn(userRole);
        when(userRepository.save(any(User.class))).thenReturn(employeeUser);

        // Act
        User result = userService.createEmployee(userDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("employee");
    }
}
