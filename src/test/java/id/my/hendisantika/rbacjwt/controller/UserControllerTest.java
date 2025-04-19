package id.my.hendisantika.rbacjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.my.hendisantika.rbacjwt.config.TokenProvider;
import id.my.hendisantika.rbacjwt.model.LoginUser;
import id.my.hendisantika.rbacjwt.model.Role;
import id.my.hendisantika.rbacjwt.model.User;
import id.my.hendisantika.rbacjwt.model.UserDto;
import id.my.hendisantika.rbacjwt.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private TokenProvider jwtTokenUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private User testUser;
    private UserDto testUserDto;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    public void setup() {
        // Setup MockMvc without security filters for simplicity in testing
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        testUser.setPhone("1234567890");
        testUser.setName("Test User");

        // Setup user DTO
        testUserDto = new UserDto();
        testUserDto.setUsername("testuser");
        testUserDto.setPassword("password");
        testUserDto.setEmail("test@example.com");
        testUserDto.setPhone("1234567890");
        testUserDto.setName("Test User");

        // Setup roles
        userRole = new Role();
        userRole.setId(1L);
        userRole.setName("USER");
        userRole.setDescription("Regular user role");

        adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ADMIN");
        adminRole.setDescription("Administrator role");

        // Setup user roles
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        testUser.setRoles(roles);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // Arrange
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("testuser");
        loginUser.setPassword("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginUser.getUsername(), loginUser.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenUtil.generateToken(any(Authentication.class))).thenReturn("test-token");

        // Act & Assert
        mockMvc.perform(post("/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    public void testSaveUser() throws Exception {
        // Arrange
        when(userService.save(any(UserDto.class))).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testAdminPing() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/users/adminping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Only Admins Can Read This"));
    }

    @Test
    public void testUserPing() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/users/userping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Any User Can Read This"));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        // Arrange
        when(userService.createEmployee(any(UserDto.class))).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/users/create/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testGetAllList() throws Exception {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(testUser);
        when(userService.findAll()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/users/find/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    public void testGetAllListByUsername() throws Exception {
        // Arrange
        when(userService.findOne("testuser")).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(get("/users/find/by/username")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
